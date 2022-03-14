package com.visitscotland.brxm.factory;

import com.visitscotland.brxm.model.Module;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.springframework.stereotype.Controller;

@Controller
public class PreviewModeFactory {

    public <T extends HippoBean> Module<T> createErrorModule(Module<T> source){
        Module<T> errorModule = new Module<>();
        errorModule.setHippoBean(source.getHippoBean());
        errorModule.setErrorMessages(source.getErrorMessages());
        return errorModule;
    }
}
