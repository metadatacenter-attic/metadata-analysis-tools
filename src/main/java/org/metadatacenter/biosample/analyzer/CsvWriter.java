package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class CsvWriter {
  @Nonnull private final File outputFile;
  @Nonnull private BufferedWriter writer;

  public CsvWriter(@Nonnull File outputFile) {
    this.outputFile = checkNotNull(outputFile);
    initializeWriter();
  }

  private void initializeWriter() {
    try {
      writer = new BufferedWriter(new FileWriter(outputFile, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeRecord(@Nonnull Record record, boolean isValid,
                                       @Nonnull List<AttributeGroupValidationReport> validationReports) {
    checkNotNull(record);
    writeCell(record.getId());
    writeCell(record.getAccession());
    writeCell(record.getPublicationDate());
    writeCell(record.getLastUpdate());
    writeCell(record.getSubmissionDate());
    writeCell(record.getAccess());
    writeCell(record.getOrganismTaxonomyId());
    writeCell(record.getOrganismTaxonomyName());
    writeCell(record.getOrganismName());
    writeCell(record.getOwnerName());
    writeCell(record.getModelName());
    writeCell(record.getPackageDisplayName());
    writeCell(record.getPackageName());
    writeCell(record.getStatus());
    writeCell(record.getStatusDate());
    writeCell("" + isValid); // is record overall valid?

    // write attributes validation results
    for(AttributeGroupValidationReport groupValidationReport : validationReports) {
      writeAttributeGroupValidation(groupValidationReport);
    }
    newLine();
  }

  public void writeAttributeGroupValidation(@Nonnull AttributeGroupValidationReport attributeGroupValidationReport) {
    writeCell(attributeGroupValidationReport.getGroupName());
    for(AttributeValidationReport report : attributeGroupValidationReport.getValidationReports()) {
      writeAttributeValidation(report);
    }
  }

  public void writeAttributeValidation(@Nonnull AttributeValidationReport report) {
    writeCell(report.getAttribute().getName());
    writeCell(report.getAttribute().getValue());
    writeCell("" + report.isValid());
    writeCell("" + report.isFilledIn());
    writeCell("" + report.isValidFormat());
  }

  public void writeCell(@Nonnull String cellText, boolean includeComma) {
    // replace quotes from cell text with single quotes
    cellText = cellText.replaceAll("\"", "'");
    try {
      writer.write("\"" + cellText + "\"" + (includeComma ? "," : ""));
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeCell(@Nonnull String cellText) {
    writeCell(cellText, true);
  }

  public void newLine() {
    try {
      writer.write("\n");
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void closeWriter() {
    try {
      writer.close();
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
    return Objects.equal(outputFile, that.outputFile) &&
        Objects.equal(writer, that.writer);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(outputFile, writer);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("outputFile", outputFile)
        .add("writer", writer)
        .toString();
  }
}
