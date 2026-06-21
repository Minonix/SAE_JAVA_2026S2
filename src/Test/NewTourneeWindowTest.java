package Test;

import ui.NewTourneeWindow;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewTourneeWindowTest {

    @Test
    public void testInitializationSansErreur() {
        try {
            NewTourneeWindow window = new NewTourneeWindow();
            
            assertNotNull("La fenêtre devrait s'instancier correctement", window);
            assertEquals("Le titre de la fenêtre est incorrect", "Options de la tournée", window.getTitle());
            assertTrue("La fenêtre de dialogue devrait être modale", window.isModal());
            
        } catch (Exception e) {
            fail("L'instanciation de la fenêtre a généré une exception : " + e.getMessage());
        }
    }
}