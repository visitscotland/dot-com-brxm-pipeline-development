package com.visitscotland.brxm.model;

import com.visitscotland.brxm.hippobeans.Day;
import com.visitscotland.brxm.hippobeans.Itinerary;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItineraryPage {

    private Itinerary document;

    private String firstStopLocation;
    private String lastStopLocation;
    private List<Day> days;
    private BigDecimal distance;
    private Map<String, ItineraryStopModule> stops;

    public Itinerary getDocument() {
        return document;
    }

    public void setDocument(Itinerary document) {
        this.document = document;
    }

    public String getFirstStopLocation() {
        return firstStopLocation;
    }

    public void setFirstStopLocation(String firstStopLocation) {
        this.firstStopLocation = firstStopLocation;
    }

    public String getLastStopLocation() {
        return lastStopLocation;
    }

    public void setLastStopLocation(String lastStopLocation) {
        this.lastStopLocation = lastStopLocation;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Map<String, ItineraryStopModule> getStops() {
        return stops;
    }

    public void setStops(Map<String, ItineraryStopModule> stops) {
        this.stops = stops;
    }

    public void addStop(ItineraryStopModule module){
        if (stops == null){
            stops = new HashMap<>();
        }
        stops.put(module.getIdentifier(), module);
    }
}
