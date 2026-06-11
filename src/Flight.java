package entities;

public class Flight{
    private String originIcao;
    private String destinationIcao;
    private String departure;
    private String arrival;

    public Flight(String originIcao, String destinationIcao, String departure, String arrival) {
        this.originIcao = originIcao;
        this.destinationIcao = destinationIcao;
        this.departure = departure;
        this.arrival = arrival;
    }

    public String getOriginIcao() {
        return originIcao;
    }

    public String getdestinationIcao() {
        return destinationIcao;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }
}