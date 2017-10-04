package util;

/**
 * @author (created on 10/4/2017).
 */
public enum AlgorithmsTypes {

    DIJKSTRA("Dijkstra"), A_STAR("A*"), BFS("BFS"), D_TIME("Dijkstra by the time");

    private String name;

    AlgorithmsTypes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
