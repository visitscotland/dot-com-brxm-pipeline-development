package com.visitscotland.brxm.model.form;

public class FeplConfiguration implements  FormConfiguration {

    private String recaptcha;
    private String submitURL;

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
}
