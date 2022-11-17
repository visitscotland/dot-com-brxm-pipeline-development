package com.visitscotland.brxm.dms.model;

import com.visitscotland.brxm.dms.DMSConstants;

public enum RegionsMapTab {
    TOWNS("twnv","townsvillages"),
    ICENTRE(DMSConstants.TYPE_SERVICES,DMSConstants.CAT_ICENTRE);

    private final String prodTypeId;
    private final String category;

    RegionsMapTab(String prodTypeId, String category) {
        this.prodTypeId = prodTypeId;
        this.category = category;
    }


    public String getProdTypeId() {
        return prodTypeId;
    }

    public String getCategory() {
        return category;
    }
}
