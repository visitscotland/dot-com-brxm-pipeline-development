package com.visitscotland.brxm.model.form;


/**
 * @deprecated Marketo is currently been retired from VisitScotland forms in favour of BloomReach Engagement
 *
 *
 */
@Deprecated(since = "12/03/2024")
public class MarketoConfiguration implements  FormConfiguration {

    private String munchkinId;
    private String marketoInstance;
    private String script;
    private String recaptcha;
    private String jsonUrl;

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
}
