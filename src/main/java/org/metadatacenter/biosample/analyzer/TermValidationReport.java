package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class TermValidationReport {
  @Nonnull private final String matchValue;
  private final boolean isFromOntology;
  private final boolean isOwlClass;
  private final boolean iriResolves;

  public TermValidationReport(@Nonnull String matchValue, boolean isFromOntology, boolean isOwlClass, boolean iriResolves) {
    this.matchValue = checkNotNull(matchValue);
    this.isFromOntology = checkNotNull(isFromOntology);
    this.isOwlClass = checkNotNull(isOwlClass);
    this.iriResolves = checkNotNull(iriResolves);
  }

  @Nonnull
  public String getMatchValue() {
    return matchValue;
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
        iriResolves == that.iriResolves &&
        Objects.equal(matchValue, that.matchValue);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(matchValue, isFromOntology, isOwlClass, iriResolves);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("matchValue", matchValue)
        .add("isFromOntology", isFromOntology)
        .add("isOwlClass", isOwlClass)
        .add("iriResolves", iriResolves)
        .toString();
  }
}
