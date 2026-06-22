import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
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

        // Inicializa todas as distâncias com infinito,
        // exceto a do vértice inicial
        for(String v: g.getVerts())
            distTo.put(v, Double.POSITIVE_INFINITY);
        // Distância do início até o início é... zero
        arrivalTime.put(origem, horarioInicial);
        distTo.put(origem, 0.0);
        dijkstra(g, origem, hubs);
    }

    private void dijkstra(WeightedTemporalDigraph g,  String origem, Map<String,Integer> hubs)
    {
        pq.insert(origem, distTo.get(origem)); // distTo.get(s) ==> 0.0
        // Enquanto houver algum vértice na fila...
        while(!pq.isEmpty()) {
            // Retira o vértice com menor distância total
            String v = pq.delMin();
            // E "relaxa" todas as arestas a partir dele
            for(Edge e: g.getAdj(v)) {
                relax(e, origem, hubs);
            }
        }        
    }

    private void relax(Edge e, String origem, Map<String,Integer> hubs) {
        String originIcao = e.getOrigin();
        String destinationIcao = e.getDestination();
        LocalDateTime chegadaAtual = arrivalTime.get(originIcao);
        if (chegadaAtual == null) {
            return;
        }   
        int waitTime = 45;
        if (hubs.containsKey(originIcao)) {
            waitTime = 60;
        }
        if (originIcao.equals(origem)) {
            waitTime = 0;
        }
        LocalDateTime horarioMinimo = chegadaAtual.plusMinutes(waitTime);
        if(e.getOriginDateTime().isBefore(horarioMinimo)){
            return;
        }
        double espera = Duration.between(chegadaAtual, e.getOriginDateTime()).toMinutes();
        double dist = distTo.get(originIcao) + espera + e.getWeight();
        // Se o custo for menor do que o atual para w...
        if(distTo.get(destinationIcao) > dist) {
            // ...significa que achamos um caminho melhor
            distTo.put(destinationIcao, dist);
            edgeTo.put(destinationIcao, e);
            arrivalTime.put(destinationIcao, e.getDestinationDateTime());
            if(pq.contains(destinationIcao))
                // Já existe na pq, então reduz o peso (distância)
                // e faz "swim" (se necessário)
                pq.decreaseValue(destinationIcao, dist);
            else
                // Não existe na pq, então insere
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
        // Enquanto não chegar na primeira aresta...
        while(e != null) {
            // Adiciona no início, pois o caminho é
            // percorrido ao contrário (do fim para o início)
            path.addFirst(e);
            // A próxima aresta é aquela que vem de V (início desta aresta)
            // (lembrando: estamos percorrendo ao CONTRÁRIO)
            e = edgeTo.get(e.getOrigin());
        }
        return path;
    }

    // public static void teste(WeightedTemporalDigraph g, String origem, LocalDateTime horarioInicial, Map<String,Integer> hubs) {
    //     DijkstraSP dij = new DijkstraSP(g, "0", horarioInicial, hubs);
    //     for(String v: g.getVerts()) {
    //         System.err.print(v+": ");
    //         if(!dij.hasPathTo(v)) {
    //             System.out.println("SEM CAMINHO");
    //         }
    //         else {
    //             for(Edge e: dij.pathTo(v)) {
    //                 System.out.print(e+" ");
    //             }
    //             System.out.println("-> "+dij.distTo(v));
    //         }
    //     }
    // }
}