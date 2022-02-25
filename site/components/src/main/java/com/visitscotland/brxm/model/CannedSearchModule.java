package com.visitscotland.brxm.model;

import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class CannedSearchModule extends Module{

    private String title;
    private String productType;
    private String credit;
    private HippoHtml description;
    private String cannedSearchEndpoint;
    private FlatLink viewAllLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public HippoHtml getDescription() {
        return description;
    }

    public void setDescription(HippoHtml description) {
        this.description = description;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public FlatLink getViewAllLink() {
        return viewAllLink;
    }

    public void setViewAllLink(FlatLink viewAllLink) {
        this.viewAllLink = viewAllLink;
    }

    public String getCannedSearchEndpoint() {
        return cannedSearchEndpoint;
    }

    public void setCannedSearchEndpoint(String cannedSearchEndpoint) {
        this.cannedSearchEndpoint = cannedSearchEndpoint;
    }
}
