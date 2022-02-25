package com.visitscotland.brxm.utils;

import com.visitscotland.utils.Contract;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import java.util.regex.Pattern;

/**
 * Wrapper for HippoHtml. Allows for a default value to be provided for a rich text field
 */
public class HippoHtmlWrapper extends HippoHtml {

    private static final Pattern REMOVE_TAGS = Pattern.compile("<[^>]++>");

    private final HippoHtml base;
    private final String defaultHtml;

    public HippoHtmlWrapper(HippoHtml base, String defaultHtml) {
        this.defaultHtml = defaultHtml;
        this.base = base;
    }

    public HippoHtmlWrapper(String defaultHtml) {
        this(null, defaultHtml);
    }

    @Override
    public String getContent() {
        return hasContent()? base.getContent() : defaultHtml;
    }

    private boolean hasContent(){
        return base != null && !Contract.isEmpty(base.getContent()) &&
                !REMOVE_TAGS.matcher(base.getContent()).replaceAll("").replace("&nbsp;", "").trim().isEmpty();
    }
}
