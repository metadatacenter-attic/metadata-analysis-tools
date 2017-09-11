package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public abstract class RecordValidator implements Validator {

  /**
   * Check if the value of the attribute is filled in, that is, it is a non-empty string.
   */
  protected boolean isFilledIn(String value) {
    return !value.trim().isEmpty();
  }

  @Nonnull
  protected AttributeValidationReport validateGeographicLocation(@Nonnull Attribute attribute) {
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
  protected boolean isValidGeographicLocation(String location) {
    if(location.contains(Utils.LOCATION_SEPARATOR)) {
      String mainEntry = location.substring(0, location.indexOf(Utils.LOCATION_SEPARATOR));
      return Utils.getValidLocations().contains(mainEntry);
    } else {
      return Utils.getValidLocations().contains(location);
    }
  }

  /**
   * Check that date of sampling is in "DD-Mmm-YYYY", "Mmm-YYYY" or "YYYY" format (eg., 30-Oct-1990, Oct-1990 or 1990) or
   * ISO 8601 standard "YYYY-mm-dd", "YYYY-mm" or "YYYY-mm-ddThh:mm:ss" (eg., 1990-10-30, 1990-10 or 1990-10-30T14:41:36)
   */
  protected boolean isValidDateFormat(String date) {
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
  protected boolean isValidCoordinateFormat(String coordinates) {
    Pattern coordinatePattern = Pattern.compile("(\\d{0,3}(\\.\\d+)?)[ ]?(N|S) (\\d{0,3}(\\.\\d+)?)[ ]?(E|W)$");
    return coordinatePattern.matcher(coordinates).matches();
  }
}
