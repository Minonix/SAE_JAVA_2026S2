package Test;

import elements.Carte;
import elements.Route;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RouteTest {

    private Carte carte;
    private Route route;

    @Before
    public void setUp() {
        carte = new Carte("TestSecteur");
        carte.ajouterCentre("S1", 'M');
        carte.ajouterCentre("S2", 'O');
        
        carte.ajouterRoute(0, 1, 8, 10, 50);
        route = carte.getRoute(0, 1);
    }

    @Test
    public void testGetters() {
        assertNotNull("La route ne devrait pas être nulle", route);
        assertEquals("La fiabilité est incorrecte", 8, route.getFiabilite());
        assertEquals("La distance est incorrecte", 10, route.getDistance());
        assertEquals("La durée est incorrecte", 50, route.getDuree());
    }

    @Test
    public void testSourceEtTarget() {
        assertEquals("La source devrait être le centre 1", "1", route.getSource().getId());
        assertEquals("La destination devrait être le centre 2", "2", route.getTarget().getId());
    }

    @Test
    public void testCalculeDureeEstimee() {
        int dureeEstimee = route.calculeDureeEstimee();
        assertEquals("Le calcul de la durée estimée avec malus de fiabilité est incorrect", 60, dureeEstimee);
    }

    @Test
    public void testSwitchAffDistance() {
        route.switchAffDistance();
        String labelApresPremierSwitch = route.getAttribute("ui.label");
        assertTrue("La distance devrait maintenant être affichée", labelApresPremierSwitch.contains("Distance : 10"));

        route.switchAffDistance();
        String labelApresDeuxiemeSwitch = route.getAttribute("ui.label");
        assertFalse("La distance ne devrait plus être affichée", labelApresDeuxiemeSwitch.contains("Distance : 10"));
    }
}