package elements;

import org.graphstream.graph.implementations.AbstractEdge;

public class Route extends AbstractEdge {
    private final int fiabilite, distance, duree;
    private String affFiabilite, affDistance, affDuree;
    private CentreSante source, target;

    
    protected Route(String id, CentreSante source, CentreSante target, int fiabilite, int distance, int duree) {
        super(id, source, target, false);
        this.source = source;
        this.target = target;
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

    public CentreSante getSource() {
        return source;
    }

    public CentreSante getTarget() {
        return target;
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