package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.components.navigation.info.GeneralPageComponentInfo;
import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.hippobeans.GeneralBSH;
import com.visitscotland.brxm.model.megalinks.HorizontalListLinksModule;
import com.visitscotland.brxm.utils.PageTemplateBuilder;
import com.visitscotland.utils.Contract;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersInfo(type = GeneralPageComponentInfo.class)
public class GeneralBSHContentComponent extends PageContentComponent<GeneralBSH> {

    private static final Logger logger = LoggerFactory.getLogger(GeneralBSHContentComponent.class);

    static final String ERROR_CODE = "errorCode";

    private final PageTemplateBuilder builder;

    public GeneralBSHContentComponent(){
        logger.debug("GeneralBSHContentComponent initialized");
        this.builder = VsComponentManager.get(PageTemplateBuilder.class);
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        addPageStatusCode(request, response);

        builder.addModules(request);
    }

    @Override
    protected void addOTYML(HstRequest request) {
        GeneralBSH page = getDocument(request);
        if (!Contract.isEmpty(page.getLinks())) {
            HorizontalListLinksModule otyml = megalinkFactory.horizontalListLayout(page, request.getLocale());
            request.setModel(OTYML_BUNDLE, otyml);
        }
    }

    //TODO mode to PageContentComponent
    private void addPageStatusCode(HstRequest request, HstResponse response){
        GeneralPageComponentInfo pageInfo = getComponentParametersInfo(request);
        int pageStatus = Integer.parseInt(pageInfo.getStatus());
        response.setStatus(pageStatus);
        if (pageStatus >= 400) {
            request.setModel(ERROR_CODE, pageStatus);
        }
    }
}
