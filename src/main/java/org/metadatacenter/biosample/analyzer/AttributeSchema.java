package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public final class AttributeSchema {
  @Nonnull private final String name;
  @Nonnull private final AttributeType type;
  @Nonnull private final List<String> values;

  public AttributeSchema(@Nonnull String name, @Nonnull AttributeType type, @Nonnull List<String> values) {
    this.name = checkNotNull(name);
    this.type = checkNotNull(type);
    this.values = ImmutableList.copyOf(checkNotNull(values));
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public AttributeType getType() {
    return type;
  }

  // This list represents the possible input values for value-set-type fields, or ontologies to fetch input values from
  @Nonnull
  public List<String> getValues() {
    return values;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AttributeSchema)) {
      return false;
    }
    AttributeSchema that = (AttributeSchema) o;
    return Objects.equal(name, that.name) &&
        type == that.type &&
        Objects.equal(values, that.values);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, type, values);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("name", name)
        .add("type", type)
        .add("values", values)
        .toString();
  }
}
