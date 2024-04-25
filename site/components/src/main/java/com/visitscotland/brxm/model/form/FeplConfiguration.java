package com.visitscotland.brxm.model.form;

public class FeplConfiguration implements  FormConfiguration {

    /**
     * Indicates the type of form to the front-end. Do not change this value unless it is agreed with UI developers
     */
    private static final String FORM_TYPE = "fepl";

    private String recaptcha;
    private String submitURL;

    private String jsonUrl;

    public String getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(String recaptcha) {
        this.recaptcha = recaptcha;
    }

    public String getSubmitURL() {
        return submitURL;
    }

    public void setSubmitURL(String submitURL) {
        this.submitURL = submitURL;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    @Override
    public String getType() {
        return FORM_TYPE;
    }
}
