package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class AttributeValidationReport {
  @Nonnull private final Attribute attribute;
  private final boolean isFilledIn;
  private final boolean isValidFormat;
  private Optional<String> matchValue;

  public AttributeValidationReport(@Nonnull Attribute attribute, boolean isFilledIn, boolean isValidFormat, Optional<String> matchValue) {
    this.attribute = checkNotNull(attribute);
    this.isFilledIn = isFilledIn;
    this.isValidFormat = isValidFormat;
    this.matchValue = checkNotNull(matchValue);
  }

  @Nonnull
  public Attribute getAttribute() {
    return attribute;
  }

  public boolean isValid() {
    return isFilledIn && isValidFormat || !isFilledIn;
  }

  public boolean isFilledIn() {
    return isFilledIn;
  }

  public boolean isValidFormat() {
    return isValidFormat;
  }

  public Optional<String> getMatchValue() {
    return matchValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AttributeValidationReport)) {
      return false;
    }
    AttributeValidationReport that = (AttributeValidationReport) o;
    return isFilledIn == that.isFilledIn &&
        isValidFormat == that.isValidFormat &&
        Objects.equal(attribute, that.attribute);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(attribute, isFilledIn, isValidFormat);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("attribute", attribute)
        .add("isFilledIn", isFilledIn)
        .add("isValidFormat", isValidFormat)
        .toString();
  }
}
