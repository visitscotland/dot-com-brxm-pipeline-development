package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.BaseDocument;

public class ErrorModule extends Module<BaseDocument> {

    public ErrorModule (BaseDocument document, String message){
        setHippoBean(document);
        addErrorMessage(message);
    }
}
