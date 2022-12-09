package com.visitscotland.brxm.components.content;

import com.visitscotland.brxm.config.VsComponentManager;
import com.visitscotland.brxm.dms.LocationLoader;
import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.hippobeans.Destination;
import com.visitscotland.brxm.utils.PageTemplateBuilder;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class DestinationContentComponent extends PageContentComponent<Destination> {

    private static final Logger logger = LoggerFactory.getLogger(DestinationContentComponent.class);

    private PageTemplateBuilder builder;
    private LocationLoader locationLoader;

    public DestinationContentComponent(){
        logger.debug("DestinationContentComponent initialized");
        this.builder = VsComponentManager.get(PageTemplateBuilder.class);
        this.locationLoader = VsComponentManager.get(LocationLoader.class);
    }

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        addAttributesToRequest(request);
    }

    void addAttributesToRequest(HstRequest request) {
        Destination document = (Destination) request.getAttribute("document");
        LocationObject location = locationLoader.getLocation(document.getLocation(), Locale.UK);
        request.setAttribute("location", location);
        request.setAttribute("region", locationLoader.getRegion(location, Locale.UK));

        builder.addModules(request, document.getLocation());
    }

}
