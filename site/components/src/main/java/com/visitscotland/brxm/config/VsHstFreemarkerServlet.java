package com.visitscotland.brxm.config;

import com.visitscotland.brxm.utils.NonTestable;
import com.visitscotland.utils.info.About;
import com.visitscotland.brxm.services.ResourceBundleService;
import com.visitscotland.utils.info.About;
import freemarker.template.TemplateModelException;
import org.hippoecm.hst.servlet.HstFreemarkerServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            getConfiguration().setSharedVariable("ResourceBundle", VsComponentManager.get(ResourceBundleService.class));
            includeVersionNumber();
            includeBranchInformation();
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

    private void includeBranchInformation() throws TemplateModelException {
        if (System.getenv().containsKey("VS_BRANCH_NAME")){
            addVariable("ciBranch", System.getenv("VS_BRANCH_NAME"));

            if (System.getenv().containsKey("VS_COMMIT_AUTHOR")){
                addVariable("ciCommitAuthor", System.getenv("VS_COMMIT_AUTHOR"));
            }
            if (System.getenv().containsKey("CHANGE_ID")){
                addVariable("ciPrID", System.getenv("CHANGE_ID"));
            }
        } else {
            try {
                Properties p = new Properties();
                p.load(getClass().getResourceAsStream("/ci/build-info.properties"));
                if (p.containsKey("VS_BRANCH_NAME")){
                    addVariable("ciBranch", p.get("VS_BRANCH_NAME"));
                }
                if (p.containsKey("VS_COMMIT_AUTHOR")){
                    addVariable("ciCommitAuthor", p.get("VS_COMMIT_AUTHOR"));
                }
                if (p.containsKey("CHANGE_ID")){
                    addVariable("ciPrID", p.get("CHANGE_ID"));
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
