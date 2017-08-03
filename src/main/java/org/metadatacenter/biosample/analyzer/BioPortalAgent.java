package org.metadatacenter.biosample.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
@Immutable
public final class BioPortalAgent {
  @Nonnull private static final String REST_URL = "http://data.bioontology.org";
  @Nonnull private final ObjectMapper mapper = new ObjectMapper();
  @Nonnull private final String bioportalApiKey;

  public BioPortalAgent(@Nonnull String bioportalApiKey) {
    this.bioportalApiKey = checkNotNull(bioportalApiKey);
  }

  @Nonnull
  public Optional<JsonNode> getResult(@Nonnull String searchString, boolean exactSearch) {
    String query = REST_URL + "/search?q=" + searchString + (exactSearch ? "&require_exact_match=true" : "") + "&page=1&pagesize=1";
    return runQuery(query);
  }

  @Nonnull
  public Optional<JsonNode> getResult(@Nonnull String searchString, boolean exactSearch, @Nonnull String ontologies) {
    String query = REST_URL + "/search?q=" + searchString + (exactSearch ? "&require_exact_match=true" : "") + "&ontologies="
        + ontologies + "&page=1&pagesize=1";
    return runQuery(query);
  }

  @Nonnull
  public Optional<JsonNode> runQuery(@Nonnull String query) {
    JsonNode node = jsonToNode(get(query));
    if(node != null) {
      return Optional.of(node.get("collection"));
    } else {
      return Optional.empty();
    }
  }

  // code adapted from https://github.com/ncbo/ncbo_rest_sample_code
  @Nullable
  private JsonNode jsonToNode(@Nonnull String json) {
    JsonNode root = null;
    try {
      if(!json.trim().isEmpty()) {
        root = mapper.readTree(json);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return root;
  }

  // code adapted from https://github.com/ncbo/ncbo_rest_sample_code
  @Nonnull
  private String get(@Nonnull String urlToGet) {
    URL url;
    HttpURLConnection conn;
    BufferedReader rd;
    String line;
    String result = "";
    try {
      url = new URL(urlToGet);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("Connection", "close");
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Authorization", "apikey token=" + bioportalApiKey);
      conn.setRequestProperty("Accept", "application/json");
      boolean connected;
      try {
        conn.connect();
        connected = true;
      } catch(ConnectException ce) {
        connected = false;
      }
      if (connected && conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
        InputStream stream = conn.getInputStream();
        rd = new BufferedReader(new InputStreamReader(stream));
        while ((line = rd.readLine()) != null) {
          result += line;
        }
        rd.close();
        stream.close();
      }
      if(connected) {
        conn.disconnect();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BioPortalAgent)) {
      return false;
    }
    BioPortalAgent that = (BioPortalAgent) o;
    return Objects.equal(mapper, that.mapper) &&
        Objects.equal(bioportalApiKey, that.bioportalApiKey);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(mapper, bioportalApiKey);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("mapper", mapper)
        .add("bioportalApiKey", bioportalApiKey)
        .toString();
  }
}
