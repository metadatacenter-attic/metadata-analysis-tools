package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public interface Record {

  @Nonnull
  String getId();

  @Nonnull
  String getAccess();

  @Nonnull
  String getPublicationDate();

  @Nonnull
  String getLastUpdate();

  @Nonnull
  String getSubmissionDate();

  @Nonnull
  String getAccession();

  @Nonnull
  String getOrganismTaxonomyId();

  @Nonnull
  String getOrganismTaxonomyName();

  @Nonnull
  String getOrganismName();

  @Nonnull
  String getModelName();

  @Nonnull
  String getPackageDisplayName();

  @Nonnull
  String getPackageName();

  @Nonnull
  String getStatus();

  @Nonnull
  String getStatusDate();

  @Nonnull
  String getOwnerName();

  @Nonnull
  Map<String, Attribute> getAttributes();

  @Nonnull
  List<Link> getLinks();

}
