package com.visitscotland.brxm.dms;


public enum RegionsMapTab implements BespokeDmsMap {
    TOWNS(DMSConstants.TYPE_TOWN,"",DMSConstants.TYPE_TOWN,"map.places", ""),
    ICENTRE(DMSConstants.TYPE_SERVICES, DMSConstants.CAT_ICENTRE,DMSConstants.CAT_ICENTRE,"map.serv", "");

    private final String prodTypeId;
    private final String dmscategory;
    private final String category;
    private final String label;
    private final String location;

    RegionsMapTab(String prodTypeId, String dmscategory,String category, String label, String location) {
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
    public RegionsMapTab getEnumType() {
        return this;
    }

    public static RegionsMapTab findByCategory(String category){
        for(RegionsMapTab val : values()){
            if( val.getCategory().equals(category)){
                return val;
            }
        }
        return null;
    }
}
