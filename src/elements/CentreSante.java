package elements;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

public class CentreSante extends SingleNode {
    private int idCentre;
    private char centerType;
    private String nomCentre;

    protected CentreSante(AbstractGraph graph, String id, String nomCentre, int idCentre, char centerType) {
        super(graph, id);
        this.nomCentre = nomCentre;
        this.idCentre = idCentre;
        this.centerType = centerType;
        initVue();
    }

    public String getNomCentre() {
        return nomCentre;
    }

    private void initVue() {
        if (idCentre == 'M') {
            this.setAttribute("ui.style", "fill-color: blue;");
        }
    }

    public int getIdCentre() {
        return idCentre;
    }

    public char getCenterType() {
        return centerType;
    }

    public void setIdCentre(int idCentre) {
        this.idCentre = idCentre;
    }

    public void setCenterType(char centerType) {
        this.centerType = centerType;
    }
}
