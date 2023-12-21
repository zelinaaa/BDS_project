package org.but.feec.ars.api;

public class CustomerCreateView {
    private Integer person_id;
    private String first_name;
    private String family_name;
    private String date_of_birth;
    private String gender;
    private String email;
    private String phone;
    private Integer address_id;
    private Integer balance;
    private Integer bookings_created;
    private char[] password;

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public Integer getPerson_id() { return person_id;}
    public void setPerson_id(Integer person_id) {this.person_id=person_id;}

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Integer address_id) {
        this.address_id = address_id;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getBookings_created() {
        return bookings_created;
    }

    public void setBookings_created(Integer bookings_created) {
        this.bookings_created = bookings_created;
    }

}
