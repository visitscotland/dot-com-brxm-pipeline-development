(function(Ext) {
    console.log("Loaded Hippo.Reports.HtmlRenderPlugin");

    Ext.ns('Hippo.Reports');

    Hippo.Reports.HtmlRenderPlugin = Ext.extend(Hippo.Reports.Portlet, {
        constructor : function(config) {
            config = Ext.apply(config, { html : config.panelHtml });
            Hippo.Reports.HtmlRenderPlugin.superclass.constructor.call(
                this, config);
        }
    });

    Ext.reg('Hippo.Reports.HtmlRenderPlugin',
        Hippo.Reports.HtmlRenderPlugin);
})(Ext);
