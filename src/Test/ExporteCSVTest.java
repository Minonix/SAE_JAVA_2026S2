package Test;

import elements.Carte;
import elements.CentreSante;
import ui.ExporteCSV;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExporteCSVTest {

    private Carte carteTest;
    private ArrayList<CentreSante> listeOpti1Camion;
    private File fichier1Camion;
    private File fichier2Camions;

    @Before
    public void setUp() {
        carteTest = new Carte("TestSecteur");
        carteTest.ajouterCentre("S1", 'M');
        carteTest.ajouterCentre("S2", 'N');
        // Ajout d'une route pour éviter que l'algo glouton ne bloque sur un graphe vide
        carteTest.ajouterRoute(0, 1, 10, 10, 10);
        
        listeOpti1Camion = new ArrayList<>();
        listeOpti1Camion.add(carteTest.getCentre(0));
        listeOpti1Camion.add(carteTest.getCentre(1));

        fichier1Camion = new File("resultats1camionEquipe2Groupe4.csv");
        fichier2Camions = new File("resultats2camionsEquipe2Groupe4.csv");
        
        if (fichier1Camion.exists()) fichier1Camion.delete();
        if (fichier2Camions.exists()) fichier2Camions.delete();
    }

    @After
    public void tearDown() {
        if (fichier1Camion.exists()) fichier1Camion.delete();
        if (fichier2Camions.exists()) fichier2Camions.delete();
    }

    @Test
    public void testExporteCSV1c_CreationFichier() {
        ExporteCSV.ExporteCSV1c(listeOpti1Camion, 100, carteTest, "TestNom");

        assertTrue("Le fichier CSV pour 1 camion devrait être créé", fichier1Camion.exists());
        assertTrue("Le fichier CSV ne devrait pas être vide", fichier1Camion.length() > 0);
    }

    @Test
    public void testExporteCSV2c_CreationFichier() {
        ArrayList<CentreSante>[] list2Camions = new ArrayList[2];
        list2Camions[0] = new ArrayList<>();
        list2Camions[0].add(carteTest.getCentre(0));
        
        list2Camions[1] = new ArrayList<>();
        list2Camions[1].add(carteTest.getCentre(1));

        ExporteCSV.ExporteCSV2c(list2Camions, carteTest, "TestNom2C");

        assertTrue("Le fichier CSV pour 2 camions devrait être créé", fichier2Camions.exists());
        assertTrue("Le fichier CSV ne devrait pas être vide", fichier2Camions.length() > 0);
    }
}