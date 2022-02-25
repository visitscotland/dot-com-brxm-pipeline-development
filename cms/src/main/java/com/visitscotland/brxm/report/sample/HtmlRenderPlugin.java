package com.visitscotland.brxm.report.sample;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.cms7.reports.AbstractExtRenderPlugin;
import org.onehippo.cms7.reports.plugins.ReportPanel;
import org.wicketstuff.js.ext.ExtComponent;
import org.wicketstuff.js.ext.util.ExtClass;
import org.wicketstuff.js.ext.util.ExtProperty;

public class HtmlRenderPlugin extends AbstractExtRenderPlugin {

    private static final JavaScriptResourceReference PANEL_JS =
            new JavaScriptResourceReference(HtmlRenderPlugin.class, "Hippo.Reports.HtmlPluginPanel.js");
    private final IPluginContext context;
    private final IPluginConfig config;

    public HtmlRenderPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);
        this.context = context;
        this.config = config;
    }

    @ExtClass("Hippo.Reports.HtmlRenderPlugin")
    public static class HtmlPluginPanel extends ReportPanel {

        @ExtProperty
        public String panelHtml;

        public HtmlPluginPanel(final IPluginContext context,
                                            final IPluginConfig config, String html) {
            super(context, config);
            panelHtml = html;
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            super.renderHead(response);
            response.render(JavaScriptHeaderItem.forReference(PANEL_JS));
        }
    }

    @Override
    public ExtComponent getExtComponent() {
        return new HtmlPluginPanel(context, config, "<h1> Hello World! </h1>");
    }

}