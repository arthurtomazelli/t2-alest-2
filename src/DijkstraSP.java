import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DijkstraSP {

    private Map<String, Double> distTo;
    private Map<String, Edge> edgeTo;
    private Map<String, LocalDateTime> arrivalTime;
    private IndexMinHeap<String, Double> pq;

    public DijkstraSP(WeightedTemporalDigraph g, String origem, LocalDateTime horarioInicial, Map<String,Integer> hubs) {
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        arrivalTime = new HashMap<>();
        pq = new IndexMinHeap<>();

        for(String v: g.getVerts())
            distTo.put(v, Double.POSITIVE_INFINITY);
        arrivalTime.put(origem, horarioInicial);
        distTo.put(origem, 0.0);
        dijkstra(g, origem, hubs);
    }

    private void dijkstra(WeightedTemporalDigraph g,  String origem, Map<String,Integer> hubs)
    {
        pq.insert(origem, distTo.get(origem)); 
        while(!pq.isEmpty()) {
            String v = pq.delMin();
            for(Edge e: g.getAdj(v)) {
                relax(e, origem, hubs);
            }
        }        
    }

    private void relax(Edge e, String origem, Map<String, Integer> hubs) {
        String originIcao = e.getOrigin();
        String destinationIcao = e.getDestination();

        // Horário em que chegamos ao aeroporto de origem desta aresta
        LocalDateTime chegadaNoOrigem = arrivalTime.get(originIcao);
        if (chegadaNoOrigem == null) return; // origem ainda não foi atingida

        int waitTime = 45;
        if (hubs.containsKey(originIcao)) waitTime = 60;
        if (originIcao.equals(origem)) waitTime = 0;

        // O voo precisa partir DEPOIS que chegamos + tempo mínimo de espera
        LocalDateTime horarioMinimo = chegadaNoOrigem.plusMinutes(waitTime);
        if (e.getOriginDateTime().isBefore(horarioMinimo)) return;

        // Espera = tempo entre nossa chegada e a partida deste voo
        double espera = Duration.between(chegadaNoOrigem, e.getOriginDateTime()).toMinutes();
        double dist = distTo.get(originIcao) + espera + e.getWeight();

        if (distTo.getOrDefault(destinationIcao, Double.POSITIVE_INFINITY) > dist) {
            distTo.put(destinationIcao, dist);
            edgeTo.put(destinationIcao, e);
            arrivalTime.put(destinationIcao, e.getDestinationDateTime()); // ← atualiza chegada
            if (pq.contains(destinationIcao))
                pq.decreaseValue(destinationIcao, dist);
            else
                pq.insert(destinationIcao, dist);
        }
    }

    public double distTo(String v) {
        return distTo.get(v);
    }

    public boolean hasPathTo(String v) {
        return edgeTo.get(v) != null;
    }

    public Iterable<Edge> pathTo(String v) {
        LinkedList<Edge> path = new LinkedList<>();
        Edge e = edgeTo.get(v);
        while(e != null) {
            path.addFirst(e);
            e = edgeTo.get(e.getOrigin());
        }
        return path;
    }

}