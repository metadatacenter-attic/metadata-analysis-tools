package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class BioSampleAttributes {
  @Nonnull private final static Logger logger = LoggerFactory.getLogger(BioSampleAttributes.class.getName());
  @Nonnull private final static String ATTRIBUTES_FILE = "attributes.csv";
  @Nonnull private static Map<AttributeType, List<AttributeSchema>> attributesMap = new HashMap<>();
  @Nonnull private static Map<String,AttributeType> attributeNames = new HashMap<>();

  static {
    try {
      InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(ATTRIBUTES_FILE);
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      String line = reader.readLine();
      while(line != null) {
        String[] tokens = line.split(",");
        String attributeName = tokens[0];
        AttributeType attributeType = getAttributeType(tokens[1]);
        attributeNames.put(attributeName, attributeType);
        List<String> values;
        if(tokens.length > 2) {
          values = getAttributeValues(tokens[2]);
        } else {
          values = new ArrayList<>();
        }
        addSchema(attributeType, new AttributeSchema(attributeName, attributeType, values));
        line = reader.readLine();
      }
      reader.close();
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void addSchema(AttributeType type, AttributeSchema schema) {
    List<AttributeSchema> schemas;
    if(attributesMap.containsKey(type)) {
      schemas = attributesMap.get(type);
    } else {
      schemas = new ArrayList<>();
    }
    schemas.add(schema);
    attributesMap.put(type, schemas);
  }

  private static AttributeType getAttributeType(String type) {
    AttributeType t = AttributeType.OTHER;
    try {
       t = AttributeType.valueOf(type.toUpperCase());
      return t;
    } catch (Exception e){
      e.printStackTrace();
    }
    return t;
  }

  private static List<String> getAttributeValues(String string) {
    String[] attrValues = string.split("\\|");
    return Arrays.asList(attrValues);
  }

  @Nonnull
  static List<AttributeSchema> getAttributesOfType(@Nonnull AttributeType type) {
    if(!attributesMap.containsKey(type)) {
      logger.error("The requested attribute type does not exist in the attributes map");
      return Collections.emptyList();
    }
    return attributesMap.get(type);
  }

  @Nonnull
  static List<String> getAttributeNamesOfType(@Nonnull AttributeType type) {
    if(!attributesMap.containsKey(type)) {
      logger.error("The requested attribute type does not exist in the attributes map");
      return Collections.emptyList();
    }
    List<AttributeSchema> as = attributesMap.get(type);
    List<String> output = new ArrayList<>();
    for(AttributeSchema s : as) {
      output.add(s.getName());
    }
    return output;
  }

  @Nonnull
  static Set<String> getAttributeNames() {
    return attributeNames.keySet();
  }

  @Nonnull
  static AttributeType getAttributeTypeForName(@Nonnull String attribute) {
    return attributeNames.get(attribute);
  }

  @Nonnull
  static Set<AttributeType> getAttributeTypes() {
    return attributesMap.keySet();
  }
}
