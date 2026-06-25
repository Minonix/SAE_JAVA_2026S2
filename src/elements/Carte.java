package elements;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import java.util.ArrayList;
import java.io.FileNotFoundException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Random;

/**
 * Gère l'intégralité du réseau de santé (le graphe) pour un secteur donné.
 * Hérite de {@link SingleGraph} de la bibliothèque GraphStream.
 * Cette classe permet la manipulation des centres (noeuds) et routes (arêtes),
 * l'importation de fichiers CSV, la génération aléatoire de réseau, 
 * et la recherche de plus courts chemins à l'aide de l'algorithme de Dijkstra.
 */
public class Carte extends SingleGraph {

    private int nbMat ;
    private int nbBloc ;
    private int nbNutri ;

    boolean routesCachees;

    /**
     * Constructeur de la carte.
     * Initialise les statistiques des centres à zéro et charge la feuille de style par défaut.
     *
     * @param id L'identifiant unique textuel de ce graphe/secteur.
     */
    public Carte(String id) {
        super(id);
        this.nbMat = 0;
        this.nbBloc = 0;
        this.nbNutri = 0;
        this.routesCachees = false;
        this.setAttribute("ui.stylesheet","url('./assets/style/style.css')");
    }

    /**
     * Récupère le nombre total de centres de type "Maternité" présents sur la carte.
     *
     * @return Le nombre de maternités.
     */
    public int getNbMat() {
        return nbMat;
    }

    /**
     * Récupère le nombre total de centres de type "Bloc opératoire" présents sur la carte.
     *
     * @return Le nombre de blocs opératoires.
     */
    public int getNbBloc() {
        return nbBloc;
    }

    /**
     * Récupère le nombre total de centres de type "Centre de Nutrition" présents sur la carte.
     *
     * @return Le nombre de centres de nutrition.
     */
    public int getNbNutri() {
        return nbNutri;
    }

    /**
     * Calcule et retourne le nombre total de centres de santé (tous types confondus) sur la carte.
     *
     * @return Le nombre total de centres.
     */
    public int getNbTotalCentres() {
        return nbNutri+nbBloc+nbMat;
    }

    /**
     * Ajoute un nouveau centre de santé (nœud) au graphe et met à jour les statistiques globales.
     *
     * @param nomCentre  Le nom textuel du centre de santé.
     * @param centerType Le caractère définissant le type ('M', 'O', ou 'N').
     */
    public void ajouterCentre(String nomCentre, char centerType) {
        CentreSante cTmp = new CentreSante(this, (getNbTotalCentres() + 1) + "", nomCentre, getNbTotalCentres() + 1, centerType);
        super.addNodeCallback(cTmp);
        switch (centerType) {
            case 'O' -> nbBloc++;
            case 'N' -> nbNutri++;
            default -> nbMat++;
        }
    }

    /**
     * Ajoute une nouvelle route (arête) entre deux centres de santé existants.
     * L'ID de la route est généré automatiquement en formatant les index (ex: "1-2").
     *
     * @param a         L'index du centre de départ (commençant à 0).
     * @param b         L'index du centre d'arrivée (commençant à 0).
     * @param fiabilite Le score de fiabilité de la route.
     * @param distance  La distance physique de la route.
     * @param duree     La durée de parcours de cette route.
     */
    public void ajouterRoute(int a, int b, int fiabilite, int distance, int duree) {
        String strTmp;
        if (a < b) {
            strTmp = a +"-" + b;
        } else {
            strTmp = b +"-" + a;
        }
        CentreSante nodeA = this.getNode(""+(a+1));
        CentreSante nodeB = this.getNode(""+(b+1));

        Route rTmp = new Route(strTmp, nodeA, nodeB, fiabilite, distance, duree);
        super.addEdgeCallback(rTmp);

        try {
            java.lang.reflect.Method method = org.graphstream.graph.implementations.AbstractNode.class
                    .getDeclaredMethod("addEdgeCallback", org.graphstream.graph.implementations.AbstractEdge.class);

            method.setAccessible(true);

            method.invoke(nodeA, rTmp);
            method.invoke(nodeB, rTmp);

        } catch (Exception e) {
            System.err.println("Erreur de réflexion lors de l'ajout de la route : " + e.getMessage());
        }

        rTmp.setAttribute("duree", (double) duree);
    }

    /**
     * Récupère un centre de santé en fonction de son index dans le graphe.
     *
     * @param i L'index (position) du nœud.
     * @return L'objet {@link CentreSante} correspondant.
     */
    public CentreSante getCentre(int i) {
        return this.getNode(i);
    }

