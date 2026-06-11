package entities;

public class Airplane {
    private String icao;
    private String iata;
    private String model;
    private String critical;

    public Airplane(String icao, String iata, String model, String critical){
        this.icao = icao;
        this.iata = iata;
        this.model = model;
        this.critical = critical;
    }

    public String getIcao() {
        return icao;
    }

    public String getIata() {
        return iata;
    }

    public String getModel() {
        return model;
    }

    public String getCritical() {
        return critical;
    }
}
