package com.visitscotland.brxm.model.sitemap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sitemap implements Serializable {

    private List<SitemapEntry> pages;

    public List<SitemapEntry> getPages() {
        return pages;
    }

    public void setPages(List<SitemapEntry> pages) {
        this.pages = pages;
    }

    public void addPage(SitemapEntry entry){
        if (pages == null){
            pages = new ArrayList<>();
        }
        pages.add(entry);
    }
}
