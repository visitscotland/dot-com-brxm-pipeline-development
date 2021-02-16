package com.visitscotland.brxm.beans.mapping.megalinks;

import com.visitscotland.brxm.beans.Megalinks;
import com.visitscotland.brxm.beans.mapping.FlatLink;
import com.visitscotland.brxm.beans.mapping.Module;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;

public class LinksModule<L extends FlatLink> extends Module<Megalinks> {

    private String title;
    private HippoHtml introduction;
    private List<L> links;
    private FlatLink cta;
    private String theme;
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

    /**
     * @deprecated  use getHippoBean instead
     */
    @Deprecated
    public HippoBean getMegalinkItem() {
        return getHippoBean();
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }
}
