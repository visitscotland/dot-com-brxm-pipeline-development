package com.visitscotland.brxm.dms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationObject implements Comparable<LocationObject> {


    private String id;
    private String key;
    private String name;
    private String type;
    private Double latitude;
    private Double longitude;

    private Set<String> types;

    @JsonIgnore
    private List<String> children;

    public LocationObject(){

    }

    public LocationObject(String id, String key, String name, String type, Double latitude, Double longitude, List<String> children, Set<String> types) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.children = children;
        this.types = types;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int compareTo(LocationObject o) {
        return this.name.compareTo(o.getName());
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public void addType(String type){
        if (types == null){
            types = new HashSet<String>();
            types.add(type);
        }
        types.add(type);

    }
}
