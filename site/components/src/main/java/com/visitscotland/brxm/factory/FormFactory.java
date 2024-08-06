package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.*;
import com.visitscotland.brxm.model.FormModule;
import com.visitscotland.brxm.model.form.BregConfiguration;
import com.visitscotland.brxm.model.form.FeplConfiguration;
import com.visitscotland.brxm.model.form.MarketoConfiguration;
import com.visitscotland.brxm.utils.ContentLogger;
import com.visitscotland.brxm.utils.SiteProperties;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.springframework.stereotype.Component;

import java.util.List;

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
        cfg.setSubmitUrl(fepl.getUrl());
        cfg.setJsonUrl(fepl.getJsonURL());

        return cfg;
    }

    private BregConfiguration getBregConfiguration(FormCompoundBreg breg){
        BregConfiguration cfg = new BregConfiguration();
        cfg.setRecaptcha(properties.getFormsRecaptcha());
        cfg.setSubmitUrl(breg.getUrl());
        cfg.setJsonUrl(breg.getJsonUrl());
        cfg.setActivityCode(breg.getActivityCode());
        cfg.setActivityDescription(breg.getActivityDescription());
        cfg.setActivitySource(breg.getActivitySource());
        List<Entry> consents = breg.getConsents();
        String consentValue = "";
        for (Entry cons : consents) {
            if (Contract.isEmpty(consentValue)){
                consentValue = cons.getKey() + "," + cons.getValue();
            } else {
                consentValue = consentValue + ";" + cons.getKey() + "," + cons.getValue();
            }

        }

        cfg.setConsents(consentValue);
        if (properties.isFormBregLegalBasisEnabled()) {
            cfg.setLegalBasis(properties.getFormBregLegalBasis());
        }

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
        } else if (cfg instanceof FormCompoundMarketo) {
            module.setConfig(getMarketoConfiguration(document.getFormConfiguration()));
        } else if (cfg instanceof FormCompoundBreg) {
            module.setConfig(getBregConfiguration((FormCompoundBreg) document.getFormConfiguration()));
        } else {
            contentLogger.warn("The Form document '{}' does not have a valid configuration. It won't appear on the page", document.getPath());
            return null;
        }

        return module;
    }

}
