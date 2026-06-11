package entities;

public class Company {
    private String icao;
    private String iata;
    private String name;
    private String country;

    public Company(String icao, String iata, String name, String country) {
        this.icao = icao;
        this.iata = iata;
        this.name = name;
        this.country = country;
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
    public String getCountry() {
        return country;
    }
}
