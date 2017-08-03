package org.metadatacenter.biosample.analyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Rafael Gonçalves <br>
 * Center for Biomedical Informatics Research <br>
 * Stanford University
 */
public class RecordParser implements Parser {
  @Nonnull private static final Logger logger = LoggerFactory.getLogger(RecordParser.class.getName());
  @Nonnull private final Document document;
  @Nonnull private RecordBuilder recordBuilder;
  @Nonnull private List<Record> records = new ArrayList<>();


  public RecordParser(@Nonnull Document document) {
    this.document = checkNotNull(document);
  }

  public void processDocument() {
    NodeList sampleList = document.getElementsByTagName("BioSample");
    for(int i = 0; i < sampleList.getLength(); i++) {
      recordBuilder = new RecordBuilder();
      Node node = sampleList.item(i);
      Record record = null;
      try {
        record = processBioSample((Element) node);
      } catch(Exception e) {
        e.printStackTrace();
        logger.debug("Failing Node: " + node);
      }
      if(record != null) {
        records.add(record);
      }
    }
  }

  public Record processBioSample(Element element) {
    recordBuilder.setAccess(element.getAttribute("access"))
        .setPublicationDate(element.getAttribute("publication_date"))
        .setLastUpdate(element.getAttribute("last_update"))
        .setSubmissionDate(element.getAttribute("submission_date"))
        .setId(element.getAttribute("id"))
        .setAccession(element.getAttribute("accession"));

    NodeList children = element.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      String nodeName = child.getNodeName();
      if(nodeName.equalsIgnoreCase("description")) {
        getOrganismDescription((Element) child);
      } else if(nodeName.equalsIgnoreCase("owner")) {
        getOwner((Element) child);
      } else if(nodeName.equalsIgnoreCase("models")) {
        getModel((Element) child);
      } else if(nodeName.equalsIgnoreCase("package")) {
        getPackage((Element) child);
      } else if(nodeName.equalsIgnoreCase("attributes")) {
        getAttributes((Element) child);
      } else if(nodeName.equalsIgnoreCase("status")) {
        getStatus((Element) child);
      } else if(nodeName.equalsIgnoreCase("links")) {
        getLinks((Element) child);
      }
    }
    return recordBuilder.build();
  }

  private void getOrganismDescription(Element element) {
    NodeList children = element.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      if(child.getNodeName().equalsIgnoreCase("organism")) {
        Element organism = (Element) child;
        recordBuilder.setOrganismTaxonomyId(organism.getAttribute("taxonomy_id"))
            .setOrganismTaxonomyName(organism.getAttribute("taxonomy_name"));

        NodeList organismChildren = organism.getChildNodes();
        for(int j = 0; j < organismChildren.getLength(); j++) {
          Node organismChild = organismChildren.item(j);
          if(organismChild.getNodeName().equalsIgnoreCase("OrganismName")) {
            recordBuilder.setOrganismName(organismChild.getTextContent());
          }
        }
      }
    }
  }

  private void getOwner(Element element) {
    NodeList children = element.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      if(child.getNodeName().equalsIgnoreCase("name")) {
        recordBuilder.setOwnerName(child.getTextContent());
      }
    }
  }

  private void getModel(Element element) {
    NodeList children = element.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node child = children.item(i);
      if (child.getNodeName().equalsIgnoreCase("model")) {
        recordBuilder.setModelName(child.getTextContent());
      }
    }
  }

  private void getPackage(Element element) {
    recordBuilder.setPackageDisplayName(element.getAttribute("display_name"))
        .setPackageName(element.getTextContent());
  }

  private void getStatus(Element element) {
    recordBuilder.setStatus(element.getAttribute("status"))
        .setStatusDate(element.getAttribute("when"));
  }

  private void getLinks(Element element) {
    NodeList children = element.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node childNode = children.item(i);
      if (childNode.getNodeName().equalsIgnoreCase("link")) {
        Element child = (Element) childNode;
        String type = child.getAttribute("type");
        String label = child.getAttribute("label");
        String target = child.getAttribute("target");
        String value = child.getTextContent();
        recordBuilder.addLink(type, label, target, value);
      }
    }
  }

  private void getAttributes(Element element) {
    NodeList children = element.getChildNodes();
    for(int i = 0; i < children.getLength(); i++) {
      Node childNode = children.item(i);
      if(childNode.getNodeName().equalsIgnoreCase("attribute")) {
        Element child = (Element) childNode;
        String name = child.getAttribute("attribute_name");
        String harmonizedName = child.getAttribute("harmonized_name");
        String displayName = child.getAttribute("display_name");
        String value = child.getTextContent();
        if(!name.isEmpty() && !harmonizedName.isEmpty() && !displayName.isEmpty()) {
          recordBuilder.addAttribute(harmonizedName, name, displayName, value);
        }
      }
    }
  }

  @Override
  @Nonnull
  public List<Record> getBioSampleRecords() {
    return records;
  }
}
