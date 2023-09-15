package com.visitscotland.brxm.dms;


public enum ICentresMapTab implements BespokeDmsMap{

    ICENTRE(DMSConstants.TYPE_SERVICES, DMSConstants.CAT_ICENTRE, DMSConstants.CAT_ICENTRE,"map.serv","");
    /*IKNOW(DMSConstants.TYPE_IKNOW,"","map.places");*/

    private final String prodTypeId;
    private final String dmscategory;
    private final String category;
    private final String label;
    private final String location;

    public ICentresMapTab getEnumType() {
        return this;
    }

    ICentresMapTab(String prodTypeId, String dmscategory,String category, String label, String location) {
        this.prodTypeId = prodTypeId;
        this.dmscategory = dmscategory;
        this.category = category;
        this.label = label;
        this.location = location;
    }

    public String getProdTypeId() {
        return prodTypeId;
    }

    public String getCategory() {
        return category;
    }

    public String getDmsCategory() {
        return dmscategory;
    }

    public String getLabel() {
        return label;
    }
    public String getLocation() {
        return location;
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
