package com.visitscotland.brxm.config;

import com.visitscotland.brxm.utils.NonTestable;
import com.visitscotland.utils.info.About;
import com.visitscotland.brxm.services.ResourceBundleService;
import freemarker.template.TemplateModelException;
import org.hippoecm.hst.servlet.HstFreemarkerServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;

/**
 * This Piece of code Extracted from the dot-org project and Enhanced for the dot-com needs (Dependency Injection Coming soon)
 *
 * {@code com.visitscotland.org.util.VSHstFreemarkerServlet.java}
 */
@NonTestable(NonTestable.Cause.INHERITANCE)
public class VsHstFreemarkerServlet extends HstFreemarkerServlet {

    private static final Logger logger = LoggerFactory.getLogger(VsHstFreemarkerServlet.class);

    public static final String BRANCH_NAME = "VS_BRANCH_NAME";
    public static final String AUTHOR = "VS_COMMIT_AUTHOR";
    public static final String PR_ID = "CHANGE_ID";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            addObject("ResourceBundle", VsComponentManager.get(ResourceBundleService.class));
            addObject("Properties", VsComponentManager.get(com.visitscotland.brxm.utils.Properties.class));
            addObject("Logger", logger);

            includeVersionNumber();
            includeBranchInformation();
        } catch (TemplateModelException e) {
            logger.error("Unable to set shared variables.", e);
        } catch (FactoryBeanNotInitializedException e) {
            //TODO: Test that ResourceBundle, Properties and Logger are still available to Freemarker
            logger.warn("VsHstFreemarkerServlet couldn't be initialized.");
        }
    }

    /** Adds an object to Freemarker **/
    public void addObject(String name, Object value) throws TemplateModelException {
        getConfiguration().setSharedVariable(name, value);
    }

    /** Adds the build number to the template */
    private void includeVersionNumber() throws TemplateModelException {
        //Sets the version number as a Freemarker shared variable so it can be inserted to all pages.
        if (About.getVersion().equals("Unknown")){
            getConfiguration().setSharedVariable("version", getClass().getPackage().getImplementationVersion());
        } else {
            getConfiguration().setSharedVariable("version", About.getVersion() + " (" + About.getBuildNumber() + ")");
        }
    }

    /** Includes branch information for CI pipelines */
    private void includeBranchInformation() throws TemplateModelException {
        if (System.getenv().containsKey(BRANCH_NAME)){
            addVariable("ciBranch", System.getenv(BRANCH_NAME));

            if (System.getenv().containsKey(AUTHOR)){
                addVariable("ciCommitAuthor", System.getenv(AUTHOR));
            }
            if (System.getenv().containsKey(PR_ID)){
                addVariable("ciPrID", System.getenv(PR_ID));
            }
        } else {
            try {
                Properties p = new Properties();
                p.load(getClass().getResourceAsStream("/ci/build-info.properties"));
                if (p.containsKey(BRANCH_NAME)){
                    addVariable("ciBranch", p.get(BRANCH_NAME));
                }
                if (p.containsKey(AUTHOR)){
                    addVariable("ciCommitAuthor", p.get(AUTHOR));
                }
                if (p.containsKey(PR_ID)){
                    addVariable("ciPrID", p.get(PR_ID));
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void addVariable(String key, Object value) throws TemplateModelException{
        getConfiguration().setSharedVariable(key, value);
    }
}
