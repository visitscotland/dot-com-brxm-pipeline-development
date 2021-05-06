package com.visitscotland.brxm.report.translation;

import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.cms7.reports.ReportsPerspective;
import org.onehippo.cms7.reports.plugins.PortalPanelPlugin;

public class TranslationReportPortalPanel extends PortalPanelPlugin {

    public static final String TRANSLATION_REPORT_PANEL_SERVICE =
            "service.reports.translationreport";

    public TranslationReportPortalPanel(final IPluginContext context,
                                        final IPluginConfig config) {
        super(context, config);
    }

    @Override
    public IModel<String> getTitle() {
        return new Model<String>("Translation report");
    }

    @Override
    public IModel<String> getHelp() {
        return new Model<String>("Report of untranslated documents");
    }

    @Override
    public String getPanelServiceId() {
        return ReportsPerspective.REPORTING_SERVICE;
    }

    @Override
    public String getPortalPanelServiceId() {
        return TRANSLATION_REPORT_PANEL_SERVICE;
    }

}
