package Test;

import elements.Carte;
import elements.CentreSante;
import ui.InfosCentreWindow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class InfosCentreWindowTest {
    
    private Carte carteTest;
    private CentreSante centreTest;
    private InfosCentreWindow infosWindow;

    @Before
    public void setUp() {
        carteTest = new Carte("TestSecteur");
        carteTest.ajouterCentre("S1", 'M');
        carteTest.ajouterCentre("S2", 'O');
        carteTest.ajouterCentre("S3", 'N');
        carteTest.ajouterCentre("S4", 'M'); 
        carteTest.ajouterRoute(0, 3, 10, 10, 10);
        carteTest.ajouterRoute(0, 1, 10, 10, 10);
        carteTest.ajouterRoute(1, 2, 10, 10, 10);

        centreTest = carteTest.getCentre("1"); 

        try {
            SwingUtilities.invokeAndWait(() -> {
                infosWindow = new InfosCentreWindow(centreTest);
            });
        } catch (Exception e) {
            fail("Erreur lors de l'initialisation de InfosCentreWindow : " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (infosWindow != null) {
            infosWindow.dispose();
        }
    }

    @Test
    public void testInitialization() {
        assertNotNull("La fenêtre d'informations devrait s'instancier", infosWindow);
        assertEquals("Le titre est incorrect", "Informations sur le centre", infosWindow.getTitle());
        assertTrue("La fenêtre devrait s'afficher automatiquement", infosWindow.isVisible());
    }
}