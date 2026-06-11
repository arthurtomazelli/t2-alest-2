import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public App() {
        in = new In();
        companies = new HashMap<>();
        airplanes = new HashMap<>();
        airports = new HashMap<>();

        companies = populateMap("../resources/cias.csv", ";", companies, 0);
        airplanes = populateMap("../resources/aeronaves.csv", ";", airplanes, 1);
        airports = populateMap("../resources/aerodromos.csv", ";", airports, 2);

        // for (String icao : airplanes.keySet()) {
        //     System.out.println(airplanes.get(icao));
        // }

        // for (String icao : companies.keySet()) {
        //     System.out.println(companies.get(icao));
        // }

        graph = new WeightedTemporalDigraph();
        flightList = in.readCSV("../resources/voos_mar2026.csv", ",");

        populateGraph(flightList, graph, formatter);
        System.out.println(graph.size());
    }

    public <T> Map<String, T> populateMap(String path, String splitChar, Map<String, T> map, int c) {
        List<String[]> list = in.readCSV(path, splitChar);

        switch (c) {
            case 0 -> {
                int cont = 0;
                for (String[] strings : list) {
                    if (strings[0] != "N/I") {
                        map.put(strings[0], (T) new Company(strings[0], strings[1], strings[2], strings[3]));
                        if(cont == 0){
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
                    map.put(strings[0], (T) new Airport(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7], strings[8]));
                }
            }
        }

        return map;
    }

    public void populateGraph(List<String[]> list, WeightedTemporalDigraph graph, DateTimeFormatter formatter) {
        for (int i = 1; i < list.size(); i++) {
            String[] strings = list.get(i);
            LocalDateTime partida = LocalDateTime.parse(strings[2].replace("\"", ""), formatter);
            LocalDateTime chegada = LocalDateTime.parse(strings[1].replace("\"", ""), formatter);

            long diferencaMinutos = (Duration.between(partida, chegada).toMinutes());

            graph.addEdge(strings[10], strings[9], diferencaMinutos, strings[5], strings[7], strings[8]);
        }
    }

}
