package entities;

public class Airport {
    private String icao;
    private String iata;
    private String name;
    private String city;
    private String state;
    private String country;
    private String critical;
    private String latitude;
    private String longitude;
    
    public Airport(String icao, String iata, String name, String city, String state, String country, String critical, String latitude, String longitude) {
        this.icao = icao;
        this.iata = iata;
        this.name = name;
        this.city = city;
        this.state = state;
        this.country = country;
        this.critical = critical;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIcao() {
        return icao;
    }

    public String getIata() {
        return iata;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getCritical() {
        return critical;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    } 
}
