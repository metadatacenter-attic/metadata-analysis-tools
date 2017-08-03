package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;

/**
 * A representation of a BioSample attribute.
 *
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public interface Attribute {

  /**
   * Get the harmonized name of the attribute. This is the the attribute name that is consistently used.
   *
   * @return String representing the harmonized name of the attribute
   */
  @Nonnull
  String getName();

  /**
   * Get the name of the attribute. Some BioSample records use a name that is different from the harmonized name.
   *
   * @return String representing the name of the attribute
   */
  @Nonnull
  String getAttributeName();

  /**
   * Get the display name of the attribute
   *
   * @return String representing the display name of the attribute
   */
  @Nonnull
  String getDisplayName();

  /**
   * Get the value of the attribute
   *
   * @return String representing the value of the attribute
   */
  @Nonnull
  String getValue();

}
