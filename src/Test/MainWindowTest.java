package Test;

import ui.MainWindow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class MainWindowTest {
    
    private MainWindow mainWindow;

    @Before
    public void setUp() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                mainWindow = new MainWindow();
            });
        } catch (Exception e) {
            fail("Erreur lors de l'initialisation de MainWindow : " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (mainWindow != null) {
            mainWindow.dispose();
        }
    }

    @Test
    public void testInitialization() {
        assertNotNull("La fenêtre principale devrait être instanciée", mainWindow);
        assertEquals("Le titre de la fenêtre devrait être 'NOM'", "NOM", mainWindow.getTitle());
        assertTrue("La fenêtre devrait être visible après son instanciation", mainWindow.isVisible());
    }

    @Test
    public void testMenuPresence() {
        JMenuBar menuBar = mainWindow.getJMenuBar();
        
        assertNotNull("La barre de menu devrait être présente", menuBar);
        assertTrue("Il devrait y avoir au moins les menus Secteur et Affichage", menuBar.getMenuCount() >= 2);
        assertEquals("Le premier menu doit être 'Secteur'", "Secteur", menuBar.getMenu(0).getText());
        assertEquals("Le deuxième menu doit être 'Affichage'", "Affichage", menuBar.getMenu(1).getText());
    }
}