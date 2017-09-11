package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class BioSampleAnalyzer {
  @Nonnull private static final Logger logger = LoggerFactory.getLogger(BioSampleAnalyzer.class.getName());
  @Nonnull private final Validator validator;

  public BioSampleAnalyzer(@Nonnull Validator validator) {
    this.validator = checkNotNull(validator);
  }

  @Nonnull
  public List<RecordValidationReport> validate(@Nonnull List<Record> records, @Nonnull File outputFolder) {
    checkNotNull(records); checkNotNull(outputFolder);
    List<RecordValidationReport> reports = new ArrayList<>();
    CsvWriter csvWriter = new CsvWriter(outputFolder);
    ProgressMonitor monitor = new ProgressMonitor(records);
    logger.info("Validating records...");
    long start = System.currentTimeMillis();
    int recordCounter = 0;  // counters
    double percentDone = 0;
    for (Record record : records) {
      recordCounter++;  // increment counters
      monitor.incrementProgress();
      if(monitor.getPercentDone() > percentDone) {
        logger.info("\t" + monitor.getPercentDone() + "%  (record #" + recordCounter + ")");
      }
      RecordValidationReport report = validator.validateBioSampleRecord(record);
      reports.add(report);
      csvWriter.writeRecord(record, validator.isValid(report), report.getAttributeGroupValidationReports());
    }
    logger.info("done " + (System.currentTimeMillis() - start) / 1000.0 + " secs");
    csvWriter.closeWriters();
    return reports;
  }

  @Nonnull
  public Optional<Document> parseDocument(@Nonnull File inputFile) {
    checkNotNull(inputFile);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    Document doc = null;
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      logger.info("Parsing XML document...");
      doc = builder.parse(inputFile);
      logger.info("done");
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
    return Optional.ofNullable(doc);
  }

  @Nonnull
  public List<Record> getBioSampleRecords(@Nonnull Document doc) {
    checkNotNull(doc);
    logger.info("Extracting BioSample records from XML document");
    RecordParser parser = new RecordParser(doc);
    parser.processDocument();
    int nrRecords = parser.getBioSampleRecords().size();
    logger.info("done. Total records processed: " + nrRecords);
    return parser.getBioSampleRecords();
  }

}
