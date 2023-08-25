package com.visitscotland.brxm.dms;


public enum DistilleryMapTab implements BespokeDmsMap {

//    DISTILLERIES(DMSConstants.TYPE_SEE_DO, DMSConstants.DISTILLERIES,DMSConstants.DISTILLERIES,"map.distillery", ""),
    CAMPBELTOWN(DMSConstants.TYPE_SEE_DO, DMSConstants.DISTILLERIES,"campbeltown", "Campbeltown", "Campbeltown Whisky"),
    HIGHLANDS(DMSConstants.TYPE_SEE_DO, DMSConstants.DISTILLERIES,"highlands","highlands", "Highlands Whisky"),
    ISLAY(DMSConstants.TYPE_SEE_DO, DMSConstants.DISTILLERIES,"islay", "islay", "Islay Whisky"),
    LOWLANDS(DMSConstants.TYPE_SEE_DO, DMSConstants.DISTILLERIES,"lowlands","lowlands", "Lowlands Whisky"),
    SPEYSIDE(DMSConstants.TYPE_SEE_DO, DMSConstants.DISTILLERIES,"speyside","Speyside", "Speyside Whisky");

    private final String prodTypeId;
    private final String dmscategory;
    private final String category;
    private final String label;
    private final String location;

    DistilleryMapTab(String prodTypeId, String dmscategory, String category, String label, String location) {
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

    @Override
    public String getLocation() {
        return location;
    }

    public DistilleryMapTab getEnumType() {
        return this;
    }

    public static DistilleryMapTab findByCategory(String category){
        for(DistilleryMapTab val : values()){
            if( val.getCategory().equals(category)){
                return val;
            }
        }
        return null;
    }
}
