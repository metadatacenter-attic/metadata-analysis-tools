package org.metadatacenter.biosample.analyzer;

import java.io.*;
import java.util.*;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class TermFileResultProcessor {
  File completeList, testedList;

  public TermFileResultProcessor(File completeList, File testedList) {
    this.completeList = completeList;
    this.testedList = testedList;
  }

  public Map<String,Boolean> parseTestedList() throws IOException {
    System.out.println("Parsing file: " + testedList.getAbsolutePath());
    Map<String,Boolean> map = new HashMap<>();
    BufferedReader reader = new BufferedReader(new FileReader(testedList));
    String line = reader.readLine();
    while(line != null) {
      String[] tokens = line.split(",");
      if(tokens.length>0) {
        String value = tokens[0];
        String isValid = tokens[1];
        if(map.containsKey(value)) {
          System.out.println("Map already contains key: " + value);
        }
        map.put(value, Boolean.valueOf(isValid));
      } else {
        System.out.println("No tokens in line: " + line);
      }
      line = reader.readLine();
    }
    reader.close();
    return map;
  }


  public List<TermCheckResult> verifyValues(List<String> completeList, Map<String,Boolean> map) {
    List<TermCheckResult> output = new ArrayList<>();

    Set<String> terms = map.keySet();

    for(String s : completeList) {
      if(terms.contains(s)) {
        output.add(new TermCheckResult(s, map.get(s)));
      }
    }

    System.out.println("Found matches for " + output.size() + " terms");

    return output;
  }


  public void serialize(List<TermCheckResult> list, String outputPath) throws IOException {
    File output = new File(outputPath);
    BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
    for(TermCheckResult r : list) {
      writer.write(r.getKey());
      writer.write(",");
      writer.write(String.valueOf(r.getValue()));
      writer.write("\n");
      writer.flush();
    }
    writer.close();
  }

  public static void main(String[] args) throws IOException {
    File completeList = new File(args[0]);
    File testedList = new File(args[1]);
    String outputFilePath = args[2];

    System.out.println("Processing tested list file");
    TermFileResultProcessor p = new TermFileResultProcessor(completeList, testedList);
    Map<String,Boolean> map = p.parseTestedList();
    System.out.println("Map size " + map.size());

    System.out.println("Processing complete list file");
    List<String> list = Utils.parseFile(completeList);
    System.out.println("Complete list size: " + list.size());

    System.out.println("Verifying values");
    List<TermCheckResult> result = p.verifyValues(list, map);

    p.serialize(result, outputFilePath);
  }
}


class TermCheckResult {
  private final String key;
  private final Boolean value;

  public TermCheckResult(String key, Boolean value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public Boolean getValue() {
    return value;
  }
}
