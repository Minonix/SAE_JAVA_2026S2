package util;

import elements.*;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import java.util.ArrayList;

/**
 * Classe utilitaire regroupant les algorithmes de calcul et d'optimisation des tournées pour les camions.
 * Elle implémente des algorithmes gloutons (Nearest Neighbour) pour la construction initiale des trajets,
 * ainsi qu'une méthode d'optimisation par recherche locale (2-opt).
 * Ces algorithmes peuvent être exécutés pour un ou deux camions, et filtrés par type de centre.
 */
public class TourneeCamion {

    /**
     * Calcule une tournée passant par tous les centres de la carte en utilisant un algorithme glouton.
     * Le camion part du premier centre et se dirige toujours vers le centre non visité le plus proche.
     *
     * @param carte Le graphe ({@link Carte}) représentant le réseau.
     * @return Une liste ordonnée de {@link CentreSante} représentant le trajet du camion.
     */
    public static ArrayList<CentreSante> tourneeGlouton(Carte carte){
        ArrayList<CentreSante> solution = new ArrayList<>();
        boolean flagLivre;
        CentreSante centreCourant = carte.getCentre(0);
        solution.add(centreCourant);
        flagLivre = true;
        for (Node n : carte.getNodeSet()) {
            flagLivre = flagLivre && solution.contains((CentreSante)n);
        }
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
        return solution;
    }

    /**
     * Calcule une tournée gloutonne en filtrant les destinations selon un type de centre spécifique.
     * Le camion ignorera les centres qui ne correspondent pas au type demandé.
     *
     * @param carte Le graphe ({@link Carte}) représentant le réseau.
     * @param type  Le caractère représentant le type de centre à desservir ('M', 'O', ou 'N').
     * @return Une liste ordonnée de {@link CentreSante} représentant le trajet filtré.
     */
    public static ArrayList<CentreSante> tourneeGlouton(Carte carte, char type){
        ArrayList<CentreSante> pass = new ArrayList<>();
        ArrayList<CentreSante> solution = new ArrayList<>();
        CentreSante centreCourant;
        boolean flagLivre;

        for (Node n : carte.getNodeSet() ){
            if (((CentreSante)n).getCenterType() != type) {
                pass.add((CentreSante) n);
            }
        }

        int id = 0;
        do {
            centreCourant = carte.getCentre(id);
            id++;
        } while (centreCourant.getCenterType() != type);
        solution.add(centreCourant);
        pass.add(centreCourant);

        flagLivre = true;
        for (Node n : carte.getNodeSet()) {
            flagLivre = flagLivre && pass.contains((CentreSante)n);
        }
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
        return solution;
    }

    /**
     * Calcule la durée totale d'une tournée en additionnant la durée de chaque route consécutive.
     * Si aucune route directe n'existe entre deux centres consécutifs, utilise Dijkstra pour 
     * trouver la durée du chemin le plus court.
     *
     * @param g La {@link Carte} du réseau.
     * @param t La liste ordonnée des centres (la tournée) dont on veut évaluer le temps de trajet.
     * @return La durée totale estimée du parcours.
     */
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

    /**
     * Calcule une tournée répartie sur deux camions passant par tous les centres.
     * Chaque étape attribue la prochaine destination la plus proche au camion disponible.
     *
     * @param carte Le graphe ({@link Carte}) représentant le réseau.
     * @return Un tableau contenant deux listes : l'index 0 pour le trajet du camion 1, l'index 1 pour le camion 2.
     */
    public static ArrayList<CentreSante>[] tourneeGlouton2C(Carte carte){
        ArrayList<CentreSante> solutionC1 = new ArrayList<>();
        ArrayList<CentreSante> solutionC2 = new ArrayList<>();
        boolean flagLivre;
        CentreSante centreCourant1 = carte.getCentre(0);
        CentreSante centreCourant2 = carte.getCentre(0);

        solutionC1.add(centreCourant1);
        solutionC2.add(centreCourant1);

        flagLivre = true;
        for (Node n : carte.getNodeSet()) {
            flagLivre = flagLivre && (solutionC1.contains((CentreSante) n) || solutionC2.contains((CentreSante) n));
        }
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

    /**
     * Calcule une tournée pour deux camions en filtrant les destinations selon un type de centre spécifique.
     *
     * @param carte Le graphe ({@link Carte}) représentant le réseau.
     * @param type  Le caractère représentant le type de centre ciblé ('M', 'O', ou 'N').
     * @return Un tableau contenant les trajets respectifs des deux camions, limités au type spécifié.
     */
    public static ArrayList<CentreSante>[] tourneeGlouton2C(Carte carte, char type){
        ArrayList<CentreSante> pass = new ArrayList<>();
        ArrayList<CentreSante> solutionC1 = new ArrayList<>();
        ArrayList<CentreSante> solutionC2 = new ArrayList<>();
        boolean flagLivre;

        for (Node n : carte.getNodeSet() ){
            if (((CentreSante)n).getCenterType() != type) {
                pass.add((CentreSante) n);
            }
        }

        CentreSante centreCourant1;
        CentreSante centreCourant2;
        int id = 0;
        do {
            centreCourant1 = carte.getCentre(id);
            centreCourant2 = carte.getCentre(id);
            id++;
        } while (centreCourant1.getCenterType() != type);
        solutionC1.add(centreCourant1);
        solutionC2.add(centreCourant1);
        pass.add(centreCourant1);

        flagLivre = true;
        for (Node n : carte.getNodeSet()) {
            flagLivre = flagLivre && pass.contains((CentreSante)n);
        }
        while (!flagLivre) {
            ArrayList<ArrayList<Integer>> infoD = carte.dijkstraInfos(centreCourant1);

            int min1 = -1;
            int imin1 = -1;
            for (int i = 0; i < infoD.size(); i++) {
                if ((min1 > infoD.get(i).getFirst() || min1 == -1) && !pass.contains(carte.getCentre(i)) && i != centreCourant1.getIdCentre()-1) {
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
                    if ((min2 > infoD.get(i).get(0) || min2 == -1) && i != imin1 && !pass.contains(carte.getCentre(i)) && i != centreCourant1.getIdCentre()) {
                        min2 = infoD.get(i).get(0);
                        imin2 = i;
                    }
                }

                Path mChemin2 = carte.dijkstraDuree(centreCourant2, carte.getCentre(imin2));

                for (Node d : mChemin2.getNodeSet()) {
                    if (!solutionC1.contains(d) && !solutionC2.contains(d)) {
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
        solution[0] = solutionC1;
        solution[1] = solutionC2;
        return solution;
    }

    /**
     * Applique l'heuristique d'optimisation 2-opt sur une tournée existante.
     * Cette méthode tente de décroiser les routes en inversant itérativement des sous-segments
     * du trajet pour trouver une combinaison réduisant la durée totale.
     *
     * @param carte           La {@link Carte} du réseau.
     * @param tourneeInitiale La tournée brute (généralement obtenue via l'algorithme glouton).
     * @return La tournée optimisée sous forme de liste de {@link CentreSante}.
     */
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
                    }
                }
            }
        }
        return meilleureTournee;
    }

    /**
     * Inverse l'ordre d'un sous-segment dans la liste de la tournée (utilisé exclusivement par l'algorithme 2-opt).
     *
     * @param tournee La liste de base.
     * @param debut   L'index de départ du segment à inverser.
     * @param fin     L'index de fin du segment à inverser.
     * @return Une nouvelle liste avec le segment spécifié inversé.
     */
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