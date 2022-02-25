package com.visitscotland.brxm.model.megalinks;

import com.visitscotland.brxm.hippobeans.Megalinks;
import com.visitscotland.brxm.model.FlatLink;
import com.visitscotland.brxm.model.Module;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;

public class LinksModule<L extends FlatLink> extends Module<Megalinks> {

    private String title;
    private HippoHtml introduction;
    private List<L> links;
    private FlatLink cta;
    private Integer themeIndex;
    private String alignment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtml getIntroduction() {
        return introduction;
    }

    public void setIntroduction(HippoHtml introduction) {
        this.introduction = introduction;
    }

    public List<L> getLinks() {
        return links;
    }

    public void setLinks(List<L> links) {
        this.links = links;
    }

    public FlatLink getCta() {
        return cta;
    }

    public void setCta(FlatLink cta) {
        this.cta = cta;
    }

    public Integer getThemeIndex() {
        return themeIndex;
    }

    public void setThemeIndex(Integer themeIndex) {
        this.themeIndex = themeIndex;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }
}
