package org.but.feec.ars.api;

public class BookingView {
    private Integer booking_id;
    private String booking_status;
    private String booking_made;
    private Integer flight_id;
    private Integer person_id;
    private Integer seat_id;

    public Integer getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Integer booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getBooking_made() {
        return booking_made;
    }

    public void setBooking_made(String booking_made) {
        this.booking_made = booking_made;
    }

    public Integer getFlight_id() {
        return flight_id;
    }

    public void setFlight_id(Integer flight_id) {
        this.flight_id = flight_id;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    public Integer getSeat_id() {
        return seat_id;
    }

    public void setSeat_id(Integer seat_id) {
        this.seat_id = seat_id;
    }

    @Override
    public String toString() {
        return "BookingView{" +
                "booking_id=" + booking_id +
                ", booking_status='" + booking_status + '\'' +
                ", booking_made='" + booking_made + '\'' +
                ", flight_id=" + flight_id +
                ", person_id=" + person_id +
                ", seat_id=" + seat_id +
                '}';
    }
}
