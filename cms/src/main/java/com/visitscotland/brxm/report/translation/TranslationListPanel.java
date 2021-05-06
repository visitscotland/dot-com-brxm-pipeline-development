package com.visitscotland.brxm.report.translation;

import com.visitscotland.brxm.translation.plugin.JcrDocument;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.IBrowseService;
import org.hippoecm.frontend.session.UserSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.onehippo.cms7.reports.plugins.ReportPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.js.ext.ExtEventAjaxBehavior;
import org.wicketstuff.js.ext.util.ExtClass;
import org.wicketstuff.js.ext.util.ExtEventListener;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;


@ExtClass("Hippo.Reports.TranslationListPanel")
public class TranslationListPanel extends ReportPanel {
    // Javascript files needed for the translation report
    // Added to page in order they appear in this list
    private static final List<String> JS_FILES = Arrays.asList("js/constants.js", "js/Hippo.Reports.PageableHttpProxy.js",
            "js/Hippo.Reports.SimpleArrayStore.js", "js/reportFilterComponents.js",  "js/reportExport.js", "js/Hippo.Reports.TranslationListPanel.js");
    private static final Logger log = LoggerFactory.getLogger(TranslationListPanel.class);

    public TranslationListPanel(IPluginContext context, IPluginConfig config) {
        super(context, config);
        this.addEventListener("documentSelected", new ExtEventListener() {
            @Override
            public void onEvent(AjaxRequestTarget target, Map<String, JSONArray> parameters) {
                getParameter("handleId", parameters).ifPresent(TranslationListPanel.this::browseToDocument);
            }
        });
    }

    @Override
    protected ExtEventAjaxBehavior newExtEventBehavior(String event) {
        if (event.equals("documentSelected")) {
            return new ExtEventAjaxBehavior("handleId");
        }

        return super.newExtEventBehavior(event);
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        JS_FILES.forEach((String file) -> {
            response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(TranslationListPanel.class, file)));
        });
    }

    protected void preRenderExtHead(StringBuilder js) {
        super.preRenderExtHead(js);
    }

    protected void onRenderProperties(JSONObject properties) throws JSONException {
        super.onRenderProperties(properties);
    }


    private void browseToDocument(String handleId)  {
        if (handleId != null && handleId.length() != 0) {
            Node handle = this.getNode(handleId);
            // Direct user to unpublished english variant
            Node node;
            try {
                node  = new JcrDocument(handle).getVariantNode(JcrDocument.VARIANT_UNPUBLISHED);
            } catch (RepositoryException ex) {
                log.error("Failed to navigate to node " + handleId + " can not create JcrDocument", ex);
                return;
            }
            if (node != null) {
                JcrNodeModel nodeModel = new JcrNodeModel(node);
                IBrowseService browseService = this.context.getService("service.browse", IBrowseService.class);
                browseService.browse(nodeModel);
            }
        } else {
            log.warn("No document path to browse to");
        }
    }


    private Node getNode(String handleId) {
        try {
            Session session = ((UserSession)this.getSession()).getJcrSession();
            return session.getNodeByIdentifier(handleId);
        } catch (RepositoryException ex) {
            log.warn("Unable to get the node " + handleId, ex);
            return null;
        }
    }

}
