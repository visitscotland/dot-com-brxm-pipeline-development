package com.visitscotland.brxm.dms;

public enum CitiesMapTab {
    ACCOMMODATION(DMSConstants.TYPE_ACCOMMODATION),
    ATTRACTION(DMSConstants.TYPE_ATTRACTIONS),
    ACTIVITIES(DMSConstants.TYPE_ACTIVITIES),
    EVENTS(DMSConstants.TYPE_EVENTS),
    FOOD_DRINK(DMSConstants.TYPE_FOOD_DRINK),
    SHOPPING(DMSConstants.TYPE_RETAIL);

    private final String prodTypeId;

    CitiesMapTab(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }


    public String getProdTypeId() {
        return prodTypeId;
    }
}
