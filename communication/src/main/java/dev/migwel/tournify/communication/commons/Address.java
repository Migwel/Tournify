package dev.migwel.tournify.communication.commons;

public class Address {
    private String city;
    private String state;
    private String street;
    private String houseNumber;
    private String country;

    public Address() {
    }

    public Address(String city, String state, String street, String houseNumber, String country) {
        this.city = city;
        this.state = state;
        this.street = street;
        this.houseNumber = houseNumber;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
