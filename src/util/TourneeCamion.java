package util;

import elements.*;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import java.util.ArrayList;

public class TourneeCamion {

    public static ArrayList<CentreSante> tourneeGlouton(Carte carte){
        ArrayList<CentreSante> solution = new ArrayList<>();

        boolean flagLivre = false ;

        CentreSante centreCourant = carte.getCentre(0);

        while (!flagLivre) {
            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant);

            int min = -1;
            int imin = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min > infoD.get(i).getFirst() || min == -1) && !solution.contains(carte.getCentre(i)) && i != centreCourant.getIdCentre()-1) {
                    min = infoD.get(i).getFirst();
                    imin = i;
                }
            }
            Path mChemin = carte.dijkstraDuree(centreCourant, carte.getCentre(imin));
            for (Node d : mChemin.getNodeSet() ) {
                if (!solution.contains((CentreSante) d)) {
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
        ArrayList<CentreSante> pass = new ArrayList<>() ;
        ArrayList<CentreSante> solution = new ArrayList<>();
        CentreSante centreCourant ;
        boolean flagLivre = false ;

        for (Node n : carte.getNodeSet() ){
            if (((CentreSante)n).getCenterType() != type) {
                pass.add((CentreSante) n);
            }
        }

        int id =0;
        do {
            centreCourant = carte.getCentre(id);
            id++ ;
        } while (centreCourant.getCenterType() != type);

        while (!flagLivre) {
            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant);

            int min = -1;
            int imin = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min > infoD.get(i).getFirst() || min == -1) && !pass.contains(carte.getCentre(i)) && i != centreCourant.getIdCentre()-1) {
                    min = infoD.get(i).getFirst();
                    imin = i;
                }
            }
            Path mChemin = carte.dijkstraDuree(centreCourant, carte.getCentre(imin));
            for (Node d : mChemin.getNodeSet() ) {
                if (!solution.contains((CentreSante) d)) {
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
            if ((g.getRoute(t.get(i).getIdCentre()-1, t.get(i+1).getIdCentre()-1) != null)) {
                duree += (g.getRoute(t.get(i).getIdCentre()-1, t.get(i + 1).getIdCentre()-1)).getDuree();
            }
            else{
                duree +=  g.dijkstraInfos(t.get(i)).get(t.get(i+1).getIdCentre()-1).getFirst();
            }
        }
        return duree ;
    }

    public static ArrayList<CentreSante>[] tourneeGlouton2C(Carte carte){
        ArrayList<CentreSante> solutionC1 = new ArrayList<>();
        ArrayList<CentreSante> solutionC2 = new ArrayList<>();

        boolean flagLivre = false ;

        CentreSante centreCourant1 = carte.getCentre(0);
        CentreSante centreCourant2 = carte.getCentre(0);

        while (!flagLivre) {

            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant1);

            int min1 = -1;
            int imin1 = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min1 > infoD.get(i).getFirst() || min1 == -1) && !solutionC1.contains(carte.getCentre(i)) && !solutionC2.contains(carte.getCentre(i)) && i != centreCourant1.getIdCentre()-1) {
                    min1 = infoD.get(i).getFirst();
                    imin1 = i;
                }
            }
            Path mChemin1 = carte.dijkstraDuree(centreCourant1, carte.getCentre(imin1));

            for (Node d : mChemin1.getNodeSet()) {
                if (!solutionC1.contains((CentreSante) d) && !solutionC2.contains((CentreSante) d)) {
                    solutionC1.add((CentreSante) d);
                    centreCourant1 = (CentreSante) d;
                }
            }
            flagLivre = true;
            for (Node n : carte.getNodeSet()) {
                flagLivre = flagLivre && (solutionC1.contains((CentreSante)n) || solutionC2.contains((CentreSante)n));
            }


            if (!flagLivre) {
                int min2 = -1;
                int imin2 = -1;
                for (int i = 0; i < infoD.size(); i++) {
                    if ((min2 > infoD.get(i).get(0) || min2 == -1) && i != imin1 && !solutionC1.contains(carte.getCentre(i)) && !solutionC2.contains(carte.getCentre(i)) && i != centreCourant1.getIdCentre()) {
                        min2 = infoD.get(i).get(0);
                        imin2 = i;
                    }
                }

                Path mChemin2 = carte.dijkstraDuree(centreCourant2, carte.getCentre(imin2));

                for (Node d : mChemin2.getNodeSet()) {
                    if (!solutionC1.contains(d) && !solutionC2.contains(d)) {
                        solutionC2.add((CentreSante) d);
                        centreCourant2 = (CentreSante) d;
                    }
                }
                flagLivre = true;
                for (Node n : carte.getNodeSet()) {
                    flagLivre = flagLivre && (solutionC1.contains((CentreSante) n) || solutionC2.contains((CentreSante) n));
                }
            }
        }
        ArrayList<CentreSante>[] solution = new ArrayList[2];
        solution[0] = solutionC1 ;
        solution[1] = solutionC2 ;
        return solution;
    }

    public static ArrayList<CentreSante>[] tourneeGlouton2C(Carte carte, char type){
        ArrayList<CentreSante> pass = new ArrayList<>() ;

        ArrayList<CentreSante> solutionC1 = new ArrayList<>();
        ArrayList<CentreSante> solutionC2 = new ArrayList<>();

        boolean flagLivre = false ;

        for (Node n : carte.getNodeSet() ){
            if (((CentreSante)n).getCenterType() != type) {
                pass.add((CentreSante) n);
            }
        }

        CentreSante centreCourant1;
        CentreSante centreCourant2;

        int id =0;
        do {
            centreCourant1 = carte.getCentre(id);
            centreCourant2 = carte.getCentre(id);
            id++ ;
        } while (centreCourant1.getCenterType() != type);

        while (!flagLivre) {

            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant1);

            int min1 = -1;
            int imin1 = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min1 > infoD.get(i).getFirst() || min1 == -1) && !pass.contains(carte.getCentre(i)) && !solutionC1.contains(carte.getCentre(i)) && !solutionC2.contains(carte.getCentre(i)) && i != centreCourant1.getIdCentre()-1) {
                    min1 = infoD.get(i).getFirst();
                    imin1 = i;
                }
            }
            Path mChemin1 = carte.dijkstraDuree(centreCourant1, carte.getCentre(imin1));

            for (Node d : mChemin1.getNodeSet()) {
                if (!solutionC1.contains((CentreSante) d) && !solutionC2.contains((CentreSante) d)) {
                    solutionC1.add((CentreSante) d);
                    pass.add((CentreSante) d);
                    centreCourant1 = (CentreSante) d;
                }
            }
            flagLivre = true;
            for (Node n : carte.getNodeSet()) {
                flagLivre = flagLivre && pass.contains((CentreSante)n);
            }

            if (!flagLivre) {
                int min2 = -1;
                int imin2 = -1;
                for (int i = 0; i < infoD.size(); i++) {
                    if ((min2 > infoD.get(i).get(0) || min2 == -1) && i != imin1 && !pass.contains(carte.getCentre(i)) && !solutionC1.contains(carte.getCentre(i)) && !solutionC2.contains(carte.getCentre(i)) && i != centreCourant1.getIdCentre()) {
                        min2 = infoD.get(i).get(0);
                        imin2 = i;
                    }
                }

                Path mChemin2 = carte.dijkstraDuree(centreCourant2, carte.getCentre(imin2));

                for (Node d : mChemin2.getNodeSet()) {
                    if (!solutionC1.contains(d) && !solutionC2.contains(d) || ((CentreSante) d).getIdCentre()==1) {
                        solutionC2.add((CentreSante) d);
                        pass.add((CentreSante) d);
                        centreCourant2 = (CentreSante) d;
                    }
                }
                flagLivre = true;
                for (Node n : carte.getNodeSet()) {
                    flagLivre = flagLivre && pass.contains((CentreSante)n);
                }
            }
        }
        ArrayList<CentreSante>[] solution = new ArrayList[2];
        solution[0] = solutionC1 ;
        solution[1] = solutionC2 ;
        return solution;
    }

    public static ArrayList<CentreSante> optimisation2Opt(Carte carte, ArrayList<CentreSante> tourneeInitiale) {
        ArrayList<CentreSante> meilleureTournee = new ArrayList<>(tourneeInitiale);
        boolean amelioration = true;

        while (amelioration) {
            amelioration = false;

            int tempsRef = getDuree(carte, meilleureTournee);

            for (int i = 1; i < meilleureTournee.size() - 2; i++) {
                for (int j = i + 1; j < meilleureTournee.size() - 1; j++) {

                    ArrayList<CentreSante> trajetTest = inverserListe(meilleureTournee, i, j);

                    if (getDuree(carte, trajetTest) < tempsRef) {
                        meilleureTournee = trajetTest;
                        amelioration = true;
                        System.out.println("i : " + i + "  | j : " + j);
                    }
                }
            }
        }
        return meilleureTournee;
    }

    private static ArrayList<CentreSante> inverserListe(ArrayList<CentreSante> tournee, int debut, int fin) {
        ArrayList<CentreSante> nouvelleTournee = new ArrayList<>(tournee);
        int gauche = debut;
        int droite = fin;

        while (gauche < droite) {
            CentreSante temp = nouvelleTournee.get(gauche);
            nouvelleTournee.set(gauche, nouvelleTournee.get(droite));
            nouvelleTournee.set(droite, temp);
            gauche++;
            droite--;
        }
        return nouvelleTournee;
    }

}

