package com.visitscotland.brxm.hippobeans.capabilities;


public interface TranslationParent {

//    List<BaseDocument> getModules();

    default String[] getChildJcrTypes() {
        return new String[] {};
    }
}
