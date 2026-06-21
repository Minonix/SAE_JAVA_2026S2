package Test;

import elements.Carte;
import elements.CentreSante;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CentreSanteTest {

    private Carte carte;
    private CentreSante centreNutrition;
    private CentreSante maternite;

    @Before
    public void setUp() {
        carte = new Carte("TestSecteur");
        carte.ajouterCentre("S1", 'N'); 
        carte.ajouterCentre("S2", 'M'); 
        
        centreNutrition = carte.getCentre("1");
        maternite = carte.getCentre("2");
    }

    @Test
    public void testGetters() {
        assertNotNull("Le centre ne devrait pas être nul", centreNutrition);
        assertEquals("Le nom du centre est incorrect", "S1", centreNutrition.getNomCentre());
        assertEquals("L'ID du centre est incorrect", 1, centreNutrition.getIdCentre());
        assertEquals("Le type du centre est incorrect", 'N', centreNutrition.getCenterType());
    }

    @Test
    public void testInitialisationStyle() {
        assertTrue("Un centre de nutrition doit être vert", ((String) centreNutrition.getAttribute("ui.style")).contains("green"));
        assertTrue("Une maternité doit être bleue", ((String) maternite.getAttribute("ui.style")).contains("blue"));
    }

    @Test
    public void testSwitchAffNomCentre() {
        String labelInitial = centreNutrition.getAttribute("ui.label");
        
        centreNutrition.switchAffNomCentre();
        String labelApresSwitch = centreNutrition.getAttribute("ui.label");
        
        assertNotEquals("Le label de l'interface devrait changer après un switch", labelInitial, labelApresSwitch);
        assertFalse("Le nom ne devrait plus être dans le label", labelApresSwitch.contains("Nom : S1"));
    }
}