package elements;

import org.graphstream.graph.implementations.AbstractEdge;

public class Route extends AbstractEdge {
    private int fiabilite, distance, duree;

    public Route(String id, CentreSante source, CentreSante target, int fiabilite, int distance, int duree) {
        super(id, source, target, false);
        this.fiabilite = fiabilite;
        this.distance = distance;
        this.duree = duree;
    }

    public int getFiabilite() {
        return fiabilite;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuree() {
        return duree;
    }

    public void setFiabilite(int fiabilite) {
        this.fiabilite = fiabilite;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}