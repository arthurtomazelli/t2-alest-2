import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeightedTemporalDigraph {
  protected Map<String, List<Edge>> graph;
  protected Set<String> vertices;
  protected int totalVertices;
  protected int totalEdges;

  public WeightedTemporalDigraph() {
    graph = new HashMap<>();
    vertices = new HashSet<>();
    totalVertices = totalEdges = 0;
  }

  public void addEdge(String origin, String destination, double weight, String flightNumber, String icaoCompany, String icaoPlane) {
    Edge e = new Edge(origin, destination, weight, flightNumber, icaoCompany, icaoPlane);
    addToList(origin, e);
    if (!vertices.contains(origin)) {
      vertices.add(origin);
      totalVertices++;
    }
    if (!vertices.contains(destination)) {
      vertices.add(destination);
      totalVertices++;
    }
    totalEdges++;
  }

  public Iterable<Edge> getAdj(String v) {
    List<Edge> res = graph.get(v);
    if (res == null)
      res = new LinkedList<>();
    return res;
  }

  public int getTotalVerts() {
    return totalVertices;
  }

  public int getTotalEdges() {
    return totalEdges;
  }

  public Set<String> getVerts() {
    return vertices;
  }

  public Iterable<Edge> getEdges() {
    Set<Edge> ed = new HashSet<>();
    for (String v : getVerts().stream().sorted().toList()) {
      for (Edge e : getAdj(v)) {
        if (!ed.contains(e)) {
          ed.add(e);
        }
      }
    }
    return ed;
  }

  // Adiciona um vértice adjacente a outro, criando a lista
  // de adjacências caso ainda não exista no dicionário
  protected List<Edge> addToList(String v, Edge e) {
    List<Edge> list = graph.get(v);
    if (list == null)
      list = new LinkedList<>();
    list.add(e);
    graph.put(v, list);
    return list;
  }

  public int size(){
    return totalEdges;
  }

  public Map<String, List<Edge>> getGraph(){
    return graph;
  }
}