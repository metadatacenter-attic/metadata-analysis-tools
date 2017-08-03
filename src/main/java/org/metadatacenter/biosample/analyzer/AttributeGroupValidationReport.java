package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class AttributeGroupValidationReport {
  @Nonnull private final String groupName;
  @Nonnull private final List<AttributeValidationReport> validationReports;

  public AttributeGroupValidationReport(@Nonnull String groupName, @Nonnull List<AttributeValidationReport> validationReports) {
    this.groupName = checkNotNull(groupName);
    this.validationReports = ImmutableList.copyOf(checkNotNull(validationReports));
  }

  @Nonnull
  public String getGroupName() {
    return groupName;
  }

  @Nonnull
  public List<AttributeValidationReport> getValidationReports() {
    return validationReports;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AttributeGroupValidationReport)) {
      return false;
    }
    AttributeGroupValidationReport that = (AttributeGroupValidationReport) o;
    return Objects.equal(groupName, that.groupName) &&
        Objects.equal(validationReports, that.validationReports);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(groupName, validationReports);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("groupName", groupName)
        .add("validationReports", validationReports)
        .toString();
  }
}
