package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public final class TermValidatorWrapper {
  @Nonnull private final List<String> terms;
  @Nonnull private final String bioPortalApiKey;

  public TermValidatorWrapper(@Nonnull List<String> terms, @Nonnull String bioPortalApiKey) {
    this.terms = checkNotNull(terms);
    this.bioPortalApiKey = checkNotNull(bioPortalApiKey);
  }

  @Nonnull
  public List<TermValidationReport> validate(@Nonnull SimpleTermCsvWriter writer) throws IOException {
    TermValidator val = new TermValidator(new BioPortalAgent(bioPortalApiKey));
    List<TermValidationReport> validationReports = new ArrayList<>();
    for(String term : terms) {
      TermValidationReport report = val.validateTerm(term, true);
      validationReports.add(report);
      writer.write(term, report);
    }
    return validationReports;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TermValidatorWrapper)) {
      return false;
    }
    TermValidatorWrapper that = (TermValidatorWrapper) o;
    return Objects.equal(terms, that.terms) &&
        Objects.equal(bioPortalApiKey, that.bioPortalApiKey);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(terms, bioPortalApiKey);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("terms", terms)
        .add("bioPortalApiKey", bioPortalApiKey)
        .toString();
  }
}
