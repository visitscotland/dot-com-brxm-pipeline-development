package com.visitscotland.brxm.utils;

import com.visitscotland.brxm.model.cfg.Metadata;
import com.visitscotland.utils.info.About;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@PropertySource("classpath:site.properties")
@Component
public class MetadataFactory {

    private static final Logger logger = LoggerFactory.getLogger(MetadataFactory.class);

    private static final String BRANCH_NAME = "VS_BRANCH_NAME";
    private static final String AUTHOR = "VS_COMMIT_AUTHOR";
    private static final String PR_ID = "CHANGE_ID";

    @Value("${ciFilePath}")
    private String ciFilePath;

    private String getVersionNumber() {
        //Sets the version number as a Freemarker shared variable, so it can be inserted to all pages.
        if (About.getVersion().equals("Unknown")) {
            return getClass().getPackage().getImplementationVersion();
        } else {
            return About.getVersion() + " (" + About.getBuildNumber() + ")";
        }
    }

    private String getCiProperty(final String property) {
        if (System.getenv().containsKey(property)) {
            return System.getenv(property);
        } else {
            java.util.Properties p = new Properties();
            try (InputStream is = getClass().getResourceAsStream(ciFilePath)) {
                if (is != null) {
                    p.load(is);
                    if (p.containsKey(property)) {
                        return p.get(property).toString();
                    }
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        return null;
    }


    public Metadata getMetadata() {
        Metadata metadata = new Metadata();

        metadata.setVersion(getVersionNumber());
        metadata.setBranch(getCiProperty(BRANCH_NAME));
        metadata.setLastCommitAuthor(getCiProperty(AUTHOR));
        metadata.setPr(getCiProperty(PR_ID));

        return metadata;
    }
}
