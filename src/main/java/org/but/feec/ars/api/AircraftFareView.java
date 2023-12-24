package org.but.feec.ars.api;

public class AircraftFareView {
    private String model;
    private Double fare_per_unit;
    private Double travel_class_fare_multiplier;
    private Integer travel_class_id;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getFare_per_unit() {
        return fare_per_unit;
    }

    public void setFare_per_unit(Double fare_per_unit) {
        this.fare_per_unit = fare_per_unit;
    }

    public Double getTravel_class_fare_multiplier() {
        return travel_class_fare_multiplier;
    }

    public void setTravel_class_fare_multiplier(Double travel_class_fare_multiplier) {
        this.travel_class_fare_multiplier = travel_class_fare_multiplier;
    }

    public Integer getTravel_class_id() {
        return travel_class_id;
    }

    public void setTravel_class_id(Integer travel_class_id) {
        this.travel_class_id = travel_class_id;
    }
}
