package com.visitscotland.brxm.dms;

public enum CitiesMapTab {
    ACCOMMODATION(DMSConstants.TYPE_ACCOMMODATION, "map.acco"),
    ATTRACTION(DMSConstants.TYPE_ATTRACTIONS, "map.attr"),
    ACTIVITIES(DMSConstants.TYPE_ACTIVITIES, "map.acti"),
    EVENTS(DMSConstants.TYPE_EVENTS, "map.even"),
    FOOD_DRINK(DMSConstants.TYPE_FOOD_DRINK, "map.cate"),
    SHOPPING(DMSConstants.TYPE_RETAIL, "map.reta");

    private final String prodTypeId;
    private final String label;

    CitiesMapTab(String prodTypeId, String label) {
        this.prodTypeId = prodTypeId;
        this.label = label;
    }


    public String getProdTypeId() {
        return prodTypeId;
    }

    public String getLabel() {
        return label;
    }
}
