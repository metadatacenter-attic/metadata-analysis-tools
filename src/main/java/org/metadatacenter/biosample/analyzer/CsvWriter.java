package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class CsvWriter {
  @Nonnull private final File outputFolder;
  @Nonnull private BufferedWriter recordWriter;
  @Nonnull private BufferedWriter attributeWriter;
  @Nonnull private final static Pattern p = Pattern.compile("\"");
  private int attributeCounter = 1;

  public CsvWriter(@Nonnull File outputFolder) {
    this.outputFolder = checkNotNull(outputFolder);
    initializeWriters();
  }

  private void initializeWriters() {
    try {
      String outputFolder = "";
      if(!this.outputFolder.getAbsolutePath().endsWith(File.separator)) {
        outputFolder = this.outputFolder + File.separator;
      }
      recordWriter = new BufferedWriter(new FileWriter(outputFolder + "biosamaple-records.csv", true));
      attributeWriter = new BufferedWriter(new FileWriter(outputFolder + "biosamaple-attributes.csv", true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeRecord(@Nonnull Record record, boolean isValid,
                                       @Nonnull List<AttributeGroupValidationReport> validationReports) {
    checkNotNull(record);
    writeCell(recordWriter, record.getId());
    writeCell(recordWriter, record.getAccession());
    writeCell(recordWriter, record.getPublicationDate());
    writeCell(recordWriter, record.getLastUpdate());
    writeCell(recordWriter, record.getSubmissionDate());
    writeCell(recordWriter, record.getAccess());
    writeCell(recordWriter, record.getOrganismTaxonomyId());
    writeCell(recordWriter, record.getOrganismTaxonomyName());
    writeCell(recordWriter, record.getOrganismName());
    writeCell(recordWriter, record.getOwnerName());
    writeCell(recordWriter, record.getModelName());
    writeCell(recordWriter, record.getPackageDisplayName());
    writeCell(recordWriter, record.getPackageName());
    writeCell(recordWriter, record.getStatus());
    writeCell(recordWriter, record.getStatusDate());
    writeCell(recordWriter, "" + isValid); // is record overall valid?

    // write attributes validation results
    for(AttributeGroupValidationReport groupValidationReport : validationReports) {
      writeAttributeGroupValidation(groupValidationReport, record.getId());
    }
    writeNewLine(recordWriter);
  }

  public void writeAttributeGroupValidation(@Nonnull AttributeGroupValidationReport attributeGroupValidationReport, @Nonnull String recordId) {
    String attributeType = attributeGroupValidationReport.getGroupName();
    for(AttributeValidationReport report : attributeGroupValidationReport.getValidationReports()) {
      writeAttributeValidation(report, recordId, attributeType);
    }
  }

  public void writeAttributeValidation(@Nonnull AttributeValidationReport report, @Nonnull String recordId, @Nonnull String attributeType) {
    writeCell(attributeWriter, "" + attributeCounter);
    writeCell(attributeWriter, recordId);
    writeCell(attributeWriter, attributeType);
    writeCell(attributeWriter, report.getAttribute().getName());
    writeCell(attributeWriter, report.getAttribute().getAttributeName());
    writeCell(attributeWriter, report.getAttribute().getDisplayName());
    writeCell(attributeWriter, report.getAttribute().getValue());
    writeCell(attributeWriter, "" + report.isValid());
    writeCell(attributeWriter, "" + report.isFilledIn());
    writeCell(attributeWriter, "" + report.isValidFormat());
    report.getMatchValue().ifPresent(match -> writeCell(attributeWriter, match));
    writeNewLine(attributeWriter);
    attributeCounter++;
  }

  public void writeCell(@Nonnull Writer writer, @Nonnull String cellText, boolean includeComma) {
    // replace quotes from cell text with single quotes
    String cellTextClean = p.matcher(cellText).replaceAll("'");
    try {
      String output = "\"" + cellTextClean + "\"" + (includeComma ? "," : "");
      writer.write(output);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeCell(@Nonnull Writer writer, @Nonnull String cellText) {
    writeCell(writer, cellText, true);
  }

  public void writeNewLine(Writer writer) {
    try {
      writer.write("\n");
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void closeWriters() {
    try {
      recordWriter.close();
      attributeWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CsvWriter)) {
      return false;
    }
    CsvWriter that = (CsvWriter) o;
    return Objects.equal(outputFolder, that.outputFolder) &&
        Objects.equal(recordWriter, that.recordWriter);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(outputFolder, recordWriter);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("outputFolder", outputFolder)
        .add("recordWriter", recordWriter)
        .toString();
  }
}
