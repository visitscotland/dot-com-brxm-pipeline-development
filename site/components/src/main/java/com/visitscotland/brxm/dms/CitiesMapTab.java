package com.visitscotland.brxm.dms;

public enum CitiesMapTab {
    ACCOMMODATION(DMSConstants.TYPE_ACCOMMODATION),
    ATTRACTION( "attr"),
    ACTIVITIES("acti"),
    EVENTS(DMSConstants.TYPE_EVENTS),
    FOOD_DRINK(DMSConstants.TYPE_FOOD_DRINK),
    SHOPPING("reta");

    private final String prodTypeId;

    CitiesMapTab(String prodTypeId) {
        this.prodTypeId = prodTypeId;
    }


    public String getProdTypeId() {
        return prodTypeId;
    }
}
