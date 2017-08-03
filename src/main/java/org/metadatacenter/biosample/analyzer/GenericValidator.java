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
  public RecordValidationReport validateBioSampleRecord(@Nonnull Record biosample) throws InvalidPackageException {
    List<AttributeGroupValidationReport> attributeGroupValidationReports = new ArrayList<>();
    Map<String,Attribute> map = biosample.getAttributes();
    List<AttributeValidationReport> ontologyTermAttributeReports = validateOntologyTermAttributes(map);
    List<AttributeValidationReport> termAttributeReports = validateTermAttributes(map);
    List<AttributeValidationReport> booleanAttributeReports = validateBooleanAttributes(map);
    List<AttributeValidationReport> integerAttributeReports = validateIntegerAttributes(map);
    List<AttributeValidationReport> valueSetAttributeReports = validateValueSetAttributes(map);

    attributeGroupValidationReports.add(new AttributeGroupValidationReport("ontologyTerms", ontologyTermAttributeReports));
    attributeGroupValidationReports.add(new AttributeGroupValidationReport("terms", termAttributeReports));
    attributeGroupValidationReports.add(new AttributeGroupValidationReport("boolean", booleanAttributeReports));
    attributeGroupValidationReports.add(new AttributeGroupValidationReport("integer", integerAttributeReports));
    attributeGroupValidationReports.add(new AttributeGroupValidationReport("valueSet", valueSetAttributeReports));
    if(recordValidators.length > 0) {
      for (RecordValidator recordValidator : recordValidators) {
        RecordValidationReport report = recordValidator.validateBioSampleRecord(biosample);
        attributeGroupValidationReports.addAll(report.getAttributeGroupValidationReports());
      }
    }
    return new RecordValidationReport(biosample, attributeGroupValidationReports);
  }

  @Nonnull
  private List<AttributeValidationReport> validateOntologyTermAttributes(@Nonnull Map<String,Attribute> map) {
    List<AttributeValidationReport> ontologyTermAttributeReports = new ArrayList<>();
    for(String attrName : ONTOLOGY_TERM_ATTR_NAMES) {
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if(attribute != null) {
        Optional<AttributeValidationReport> reportOpt = validateOntologyTermAttributes(attribute);
        report = reportOpt.orElseGet(() -> new AttributeValidationReport(attribute, false, false));
      } else {
        report = new AttributeValidationReport(
            new AttributeImpl(attrName, attrName, attrName, ""), false, false);
      }
      ontologyTermAttributeReports.add(report);
    }
    return ontologyTermAttributeReports;
  }

  @Nonnull
  private List<AttributeValidationReport> validateTermAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> termAttributeReports = new ArrayList<>();
    for (String attrName : TERM_ATTR_NAMES) {
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        Optional<AttributeValidationReport> reportOpt = validateTermAttributes(attribute);
        if (reportOpt.isPresent()) {
          report = reportOpt.get();
        } else {
          report = new AttributeValidationReport(attribute, false, false);
        }
      } else {
        report = new AttributeValidationReport(
            new AttributeImpl(attrName, attrName, attrName, ""), false, false);
      }
      termAttributeReports.add(report);
    }
    return termAttributeReports;
  }

  @Nonnull
  private List<AttributeValidationReport> validateIntegerAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> integerAttributeReports = new ArrayList<>();
    for (String attrName : INTEGER_ATTR_NAMES) {
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateIntegerAttribute(attribute);
      } else {
        report = new AttributeValidationReport(
            new AttributeImpl(attrName, attrName, attrName, ""), false, false);
      }
      integerAttributeReports.add(report);
    }
    return integerAttributeReports;
  }

  @Nonnull
  private List<AttributeValidationReport> validateBooleanAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> booleanAttributeReports = new ArrayList<>();
    for (String attrName : BOOLEAN_ATTR_NAMES) {
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateBooleanAttribute(attribute);
      } else {
        report = new AttributeValidationReport(
            new AttributeImpl(attrName, attrName, attrName, ""), false, false);
      }
      booleanAttributeReports.add(report);
    }
    return booleanAttributeReports;
  }

  @Nonnull
  private List<AttributeValidationReport> validateValueSetAttributes(@Nonnull Map<String, Attribute> map) {
    List<AttributeValidationReport> reports = new ArrayList<>();
    for (String attrName : VALUE_SETS.keySet()) {
      Attribute attribute = map.get(attrName);
      AttributeValidationReport report;
      if (attribute != null) {
        report = validateValueSetAttributes(attribute);
      } else {
        report = new AttributeValidationReport(
            new AttributeImpl(attrName, attrName, attrName, ""), false, false);
      }
      reports.add(report);
    }
    return reports;
  }

  @Nonnull
  private Optional<AttributeValidationReport> validateOntologyTermAttributes(@Nonnull Attribute attribute) {
    String attrName = attribute.getName();
    AttributeValidationReport report = null;
    if(attrName.equalsIgnoreCase(CHEMICAL_ADMINISTRATION_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true);
    }
    else if(attrName.equalsIgnoreCase(DISEASE_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true);
    }
    else if(attrName.equalsIgnoreCase(ENVIRONMENT_BIOME_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true, "ENVO");
    }
    else if(attrName.equalsIgnoreCase(ENVIRONMENT_FEATURE_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true, "ENVO");
    }
    else if(attrName.equalsIgnoreCase(ENVIRONMENT_MATERIAL_ATTR_NAME)) {
     report = validateOntologyTermAttribute(attribute, true, "ENVO");
    }
    else if(attrName.equalsIgnoreCase(HOST_DISEASE_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true, "DOID", "MESH");
    }
    else if(attrName.equalsIgnoreCase(HOST_TISSUE_SAMPLED_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true, "BTO");
    }
    else if(attrName.equalsIgnoreCase(PHENOTYPE_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true, "PATO");
    }
    else if(attrName.equalsIgnoreCase(PLANT_BODY_SITE_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, true, "PATO");
    }
    else {
      logger.error("Unexpected attribute named " + attrName + " is not an ontology term attribute");
    }
    return Optional.ofNullable(report);
  }

  @Nonnull
  private Optional<AttributeValidationReport> validateTermAttributes(@Nonnull Attribute attribute) {
    String attrName = attribute.getName();
    AttributeValidationReport report = null;
    if(attrName.equalsIgnoreCase(BODY_HABITAT_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, false);
    }
    else if(attrName.equalsIgnoreCase(GEO_LOCATION_ATTR_NAME)) {
      report = validateGeographicLocation(attribute);
    }
    else if(attrName.equalsIgnoreCase(HEALTH_STATE_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, false);
    }
    else if(attrName.equalsIgnoreCase(HOST_BODY_HABITAT_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, false);
    }
    else if(attrName.equalsIgnoreCase(PATHOGENICITY_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, false);
    }
    else if(attrName.equalsIgnoreCase(PLOIDY_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, false);
    }
    else if(attrName.equalsIgnoreCase(PROPAGATION_ATTR_NAME)) {
      report = validateOntologyTermAttribute(attribute, false);
    }
    return Optional.ofNullable(report);
  }

  @Nonnull
  private AttributeValidationReport validateValueSetAttributes(@Nonnull Attribute attribute) {
    String attrName = attribute.getName();
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    if(isFilledIn && VALUE_SETS.containsKey(attrName)) {
      List<String> values = VALUE_SETS.get(attrName);
      for(String v : values) {
        if(value.equalsIgnoreCase(v)) {
          isValidFormat = true;
          break;
        }
      }
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
  }

  @Nonnull
  private AttributeValidationReport validateOntologyTermAttribute(@Nonnull Attribute attribute, boolean exactMatch,
                                                                  @Nonnull String... ontologies) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    if(isFilledIn) {
      TermValidationReport report;
      if (ontologies.length > 0) {
        report = termValidator.validateTerm(normalize(value), exactMatch, ontologies);
      } else {
        report = termValidator.validateTerm(normalize(value), exactMatch);
      }
      isValidFormat = report.isResolvableOntologyClass();
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
  }

  @Nonnull
  private AttributeValidationReport validateBooleanAttribute(@Nonnull Attribute attribute) {
    String value = attribute.getValue().trim();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = false;
    if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
      isValidFormat = true;
    }
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
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
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
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
