package Test;

import elements.Carte;
import elements.CentreSante;
import elements.Route;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CarteTest {

    private Carte carte;

    @Before
    public void setUp() {
        carte = new Carte("TestGraph");
    }

    @Test
    public void testAjoutCentresEtStatistiques() {
        assertEquals("La carte devrait être vide au départ", 0, carte.getNbTotalCentres());

        carte.ajouterCentre("S1", 'M');
        carte.ajouterCentre("S2", 'O');
        carte.ajouterCentre("S3", 'M');
        carte.ajouterCentre("S4", 'N');

        assertEquals("Il devrait y avoir 4 centres au total", 4, carte.getNbTotalCentres());
        assertEquals("Le compteur de maternités est faux", 2, carte.getNbMat());
        assertEquals("Le compteur de blocs opératoires est faux", 1, carte.getNbBloc());
        assertEquals("Le compteur de centres de nutrition est faux", 1, carte.getNbNutri());
    }

    @Test
    public void testAjoutEtRecuperationRoute() {
        carte.ajouterCentre("S1", 'M');
        carte.ajouterCentre("S2", 'O');

        carte.ajouterRoute(0, 1, 5, 20, 30);
        
        Route route = carte.getRoute(0, 1);
        assertNotNull("La route 0-1 devrait exister", route);
        assertEquals("L'identifiant de la route généré par GraphStream est incorrect", "0-1", route.getId());
        
        Route routeInverse = carte.getRoute(1, 0);
        assertNotNull("La méthode getRoute devrait trouver la route peu importe le sens des index", routeInverse);
        assertEquals("Les deux recherches devraient renvoyer le même objet Route", route, routeInverse);
    }

    @Test
    public void testConstruireGrapheAleatoire() {
        int nbSommetsVoulus = 10;
        Carte carteAleatoire = Carte.construireGrapheAleatoire("Alea", nbSommetsVoulus);

        assertEquals("Le nombre de sommets générés est incorrect", nbSommetsVoulus, carteAleatoire.getNbTotalCentres());
        assertTrue("Le graphe aléatoire devrait contenir des routes", carteAleatoire.getEdgeCount() > 0);
    }

    @Test
    public void testDijkstraDuree() {
        carte.ajouterCentre("S1", 'M');
        carte.ajouterCentre("S2", 'O');
        carte.ajouterCentre("S3", 'N');

        carte.ajouterRoute(0, 1, 10, 10, 15);
        carte.ajouterRoute(1, 2, 10, 10, 15);
        carte.ajouterRoute(0, 2, 10, 10, 50);

        CentreSante depart = carte.getCentre("1");
        CentreSante arrivee = carte.getCentre("3");

        org.graphstream.graph.Path chemin = carte.dijkstraDuree(depart, arrivee);
        
        assertNotNull("Un chemin devrait être trouvé", chemin);
        assertEquals("Le chemin le plus rapide devrait passer par S2 (2 arêtes)", 2, chemin.getEdgeCount());
    }
}