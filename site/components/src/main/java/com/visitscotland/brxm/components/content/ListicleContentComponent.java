package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.factory.ListicleFactory;
import com.visitscotland.brxm.hippobeans.Listicle;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class ListicleContentComponent extends PageContentComponent<Listicle> {

    private static final Logger logger = LoggerFactory.getLogger(ListicleContentComponent.class);

    public static final String LISTICLE_ITEMS = "items";

    private ListicleFactory factory;

    public ListicleContentComponent(){
        logger.debug("ListicleContentComponent initialized");

        factory = VsComponentManager.get(ListicleFactory.class);
    }



    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        request.setAttribute(LISTICLE_ITEMS, factory.generateItems(Locale.UK, getDocument(request)));
    }

}
