import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entities.Airplane;
import entities.Airport;
import entities.Company;

public class App {
    private In in;
    private List<String[]> flightList;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private WeightedTemporalDigraph graph;
    private Map<String, Airplane> airplanes;
    private Map<String, Company> companies;
    private Map<String, Airport> airports;
    private Map<String, Integer> grau;
    private Map<String, Integer> topFiveHubs;
    private Scanner sc = new Scanner(System.in);

    public App() {
        in = new In();
        companies = new HashMap<>();
        airplanes = new HashMap<>();
        airports = new HashMap<>();

        // companies = populateMap("../resources/cias.csv", ";", companies, 0);
        // airplanes = populateMap("../resources/aeronaves.csv", ";", airplanes, 1);
        // airports = populateMap("../resources/aerodromos.csv", ";", airports, 2);

        companies = populateMap("./resources/cias.csv", ";", companies, 0);
        airplanes = populateMap("./resources/aeronaves.csv", ";", airplanes, 1);
        airports = populateMap("./resources/aerodromos.csv", ";", airports, 2);

        graph = new WeightedTemporalDigraph();
        flightList = in.readCSV("./resources/voos_mar2026.csv", ",");

        populateGraph(flightList, graph, formatter);
        System.out.println(graph.size());
        
        grau = new HashMap<>();
        topFiveHubs = new HashMap<>();

        calculateTotalDegree();
        calculateTopFiveHubs();
        solicitarVoo();
    }

    @SuppressWarnings("unchecked")
    public <T> Map<String, T> populateMap(String path, String splitChar, Map<String, T> map, int c) {
        List<String[]> list = in.readCSV(path, splitChar);

        switch (c) {
            case 0 -> {
                int cont = 0;
                for (String[] strings : list) {
                    if (!strings[0].equals("N/I")) {
                        map.put(strings[0], (T) new Company(strings[0], strings[1], strings[2], strings[3]));
                        if (cont == 0) {
                            System.out.println(map.keySet());
                        }
                        cont++;
                    }
                }
            }
            case 1 -> {
                for (String[] strings : list) {
                    map.put(strings[0], (T) new Airplane(strings[0], strings[1], strings[2], strings[3]));
                }
            }
            case 2 -> {
                for (String[] strings : list) {
                    map.put(strings[0], (T) new Airport(strings[0], strings[1], strings[2], strings[3], strings[4],
                            strings[5], strings[6], strings[7], strings[8]));
                }
            }
        }

        return map;
    }

    public void populateGraph(List<String[]> list, WeightedTemporalDigraph graph, DateTimeFormatter formatter) {
        for (int i = 0; i < list.size(); i++) {
            String[] strings = list.get(i);

            String origin = strings[10];
            String destination = strings[9];
            String flightNumber = strings[5];
            String icaoCompany = strings[7];
            String icaoPlane = strings[8];

            LocalDateTime departure = parseDateTime(strings[2]);
            LocalDateTime arrival = parseDateTime(strings[1]);

            long diferencaMinutos = (Duration.between(departure, arrival).toMinutes());

            graph.addEdge(origin, destination, diferencaMinutos, flightNumber, icaoCompany, icaoPlane, departure, arrival);
        }
    }


    public void calculateTotalDegree() {
        Map<String, Integer> mapOriginDegree = new HashMap<>();
        Map<String, Integer> mapDestinationDegree = new HashMap<>();

        for (Edge e : graph.getEdges()) {
            String origin = e.getOrigin();
            String destination = e.getDestination();

            mapOriginDegree.put(origin, mapOriginDegree.getOrDefault(origin, 0) + 1);
            mapDestinationDegree.put(destination, mapDestinationDegree.getOrDefault(destination, 0) + 1);
        }

        for (String airport : airports.keySet()) {
            int originDegree = mapOriginDegree.getOrDefault(airport, 0);
            int destinationDegree = mapDestinationDegree.getOrDefault(airport, 0);

            if(airports.get(airport).getCountry().equals("BRASIL")) grau.put(airport, originDegree + destinationDegree);;
        }

        for (String airport : grau.keySet()) {
            int grauValor = grau.get(airport);

            if(grauValor == 0) continue;

            System.out.println(airport + " -> " + grau.get(airport));
        }
    }

    public void calculateTopFiveHubs(){
        List<String> topFive = new ArrayList<>(grau.keySet());
        topFive = topFive.subList(0, 5);

        for (String s : grau.keySet()) {
            if (topFive.contains(s)) continue;

            int minIndex = 0;
            for (int i = 1; i < topFive.size(); i++) {
                if (grau.get(topFive.get(i)) < grau.get(topFive.get(minIndex))) {
                    minIndex = i;
                }
            }

            if (grau.get(s) > grau.get(topFive.get(minIndex))) {
                topFive.remove(minIndex);
                topFive.add(minIndex, s);
            }
        }

        System.out.println("--------------------------");

        topFive.sort((a, b) -> grau.get(b) - grau.get(a));

        for(String s : topFive){
            topFiveHubs.put(s, grau.get(s));
            System.out.println(s + " -> " + grau.get(s));
        }
    }

    public void solicitarVoo(){
        System.out.print("Digite um aeroporto de origem (código ICAO): \n-> ");
        String icaoOrigem = sc.nextLine();
        if (!grau.keySet().contains(icaoOrigem)) {
            System.out.println("ICAO não encontrado");
            return;
        }
        
        System.out.println();
        
        System.out.print("Digite um aeroporto de destino (código ICAO): \n-> ");
        String icaoDestino = sc.nextLine();
        if (!grau.keySet().contains(icaoDestino)) {
            System.out.println("ICAO não encontrado");
        }

        System.out.println();
        
        System.out.print("Digite uma data e horário (dd/MM/yyyy HH:mm): \n-> ");
        String dataString = sc.nextLine();
        LocalDateTime data;
        try {
            data = parseDateTime(dataString); 
        } catch (Exception e) {
            System.out.println("Data inválida");
            return;
        }
        

        System.out.println("Deseja eliminar um dos 5 hubs principais (ver abaixo) (s/n): ");
        
        for(String s : topFiveHubs.keySet()){
            System.out.println("- " + s);
        }

        System.out.print("-> ");
        String escolha = sc.nextLine().toLowerCase();

        System.out.println();

        if(escolha.equals("s")){
            System.out.print("Qual dos hubs você deseja eliminar? ");
            String hubEliminado = sc.nextLine();

            if(removeHub(hubEliminado)){
                System.out.println("Hub " + hubEliminado + " removido com sucesso.");
            } else {
                System.out.println("Hub " + hubEliminado + " não existente.");
            }
        }

        DijkstraSP dij = new DijkstraSP(graph, icaoOrigem, data, topFiveHubs);

        if (dij.hasPathTo(icaoDestino)) {

            System.out.println("Menor rota:");

            for (Edge e : dij.pathTo(icaoDestino)) {
                System.out.println(e.getOrigin() +  " -> " + e.getDestination() + ": " + e.getOriginDateTime().format(formatter));
            }

            System.out.println("Custo total: " + dij.distTo(icaoDestino));

        } else {
            System.out.println("SEM CAMINHO");
        }

        // for(String s : topFiveHubs.keySet()){
        //     System.out.println("- " + s);
        // }

        System.out.println();
    }

    public boolean removeHub(String hub){
        if(topFiveHubs.containsKey(hub)){
            topFiveHubs.remove(hub);
            graph.removeFromList(hub);
            return true;
        }

        return false;
    }

    public LocalDateTime parseDateTime(String localDateTime){
        return LocalDateTime.parse(localDateTime.replace("\"", ""), formatter);
    }

}   