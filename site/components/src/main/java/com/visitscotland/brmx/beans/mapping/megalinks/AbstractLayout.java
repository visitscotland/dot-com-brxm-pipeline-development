package com.visitscotland.brmx.beans.mapping.megalinks;

import com.visitscotland.brmx.beans.Megalinks;
import com.visitscotland.brmx.beans.mapping.FlatLink;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.List;

public abstract class AbstractLayout<L extends FlatLink> {

    private String title;
    private HippoHtml introduction;
    private List<L> links;
    private FlatLink cta;
    private Megalinks megalinkItem;
    private String style;

    //TODO move this method to the module class
    public String getType(){
        return getClass().getSimpleName();
    }

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

    public Megalinks getMegalinkItem() {
        return megalinkItem;
    }

    public void setMegalinkItem(Megalinks megalinkItem) {
        this.megalinkItem = megalinkItem;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
