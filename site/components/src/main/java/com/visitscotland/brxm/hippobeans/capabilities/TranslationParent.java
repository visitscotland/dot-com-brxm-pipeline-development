package com.visitscotland.brxm.hippobeans.capabilities;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface TranslationParent {

    @JsonIgnore
    default String[] getChildJcrTypes() {
        return new String[] {};
    }
}
