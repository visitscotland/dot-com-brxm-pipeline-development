package com.visitscotland.brxm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.model.form.FormConfiguration;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class FormModule extends Module<MarketoForm> {

    private String title;
    private String jsonUrl;
    private String noJavaScriptMessage;
    private HippoHtml copy;

    private FormConfiguration config;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    /**
     * @deprecated Marketo forms are about to be removed. This method will be deleted after MarketoForm class is retired
     */
    @Deprecated(forRemoval = true)
    @JsonIgnore
    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public String getNoJavaScriptMessage() {
        return noJavaScriptMessage;
    }

    public void setNoJavaScriptMessage(String noJavaScriptMessage) {
        this.noJavaScriptMessage = noJavaScriptMessage;
    }

    public HippoHtml getCopy() {
        return copy;
    }

    public void setCopy(HippoHtml copy) {
        this.copy = copy;
    }

    public FormConfiguration getConfig() {
        return config;
    }

    public void setConfig(FormConfiguration config) {
        this.config = config;
    }
}
