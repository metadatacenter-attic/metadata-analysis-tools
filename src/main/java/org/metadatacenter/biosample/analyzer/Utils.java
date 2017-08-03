package org.metadatacenter.biosample.analyzer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class Utils {
  public static final String LOCATION_SEPARATOR = ":";

  private static Set<String> invalidAttributeEntries = new HashSet<>();
  private static List<String> validLocations = new ArrayList<>();

  static {
    invalidAttributeEntries.add("not applicable");
    invalidAttributeEntries.add("not_applicable");
    invalidAttributeEntries.add("not collected");
    invalidAttributeEntries.add("missing");
    invalidAttributeEntries.add("null");
    invalidAttributeEntries.add("?");
    invalidAttributeEntries.add("-");
    invalidAttributeEntries.add("na");
    invalidAttributeEntries.add("n/a");
    invalidAttributeEntries.add("unknown");
    invalidAttributeEntries.add("none provided");
  }

  static {
    try {
      InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("country-list.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line = reader.readLine();
      while(line != null) {
        validLocations.add(line.trim());
        line = reader.readLine();
      }
      reader.close();
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Set<String> getInvalidEntries() {
    return invalidAttributeEntries;
  }

  public static List<String> getValidLocations() {
    return validLocations;
  }

  public static AttributeValidationReport getMissingAttributeReport(String attrName) {
    return new AttributeValidationReport(new AttributeImpl(attrName, attrName, attrName, ""), false, false);
  }

}
