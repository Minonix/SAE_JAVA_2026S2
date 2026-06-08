package elements;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import org.graphstream.graph.implementations.SingleNode;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;



public class Carte extends SingleGraph {
    private Route rTmp;
    private CentreSante cTmp;

    private int nbMat ;
    private int nbBloc ;
    private int nbNutri ;


    public Carte(String id) {
        super(id);
        this.nbMat = 0;
        this.nbBloc = 0;
        this.nbNutri = 0;
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
        CentreSante nodeA = (CentreSante) this.getNode(""+a);
        CentreSante nodeB = (CentreSante) this.getNode(""+b);

        rTmp = new Route(strTmp, nodeA, nodeB, fiabilite, distance, duree);
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

    public ArrayList<Integer> DkjistraDuree(CentreSante origine, CentreSante arrive) {
        ArrayList<Integer> chemin = new ArrayList<Integer>() ;
        for (Edge edge : this.getEdgeSet()) {
            if (edge instanceof Route) {
                double d = (double) ((Route) edge).getDuree();
                edge.setAttribute("duree", d);
            }
        }
        Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.EDGE, null, "duree");

        dijkstra.init(this);
        dijkstra.setSource(origine);
        dijkstra.compute();

        for (Node t : dijkstra.getPath(arrive).getNodeSet()){
            chemin.add(t.getIndex());
        }
        dijkstra.clear();
        return chemin ;
    }

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
            if (i!=0) {
                proba_fiabl = alea.nextInt(2, 9);
                proba_dist = alea.nextInt(10, 51);
                proba_duree = alea.nextInt(10, 121);
                routeForce = alea.nextInt(i);
                graph.ajouterRoute(i, routeForce, proba_fiabl, proba_dist, proba_duree);
            }
            for (int j=i; j< nbSommets; j++){
                proba_existe = alea.nextInt(5);
                if (proba_existe == 0 && j != routeForce){
                    proba_fiabl = alea.nextInt(2, 9) ;
                    proba_dist = alea.nextInt(10,51) ;
                    proba_duree = alea.nextInt(10,121) ;
                    graph.ajouterRoute(i, j, proba_fiabl,proba_dist,proba_duree) ;
                }
            }
        }
        return graph ;
    }

    public static Carte Parsing(String id, String chemin) {
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
        } catch (CsvValidationException e) {
            System.err.println("Erreur de validation CSV : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur critique inattendue : " + e.getMessage());
        }
        return carte;
    }


}
