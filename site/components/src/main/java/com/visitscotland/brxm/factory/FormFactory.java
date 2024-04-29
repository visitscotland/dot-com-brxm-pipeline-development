package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.Form;
import com.visitscotland.brxm.hippobeans.FormCompoundFepl;
import com.visitscotland.brxm.hippobeans.FormCompoundMarketo;
import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.model.FormModule;
import com.visitscotland.brxm.model.form.FeplConfiguration;
import com.visitscotland.brxm.model.form.MarketoConfiguration;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.SiteProperties;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.springframework.stereotype.Component;

@Component
public class FormFactory {

    private final SiteProperties properties;

    private final ContentLogger contentLogger;

    public FormFactory(SiteProperties properties, ContentLogger contentLogger) {
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
        cfg.setRecaptcha(properties.getFormsRecaptcha());
        cfg.setMarketoInstance(properties.getFormsMarketoUrl());
        cfg.setMunchkinId(properties.getFormsMarketoMunchkin());
        cfg.setScript(properties.getFormsMarketoScript());
        /** TODO: This property should go away
         * @see MarketoConfiguration.production
         */
        cfg.setProduction(properties.getFormsMarketoIsProduction());

        if (bean instanceof FormCompoundMarketo) {
            cfg.setJsonUrl(((FormCompoundMarketo) bean).getJsonUrl());
        } else if (bean instanceof MarketoForm){
            cfg.setJsonUrl(((MarketoForm) bean).getJsonUrl());
        }

        return cfg;
    }

    private FeplConfiguration getFeplConfiguration(FormCompoundFepl fepl){
        FeplConfiguration cfg = new FeplConfiguration();
        cfg.setRecaptcha(properties.getFormsRecaptcha());
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
