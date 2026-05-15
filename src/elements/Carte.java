package elements;

import org.graphstream.graph.implementations.SingleGraph;

public class Carte extends SingleGraph {
    public Carte(String id) {
        super(id);
    }

    public void ajouterCentre(CentreSante centre) {
        super.addNodeCallback(centre);
    }

    // TODO, plusieurs pistes possibles
    public void ajouterRoute() {

    }
}
