package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.Stackla;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class UserGeneratedContentModule extends Module<Stackla> {

    private String title;
    private HippoHtml copy;
    private String storystreamId;
    private String noCookiesMessage;
    private String noJsMessage;
    private String noCookiesLinkLabel;

    public void setNoCookiesMessage(String noCookiesMessage) {
        this.noCookiesMessage = noCookiesMessage;
    }

    public void setNoJsMessage(String noJsMessage) {
        this.noJsMessage = noJsMessage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCopy(HippoHtml copy) {
        this.copy = copy;
    }

    public String getStorystreamId() {
        return storystreamId;
    }

    public void setStorystreamId(String storystreamId) {
        this.storystreamId = storystreamId;
    }

    public void setNoCookiesLinkLabel(String noCookiesLinkLabel) {
        this.noCookiesLinkLabel = noCookiesLinkLabel;
    }

    public String getTitle() {
        return title;
    }

    public HippoHtml getCopy() {
        return copy;
    }


     public String getNoCookiesMessage() {
        return noCookiesMessage;
    }

    public String getNoJsMessage() {
        return noJsMessage;
    }

    public String getNoCookiesLinkLabel() {
        return noCookiesLinkLabel;
    }

}
