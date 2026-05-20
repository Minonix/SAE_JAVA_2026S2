package elements;

import org.graphstream.graph.implementations.SingleGraph;

public class Carte extends SingleGraph {
    Route rTmp;
    CentreSante cTmp;

    public Carte(String id) {
        super(id);
    }

    public void ajouterCentre(int idCentre, char centerType) {
        cTmp = new CentreSante(this, Integer.toString(idCentre), idCentre, centerType);
        super.addNodeCallback(cTmp);
    }

    public void ajouterRoute(String nomRoute, int a, int b, int fiabilite, int distance, int duree) {
        rTmp = new Route(nomRoute, this.getNode(Integer.toString(a)), this.getNode(Integer.toString(b)), fiabilite, distance, duree);
        super.addEdgeCallback(rTmp);
    }
}
