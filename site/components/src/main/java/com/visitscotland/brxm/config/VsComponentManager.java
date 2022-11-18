package com.visitscotland.brxm.config;

import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;

/**
 * Implementing {@code ApplicationContextAware} would give this class direct access to Spring Context
 */
public class VsComponentManager {

    private static ComponentManager manager;

    private VsComponentManager(){}

    /**
     * This method is convenient for unit testing for objects that are not injected because they need to keep
     * their state (thread-safety)
     *
     * @param manager
     */
    public static void setComponentManager(ComponentManager manager) {
        VsComponentManager.manager = manager;
    }

    public static <T> T get(Class<T> type){
        if (manager != null) {
            return manager.getComponent(type);
        } else if (HstServices.getComponentManager() != null){
            return HstServices.getComponentManager().getComponent(type);
        } else {
            throw new FactoryBeanNotInitializedException();
        }
    }

}
