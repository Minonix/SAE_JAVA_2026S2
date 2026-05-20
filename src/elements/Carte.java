package elements;

import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;

public class Carte extends SingleGraph {
    Route rTmp;
    CentreSante cTmp;

    private int nbMat ;
    private int nbBloc ;
    private int nbNutri ;


    public Carte(String id) {
        super(id);
        int nbMat = 0 ;
        int nbBloc = 0;
        int nbNutri = 0;
    }

    public void ajouterCentre(int idCentre, char centerType) {
        cTmp = new CentreSante(this, Integer.toString(idCentre), idCentre, centerType);
        super.addNodeCallback(cTmp);
    }

    public void ajouterRoute(String nomRoute, int a, int b, int fiabilite, int distance, int duree) {
        rTmp = new Route(nomRoute, this.getNode(Integer.toString(a)), this.getNode(Integer.toString(b)), fiabilite, distance, duree);
        super.addEdgeCallback(rTmp);
    }

    public static void construireGrapheAleatoire(String nomGraph,int nbSommets){
        Carte graph = new Carte(nomGraph);


        Random alea = new Random();
        int proba ;
        for (int i=0 ; i< nbSommets ; i++){
            proba = alea.nextInt(5);
            if (proba < 3){
                graph.ajouterCentre(i, 'M');

                graph.nbMat++;
            }
            else if (proba == 3){

            }
            else {

            }
        }


    }

}
