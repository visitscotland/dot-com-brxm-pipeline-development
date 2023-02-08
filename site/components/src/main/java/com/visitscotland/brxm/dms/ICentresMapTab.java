package com.visitscotland.brxm.dms;


public enum ICentresMapTab {
    ICENTRE(DMSConstants.TYPE_SERVICES, DMSConstants.CAT_ICENTRE,"map.serv");
    /*IKNOW(DMSConstants.TYPE_IKNOW,"","map.places");*/

    private final String prodTypeId;
    private final String category;
    private final String label;

    ICentresMapTab(String prodTypeId, String category, String label) {
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

    public static ICentresMapTab findByCategory(String category){
        for(ICentresMapTab val : values()){
            if( val.getCategory().equals(category)){
                return val;
            }
        }
        return null;
    }
}
