package elements;

import org.graphstream.graph.implementations.SingleGraph;

import javax.print.StreamPrintService;
import javax.sql.rowset.spi.SyncProvider;
import java.util.Random;

public class Carte extends SingleGraph {
    private Route rTmp;
    private CentreSante cTmp;

    private int nbMat ;
    private int nbBloc ;
    private int nbNutri ;


    public Carte(String id) {
        super(id);
        int nbMat = 0 ;
        int nbBloc = 0;
        int nbNutri = 0;
    }

    public int getNbMat() {
        return nbMat;
    }

    public int getNbBloc() {
        return nbBloc;
    }

    public int getNbNutri() {
        return nbNutri;
    }

    public int getNbTotalCentres() {
        return nbNutri+nbBloc+nbMat;
    }

    public void ajouterCentre(String nomCentre, char centerType) {
        cTmp = new CentreSante(this, getNbTotalCentres()+"", nomCentre, getNbTotalCentres(), centerType);
        super.addNodeCallback(cTmp);
        if (centerType == 'O') nbBloc++;
        else if (centerType == 'N') nbNutri++;
        else nbMat++;
    }

    public void ajouterRoute(int a, int b, int fiabilite, int distance, int duree) {
        String strTmp;
        if (a < b) {
            strTmp = a +"-" + b;
        } else {
            strTmp = b +"-" + a;
        }
        rTmp = new Route(strTmp, this.getNode(""+a), this.getNode(""+b), fiabilite, distance, duree);
        super.addEdgeCallback(rTmp);
    }

    public CentreSante getCentre(int i) {
        return this.getNode(i);
    }

    public Route getRoute(int a, int b) {
        String strTmp;
        if (a < b) {
            strTmp = a +"-" + b;
        } else {
            strTmp = b +"-" + a;
        }
        return this.getEdge(strTmp);
    }

    public static Carte construireGrapheAleatoire(String nomGraph, int nbSommets){
        Carte graph = new Carte(nomGraph);

        Random alea = new Random();
        int proba_centre;
        int proba_existe;
        int proba_fiabl;
        int proba_dist;
        int proba_duree;
        for (int i=0 ; i< nbSommets ; i++){
            proba_centre = alea.nextInt(5);
            if (proba_centre < 3){
                graph.ajouterCentre("" + i, 'M');
            }
            else if (proba_centre == 3){
                graph.ajouterCentre("" + i, 'O');
            }
            else {
                graph.ajouterCentre("" + i, 'N');
            }
        }
        for (int i=0; i< nbSommets; i++){
            for (int j=i; j< nbSommets; j++){
                proba_existe = alea.nextInt(5);
                if (proba_existe == 0){
                    proba_fiabl = alea.nextInt(2, 9) ;
                    proba_dist = alea.nextInt(10,51) ;
                    proba_duree = alea.nextInt(10,121) ;
                    graph.ajouterRoute(i, j, proba_fiabl,proba_dist,proba_duree) ;
                }
            }
        }
        return graph ;
    }
}