    /**
     * Récupère un centre de santé en fonction de son identifiant textuel.
     *
     * @param id L'identifiant (String) du nœud.
     * @return L'objet {@link CentreSante} correspondant.
     */
    public CentreSante getCentre(String id) {
        return this.getNode(id);
    }

    /**
     * Récupère une route spécifique entre deux centres de santé.
     * Formate automatiquement la requête d'ID indépendamment du sens de la demande (A->B ou B->A).
     *
     * @param a L'identifiant du premier centre.
     * @param b L'identifiant du second centre.
     * @return L'objet {@link Route} reliant ces deux centres, ou null si elle n'existe pas.
     */
    public Route getRoute(int a, int b) {
        String strTmp;
        if (a < b) {
            strTmp = a +"-" + b;
        } else {
            strTmp = b +"-" + a;
        }
        return this.getEdge(strTmp);
    }

    /**
     * Calcule le plus court chemin entre deux centres en se basant sur la durée de parcours.
     *
     * @param origine     Le {@link CentreSante} de départ.
     * @param destination Le {@link CentreSante} d'arrivée.
     * @return Le chemin optimal ({@link Path}) minimisant la durée.
     */
    public Path dijkstraDuree(CentreSante origine, CentreSante destination) {
        Path chemin ;
        for (Edge edge : this.getEdgeSet()) {
            if (edge instanceof Route route) {
                double d = route.getDuree();
                edge.setAttribute("duree", d);
            }
        }
        Dijkstra dij = dijkstra(origine, "duree");
        chemin = dij.getPath(destination) ;
        dij.clear();
        return chemin ;
    }

    /**
     * Calcule le plus court chemin entre deux centres en se basant sur la durée de parcours estimée
     * (durée pondérée par la fiabilité de la route). Ne prend en compte que les routes ayant
     * une fiabilité minimale de 0.25.
     *
     * @param origine     Le {@link CentreSante} de départ.
     * @param destination Le {@link CentreSante} d'arrivée.
     * @return Le chemin optimal ({@link Path}) minimisant la durée estimée.
     */
    public Path dijkstraEstimation(CentreSante origine, CentreSante destination) {
        Path chemin ;
        for (Edge edge : this.getEdgeSet()) {
            if (((Route) edge).getFiabilite() >= 0.25) {
                double d = ((Route) edge).calculeDureeEstimee();
                edge.setAttribute("dureeEstimee", d);
            }
        }
        Dijkstra dij = dijkstra(origine, "dureeEstimee");
        chemin = dij.getPath(destination) ;
        dij.clear() ;
        return chemin ;
    }

    /**
     * Réalise une analyse complète des chemins depuis un centre d'origine vers tous les autres centres.
     * L'ordre des données dans les listes renvoyées est : [0] = Durée, [1] = Durée Estimée, [2] = Distance.
     *
     * @param origine Le {@link CentreSante} servant de point de départ pour l'algorithme.
     * @return Une liste de listes d'entiers contenant les différentes métriques de trajet pour chaque sommet.
     */
    public ArrayList<ArrayList<Integer>> dijkstraInfos(CentreSante origine) {
        ArrayList<ArrayList<Integer>> chemin = new  ArrayList<>() ;
        for (Edge edge : this.getEdgeSet()) {
            double d = ((Route) edge).getDuree();
            double di = ((Route) edge).getDistance();
            edge.setAttribute("duree", d);
            edge.setAttribute("distance", di);
            if (((Route) edge).getFiabilite() >= 0.25) {
                double e = ((Route) edge).calculeDureeEstimee();
                edge.setAttribute("dureeEstimee", e);
            }
        }
        Dijkstra dijDu = dijkstra(origine, "duree");
        Dijkstra dijDi = dijkstra(origine, "distance");
        Dijkstra dijEs = dijkstra(origine, "dureeEstimee");

        for (Node t : this.getNodeSet()){
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add((int)dijDu.getPathLength(t));
            temp.add((int)dijEs.getPathLength(t));
            temp.add((int)dijDi.getPathLength(t));
            chemin.add(t.getIndex(), temp) ;
        }
        dijDu.clear();
        dijDi.clear();
        dijEs.clear();
        return chemin ;
    }

    private Dijkstra dijkstra(CentreSante origine, String attibut){
        Dijkstra dij = new Dijkstra(Dijkstra.Element.EDGE, null, attibut);
        dij.init(this);
        dij.setSource(origine);
        dij.compute();
        return dij ;
    }

