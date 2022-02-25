package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.Stackla;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class StacklaModule extends Module<Stackla> {

    private String title;
    private HippoHtml copy;
    private String dataId;
    private String dataHash;
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

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
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

    public String getDataId() {
        return dataId;
    }

    public String getDataHash() {
        return dataHash;
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
