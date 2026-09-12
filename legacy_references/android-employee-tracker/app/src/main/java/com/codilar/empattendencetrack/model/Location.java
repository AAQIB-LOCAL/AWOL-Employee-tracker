package com.codilar.empattendencetrack.model;

public class Location {

    private String id;
    private double lat;
    private double lang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public Location(String id, double lat, double lang) {

        this.id=  id;
        this.lat = lat;
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", lat='" + lat + '\'' +
                ", lang=" + lang +
                '}';
    }
}
