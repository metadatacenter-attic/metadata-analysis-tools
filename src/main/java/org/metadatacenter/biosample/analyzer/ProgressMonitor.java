package org.metadatacenter.biosample.analyzer;

import javax.annotation.Nonnull;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class ProgressMonitor {
  @Nonnull private final Integer total;
  private Double done;

  public ProgressMonitor(@Nonnull Collection<?> collection) {
    checkNotNull(collection, "Collection of objects must not be null");
    total = collection.size();
  }

  @Nonnull
  public Integer incrementProgress() {
    if (done == null) {
      done = 1.0;
    } else {
      done++;
    }
    return getPercentDone();
  }

  @Nonnull
  public Integer getPercentDone() {
    return (int) ((done / total) * 100.0);
  }
}
