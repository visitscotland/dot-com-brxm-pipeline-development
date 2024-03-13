package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.model.MarketoFormModule;
import com.visitscotland.brxm.model.form.MarketoConfiguration;
import com.visitscotland.brxm.utils.Properties;
import org.springframework.stereotype.Component;

@Component
public class MarketoFormFactory {


    private final Properties properties;

    public MarketoFormFactory(Properties properties){
        this.properties = properties;
    }

    public MarketoFormModule getModule(MarketoForm document) {
        MarketoConfiguration cfg = new MarketoConfiguration();
        cfg.setRecaptcha(properties.getProperty("form.recaptcha-key"));
        cfg.setMarketoInstance(properties.getProperty("form.marketo.instance-url"));
        cfg.setMunchkinId(properties.getProperty("form.marketo.munchkin"));
        cfg.setScript(properties.getProperty("form.marketo.script"));

        MarketoFormModule module = new MarketoFormModule();
        module.setConfig(cfg);
        module.setTitle(document.getTitle());
        module.setJsonUrl(document.getJsonUrl());
        module.setNoJavaScriptMessage(document.getNonJavaScriptMessage());
        module.setCopy(document.getCopy());

        return module;
    }

}
