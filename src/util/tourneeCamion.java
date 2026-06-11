package util;

import elements.*;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class tourneeCamion {

    public static ArrayList<Route> tourneeGlouton(Carte carte){
        ArrayList<Route> solution = new ArrayList<Route>();
        ArrayList<CentreSante> estLivre = new ArrayList<CentreSante>();

        boolean flagLivre = false ;

        CentreSante centreCourant = carte.getCentre(0);

        while (!flagLivre) {
            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant);

            int min = -1;
            int imin = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min > infoD.get(i).get(0) || min == -1) && !estLivre.contains(carte.getCentre(i)) && i != centreCourant.getIdCentre()) {
                    min = infoD.get(i).get(0);
                    imin = i;
                }
            }
            Path mChemin = carte.dijkstraDuree(centreCourant, carte.getCentre(imin));
            System.out.println("Chemin :" + mChemin);
            for (Edge e : mChemin.getEdgeSet()) {
                solution.add((Route) e);
            }
            for (Node d : mChemin.getNodeSet()) {
                estLivre.add((CentreSante)d);
                centreCourant = (CentreSante) d;

            }

            flagLivre = true;
            for (Node n : carte.getNodeSet()) {
                flagLivre = flagLivre && estLivre.contains((CentreSante)n);
            }
        }
        System.out.println(estLivre);

        return solution ;

//        ArrayList<CentreSante> solution = new ArrayList<CentreSante>();
//
//        int nb_centre = carte.getNbTotalCentres() ;
//        HashMap<Node, Boolean> estLivre = new HashMap<Node, Boolean>();
//        boolean flagLivre = false ;
//        CentreSante centreCourant = carte.getCentre(0);
//        solution.add(centreCourant);
//        for (Node n : carte.getNodeSet()) {
//            estLivre.put(n, false) ;
//        }
//        estLivre.put(centreCourant, true);
//        while (!flagLivre) {
//            boolean flagTemp = false ;
//            Node n_suiv ;
//            Iterator<Node> itCentre = (centreCourant.getNeighborNodeIterator()) ;
//            while (itCentre.hasNext() && !flagTemp) {
//                n_suiv = itCentre.next();
//                if (!estLivre.get(n_suiv)) {
//                    estLivre.put(n_suiv, true);
//                    solution.add((CentreSante) n_suiv);
//                    flagTemp = true;
//                    centreCourant = (CentreSante) n_suiv;
//                }
//            }
//            if (!flagTemp) {
//                flagLivre = true ;
//                for (Node n : carte.getNodeSet()) {
//                    flagLivre = flagLivre && estLivre.get(n) ;
//                }
//            }
//            if (!flagLivre) {
//                centreCourant = (CentreSante)centreCourant.getNeighborNodeIterator().next() ;
//            }
//        }
//        return solution ;
    }
}

