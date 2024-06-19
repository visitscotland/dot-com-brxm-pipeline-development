package com.visitscotland.brxm.model.form;

public class BregConfiguration implements  FormConfiguration {

    /**
     * Indicates the type of form to the front-end. Do not change this value unless it is agreed with UI developers
     */
    private static final String FORM_TYPE = "breg";

    private String recaptcha;
    private String submitUrl;
    private String jsonUrl;
    private String activityCode;
    private String activityDescription;
    private String activitySource;
    private String consents;
    private String legalBasis;



    public String getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(String recaptcha) {
        this.recaptcha = recaptcha;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivitySource() {
        return activitySource;
    }

    public void setActivitySource(String activitySource) {
        this.activitySource = activitySource;
    }

    public String getConsents() {
        return consents;
    }

    public void setConsents(String consents) {
        this.consents = consents;
    }

    @Override
    public String getType() {
        return FORM_TYPE;
    }

    public String getLegalBasis() {
        return legalBasis;
    }

    public void setLegalBasis(String legalBasis) {
        this.legalBasis = legalBasis;
    }
}
