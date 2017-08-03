package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class RecordBuilder {
  @Nonnull private String id;
  @Nonnull private String access;
  @Nonnull private String publicationDate;
  @Nonnull private String lastUpdate;
  @Nonnull private String submissionDate;
  @Nonnull private String accession;
  @Nonnull private String organismTaxonomyId;
  @Nonnull private String organismTaxonomyName;
  @Nonnull private String organismName = "";
  @Nonnull private String modelName;
  @Nonnull private String packageDisplayName;
  @Nonnull private String packageName;
  @Nonnull private String status;
  @Nonnull private String statusDate;
  @Nonnull private String ownerName;
  @Nonnull private Map<String,Attribute> attributes = new HashMap<>();
  @Nonnull private List<Link> links = new ArrayList<>();

  public RecordBuilder() {
    // no-arguments constructor
  }

  public RecordBuilder setId(@Nonnull String id) {
    this.id = checkNotNull(id);
    return this;
  }

  public RecordBuilder setAccess(@Nonnull String access) {
    this.access = checkNotNull(access);
    return this;
  }

  public RecordBuilder setPublicationDate(@Nonnull String publicationDate) {
    this.publicationDate = checkNotNull(publicationDate);
    return this;
  }

  public RecordBuilder setLastUpdate(@Nonnull String lastUpdate) {
    this.lastUpdate = checkNotNull(lastUpdate);
    return this;
  }

  public RecordBuilder setSubmissionDate(@Nonnull String submissionDate) {
    this.submissionDate = checkNotNull(submissionDate);
    return this;
  }

  public RecordBuilder setAccession(@Nonnull String accession) {
    this.accession = checkNotNull(accession);
    return this;
  }

  public RecordBuilder setOrganismTaxonomyId(@Nonnull String organismTaxonomyId) {
    this.organismTaxonomyId = checkNotNull(organismTaxonomyId);
    return this;
  }

  public RecordBuilder setOrganismTaxonomyName(@Nonnull String organismTaxonomyName) {
    this.organismTaxonomyName = checkNotNull(organismTaxonomyName);
    return this;
  }

  public RecordBuilder setOrganismName(@Nonnull String organismName) {
    this.organismName = checkNotNull(organismName);
    return this;
  }

  public RecordBuilder setModelName(@Nonnull String modelName) {
    this.modelName = checkNotNull(modelName);
    return this;
  }

  public RecordBuilder setPackageDisplayName(@Nonnull String packageDisplayName) {
    this.packageDisplayName = checkNotNull(packageDisplayName);
    return this;
  }

  public RecordBuilder setPackageName(@Nonnull String packageName) {
    this.packageName = checkNotNull(packageName);
    return this;
  }

  public RecordBuilder setStatus(@Nonnull String status) {
    this.status = checkNotNull(status);
    return this;
  }

  public RecordBuilder setStatusDate(@Nonnull String statusDate) {
    this.statusDate = checkNotNull(statusDate);
    return this;
  }

  public RecordBuilder setOwnerName(@Nonnull String ownerName) {
    this.ownerName = checkNotNull(ownerName);
    return this;
  }

  public RecordBuilder addAttribute(@Nonnull String harmonizedName, @Nonnull String name,
                                    @Nonnull String displayName, @Nonnull String value) {
    checkNotNull(harmonizedName);
    checkNotNull(name);
    checkNotNull(displayName);
    checkNotNull(value);
    return addAttribute(harmonizedName, new AttributeImpl(harmonizedName, name, displayName, value));
  }

  public RecordBuilder addAttribute(@Nonnull String attributeName, @Nonnull Attribute attribute) {
    attributes.put(checkNotNull(attributeName), checkNotNull(attribute));
    return this;
  }

  public RecordBuilder addLink(@Nonnull String type, @Nonnull String target, @Nonnull String label, @Nonnull String value) {
    checkNotNull(type);
    checkNotNull(target);
    checkNotNull(label);
    checkNotNull(value);
    return addLink(new Link(type, target, label, value));
  }

  public RecordBuilder addLink(@Nonnull Link link) {
    links.add(checkNotNull(link));
    return this;
  }

  public Record build() {
    return new RecordImpl(id, access, publicationDate, lastUpdate, submissionDate, accession, organismTaxonomyId,
        organismTaxonomyName, organismName, modelName, packageDisplayName, packageName, status, statusDate, ownerName,
        attributes, links);
  }
}
