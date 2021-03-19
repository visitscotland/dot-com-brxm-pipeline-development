package com.visitscotland.brxm.components;

import com.visitscotland.brxm.dms.model.LocationObject;
import com.visitscotland.brxm.dms.LocationLoader;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.Plugin;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.session.UserSession;
import org.onehippo.forge.selection.frontend.model.ListItem;
import org.onehippo.forge.selection.frontend.model.ValueList;
import org.onehippo.forge.selection.frontend.provider.IValueListProvider;
import org.onehippo.forge.selection.frontend.utils.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.visitscotland.brxm.model.ValueListLocation;

import javax.jcr.Session;
import java.util.List;
import java.util.Locale;

/**
 * /cms/console/?1&path=/hippo:configuration/hippo:frontend/cms/cms-services/DmsImageLocationListProvider
 */
public class DmsLocationListProvider extends Plugin implements IValueListProvider {

    private static final Logger logger = LoggerFactory.getLogger(DmsLocationListProvider.class);

    private final static String PROVIDER = "valuelist.provider";
    private static final String LEVELS = "location.levels";
    private static final String SOURCE = "source";

    private final String[] levels;

    public DmsLocationListProvider(IPluginContext context, IPluginConfig config) {
        super(context, config);
        levels = config.getStringArray(LEVELS);
        String name = config.getString(PROVIDER);

        context.registerService(this, name);
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " registered under " + name);
        }
    }

    public ValueList getValueList(IPluginConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Argument 'config' may not be null");
        } else {
            return this.getValueList(config.getString(SOURCE));
        }
    }

    public ValueList getValueList(String name) {
       return this.getValueList(name, null);
    }

    public ValueList getValueList(String name, Locale locale) {

        List<LocationObject> locations = LocationLoader.getInstance().getLocationsByLevel(levels);
        ValueListLocation valueList = new ValueListLocation();

        for (LocationObject location: locations) {
            ListItem list = new ListItem(location.getName(),location.getName());
            valueList.add(list);
        }
        return valueList;
    }

    public List<String> getValueListNames() {
        return JcrUtils.getValueListNames(obtainSession());
    }

    protected Session obtainSession() {
        UserSession userSession = (UserSession) org.apache.wicket.Session.get();
        return userSession.getJcrSession();
    }
}



