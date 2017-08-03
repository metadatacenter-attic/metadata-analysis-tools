package org.metadatacenter.biosample.analyzer;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class RecordImpl implements Record {
  @Nonnull private final String id;
  @Nonnull private final String access;
  @Nonnull private final String publicationDate;
  @Nonnull private final String lastUpdate;
  @Nonnull private final String submissionDate;
  @Nonnull private final String accession;
  @Nonnull private final String organismTaxonomyId;
  @Nonnull private final String organismTaxonomyName;
  @Nonnull private final String organismName;
  @Nonnull private final String modelName;
  @Nonnull private final String packageDisplayName;
  @Nonnull private final String packageName;
  @Nonnull private final String status;
  @Nonnull private final String statusDate;
  @Nonnull private final String ownerName;
  @Nonnull private final ImmutableMap<String,Attribute> attributes;
  @Nonnull private final ImmutableList<Link> links;

  /**
   * Package-private constructor. Use builder {@link RecordBuilder}
   */
  RecordImpl(@Nonnull String id, @Nonnull String access, @Nonnull String publicationDate,
             @Nonnull String lastUpdate, @Nonnull String submissionDate, @Nonnull String accession,
             @Nonnull String organismTaxonomyId, @Nonnull String organismTaxonomyName,
             @Nonnull String organismName, @Nonnull String modelName, @Nonnull String packageDisplayName,
             @Nonnull String packageName, @Nonnull String status, @Nonnull String statusDate,
             @Nonnull String ownerName, @Nonnull Map<String,Attribute> attributes, @Nonnull List<Link> links) {
   this.id = checkNotNull(id);
   this.access = checkNotNull(access);
   this.publicationDate = checkNotNull(publicationDate);
   this.lastUpdate = checkNotNull(lastUpdate);
   this.submissionDate = checkNotNull(submissionDate);
   this.accession = checkNotNull(accession);
   this.organismTaxonomyId = checkNotNull(organismTaxonomyId);
   this.organismTaxonomyName = checkNotNull(organismTaxonomyName);
   this.organismName = checkNotNull(organismName);
   this.modelName = checkNotNull(modelName);
   this.packageDisplayName = checkNotNull(packageDisplayName);
   this.packageName = checkNotNull(packageName);
   this.status = checkNotNull(status);
   this.statusDate = checkNotNull(statusDate);
   this.ownerName = checkNotNull(ownerName);
   this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
   this.links = ImmutableList.copyOf(checkNotNull(links));
  }

  @Override
  @Nonnull
  public String getId() {
    return id;
  }

  @Override
  @Nonnull
  public String getAccess() {
    return access;
  }

  @Override
  @Nonnull
  public String getPublicationDate() {
    return publicationDate;
  }

  @Override
  @Nonnull
  public String getLastUpdate() {
    return lastUpdate;
  }

  @Override
  @Nonnull
  public String getSubmissionDate() {
    return submissionDate;
  }

  @Override
  @Nonnull
  public String getAccession() {
    return accession;
  }

  @Override
  @Nonnull
  public String getOrganismTaxonomyId() {
    return organismTaxonomyId;
  }

  @Override
  @Nonnull
  public String getOrganismTaxonomyName() {
    return organismTaxonomyName;
  }

  @Override
  @Nonnull
  public String getOrganismName() {
    return organismName;
  }

  @Override
  @Nonnull
  public String getModelName() {
    return modelName;
  }

  @Override
  @Nonnull
  public String getPackageDisplayName() {
    return packageDisplayName;
  }

  @Override
  @Nonnull
  public String getPackageName() {
    return packageName;
  }

  @Override
  @Nonnull
  public String getStatus() {
    return status;
  }

  @Override
  @Nonnull
  public String getStatusDate() {
    return statusDate;
  }

  @Override
  @Nonnull
  public String getOwnerName() {
    return ownerName;
  }

  @Override
  @Nonnull
  public Map<String, Attribute> getAttributes() {
    return attributes;
  }

  @Override
  @Nonnull
  public List<Link> getLinks() {
    return links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RecordImpl)) {
      return false;
    }
    RecordImpl that = (RecordImpl) o;
    return Objects.equal(id, that.id) &&
        Objects.equal(access, that.access) &&
        Objects.equal(publicationDate, that.publicationDate) &&
        Objects.equal(lastUpdate, that.lastUpdate) &&
        Objects.equal(submissionDate, that.submissionDate) &&
        Objects.equal(accession, that.accession) &&
        Objects.equal(organismTaxonomyId, that.organismTaxonomyId) &&
        Objects.equal(organismTaxonomyName, that.organismTaxonomyName) &&
        Objects.equal(organismName, that.organismName) &&
        Objects.equal(modelName, that.modelName) &&
        Objects.equal(packageDisplayName, that.packageDisplayName) &&
        Objects.equal(packageName, that.packageName) &&
        Objects.equal(status, that.status) &&
        Objects.equal(statusDate, that.statusDate) &&
        Objects.equal(ownerName, that.ownerName) &&
        Objects.equal(attributes, that.attributes) &&
        Objects.equal(links, that.links);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, access, publicationDate, lastUpdate, submissionDate, accession, organismTaxonomyId,
        organismTaxonomyName, organismName, modelName, packageDisplayName, packageName, status, statusDate, ownerName, attributes, links);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("access", access)
        .add("publicationDate", publicationDate)
        .add("lastUpdate", lastUpdate)
        .add("submissionDate", submissionDate)
        .add("accession", accession)
        .add("organismTaxonomyId", organismTaxonomyId)
        .add("organismTaxonomyName", organismTaxonomyName)
        .add("organismName", organismName)
        .add("modelName", modelName)
        .add("packageDisplayName", packageDisplayName)
        .add("packageName", packageName)
        .add("status", status)
        .add("statusDate", statusDate)
        .add("ownerName", ownerName)
        .add("attributes", attributes)
        .add("links", links)
        .toString();
  }
}
