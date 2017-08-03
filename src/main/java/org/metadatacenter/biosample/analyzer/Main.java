package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

  public Main() {
    // no-arguments constructor
  }

  public Document parseDocument(File inputFile) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    logger.info("Parsing XML document...");
    Document doc = builder.parse(inputFile);
    logger.info("done");
    return doc;
  }

  public List<Record> getBioSampleRecords(Document doc) {
    logger.info("Extracting BioSample records from XML document");
    RecordParser parser = new RecordParser(doc);
    parser.processDocument();
    int nrRecords = parser.getBioSampleRecords().size();
    logger.info("done. Total records processed: " + nrRecords);
    return parser.getBioSampleRecords();
  }

  public File getFile(String filePath, boolean checkExists) throws IOException {
    if(filePath.trim().isEmpty()) {
      throw new IOException("Must provide a non-empty file path");
    }
    else if(checkExists && !new File(filePath).exists()) {
      throw new IOException("The given file does not exist: " + filePath);
    }
    else {
      return new File(filePath);
    }
  }

  public void validateDocument(File inputFile, File outputFile, String bioPortalApiKey) throws InvalidPackageException {
    Document doc = null;
    try {
      doc = parseDocument(inputFile);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
    }
    if(doc != null) {
      List<Record> records = getBioSampleRecords(doc);
      logger.info("Validating records...");
      long start = System.currentTimeMillis();
      validate(records, outputFile, bioPortalApiKey);
      long end = System.currentTimeMillis();
      logger.info("done " + (end - start) / 1000.0 + " secs");
    }
  }

  // TODO: split out CSV writing to a separate method. These validation methods should return a set of RecordValidationReport each.

  public void validate(List<Record> records, File outputFile, String bioPortalApiKey) throws InvalidPackageException {
    CsvWriter csvWriter = new CsvWriter(outputFile);
    GenericValidator validator = new GenericValidator(new TermValidator(new BioPortalAgent(bioPortalApiKey)));
    DecimalFormat df = new DecimalFormat("#");
    int nrRecords = records.size();
    double counter = 0;
    double percentage = 0;
    for (Record record : records) {
      counter++;
      double percentageNew = Double.valueOf(df.format((counter/nrRecords)*100));
      if(percentageNew > percentage) {
        logger.info("\t" + (int) percentage + "%  (record #" + (int) counter + ")");
      }
      percentage = percentageNew;
      RecordValidationReport report = validator.validateBioSampleRecord(record);
      boolean isValid = validator.isValid(report);
      csvWriter.writeRecord(record, isValid, report.getAttributeGroupValidationReports());
    }
    csvWriter.closeWriter();
  }

  public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, InvalidPackageException {
    String inputFilePath = args[0];
    String outputFilePath = args[1];
    String bioPortalApiKey = args[2];
    logger.info("Input file path: " + inputFilePath);
    logger.info("Output file path: " + outputFilePath);

    Main main = new Main();
    File inputFile = main.getFile(inputFilePath, true);
    File outputFile = main.getFile(outputFilePath, false);
    main.validateDocument(inputFile, outputFile, bioPortalApiKey);
  }
}
