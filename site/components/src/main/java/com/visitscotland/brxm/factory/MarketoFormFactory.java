package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.model.MarketoFormModule;
import com.visitscotland.brxm.model.form.MarketoConfiguration;
import com.visitscotland.brxm.utils.Properties;
import org.springframework.stereotype.Component;

@Component
public class MarketoFormFactory {

    private static final String PROP_RECAPTCHA = "form.recaptcha-key";
    private static final String PROP_MARKETO_URL = "form.marketo.instance-url";
    private static final String PROP_MARKETO_MUNCHKIN = "form.marketo.munchkin";
    private static final String PROP_MARKETO_SCRIPT = "form.marketo.script";

    private final Properties properties;

    public MarketoFormFactory(Properties properties) {
        this.properties = properties;
    }

    public MarketoFormModule getModule(MarketoForm document) {
        MarketoConfiguration cfg = new MarketoConfiguration();
        cfg.setRecaptcha(properties.getProperty(PROP_RECAPTCHA));
        cfg.setMarketoInstance(properties.getProperty(PROP_MARKETO_URL));
        cfg.setMunchkinId(properties.getProperty(PROP_MARKETO_MUNCHKIN));
        cfg.setScript(properties.getProperty(PROP_MARKETO_SCRIPT));

        MarketoFormModule module = new MarketoFormModule();
        module.setConfig(cfg);
        module.setTitle(document.getTitle());
        module.setJsonUrl(document.getJsonUrl());
        module.setNoJavaScriptMessage(document.getNonJavaScriptMessage());
        module.setCopy(document.getCopy());

        return module;
    }

}
