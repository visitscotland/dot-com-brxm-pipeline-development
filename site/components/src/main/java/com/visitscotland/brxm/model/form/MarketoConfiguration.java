package com.visitscotland.brxm.model.form;


/**
 * @deprecated Marketo is currently been retired from VisitScotland forms in favour of BloomReach Engagement
 *
 *
 */
@Deprecated(since = "12/03/2024")
public class MarketoConfiguration implements  FormConfiguration {

    /**
     * Indicates the type of form to the front-end. Do not change this value unless it is agreed with UI developers
     */
    private static final String FORM_TYPE = "marketo";

    private String munchkinId;
    private String marketoInstance;
    private String script;
    private String recaptcha;
    private String jsonUrl;

    /**
     * It allows Marketo to difference between sandbox and live.
     *
     * This information should probably go into the back end but since this form type is in process of being retired,
     * it is not worth the change
     */
    private boolean production;

    public String getMunchkinId() {
        return munchkinId;
    }

    public void setMunchkinId(String munchkinId) {
        this.munchkinId = munchkinId;
    }

    public String getMarketoInstance() {
        return marketoInstance;
    }

    public void setMarketoInstance(String marketoInstance) {
        this.marketoInstance = marketoInstance;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getRecaptcha() {
        return recaptcha;
    }

    public void setRecaptcha(String recaptcha) {
        this.recaptcha = recaptcha;
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

    public boolean isProduction() {
        return production;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }
}