    /**
     * Méthode utilitaire générant un graphe (secteur) de manière totalement aléatoire.
     * Crée les centres et attribue des connexions avec des statistiques procédurales.
     *
     * @param nomGraph  L'identifiant textuel du graphe généré.
     * @param nbSommets Le nombre total de centres de santé à créer.
     * @return Une nouvelle instance de {@link Carte} configurée aléatoirement.
     */
    public static Carte construireGrapheAleatoire(String nomGraph, int nbSommets){
        Carte graph = new Carte(nomGraph);

        Random alea = new Random();
        int routeForce = -1;
        int proba_centre;
        int proba_existe;
        int proba_fiabl;
        int proba_dist;
        int proba_duree;
        for (int i=0 ; i< nbSommets ; i++){
            proba_centre = alea.nextInt(5);
            if (proba_centre < 3){
                graph.ajouterCentre("S" + (i+1), 'M');
            }
            else if (proba_centre == 3){
                graph.ajouterCentre("S" + (i+1), 'O');
            }
            else {
                graph.ajouterCentre("S" + (i+1), 'N');
            }
        }
        for (int i=0; i< nbSommets; i++){
            if (i!=nbSommets-1) {
                proba_fiabl = alea.nextInt(2, 9);
                proba_dist = alea.nextInt(10, 51);
                proba_duree = alea.nextInt(10, 121);
                routeForce = alea.nextInt(i+1, nbSommets);
                graph.ajouterRoute(i, routeForce, proba_fiabl, proba_dist, proba_duree);
            }
            for (int j=i; j< nbSommets; j++){
                proba_existe = alea.nextInt(5);
                if (proba_existe == 0 && j != routeForce && i!=j){
                    proba_fiabl = alea.nextInt(2, 9) ;
                    proba_dist = alea.nextInt(10,51) ;
                    proba_duree = alea.nextInt(10,121) ;
                    graph.ajouterRoute(i, j, proba_fiabl,proba_dist,proba_duree) ;
                }
            }
        }
        return graph ;
    }

