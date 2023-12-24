package org.but.feec.ars.api;

public class FlightScheduleView {
    private Integer flight_id;
    private String departure_dt;
    private String arrival_dt;
    private String origin_airport;
    private String destination_airport;

    private Integer origin_airport_id;
    private Integer destination_airport_id;
    private String is_scheduled;
    private Integer aircraft_id;

    public Integer getAircraft_id() {
        return aircraft_id;
    }

    public void setAircraft_id(Integer aircraft_id) {
        this.aircraft_id = aircraft_id;
    }

    public String isIs_scheduled() {
        return is_scheduled;
    }

    public void setIs_scheduled(String is_scheduled) {
        this.is_scheduled = is_scheduled;
    }

    public Integer getOrigin_airport_id() {
        return origin_airport_id;
    }

    public void setOrigin_airport_id(Integer origin_airport_id) {
        this.origin_airport_id = origin_airport_id;
    }

    public Integer getDestination_airport_id() {
        return destination_airport_id;
    }

    public void setDestination_airport_id(Integer destination_airport_id) {
        this.destination_airport_id = destination_airport_id;
    }

    public Integer getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(Integer flight_id) {
        this.flight_id = flight_id;
    }

    public String getDeparture_dt() {
        return departure_dt;
    }

    public void setDeparture_dt(String departure_dt) {
        this.departure_dt = departure_dt;
    }

    public String getArrival_dt() {
        return arrival_dt;
    }

    public void setArrival_dt(String arrival_dt) {
        this.arrival_dt = arrival_dt;
    }

    public String getOrigin_airport() {
        return origin_airport;
    }

    public void setOrigin_airport(String origin_airport) {
        this.origin_airport = origin_airport;
    }

    public String getDestination_airport() {
        return destination_airport;
    }

    public void setDestination_airport(String destination_airport) {
        this.destination_airport = destination_airport;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s, %s -> %s", origin_airport, destination_airport, departure_dt, arrival_dt);
    }
}
