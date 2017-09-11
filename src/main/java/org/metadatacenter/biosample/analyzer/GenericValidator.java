package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class GenericValidator extends RecordValidator {
  @Nonnull private static final Logger logger = LoggerFactory.getLogger(GenericValidator.class.getName());
  @Nonnull private final TermValidator termValidator;
  @Nonnull private final RecordValidator[] recordValidators;

  public GenericValidator(@Nonnull TermValidator termValidator, @Nonnull RecordValidator... recordValidators) {
    this.termValidator = checkNotNull(termValidator);
    this.recordValidators = checkNotNull(recordValidators);
  }

  @Nonnull
  @Override
  public RecordValidationReport validateBioSampleRecord(@Nonnull Record biosample) {
    List<AttributeGroupValidationReport> attributeGroupValidationReports = new ArrayList<>();
    Map<String,Attribute> map = biosample.getAttributes();
    attributeGroupValidationReports.add(validateOntologyTermAttributes(map));
    attributeGroupValidationReports.add(validateTermAttributes(map));
    attributeGroupValidationReports.add(validateBooleanAttributes(map));
    attributeGroupValidationReports.add(validateIntegerAttributes(map));
    attributeGroupValidationReports.add(validateValueSetAttributes(map));
    if(recordValidators.length > 0) {
      for (RecordValidator recordValidator : recordValidators) {
        RecordValidationReport report = recordValidator.validateBioSampleRecord(biosample);
        attributeGroupValidationReports.addAll(report.getAttributeGroupValidationReports());
      }
    }
    return new RecordValidationReport(biosample, attributeGroupValidationReports);
  }

  @Nonnull
  private AttributeGroupValidationReport validateOntologyTermAttributes(@Nonnull Map<String,Attribute> map) {
    List<AttributeValidationReport> ontologyTermAttributeReports = new ArrayList<>();
    for (AttributeSchema schema : BioSampleAttributes.getAttributesOfType(AttributeType.ONTOLOGY_TERM)) {
      String attrName = schema.getName();
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if(attribute != null) {
        report = validateOntologyTermAttribute(attribute, true,
            schema.getValues().toArray(new String[schema.getValues().size()]));
      } else {
        report = Utils.getMissingAttributeReport(attrName);
      }
      ontologyTermAttributeReports.add(report);
    }
    return new AttributeGroupValidationReport(AttributeType.ONTOLOGY_TERM.name().toLowerCase(), ontologyTermAttributeReports);
  }

  @Nonnull
  private AttributeGroupValidationReport validateTermAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> termAttributeReports = new ArrayList<>();
    for (AttributeSchema schema : BioSampleAttributes.getAttributesOfType(AttributeType.TERM)) {
      String attrName = schema.getName();
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateTermAttribute(attribute, schema);
      } else {
        report = Utils.getMissingAttributeReport(attrName);
      }
      termAttributeReports.add(report);
    }
    return new AttributeGroupValidationReport(AttributeType.TERM.name().toLowerCase(), termAttributeReports);
  }

  @Nonnull
  private AttributeGroupValidationReport validateIntegerAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> integerAttributeReports = new ArrayList<>();
    for (AttributeSchema schema : BioSampleAttributes.getAttributesOfType(AttributeType.INTEGER)) {
      String attrName = schema.getName();
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateIntegerAttribute(attribute);
      } else {
        report = Utils.getMissingAttributeReport(attrName);
      }
      integerAttributeReports.add(report);
    }
    return new AttributeGroupValidationReport(AttributeType.INTEGER.name().toLowerCase(), integerAttributeReports);
  }

  @Nonnull
  private AttributeGroupValidationReport validateBooleanAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> booleanAttributeReports = new ArrayList<>();
    for (AttributeSchema schema : BioSampleAttributes.getAttributesOfType(AttributeType.BOOLEAN)) {
      String attrName = schema.getName();
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateBooleanAttribute(attribute);
      } else {
        report = Utils.getMissingAttributeReport(attrName);
      }
      booleanAttributeReports.add(report);
    }
    return new AttributeGroupValidationReport(AttributeType.BOOLEAN.name().toLowerCase(), booleanAttributeReports);
  }

  @Nonnull
  private AttributeGroupValidationReport validateValueSetAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> reports = new ArrayList<>();
    for (AttributeSchema schema : BioSampleAttributes.getAttributesOfType(AttributeType.VALUE_SET)) {
      String attrName = schema.getName();
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateValueSetAttribute(attribute, schema);
      } else {
        report = Utils.getMissingAttributeReport(attrName);
      }
      reports.add(report);
    }
    return new AttributeGroupValidationReport(AttributeType.VALUE_SET.name().toLowerCase(), reports);
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
    if(isFilledIn) {
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
    String result = str.replaceAll("\\[", "");
    result = result.replaceAll("]", "");
    if (result.contains(":")) {
      result = result.substring(result.indexOf(":")+1, result.length());
    }
    return result;
  }
}
