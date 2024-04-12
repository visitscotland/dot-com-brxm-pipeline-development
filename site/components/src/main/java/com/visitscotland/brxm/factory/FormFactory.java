package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Form;
import com.visitscotland.brxm.hippobeans.FormCompoundFepl;
import com.visitscotland.brxm.hippobeans.FormCompoundMarketo;
import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.model.FormModule;
import com.visitscotland.brxm.model.form.FeplConfiguration;
import com.visitscotland.brxm.model.form.MarketoConfiguration;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.springframework.stereotype.Component;

@Component
public class FormFactory {

    static final String PROP_RECAPTCHA = "form.recaptcha-key";
    static final String PROP_MARKETO_URL = "form.maketo.instance-url";
    static final String PROP_MARKETO_MUNCHKIN = "form.marketo.munchkin";
    static final String PROP_MARKETO_SCRIPT = "form.marketo.script";
    static final String PROP_MARKETO_IS_PRODUCTION = "form.is-production";

    private final Properties properties;

    private final ContentLogger contentLogger;

    public FormFactory(Properties properties, ContentLogger contentLogger) {
        this.properties = properties;
        this.contentLogger = contentLogger;
    }

    public FormModule getModule(MarketoForm document) {
        MarketoConfiguration cfg = getMarketoConfiguration(document);

        FormModule module = new FormModule();
        module.setConfig(cfg);
        module.setTitle(document.getTitle());
        module.setJsonUrl(document.getJsonUrl());
        module.setNoJavaScriptMessage(document.getNonJavaScriptMessage());
        module.setCopy(document.getCopy());

        return module;
    }

    private MarketoConfiguration getMarketoConfiguration(HippoBean bean) {
        MarketoConfiguration cfg = new MarketoConfiguration();
        cfg.setRecaptcha(properties.getProperty(PROP_RECAPTCHA));
        cfg.setMarketoInstance(properties.getProperty(PROP_MARKETO_URL));
        cfg.setMunchkinId(properties.getProperty(PROP_MARKETO_MUNCHKIN));
        cfg.setScript(properties.getProperty(PROP_MARKETO_SCRIPT));
        /** TODO: This property should go away
         * @see MarketoConfiguration.production
         */
        cfg.setProduction(properties.readBoolean(PROP_MARKETO_IS_PRODUCTION));

        if (bean instanceof FormCompoundMarketo) {
            cfg.setJsonUrl(((FormCompoundMarketo) bean).getJsonUrl());
        } else if (bean instanceof MarketoForm){
            cfg.setJsonUrl(((MarketoForm) bean).getJsonUrl());
        }

        return cfg;
    }

    private FeplConfiguration getFeplConfiguration(FormCompoundFepl fepl){
        FeplConfiguration cfg = new FeplConfiguration();
        cfg.setRecaptcha(properties.getProperty(PROP_RECAPTCHA));
        cfg.setSubmitURL(fepl.getUrl());
        cfg.setJsonUrl(fepl.getJsonURL());

        return cfg;
    }

    public FormModule getModule(Form document) {
        HippoCompound cfg = document.getFormConfiguration();

        FormModule module = new FormModule();
        module.setTitle(document.getTitle());
        module.setNoJavaScriptMessage(document.getNonJavaScriptMessage());
        module.setCopy(document.getCopy());

        if (cfg instanceof FormCompoundFepl) {
            module.setConfig(getFeplConfiguration((FormCompoundFepl) document.getFormConfiguration()));
        } else if (cfg instanceof FormCompoundMarketo){
            module.setConfig(getMarketoConfiguration(document.getFormConfiguration()));
        } else {
            contentLogger.warn("The Form document '{}' does not have a valid configuration. It won't appear on the page", document.getPath());
            return null;
        }

        return module;
    }

}
