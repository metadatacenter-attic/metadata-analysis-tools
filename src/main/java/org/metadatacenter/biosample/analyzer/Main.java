package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  private static File getFile(String filePath) throws IOException {
    if(filePath.trim().isEmpty()) {
      throw new IOException("Must provide a non-empty file path");
    }
    else {
      File f = new File(filePath);
      f.mkdirs();
      return f;
    }
  }

  public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, InvalidPackageException {
    String inputFilePath = args[0];
    String outputFolderPath = args[1];
    String bioPortalApiKey = args[2];
    logger.info("Input file: " + inputFilePath);
    logger.info("Output folder: " + outputFolderPath);

    File inputFile = getFile(inputFilePath);
    File outputFolder = getFile(outputFolderPath);

    Validator validator = new GenericValidator(new TermValidator(new BioPortalAgent(bioPortalApiKey)));
    BioSampleAnalyzer analyzer = new BioSampleAnalyzer(validator);
    analyzer.parseDocument(inputFile).ifPresent(doc -> {
      List<Record> records = analyzer.getBioSampleRecords(doc);
      analyzer.validate(records, outputFolder);
    });
  }
}
