package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class MetagenomePackageValidator extends RecordValidator {
  @Nonnull private static final String NAME = "Metagenome.environmental.1.0";
  @Nonnull static final String COLLECTION_DATE_ATTR_NAME = "collection_date";
  @Nonnull static final String LATITUDE_LONGITUDE_ATTR_NAME = "lat_lon";
  @Nonnull static final String ISOLATION_SRC_ATTR_NAME = "isolation_source";
  @Nonnull static final String HOST_ATTR_NAME = "host";
  @Nonnull private static final String REFERENCE_BIOMATERIAL_ATTR_NAME = "ref_biomaterial";
  @Nonnull private static final String SAMPLE_COLLECTION_DEVICE_ATTR_NAME = "samp_collect_device";
  @Nonnull private static final String SAMPLE_MATERIAL_PROCESSING_ATTR_NAME = "samp_mat_process";
  @Nonnull private static final String SAMPLE_SIZE_ATTR_NAME = "samp_size";
  @Nonnull private static final String SOURCE_MATERIAL_ID_ATTR_NAME = "source_material_id";

  /**
   * Follows package definition at https://www.ncbi.nlm.nih.gov/biosample/docs/packages/Metagenome.environmental.1.0/
   */
  public MetagenomePackageValidator() {
    // no-arguments constructor
  }

  @Nonnull
  @Override
  public RecordValidationReport validateBioSampleRecord(@Nonnull Record biosample) throws InvalidPackageException {
    if(!biosample.getPackageName().equalsIgnoreCase(NAME)) {
      throw new InvalidPackageException("Package " + biosample.getPackageName() + " is not the expected sample package ("
          + NAME + ") for this validator");
    }
    List<AttributeGroupValidationReport> attributeGroupValidationReports = new ArrayList<>();
    List<AttributeValidationReport> requiredAttributeReports = new ArrayList<>();
    List<AttributeValidationReport> optionalAttributeReports = new ArrayList<>();

    Map<String, Attribute> map = biosample.getAttributes();

    Attribute collectionDate = map.get(COLLECTION_DATE_ATTR_NAME);
    if(collectionDate != null) {
      requiredAttributeReports.add(validateCollectionDate(collectionDate));
    } else {
      requiredAttributeReports.add(Utils.getMissingAttributeReport(COLLECTION_DATE_ATTR_NAME));
    }

    Attribute geoLocationName = map.get(GEO_LOCATION_ATTR_NAME);
    if(geoLocationName != null) {
      requiredAttributeReports.add(validateGeographicLocation(geoLocationName));
    } else {
      requiredAttributeReports.add(Utils.getMissingAttributeReport(GEO_LOCATION_ATTR_NAME));
    }

    Attribute latitudeLongitude = map.get(LATITUDE_LONGITUDE_ATTR_NAME);
    if(latitudeLongitude != null) {
      requiredAttributeReports.add(validateLatitudeLongitude(latitudeLongitude));
    } else {
      requiredAttributeReports.add(Utils.getMissingAttributeReport(LATITUDE_LONGITUDE_ATTR_NAME));
    }

    Attribute host = map.get(HOST_ATTR_NAME);
    if(host != null) {
      requiredAttributeReports.add(validateHost(host));
    } else {
      requiredAttributeReports.add(Utils.getMissingAttributeReport(HOST_ATTR_NAME));
    }

    Attribute isolationSource = map.get(ISOLATION_SRC_ATTR_NAME);
    if(isolationSource != null) {
      requiredAttributeReports.add(validateIsolationSource(isolationSource));
    } else {
      requiredAttributeReports.add(Utils.getMissingAttributeReport(ISOLATION_SRC_ATTR_NAME));
    }

    // optional attributes
    Attribute referenceBiomaterial = map.get(REFERENCE_BIOMATERIAL_ATTR_NAME);
    if(referenceBiomaterial != null) {
      optionalAttributeReports.add(validateRefBioMaterial(referenceBiomaterial));
    } else {
      optionalAttributeReports.add(Utils.getMissingAttributeReport(REFERENCE_BIOMATERIAL_ATTR_NAME));
    }

    Attribute relToOxygen = map.get(RELATIONSHIP_TO_OXYGEN_ATTR_NAME);
    if(relToOxygen != null) {
      optionalAttributeReports.add(validateRelationshipToOxygen(relToOxygen));
    } else {
      optionalAttributeReports.add(Utils.getMissingAttributeReport(RELATIONSHIP_TO_OXYGEN_ATTR_NAME));
    }

    Attribute sampleCollection = map.get(SAMPLE_COLLECTION_DEVICE_ATTR_NAME);
    if(sampleCollection != null) {
      optionalAttributeReports.add(validateSampleCollectionDevice(sampleCollection));
    } else {
      optionalAttributeReports.add(Utils.getMissingAttributeReport(SAMPLE_COLLECTION_DEVICE_ATTR_NAME));
    }

    Attribute sampleProcTime = map.get(SAMPLE_MATERIAL_PROCESSING_ATTR_NAME);
    if(sampleProcTime != null) {
      optionalAttributeReports.add(validateSampleMaterialProcessing(sampleProcTime));
    } else {
      optionalAttributeReports.add(Utils.getMissingAttributeReport(SAMPLE_MATERIAL_PROCESSING_ATTR_NAME));
    }

    Attribute sampleSize = map.get(SAMPLE_SIZE_ATTR_NAME);
    if(sampleSize != null) {
      optionalAttributeReports.add(validateSampleSize(sampleSize));
    } else {
      optionalAttributeReports.add(Utils.getMissingAttributeReport(SAMPLE_SIZE_ATTR_NAME));
    }

    Attribute sourceMaterialId = map.get(SOURCE_MATERIAL_ID_ATTR_NAME);
    if(sourceMaterialId != null) {
      optionalAttributeReports.add(validateSourceMaterialId(sourceMaterialId));
    } else {
      optionalAttributeReports.add(Utils.getMissingAttributeReport(SOURCE_MATERIAL_ID_ATTR_NAME));
    }

    attributeGroupValidationReports.add(new AttributeGroupValidationReport("required", requiredAttributeReports));
    attributeGroupValidationReports.add(new AttributeGroupValidationReport("optional", optionalAttributeReports));
    return new RecordValidationReport(biosample, attributeGroupValidationReports);
  }

  /**
   * Check whether all required attributes are filled in properly
   */
  @Override
  public boolean isValid(@Nonnull RecordValidationReport report) {
    boolean isValid = true;
    boolean hasHost = false, hasIsolationSource = false;
    for (AttributeGroupValidationReport group : report.getAttributeGroupValidationReports()) {
      for (AttributeValidationReport r : group.getValidationReports()) {
        String attributeName = r.getAttribute().getName();
        if (attributeName.equalsIgnoreCase(COLLECTION_DATE_ATTR_NAME)) {
          if (!r.isValid()) {
            isValid = false;
            break;
          }
        } else if (attributeName.equalsIgnoreCase(GEO_LOCATION_ATTR_NAME)) {
          if (!r.isValid()) {
            isValid = false;
            break;
          }
        } else if (attributeName.equalsIgnoreCase(LATITUDE_LONGITUDE_ATTR_NAME)) {
          if (!r.isValid()) {
            isValid = false;
            break;
          }
        } else if (attributeName.equalsIgnoreCase(HOST_ATTR_NAME)) {
          if (r.isValid()) {
            hasHost = true;
          }
        } else if (attributeName.equalsIgnoreCase(ISOLATION_SRC_ATTR_NAME)) {
          if (r.isValid()) {
            hasIsolationSource = true;
          }
        }
      }
    }
    return isValid && (hasHost || hasIsolationSource);
  }

  // required attribute
  @Nonnull
  private AttributeValidationReport validateCollectionDate(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = isValidDateFormat(value);
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
  }

  // required attribute
  @Nonnull
  private AttributeValidationReport validateLatitudeLongitude(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    boolean isValidFormat = isValidCoordinateFormat(value);
    return new AttributeValidationReport(attribute, isFilledIn, isValidFormat);
  }

  // either this or isolation source must be provided
  @Nonnull
  private AttributeValidationReport validateHost(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  // either this or host must be provided
  @Nonnull
  private AttributeValidationReport validateIsolationSource(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  // optional attribute
  @Nonnull
  private AttributeValidationReport validateRefBioMaterial(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  // optional attribute
  @Nonnull
  private AttributeValidationReport validateSampleCollectionDevice(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  // optional attribute
  @Nonnull
  private AttributeValidationReport validateSampleMaterialProcessing(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  // optional attribute
  @Nonnull
  private AttributeValidationReport validateSampleSize(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  // optional attribute
  @Nonnull
  private AttributeValidationReport validateSourceMaterialId(@Nonnull Attribute attribute) {
    String value = attribute.getValue();
    boolean isFilledIn = isFilledIn(value);
    return new AttributeValidationReport(attribute, isFilledIn, true);
  }

  /**
   * Check that date of sampling is in "DD-Mmm-YYYY", "Mmm-YYYY" or "YYYY" format (eg., 30-Oct-1990, Oct-1990 or 1990) or
   * ISO 8601 standard "YYYY-mm-dd", "YYYY-mm" or "YYYY-mm-ddThh:mm:ss" (eg., 1990-10-30, 1990-10 or 1990-10-30T14:41:36)
   */
  private boolean isValidDateFormat(String date) {
    Pattern datePattern = Pattern.compile("(^\\d{2}-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4})|(" +
        "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{4})|(\\d{4})$");
    boolean matchesPattern = datePattern.matcher(date).matches();

    Pattern isoPattern = Pattern.compile("(^(\\d{4})\\D?(0[1-9]|1[0-2])\\D?([12]\\d|0[1-9]|3[01])(\\D?" +
        "([01]\\d|2[0-3])\\D?([0-5]\\d)\\D?([0-5]\\d)?\\D?(\\d{3})?)?)|(\\d{4}-\\d{2}-\\d{2})|(\\d{4}-\\d{2})$");
    boolean matchesIsoPattern = isoPattern.matcher(date).matches();

    return matchesPattern || matchesIsoPattern;
  }

  /**
   * Check that latitude and longitude are specified as degrees in format "d[d.dddd] N|S d[dd.dddd] W|E", eg, 38.98 N 77.11 W
   */
  private boolean isValidCoordinateFormat(String coordinates) {
    Pattern coordinatePattern = Pattern.compile("(\\d{0,3}(\\.\\d+)?)[ ]?(N|S) (\\d{0,3}(\\.\\d+)?)[ ]?(E|W)$");
    return coordinatePattern.matcher(coordinates).matches();
  }

}
