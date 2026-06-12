package util;

import elements.*;
import org.graphstream.graph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class tourneeCamion {

    public static ArrayList<CentreSante> tourneeGlouton(Carte carte){
        ArrayList<CentreSante> solution = new ArrayList<CentreSante>();

        int nb_centre = carte.getNbTotalCentres() ;
        HashMap<Node, Boolean> estLivre = new HashMap<Node, Boolean>();
        boolean flagLivre = false ;
        CentreSante centreCourant = carte.getCentre(0);
        solution.add(centreCourant);
        for (Node n : carte.getNodeSet()) {
            estLivre.put(n, false) ;
        }
        estLivre.put(centreCourant, true);
        while (!flagLivre) {
            boolean flagTemp = false ;
            Node n_suiv ;
            Iterator<Node> itCentre = (centreCourant.getNeighborNodeIterator()) ;
            while (itCentre.hasNext() && !flagTemp) {
                n_suiv = itCentre.next();
                if (!estLivre.get(n_suiv)) {
                    estLivre.put(n_suiv, true);
                    solution.add((CentreSante) n_suiv);
                    flagTemp = true;
                    centreCourant = (CentreSante) n_suiv;
                }
            }
            if (!flagTemp) {
                flagLivre = true ;
                for (Node n : carte.getNodeSet()) {
                    flagLivre = flagLivre && estLivre.get(n) ;
                }
            }
            if (!flagLivre) {
                centreCourant = (CentreSante)centreCourant.getNeighborNodeIterator().next() ;
            }
        }
        return solution ;
    }
}

