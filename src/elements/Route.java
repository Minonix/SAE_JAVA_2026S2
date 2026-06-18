package elements;

import org.graphstream.graph.implementations.AbstractEdge;

public class Route extends AbstractEdge {
    private int fiabilite, distance, duree;
    private String affFiabilite, affDistance, affDuree;

    protected Route(String id, CentreSante source, CentreSante target, int fiabilite, int distance, int duree) {
        super(id, source, target, false);
        this.fiabilite = fiabilite;
        this.distance = distance;
        this.duree = duree;

        this.affDistance = "";
        this.affFiabilite = "";
        this.affDuree = "";
    }

    private void initVue() {
        this.setAttribute("ui.label", affDistance+affDuree+affFiabilite);

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

    public int calculeDureeEstimee(){
        return duree*(20-fiabilite)/10 ;
    }

    public void switchAffDistance() {
        if (affDistance.isEmpty()) affDistance = "Distance : "+distance+"  ";
        else affDistance = "";
        initVue();
    }

    public void switchAffDuree() {
        if (affDuree.isEmpty()) affDuree = "Durée : "+duree+"  ";
        else affDuree = "";
        initVue();
    }

    public void switchAffFiabilite() {
        if (affFiabilite.isEmpty()) affFiabilite = "Fiabilité : "+fiabilite;
        else affFiabilite = "";
        initVue();
    }
}