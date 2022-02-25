package com.visitscotland.brxm.report.sample;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.onehippo.cms7.reports.ReportsPerspective;
import org.onehippo.cms7.reports.plugins.PortalPanelPlugin;

public class SampleReportPluginPanel extends PortalPanelPlugin {

    public static final String SAMPLE_REPORT_WICKET_ID =
            "service.reports.sample-report";

    public SampleReportPluginPanel(final IPluginContext context,
                                   final IPluginConfig config) {
        super(context, config);
    }

    @Override
    public IModel<String> getTitle() {
        return new Model<String>("Sample report");
    }

    @Override
    public IModel<String> getHelp() {
        return new Model<String>("Sample report description");
    }

    @Override
    public String getPanelServiceId() {
        return ReportsPerspective.REPORTING_SERVICE;
    }

    @Override
    public String getPortalPanelServiceId() {
        return SAMPLE_REPORT_WICKET_ID;
    }

}
