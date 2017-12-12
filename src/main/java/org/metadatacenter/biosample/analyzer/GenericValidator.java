package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class GenericValidator implements Validator {
  @Nonnull private static final Logger logger = LoggerFactory.getLogger(GenericValidator.class.getName());
  @Nonnull private final TermValidator termValidator;
  @Nonnull private final static Pattern p1 = Pattern.compile("\\["), p2 = Pattern.compile("]");

  public GenericValidator(@Nonnull TermValidator termValidator) {
    this.termValidator = checkNotNull(termValidator);
  }

  public RecordValidationReport validateBioSampleRecord(@Nonnull Record biosample) {
    List<AttributeGroupValidationReport> attributeGroupValidationReports = new ArrayList<>();
    Map<String,Attribute> map = biosample.getAttributes();
    // validate record against known attribute types
    for(AttributeType attrType : BioSampleAttributes.getAttributeTypes()) {
      List<AttributeValidationReport> reports = new ArrayList<>();
      for (AttributeSchema schema : BioSampleAttributes.getAttributesOfType(attrType)) {
        String attrName = schema.getName();
        Attribute attribute = map.get(attrName);
        AttributeValidationReport report;
        if(attribute != null) {
          report = validateAttribute(attribute, schema);
        } else {
          report = Utils.getMissingAttributeReport(attrName);
        }
        reports.add(report);
      }
      attributeGroupValidationReports.add(new AttributeGroupValidationReport(attrType.name().toLowerCase(), reports));
    }
    return new RecordValidationReport(biosample, attributeGroupValidationReports);
  }

  public AttributeValidationReport validateAttribute(Attribute attribute, AttributeSchema schema) {
    AttributeType type = schema.getType();
    AttributeValidationReport report;
    if(type.equals(AttributeType.BOOLEAN)) {
      report = validateBooleanAttribute(attribute);
    }
    else if(type.equals(AttributeType.INTEGER)) {
      report = validateIntegerAttribute(attribute);
    }
    else if(type.equals(AttributeType.VALUE_SET)) {
      report = validateValueSetAttribute(attribute, schema);
    }
    else if(type.equals(AttributeType.TERM)) {
      report = validateTermAttribute(attribute, schema);
    }
    else if(type.equals(AttributeType.ONTOLOGY_TERM)) {
      report = validateOntologyTermAttribute(attribute, true,
          schema.getValues().toArray(new String[schema.getValues().size()]));
    }
    else if(type.equals(AttributeType.TIMESTAMP)) {
      report = validateTimestampAttribute(attribute);
    }
    else {
      report = Utils.getMissingAttributeReport(attribute.getName());
      logger.error("Missing functionality to handle attributes of type: " + type);
    }
    return report;
  }

  @Nonnull
  private AttributeValidationReport validateTermAttribute(@Nonnull Attribute attribute, @Nonnull AttributeSchema schema) {
    AttributeValidationReport report;
    if(schema.getValues().contains("GEOLOC")) {
      report = validateGeographicLocation(attribute);
    } else {
      report = validateOntologyTermAttribute(attribute, true);
    }
    return report;
  }

  @Nonnull
  private AttributeValidationReport validateValueSetAttribute(@Nonnull Attribute attribute, @Nonnull AttributeSchema schema) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    String match = null;
    if(isFilledIn) {
      List<String> values = schema.getValues();
      for(String v : values) {
        if(value.equalsIgnoreCase(v)) {
          isValidFormat = true;
          match = v;
          break;
        }
      }
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat, Optional.ofNullable(match));
  }

  @Nonnull
  private AttributeValidationReport validateOntologyTermAttribute(@Nonnull Attribute attribute, boolean exactMatch,
                                                                  @Nonnull String... ontologies) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    String match = null;
    if(isFilledIn && !Utils.isInvalidEntry(value)) {
      TermValidationReport report;
      if (ontologies.length > 0) {
        report = termValidator.validateTerm(normalize(value), exactMatch, ontologies);
      } else {
        report = termValidator.validateTerm(normalize(value), exactMatch);
      }
      isValidFormat = report.isResolvableOntologyClass();
      match = report.getMatchValue();
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat, Optional.ofNullable(match));
  }

  @Nonnull
  private AttributeValidationReport validateBooleanAttribute(@Nonnull Attribute attribute) {
    String value = attribute.getValue().trim();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
      isValidFormat = true;
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat, Optional.empty());
  }

  @Nonnull
  private AttributeValidationReport validateIntegerAttribute(@Nonnull Attribute attribute) {
    String value = attribute.getValue().trim();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    if(isFilledIn) {
      try {
        Integer.parseInt(value);
        isValidFormat = true;
      } catch (NumberFormatException e) {
        isValidFormat = false;
      }
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat, Optional.empty());
  }

  @Nonnull
  private AttributeValidationReport validateTimestampAttribute(@Nonnull Attribute attribute) {
    String value = attribute.getValue().trim();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = isValidDateFormat(value);
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat, Optional.empty());
  }

  /**
   * Check whether all the provided attributes are filled in properly
   */
  @Override
  public boolean isValid(@Nonnull RecordValidationReport report) {
    boolean isValid = true;
    for(AttributeGroupValidationReport group : report.getAttributeGroupValidationReports()) {
      for (AttributeValidationReport r : group.getValidationReports()) {
        if (!r.isValid()) {
          isValid = false;
          break;
        }
      }
    }
    return isValid;
  }

  @Nonnull
  private String normalize(@Nonnull String str) {
    String r = p1.matcher(str).replaceAll("");
    String result = p2.matcher(r).replaceAll("");
    if (result.contains(":")) {
      result = result.substring(result.indexOf(":")+1, result.length());
    }
    return result;
  }

  protected boolean isFilledIn(String value) {
    return !value.trim().isEmpty();
  }

  @Nonnull
  private AttributeValidationReport validateGeographicLocation(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = isValidGeographicLocation(value);
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat, Optional.empty());
  }

  /**
   * Check that the main location is a term from the list at http://www.insdc.org/documents/country-qualifier-vocabulary.
   * A colon is used to separate the country or ocean from more detailed information about the location,
   * eg "Canada: Vancouver" or "Germany: halfway down Zugspitze, Alps"
   */
  private boolean isValidGeographicLocation(String location) {
    if(location.contains(Utils.LOCATION_SEPARATOR)) {
      String mainEntry = location.substring(0, location.indexOf(Utils.LOCATION_SEPARATOR));
      return Utils.getValidLocations().contains(mainEntry);
    } else {
      return Utils.getValidLocations().contains(location);
    }
  }

  private final Pattern datePattern = Pattern.compile("(^\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4})|(" +
      "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4})|(\\d{4})$");

  private final Pattern isoPattern = Pattern.compile("(^(\\d{4})\\D?(0[1-9]|1[0-2])\\D?([12]\\d|0[1-9]|3[01])(\\D?" +
      "([01]\\d|2[0-3])\\D?([0-5]\\d)\\D?([0-5]\\d)?\\D?(\\d{3})?)?)|(\\d{4}-\\d{2}-\\d{2})|(\\d{4}-\\d{2})$");

  /**
   * Check that date of sampling is in "DD-Mmm-YYYY", "Mmm-YYYY" or "YYYY" format (eg., 30-Oct-1990, Oct-1990 or 1990) or
   * ISO 8601 standard "YYYY-mm-dd", "YYYY-mm" or "YYYY-mm-ddThh:mm:ss" (eg., 1990-10-30, 1990-10 or 1990-10-30T14:41:36)
   */
  private boolean isValidDateFormat(String date) {
    return datePattern.matcher(date).matches() || isoPattern.matcher(date).matches();
  }

  /**
   * Check that latitude and longitude are specified as degrees in format "d[d.dddd] N|S d[dd.dddd] W|E", eg, 38.98 N 77.11 W
   */
  private boolean isValidCoordinateFormat(String coordinates) {
    Pattern coordinatePattern = Pattern.compile("(\\d{0,3}(\\.\\d+)?)[ ]?(N|S) (\\d{0,3}(\\.\\d+)?)[ ]?(E|W)$");
    return coordinatePattern.matcher(coordinates).matches();
  }
}
