package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.TravelInformationTab;

import java.util.List;

public class TravelInformationModuleTab extends Module<TravelInformationTab>  {

    private String title;
    private List<TravelInformationTransportRowModule> travelInformationTransportRows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TravelInformationTransportRowModule> getTravelInformationTransportRows() {
        return travelInformationTransportRows;
    }

    public void setTravelInformationTransportRows(List<TravelInformationTransportRowModule> travelInformationTransportRows) {
        this.travelInformationTransportRows = travelInformationTransportRows;
    }
}
