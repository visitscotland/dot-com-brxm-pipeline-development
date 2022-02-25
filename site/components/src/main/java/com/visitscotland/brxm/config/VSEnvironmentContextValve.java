package com.visitscotland.brxm.config;

import com.visitscotland.brxm.utils.Properties;
import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;

public class VSEnvironmentContextValve extends AbstractOrderableValve {

    private static boolean active = true;
    private Properties properties = null;

    private String getDMSStack(){
        if (properties == null){
            properties = VsComponentManager.get(Properties.class);
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
                valveContext.getServletResponse().addHeader(Properties.DMS_DATA_HOST, getDMSStack());
            }
        } finally {
            valveContext.invokeNext();
        }
    }
}
