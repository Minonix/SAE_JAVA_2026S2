package elements;

import org.graphstream.graph.implementations.AbstractEdge;

/**
 * Représente une route (une arête) reliant deux centres de santé dans le graphe.
 * Hérite de {@link AbstractEdge} de la bibliothèque GraphStream.
 * Une route possède un niveau de fiabilité, une distance physique et une durée de parcours.
 * La classe embarque également la logique pour modifier son affichage textuel sur l'interface.
 */
public class Route extends AbstractEdge {
    private final int fiabilite, distance, duree;
    private String affFiabilite, affDistance, affDuree;
    private final CentreSante source;
    private final CentreSante target;

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

    /**
     * Récupère le centre de santé défini comme point de départ de la route.
     *
     * @return Le {@link CentreSante} source.
     */
    public CentreSante getSource() {
        return source;
    }

    /**
     * Récupère le centre de santé défini comme point d'arrivée de la route.
     *
     * @return Le {@link CentreSante} cible (target).
     */
    public CentreSante getTarget() {
        return target;
    }
    
    /**
     * Récupère le niveau de fiabilité de cette route.
     *
     * @return Un entier représentant la fiabilité de la route (généralement sur 10).
     */
    public int getFiabilite() {
        return fiabilite;
    }

    /**
     * Récupère la distance physique de cette route.
     *
     * @return La distance sous forme d'entier.
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Récupère la durée standard pour parcourir cette route.
     *
     * @return La durée sous forme d'entier.
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Calcule la durée estimée du parcours en appliquant un malus 
     * basé sur le niveau de fiabilité de la route.
     *
     * @return La durée ajustée (estimée) sous forme d'entier.
     */
    public int calculeDureeEstimee(){
        return duree*(20-fiabilite)/10 ;
    }

    /**
     * Active ou désactive l'affichage de la distance sur le label visuel de l'arête.
     */
    public void switchAffDistance() {
        if (affDistance.isEmpty()) affDistance = "Distance : "+distance+"  ";
        else affDistance = "";
        initVue();
    }

    /**
     * Active ou désactive l'affichage de la durée sur le label visuel de l'arête.
     */
    public void switchAffDuree() {
        if (affDuree.isEmpty()) affDuree = "Durée : "+duree+"  ";
        else affDuree = "";
        initVue();
    }

    /**
     * Active ou désactive l'affichage de la fiabilité sur le label visuel de l'arête.
     */
    public void switchAffFiabilite() {
        if (affFiabilite.isEmpty()) affFiabilite = "Fiabilité : "+fiabilite;
        else affFiabilite = "";
        initVue();
    }
}   