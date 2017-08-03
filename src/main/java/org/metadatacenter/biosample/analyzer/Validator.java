package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public interface Validator {

  @Nonnull
  RecordValidationReport validateBioSampleRecord(@Nonnull Record biosample) throws InvalidPackageException;

  boolean isValid(@Nonnull RecordValidationReport report);

}
