package com.visitscotland.brxm.dms;


public enum RegionsMapTab {
    TOWNS(DMSConstants.TYPE_TOWN,"","map.places"),
    ICENTRE(DMSConstants.TYPE_SERVICES, DMSConstants.CAT_ICENTRE,"map.serv");

    private final String prodTypeId;
    private final String category;
    private final String label;

    RegionsMapTab(String prodTypeId, String category, String label) {
        this.prodTypeId = prodTypeId;
        this.category = category;
        this.label = label;
    }


    public String getProdTypeId() {
        return prodTypeId;
    }

    public String getCategory() {
        return category;
    }

    public String getLabel() {
        return label;
    }
}
