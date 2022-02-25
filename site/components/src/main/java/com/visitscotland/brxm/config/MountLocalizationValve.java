package com.visitscotland.brxm.config;

import org.apache.commons.lang3.LocaleUtils;
import org.hippoecm.hst.container.valves.AbstractOrderableValve;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.internal.HstMutableRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Locale;

/**
 * This valve is configured in site/components/src/main/resources/META-INF/hst-assembly/overrides/custom-valves.xml
 * It overwrites the locale set by LocalizationValve (which is configured to execute just before this one).
 * LocalizationValve obtains the locale from the content locale, whereas this valve only looks at the mount
 * This means that if the URL is /es/site/..., then the locale is always Spanish, even when the sitemap points to English content
 * This is important for the translation fallback
 */
public class MountLocalizationValve extends AbstractOrderableValve {
    private static final Logger logger = LoggerFactory.getLogger(MountLocalizationValve.class);

    @Override
    public void invoke(ValveContext valveContext) throws ContainerException {
        try {
            HstMutableRequestContext requestContext = (HstMutableRequestContext) valveContext.getRequestContext();
            Locale requestLocale = requestContext.getPreferredLocale();
            String mountLocale = requestContext.getResolvedMount().getMount().getLocale();

            try {
                requestLocale = LocaleUtils.toLocale(mountLocale);
            } catch (IllegalArgumentException e) {
                logger.error("Mount has invalid locale {}", mountLocale);
            }

            requestContext.setPreferredLocale(requestLocale);
            requestContext.setLocales(Collections.singletonList(requestLocale));
            requestContext.setAttribute("javax.servlet.jsp.jstl.fmt.locale.application", requestLocale);
            valveContext.getServletRequest().setAttribute("javax.servlet.jsp.jstl.fmt.locale.request", requestLocale);
        } finally {
            valveContext.invokeNext();
        }
    }
}
