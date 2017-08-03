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
public final class RecordValidationReport {
  @Nonnull private final Record record;
  @Nonnull private final List<AttributeGroupValidationReport> attributeGroupValidationReports;

  public RecordValidationReport(@Nonnull Record record,
                                @Nonnull List<AttributeGroupValidationReport> attributeGroupValidationReports) {
    this.record = checkNotNull(record);
    this.attributeGroupValidationReports = ImmutableList.copyOf(checkNotNull(attributeGroupValidationReports));
  }

  @Nonnull
  public Record getMetadataRecord() {
    return record;
  }

  @Nonnull
  public List<AttributeGroupValidationReport> getAttributeGroupValidationReports() {
    return attributeGroupValidationReports;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RecordValidationReport)) {
      return false;
    }
    RecordValidationReport that = (RecordValidationReport) o;
    return Objects.equal(record, that.record) &&
        Objects.equal(attributeGroupValidationReports, that.attributeGroupValidationReports);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(record, attributeGroupValidationReports);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("bioSampleRecord", record)
        .add("attributeGroupValidationReports", attributeGroupValidationReports)
        .toString();
  }
}
