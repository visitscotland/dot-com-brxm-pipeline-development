package com.visitscotland.brxm.config;

import com.visitscotland.brxm.utils.NonTestable;
import com.visitscotland.utils.info.About;
import com.visitscotland.brxm.services.ResourceBundleService;
import freemarker.template.TemplateModelException;
import org.hippoecm.hst.servlet.HstFreemarkerServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * This Piece of code Extracted from the dot-org project and Enhanced for the dot-com needs (Dependency Injection Coming soon)
 *
 * {@code com.visitscotland.org.util.VSHstFreemarkerServlet.java}
 */
@NonTestable(NonTestable.Cause.INHERITANCE)
public class VsHstFreemarkerServlet extends HstFreemarkerServlet {

    private static final Logger logger = LoggerFactory.getLogger(VsHstFreemarkerServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            getConfiguration().setSharedVariable("ResourceBundle", VsComponentManager.get(ResourceBundleService.class));
            includeVersionNumber();
        } catch (TemplateModelException e) {
            logger.error("Unable to set shared variables.", e);
        }
    }

    private void includeVersionNumber() throws TemplateModelException {
        //Sets the version number as a Freemarker shared variable so it can be inserted to all pages.
        if (About.getVersion().equals("Unknown")){
            getConfiguration().setSharedVariable("version", getClass().getPackage().getImplementationVersion());
        } else {
            getConfiguration().setSharedVariable("version", About.getVersion() + " (" + About.getBuildNumber() + ")");
        }
    }
}
