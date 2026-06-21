package elements;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

/**
 * Représente un centre de santé (un nœud) dans le graphe.
 * Hérite de {@link SingleNode} de la bibliothèque GraphStream.
 * Un centre de santé se définit par son nom, son identifiant unique et son type 
 * ('M' pour Maternité, 'O' pour Bloc Opératoire, 'N' pour Centre de Nutrition).
 * La classe gère également les attributs visuels du nœud pour l'interface graphique.
 */
public class CentreSante extends SingleNode {
    private int idCentre;
    private char centerType;
    private final String nomCentre;

    private String affIdCentre;
    private String affCenterType;
    private String affNomCentre;

    protected CentreSante(AbstractGraph graph, String id, String nomCentre, int idCentre, char centerType) {
        super(graph, id);
        this.nomCentre = nomCentre;
        this.idCentre = idCentre;
        this.centerType = centerType;

        this.affIdCentre = "";
        this.affCenterType = "";
        this.affNomCentre = "";
        switchAffIdCentre();
        switchAffCenterType();
        switchAffNomCentre();

        initVue();
    }

    /**
     * Récupère le nom formaté ou brut du centre de santé.
     *
     * @return Le nom textuel du centre.
     */
    public String getNomCentre() {
        return nomCentre;
    }

    private void initVue() {
        this.setAttribute("ui.label", affIdCentre+affNomCentre+affCenterType);
        if (centerType == 'M') {
            this.setAttribute("ui.style", "fill-color: blue;");
        } else if (centerType == 'O') {
            this.setAttribute("ui.style", "fill-color: red;");
        } else {
            this.setAttribute("ui.style", "fill-color: green;");
        }
    }

    /**
     * Récupère l'identifiant numérique unique du centre de santé.
     *
     * @return L'identifiant sous forme d'entier.
     */
    public int getIdCentre() {
        return idCentre;
    }

    /**
     * Récupère le caractère représentant le type du centre de santé.
     * 'M' = Maternité, 'O' = Bloc Opératoire, 'N' = Centre de Nutrition.
     *
     * @return Le caractère symbolisant le type de centre.
     */
    public char getCenterType() {
        return centerType;
    }

    /**
     * Active ou désactive l'affichage de l'identifiant du centre sur l'interface graphique.
     * Met à jour le label visuel du nœud en conséquence.
     */
    public void switchAffIdCentre() {
        if (affIdCentre.isEmpty()) affIdCentre = "ID: "+idCentre + "  ";
        else affIdCentre = "";
        initVue();
    }

    /**
     * Active ou désactive l'affichage du type du centre sur l'interface graphique.
     * Met à jour le label visuel du nœud en conséquence.
     */
    public void switchAffCenterType() {
        if (affCenterType.isEmpty()) affCenterType= "Type : "+centerType;
        else affCenterType = "";
        initVue();
    }

    /**
     * Active ou désactive l'affichage du nom du centre sur l'interface graphique.
     * Met à jour le label visuel du nœud en conséquence.
     */
    public void switchAffNomCentre() {
        if (affNomCentre.isEmpty()) affNomCentre = "Nom : "+nomCentre+"  ";
        else affNomCentre = "";
        initVue();
    }
}