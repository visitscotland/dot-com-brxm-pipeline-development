package com.visitscotland.brxm.dms;

public interface BespokeDmsMap {

     String getProdTypeId();
     String getDmsCategory();
     String getCategory();
     String getLabel();
     String getLocation();

     BespokeDmsMap getEnumType();

}
