package com.visitscotland.brmx.beans;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** 
 * TODO: Beanwriter: Failed to create getter for node type: hippo:compound
 */
@HippoEssentialsGenerated(internalName = "visitscotland:Day")
@Node(jcrType = "visitscotland:Day")
public class Day extends BaseDocument implements TranslationLinkContainer {
    @HippoEssentialsGenerated(internalName = "visitscotland:title")
    public String getTitle() {
        return getSingleProperty("visitscotland:title");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("visitscotland:introduction");
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:stops", allowModifications = false)
    public List<Stop> getStops() {
        return getLinkedBeans("visitscotland:stops", Stop.class);
    }

    @HippoEssentialsGenerated(internalName = "visitscotland:transports")
    public String[] getTransports() {
        return getMultipleProperty("visitscotland:transports");
    }

    @Override
    public String[] getTranslatableLinkNames() {
        return new String[] {"visitscotland:stops"};
    }
}
