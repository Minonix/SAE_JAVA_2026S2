package elements;

import org.graphstream.graph.implementations.SingleGraph;

public class Carte extends SingleGraph {
    public Carte(String id) {
        super(id);
    }

    public void ajouterCentre(CentreSante centre) {
        super.addNodeCallback(centre);
    }

    public void ajouterRoute(String nomRoute,CentreSante a, CentreSante b) {
        super.addEdge(nomRoute, a, b);
    }
}
