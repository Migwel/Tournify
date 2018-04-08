package net.migwel.tournify.data.consumer.smashgg;

public class Tournament {
    private String name;
    private Long startAt;
    private String city;
    private String addrState;
    private String postalCode;
    private String countryCode;
    private String venueAddress;

    public String getName() {
        return name;
    }

    public Long getStartAt() {
        return startAt;
    }

    public String getCity() {
        return city;
    }

    public String getAddrState() {
        return addrState;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getVenueAddress() {
        return venueAddress;
    }
}
