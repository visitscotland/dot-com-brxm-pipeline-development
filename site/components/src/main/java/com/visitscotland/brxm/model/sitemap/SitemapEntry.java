package com.visitscotland.brxm.model.sitemap;

import java.io.Serializable;
import java.util.Date;

public class SitemapEntry implements Serializable {
    private String url;
    private Date lastModification;

    public SitemapEntry() {

    }

    public SitemapEntry(String url, Date lastModification) {
        this.url = url;
        this.lastModification = lastModification;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }
}
