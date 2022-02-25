package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.TravelInformation;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

public class TravelInformationModule extends Module<TravelInformation> {

    private String title;
    private HippoHtml copy;
    private TravelInformationModuleTab gettingTo;
    private TravelInformationModuleTab gettingAround;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HippoHtml getCopy() {
        return copy;
    }

    public void setCopy(HippoHtml copy) {
        this.copy = copy;
    }

    public TravelInformationModuleTab getGettingTo() {
        return gettingTo;
    }

    public void setGettingTo(TravelInformationModuleTab gettingTo) {
        this.gettingTo = gettingTo;
    }

    public TravelInformationModuleTab getGettingAround() {
        return gettingAround;
    }

    public void setGettingAround(TravelInformationModuleTab gettingAround) {
        this.gettingAround = gettingAround;
    }
}
