package com.visitscotland.brxm.components;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.onehippo.forge.exdocpicker.api.ExternalDocumentCollection;
import org.onehippo.forge.exdocpicker.api.ExternalDocumentServiceContext;
import org.onehippo.forge.exdocpicker.impl.SimpleExternalDocumentCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DMSAwardPicker extends AbstractDMSPicker {

    /**
     * Plugin parameter name for physical document field name (JCR property name).
     */

    private static final long serialVersionUID = 1L;

    private static final String TYPE = "award";

    public DMSAwardPicker() {
        super (TYPE);
    }

    @Override
    public ExternalDocumentCollection<JSONObject> searchExternalDocuments(ExternalDocumentServiceContext context, String queryString) {

        ExternalDocumentCollection<JSONObject> docCollection = new SimpleExternalDocumentCollection<JSONObject>();
        for (int i = 0; i < getDocArray().size(); i++) {
            JSONObject doc = getDocArray().getJSONObject(i);
            if (StringUtils.contains(doc.getString("name").toLowerCase(), Objects.toString(queryString, "").toLowerCase())) {
                docCollection.add(doc);
            }
        }

        return docCollection;
    }

    @Override
    protected List<String> productTypesForPSR(String productType){
        return new ArrayList<>();
    }


}