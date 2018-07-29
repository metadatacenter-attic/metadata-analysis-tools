package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public final class TimestampAnalyzer {
  @Nonnull private final File timestampFile;

  public TimestampAnalyzer(@Nonnull File timestampFile) {
    this.timestampFile = checkNotNull(timestampFile);
  }

  @Nonnull
  public List<TermCheckResult> analyseTimestamps() throws IOException {
    List<String> timestamps = Utils.parseFile(timestampFile);
    return validate(timestamps);
  }

  @Nonnull
  public List<TermCheckResult> analyseTimestamps(@Nonnull String outputFile) throws IOException {
    List<TermCheckResult> results = analyseTimestamps();
    serialize(results, outputFile);
    return results;
  }

  @Nonnull
  private List<TermCheckResult> validate(@Nonnull List<String> values) {
    List<TermCheckResult> output = new ArrayList<>();
    GenericValidator val = new GenericValidator(new TermValidator(new BioPortalAgent("")));
    for(String s : values) {
      boolean isValid = val.isValidDateFormat(s);
      output.add(new TermCheckResult(s, isValid));
    }
    return output;
  }

  private void serialize(List<TermCheckResult> list, String outputPath) throws IOException {
    File output = new File(outputPath);
    BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
    for(TermCheckResult r : list) {
      writer.write("\"" + r.getKey() + "\"");
      writer.write(",");
      writer.write("\"" + String.valueOf(r.getValue()) + "\"");
      writer.write("\n");
      writer.flush();
    }
    writer.close();
  }
}
