package com.visitscotland.brxm.dms;

public enum CitiesMapTab {
    ACCOMMODATION(DMSConstants.PATH_ACCOMMODATION, DMSConstants.TYPE_ACCOMMODATION),
    ATTRACTION("Attraction", "attr"),
    ACTIVITIES("Activities", "acti"),
    EVENTS(DMSConstants.PATH_EVENTS, DMSConstants.TYPE_EVENTS),
    FOOD_DRINK(DMSConstants.PATH_FOOD_DRINK,  DMSConstants.TYPE_FOOD_DRINK),
    SHOPPING("Shopping",  "reta");

    private final String prodType;
    private final String prodTypeId;

    CitiesMapTab(String prodType, String prodTypeId) {
        this.prodType = prodType;
        this.prodTypeId = prodTypeId;
    }

    public String getProdType() {
        return prodType;
    }

    public String getProdTypeId() {
        return prodTypeId;
    }
}
