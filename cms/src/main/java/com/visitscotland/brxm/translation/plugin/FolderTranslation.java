package com.visitscotland.brxm.translation.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.io.Serializable;

public class FolderTranslation implements Serializable {

    private static final long serialVersionUID = 1L;

    static final Logger log = LoggerFactory.getLogger(FolderTranslation.class);

    private final String id;
    private String name;
    private String url;

    private String namefr = "";
    private String urlfr = "";
    private boolean mutable = true;
    private boolean hasSameNameSibling = false;
    private String sameNameSiblingId;
    private boolean hasSameUrlSibling = false;
    private String sameUrlSiblingId;
    private Node sourceNode;
    private boolean isLinkedDocument = false;

    public FolderTranslation(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getUrlfr() {
        return urlfr;
    }

    public void setUrlfr(String url) {
        if (!mutable) {
            throw new UnsupportedOperationException("Translation is immutable");
        }
        this.urlfr = url;
    }

    public String getNamefr() {
        return namefr;
    }

    public void setNamefr(String name) {
        if (!mutable) {
            throw new UnsupportedOperationException("Translation is immutable");
        }
        this.namefr = name;
    }

    public void setEditable(boolean mutable) {
        this.mutable = mutable;
    }

    public boolean isEditable() {
        return mutable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHasSameNameSibling(boolean hasSameNameSibling) {
        this.hasSameNameSibling = hasSameNameSibling;
    }

    public boolean hasSameNameSibling() {
        return hasSameNameSibling;
    }

    public boolean hasSameUrlSibling() {
        return hasSameUrlSibling;
    }

    public void setHasSameUrlSibling(boolean hasSameUrlSibling) {
        this.hasSameUrlSibling = hasSameUrlSibling;
    }

    public String getSameUrlSiblingId() {
        return sameUrlSiblingId;
    }

    public String getSameNameSiblingId() {
        return sameNameSiblingId;
    }

    public void setSameNameSiblingId(String sameNameSiblingId) {
        this.sameNameSiblingId = sameNameSiblingId;
    }

    public void setSameUrlSiblingId(String sameUrlSiblingId) {
        this.sameUrlSiblingId = sameUrlSiblingId;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(Node sourceNode) {
        this.sourceNode = sourceNode;
    }

    public boolean isLinkedDocument() {
        return isLinkedDocument;
    }

    public void setIsLinkedDocument(boolean isLinkedDocument) {
        this.isLinkedDocument = isLinkedDocument;
    }
}
