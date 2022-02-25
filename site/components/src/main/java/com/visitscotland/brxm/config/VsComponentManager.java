package com.visitscotland.brxm.config;

import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.site.HstServices;

/**
 * Implementing {@code ApplicationContextAware} would give this class direct access to Spring Context
 */
public class VsComponentManager {

    private static ComponentManager manager;

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
        if (manager == null) {
            return HstServices.getComponentManager().getComponent(type);
        } else {
            return (T) manager.getComponent(type);
        }
    }

}
