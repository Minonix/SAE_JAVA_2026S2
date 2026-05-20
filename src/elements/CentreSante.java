package elements;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

public class CentreSante extends SingleNode {
    private int idCentre;
    private char centerType;

    protected CentreSante(AbstractGraph graph, String id, int idCentre, char centerType) {
        super(graph, id);
        this.idCentre = idCentre;
        this.centerType = centerType;
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
