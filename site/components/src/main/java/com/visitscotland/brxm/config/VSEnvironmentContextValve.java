package com.visitscotland.brxm.config;

import com.visitscotland.brxm.utils.CMSProperties;
import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;

public class VSEnvironmentContextValve extends AbstractOrderableValve {

    private static boolean active = true;
    private CMSProperties properties = null;

    private String getDMSStack(){
        if (properties == null){
            properties = VsComponentManager.get(CMSProperties.class);
            if (properties == null){
                return null;
            }
        }

        return properties.getDmsDataHost();
    }

    @Override
    public void invoke(ValveContext valveContext) throws ContainerException {
        try {
            if  (active) {
                valveContext.getServletResponse().addHeader(CMSProperties.DMS_DATA_HOST, getDMSStack());
            }
        } finally {
            valveContext.invokeNext();
        }
    }
}
