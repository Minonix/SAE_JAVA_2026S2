package elements;

import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.SingleNode;

public class CentreSante extends SingleNode {
    private int idCentre;
    private char centerType;

    public CentreSante(AbstractGraph graph, String id, int id_centre, char center_type) {
        super(graph, id);
        this.idCentre = id_centre;
        this.centerType = center_type;
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
