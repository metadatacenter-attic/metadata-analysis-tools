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
public final class AttributeImpl implements Attribute {
  @Nonnull private final String harmonizedName;
  @Nonnull private final String attributeName;
  @Nonnull private final String displayName;
  @Nonnull private final String value;

  public AttributeImpl(@Nonnull String harmonizedName, @Nonnull String attributeName, @Nonnull String displayName, @Nonnull String value) {
    this.attributeName = checkNotNull(attributeName);
    this.harmonizedName = checkNotNull(harmonizedName);
    this.displayName = checkNotNull(displayName);
    this.value = checkNotNull(value);
  }

  @Override
  @Nonnull
  public String getName() {
    return harmonizedName;
  }

  @Override
  @Nonnull
  public String getAttributeName() {
    return attributeName;
  }

  @Override
  @Nonnull
  public String getDisplayName() {
    return displayName;
  }

  @Override
  @Nonnull
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AttributeImpl)) {
      return false;
    }
    AttributeImpl that = (AttributeImpl) o;
    return Objects.equal(harmonizedName, that.harmonizedName) &&
        Objects.equal(attributeName, that.attributeName) &&
        Objects.equal(displayName, that.displayName) &&
        Objects.equal(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(harmonizedName, attributeName, displayName, value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("harmonizedName", harmonizedName)
        .add("attributeName", attributeName)
        .add("displayName", displayName)
        .add("value", value)
        .toString();
  }
}
