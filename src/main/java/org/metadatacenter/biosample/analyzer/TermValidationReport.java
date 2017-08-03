package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class TermValidationReport {
  private final boolean isFromOntology;
  private final boolean isOwlClass;
  private final boolean iriResolves;

  public TermValidationReport(boolean isFromOntology, boolean isOwlClass, boolean iriResolves) {
    this.isFromOntology = checkNotNull(isFromOntology);
    this.isOwlClass = checkNotNull(isOwlClass);
    this.iriResolves = checkNotNull(iriResolves);
  }

  public boolean isFromOntology() {
    return isFromOntology;
  }

  public boolean isOwlClass() {
    return isOwlClass;
  }

  public boolean iriResolves() {
    return iriResolves;
  }

  public boolean isResolvableOntologyClass() {
    return isFromOntology && isOwlClass && iriResolves;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TermValidationReport)) {
      return false;
    }
    TermValidationReport that = (TermValidationReport) o;
    return isFromOntology == that.isFromOntology &&
        isOwlClass == that.isOwlClass &&
        iriResolves == that.iriResolves;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(isFromOntology, isOwlClass, iriResolves);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("isFromOntology", isFromOntology)
        .add("isOwlClass", isOwlClass)
        .add("iriResolves", iriResolves)
        .toString();
  }
}
