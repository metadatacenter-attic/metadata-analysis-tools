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
public final class Link {
  @Nonnull private final String type;
  @Nonnull private final String target;
  @Nonnull private final String label;
  @Nonnull private final String value;

  public Link(@Nonnull String type, @Nonnull String target, @Nonnull String label, @Nonnull String value) {
    this.type = checkNotNull(type);
    this.target = checkNotNull(target);
    this.label = checkNotNull(label);
    this.value = checkNotNull(value);
  }

  @Nonnull
  public String getType() {
    return type;
  }

  @Nonnull
  public String getTarget() {
    return target;
  }

  @Nonnull
  public String getLabel() {
    return label;
  }

  @Nonnull
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Link)) {
      return false;
    }
    Link that = (Link) o;
    return Objects.equal(type, that.type) &&
        Objects.equal(target, that.target) &&
        Objects.equal(label, that.label) &&
        Objects.equal(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(type, target, label, value);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("type", type)
        .add("target", target)
        .add("label", label)
        .add("value", value)
        .toString();
  }
}
