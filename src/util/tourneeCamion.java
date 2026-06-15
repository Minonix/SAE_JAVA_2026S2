package util;

import elements.*;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import java.util.ArrayList;

public class tourneeCamion {

    public static ArrayList<CentreSante> tourneeGlouton(Carte carte){
        ArrayList<CentreSante> solution = new ArrayList<CentreSante>();

        boolean flagLivre = false ;

        CentreSante centreCourant = carte.getCentre(0);

        while (!flagLivre) {
            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant);

            int min = -1;
            int imin = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min > infoD.get(i).get(0) || min == -1) && !solution.contains(carte.getCentre(i)) && i != centreCourant.getIdCentre()) {
                    min = infoD.get(i).get(0);
                    imin = i;
                }
            }
            Path mChemin = carte.dijkstraDuree(centreCourant, carte.getCentre(imin));
            for (Node d : mChemin.getNodeSet() ) {
                if (!solution.contains(d)) {
                    solution.add((CentreSante) d);
                }
                centreCourant = (CentreSante) d;

            }
            flagLivre = true;
            for (Node n : carte.getNodeSet()) {
                flagLivre = flagLivre && solution.contains((CentreSante)n);
            }
        }

        return solution ;
    }

    public static ArrayList<CentreSante> tourneeGlouton(Carte carte, char type){
        ArrayList<CentreSante> pass = new ArrayList<CentreSante>() ;
        ArrayList<CentreSante> solution = new ArrayList<CentreSante>();
        CentreSante centreCourant ;
        boolean flagLivre = false ;

        for (Node n : carte.getNodeSet() ){
            if (((CentreSante)n).getCenterType() == type) {
                pass.add((CentreSante) n);
            }
        }


        int id =0;
        do {
            centreCourant = carte.getCentre(id);
            id++ ;
        } while (centreCourant.getCenterType() == type);

        while (!flagLivre) {
            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant);

            int min = -1;
            int imin = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min > infoD.get(i).get(0) || min == -1) && !pass.contains(carte.getCentre(i)) && i != centreCourant.getIdCentre()) {
                    min = infoD.get(i).get(0);
                    imin = i;
                }
            }
            Path mChemin = carte.dijkstraDuree(centreCourant, carte.getCentre(imin));
            for (Node d : mChemin.getNodeSet() ) {
                if (!solution.contains(d)) {
                    pass.add((CentreSante) d);
                    solution.add((CentreSante) d);
                }
                centreCourant = (CentreSante) d;

            }
            flagLivre = true;
            for (Node n : carte.getNodeSet()) {
                flagLivre = flagLivre && pass.contains((CentreSante)n);
            }
        }

        return solution ;
    }

    public static int getDuree(Carte g, ArrayList<CentreSante> t){
        int duree = 0 ;
        for (int i=0 ; i < t.size()-1; i++){
            if ((g.getRoute(t.get(i).getIdCentre(), t.get(i+1).getIdCentre()) != null)) {
                duree += (g.getRoute(t.get(i).getIdCentre(), t.get(i + 1).getIdCentre())).getDuree();
            }
            else{
                duree +=  g.dijkstraInfos(t.get(i)).get(t.get(i+1).getIdCentre()).get(0);
            }
        }
        return duree ;
    }
}

