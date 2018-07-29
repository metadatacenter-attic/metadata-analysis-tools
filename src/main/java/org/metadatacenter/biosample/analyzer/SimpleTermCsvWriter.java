package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public final class SimpleTermCsvWriter {
  @Nonnull private BufferedWriter writer;

  public SimpleTermCsvWriter(@Nonnull String outputFolder, @Nonnull String outputFileName) {
    initializeWriter(checkNotNull(outputFolder), checkNotNull(outputFileName));
  }

  private void initializeWriter(@Nonnull String outputFolder, @Nonnull String outputFileName) {
    String outputPath = outputFolder;
    if (!outputFolder.endsWith(File.separator)) {
      outputPath += File.separator;
    }
    try {
      writer = new BufferedWriter(new FileWriter(outputPath + outputFileName, true));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void write(String value, TermValidationReport report) throws IOException {
    writer.write("\"" + value + "\"");
    writer.write(",");
    writer.write("" + report.isResolvableOntologyClass());
    writer.write(",");
    writer.write("\"" + report.getMatchValue() + "\"");
    writer.write("\n");
    writer.flush();
  }

}
