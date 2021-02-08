package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.cfg.SpringContext;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.brxm.utils.HippoUtilsService;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.onehippo.cms7.essentials.components.CommonComponent;

public class InternalComponent extends CommonComponent {

    private InternalParameterProcessor processor;

    public InternalComponent() {
        processor = SpringContext.getBean(InternalParameterProcessor.class);
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        processor.addLocalizedURLs(request);
        processor.processParameters(request);
    }
}
