import java.time.LocalDateTime;

public class Edge implements Comparable<Edge> {
    private String origin;
    private String destination;
    private double weight;
    private String flightNumber;
    private String icaoCompany;
    private String icaoPlane;
    private LocalDateTime originDateTime;
    private LocalDateTime destinationDateTime;

    public Edge(String origin, String destination, double weight, String flightNumber, String icaoCompany,
            String icaoPlane, LocalDateTime originDateTime, LocalDateTime destinationDateTime) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.flightNumber = flightNumber;
        this.icaoCompany = icaoCompany;
        this.icaoPlane = icaoPlane;
        this.originDateTime = originDateTime;
        this.destinationDateTime = destinationDateTime;
    }

    public String getOrigin() {
        return origin;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public String getFlightNumber() {
        return flightNumber;
    }
    
    public String getIcaoCompany() {
        return icaoCompany;
    }
    
    public String getIcaoPlane() {
        return icaoPlane;
    }
    
    public LocalDateTime getOriginDateTime() {
        return originDateTime;
    }

    public LocalDateTime getDestinationDateTime() {
        return destinationDateTime;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
    
    @Override
    public String toString() {
        return "Edge [origin=" + origin + ", destination=" + destination + ", weight=" + weight + ", flightNumber="
        + flightNumber + ", icaoCompany=" + icaoCompany + ", icaoPlane=" + icaoPlane + "]";
    }
}