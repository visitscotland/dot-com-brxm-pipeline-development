package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.hippobeans.MarketoForm;
import com.visitscotland.brxm.model.MarketoFormModule;
import org.springframework.stereotype.Component;

@Component
public class MarketoFormFactory {

    public MarketoFormModule getModule(MarketoForm document) {
        MarketoFormModule module = new MarketoFormModule();
        module.setTitle(document.getTitle());
        module.setJsonUrl(document.getJsonUrl());
        module.setNoJavaScriptMessage(document.getNonJavaScriptMessage());
        module.setCopy(document.getCopy());
        return module;
    }

}