    /**
     * Scanne, parse et importe un fichier CSV pour construire un secteur complet (sommets et arêtes).
     *
     * @param id     L'identifiant textuel du graphe à créer.
     * @param chemin Le chemin du fichier CSV sur le disque.
     * @return Une instance de {@link Carte} représentant les données du fichier.
     * @throws FileNotFoundException Si le fichier pointé par le chemin est introuvable.
     */
    public static Carte Parsing(String id, String chemin) throws FileNotFoundException{
        Carte carte = new Carte(id);
        ArrayList<ArrayList<String>> recordsCSV = new ArrayList<>();

        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(chemin)).withCSVParser(parser).build()) {

            String[] valeurLigne;
            while ((valeurLigne = csvReader.readNext()) != null) {
                if (valeurLigne.length > 1 && !valeurLigne[0].trim().startsWith("//")) {
                    recordsCSV.add(new ArrayList<>(Arrays.asList(valeurLigne)));
                    
                    char idCentre = valeurLigne[1].trim().isEmpty() ? ' ' : valeurLigne[1].trim().charAt(0);
                    carte.ajouterCentre(valeurLigne[0].trim(), idCentre);
                }
            }

            for (int i = 0; i < recordsCSV.size(); i++) {
                ArrayList<String> ligneCourante = recordsCSV.get(i);

                for (int y = 2; y < ligneCourante.size(); y++) {
                    if (i >= (y - 2)) continue;

                    String cellule = ligneCourante.get(y).trim();
                    if (cellule.isEmpty()) continue;

                    try (CSVReader ligneReader = new CSVReader(new StringReader(cellule))) {
                        String[] valeurLien = ligneReader.readNext();

                        if (valeurLien != null && valeurLien.length >= 3) {
                            try {
                                int proba = Integer.parseInt(valeurLien[0].trim());
                                int distance = Integer.parseInt(valeurLien[1].trim());
                                int duree = Integer.parseInt(valeurLien[2].trim());
                                
                                carte.ajouterRoute(i, y - 2, proba, distance, duree);
                            } catch (NumberFormatException e) {
                                System.err.println("Erreur de parsing numérique à la ligne " + i + ", colonne " + y + " : " + e.getMessage());
                            } 
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
            throw new FileNotFoundException();
        } catch (CsvValidationException e) {
            System.err.println("Erreur de validation CSV : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur critique inattendue : " + e.getMessage());
        }
        return carte;
    }

    /**
     * Active ou désactive l'affichage du nom pour l'ensemble des centres du graphe.
     */
    public void switchAffNoms() {
        CentreSante c;
        for (Node n : this) {
            c = (CentreSante) n;
            c.switchAffNomCentre();
        }
    }

    /**
     * Active ou désactive l'affichage de l'identifiant pour l'ensemble des centres du graphe.
     */
    public void switchAffIDs() {
        CentreSante c;
        for (Node n : this) {
            c = (CentreSante) n;
            c.switchAffIdCentre();
        }
    }

    /**
     * Active ou désactive l'affichage du type pour l'ensemble des centres du graphe.
     */
    public void switchAffTypes() {
        CentreSante c;
        for (Node n : this) {
            c = (CentreSante) n;
            c.switchAffCenterType();
        }
    }

    /**
     * Réinitialise la couleur et l'épaisseur de toutes les routes du graphe à leur état par défaut (noir).
     * Respecte également l'état de visibilité actuel (masqué ou normal).
     */
    public void resetCouleursRoutes() {
        String visibility = "";
        if (routesCachees) {
            visibility = " visibility-mode: hidden;";
        }
        for (Edge e : this.getEdgeSet()) {
            e.setAttribute("ui.style", "fill-color: black; size: 1;"+visibility);
        }
    }

    /**
     * Réinitialise la couleur et l'épaisseur d'une seule route spécifique à son état par défaut (noir).
     *
     * @param r La {@link Route} à réinitialiser.
     */
    public void resetCouleurRoute(Route r) {
        String visibility = "";
        if (routesCachees) {
            visibility = " visibility-mode: hidden;";
        }
        r.setAttribute("ui.style", "fill-color: black; size: 1;" + visibility);
    }

    /**
     * Applique une couleur et augmente l'épaisseur pour un ensemble de routes constituant un chemin.
     * Force la visibilité à l'état normal pour mettre le chemin en évidence.
     *
     * @param chemin Le {@link Path} (ensemble de nœuds et d'arêtes) à mettre en valeur.
     * @param color  La couleur cible ({@link Color}) à appliquer.
     */
    public void colorerChemin(Path chemin, Color color) {
        String visibility = "";
        if (routesCachees) {
            visibility = " visibility-mode: normal;";
        }
        for (Edge e : chemin.getEdgeSet()) {
            e.setAttribute("ui.style", "fill-color: rgb("+color.getRed()+", "+color.getGreen()+", "+color.getBlue()+"); size: 3;"+visibility);
        }
    }

    /**
     * Met en évidence avec une couleur spécifique toutes les routes dont la fiabilité est 
     * strictement inférieure à un seuil donné.
     *
     * @param seuil La note de fiabilité en dessous de laquelle la route est colorée.
     * @param color La couleur cible ({@link Color}) à appliquer sur les routes dangereuses.
     */
    public void colorerSeuil(double seuil, Color color) {
        for (Edge e : this.getEdgeSet()) {
            if (((Route)e).getFiabilite() < seuil) {
                e.setAttribute("ui.style", "fill-color: rgb("+color.getRed()+", "+color.getGreen()+", "+color.getBlue()+");");
            }
        }
    }

    /**
     * Bascule la visibilité globale (cacher/montrer) pour toutes les routes qui ne sont pas 
     * actuellement mises en valeur (qui n'ont pas une taille augmentée).
     */
    public void switchCacherRoutes() {
        String oldParams;
        if (routesCachees) {
            for (Edge e : this.getEdgeSet()) {
                if (e.getAttribute("ui.style") == null) {
                    oldParams = "";
                }
                else {
                    oldParams = e.getAttribute("ui.style");
                }
                e.setAttribute("ui.style", oldParams+"visibility-mode: normal;");
            }
        }
        else {
            for (Edge e : this.getEdgeSet()) {

                if (e.getAttribute("ui.style") == null || !((String)e.getAttribute("ui.style")).contains("size: 3;")) {
                    if (e.getAttribute("ui.style") == null) {
                        oldParams = "";
                    }
                    else {
                        oldParams = e.getAttribute("ui.style");
                    }
                    e.setAttribute("ui.style", oldParams+"visibility-mode: hidden;");
                }
            }
        }
        routesCachees = !routesCachees;
    }

    /**
     * Active ou désactive l'affichage textuel de la distance sur l'intégralité des routes du graphe.
     */
    public void switchAffDistance() {
        Route r;
        for (Edge e : this.getEdgeSet()) {
            r = (Route) e;
            r.switchAffDistance();
        }
    }

    /**
     * Active ou désactive l'affichage textuel de la durée sur l'intégralité des routes du graphe.
     */
    public void switchAffDuree() {
        Route r;
        for (Edge e : this.getEdgeSet()) {
            r = (Route) e;
            r.switchAffDuree();
        }
    }

    /**
     * Active ou désactive l'affichage textuel de la fiabilité sur l'intégralité des routes du graphe.
     */
    public void switchAffFiabilite() {
        Route r;
        for (Edge e : this.getEdgeSet()) {
            r = (Route) e;
            r.switchAffFiabilite();
        }
    }
}