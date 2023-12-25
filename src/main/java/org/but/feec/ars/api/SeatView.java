package org.but.feec.ars.api;

public class SeatView {
    private Integer seat_id;
    private Integer seat_number;
    private Integer aircraft_model_id;
    private Integer travel_class_id;

    public Integer getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(Integer seat_id) {
        this.seat_id = seat_id;
    }

    public Integer getSeat_number() {
        return seat_number;
    }

    public void setSeat_number(Integer seat_number) {
        this.seat_number = seat_number;
    }

    public Integer getAircraft_model_id() {
        return aircraft_model_id;
    }

    public void setAircraft_model_id(Integer aircraft_model_id) {
        this.aircraft_model_id = aircraft_model_id;
    }

    public Integer getTravel_class_id() {
        return travel_class_id;
    }

    public void setTravel_class_id(Integer travel_class_id) {
        this.travel_class_id = travel_class_id;
    }

    @Override
    public String toString() {
        return seat_number.toString();
    }
}
