package org.metadatacenter.biosample.analyzer;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class InvalidPackageException extends Exception {

  public InvalidPackageException() {
    super();
  }

  public InvalidPackageException(String message) {
    super(message);
  }
}
