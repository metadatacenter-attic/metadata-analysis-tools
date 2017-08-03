package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public interface Parser {

  void processDocument();

  @Nonnull
  List<Record> getBioSampleRecords();

}
