package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

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

  public static boolean isInvalidEntry(String s) {
    return invalidAttributeEntries.contains(s);
  }

  public static Set<String> getInvalidEntries() {
    return invalidAttributeEntries;
  }

  public static List<String> getValidLocations() {
    return validLocations;
  }

  public static AttributeValidationReport getMissingAttributeReport(@Nonnull String attrName) {
    checkNotNull(attrName);
    return getMissingAttributeReport(new AttributeImpl(attrName, attrName, attrName, ""));
  }

  public static AttributeValidationReport getMissingAttributeReport(@Nonnull Attribute attribute) {
    checkNotNull(attribute);
    return new AttributeValidationReport(attribute, false, false, Optional.empty());
  }

}
