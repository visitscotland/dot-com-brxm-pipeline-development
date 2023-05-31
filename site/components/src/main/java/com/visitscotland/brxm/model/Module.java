package com.visitscotland.brxm.model;

import org.hippoecm.hst.content.beans.standard.HippoBean;

import java.util.ArrayList;
import java.util.List;

public class Module<T extends HippoBean> {

    private T hippoBean;

    private String anchor;

    private List<String> errorMessages;

    private String marketoId;

    public String getType(){
        return getClass().getSimpleName();
    }

    public T getHippoBean() {
        return hippoBean;
    }

    public void setHippoBean(T hippoBean) {
        this.hippoBean = hippoBean;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessage(String message){
        if (errorMessages == null){
            errorMessages = new ArrayList<>();
        }
        errorMessages.add(message);
    }

    public String getMarketoId() {
        return marketoId;
    }

    public void setMarketoId(String marketoId) {
        this.marketoId = marketoId;
    }

}
