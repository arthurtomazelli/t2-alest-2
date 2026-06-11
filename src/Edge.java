public class Edge implements Comparable<Edge> {
    private String origin;
    private String destination;
    private double weight;
    private String flightNumber;
    private String icaoCompany;
    private String icaoPlane;

    public Edge(String origin, String destination, double weight, String flightNumber, String icaoCompany,
            String icaoPlane) {
        this.origin = origin;
        this.destination = destination;
        this.weight = weight;
        this.flightNumber = flightNumber;
        this.icaoCompany = icaoCompany;
        this.icaoPlane = icaoPlane;
    }

    public String getOrigin() {
        return origin;
    }

    public String getdestination() {
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