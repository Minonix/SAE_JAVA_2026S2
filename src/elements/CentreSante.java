package elements;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

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

    public int getIdCentre() {
        return idCentre;
    }

    public char getCenterType() {
        return centerType;
    }

    public void switchAffIdCentre() {
        if (affIdCentre.isEmpty()) affIdCentre = "ID: "+idCentre + "  ";
        else affIdCentre = "";
        initVue();
    }

    public void switchAffCenterType() {
        if (affCenterType.isEmpty()) affCenterType= "Type : "+centerType;
        else affCenterType = "";
        initVue();
    }

    public void switchAffNomCentre() {
        if (affNomCentre.isEmpty()) affNomCentre = "Nom : "+nomCentre+"  ";
        else affNomCentre = "";
        initVue();
    }
}
