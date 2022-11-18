package com.visitscotland.brxm.dms;


public enum RegionsMapTab {
    TOWNS(DMSConstants.TYPE_TOWN,""),
    ICENTRE(DMSConstants.TYPE_SERVICES, DMSConstants.CAT_ICENTRE);

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
