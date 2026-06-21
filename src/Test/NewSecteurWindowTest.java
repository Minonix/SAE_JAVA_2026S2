package Test;

import ui.NewSecteurWindow;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewSecteurWindowTest {

    @Test
    public void testInitializationSansErreur() {
        try {
            NewSecteurWindow window = new NewSecteurWindow();
            
            assertNotNull("La fenêtre devrait s'instancier correctement", window);
            assertEquals("Le titre de la fenêtre est incorrect", "Création d'un Nouveau Secteur", window.getTitle());
            assertTrue("La fenêtre de dialogue devrait être modale", window.isModal());
            
        } catch (Exception e) {
            fail("L'instanciation de la fenêtre a généré une exception : " + e.getMessage());
        }
    }
}