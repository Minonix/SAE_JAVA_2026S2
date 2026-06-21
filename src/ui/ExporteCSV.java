package ui;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.opencsv.CSVWriter;

import elements.Carte;
import elements.CentreSante;
import util.TourneeCamion;

import javax.swing.*;
import java.io.File;

/**
 * Classe utilitaire gérant l'exportation des résultats des tournées vers des fichiers CSV.
 * Permet d'enregistrer les statistiques et l'ordre de passage des algorithmes (glouton) 
 * pour les configurations à 1 camion ou 2 camions.
 */
public class ExporteCSV {

    /**
     * Constructeur par défaut.
     */
    public ExporteCSV() {}

    /**
     * Exporte les résultats d'une tournée effectuée par un unique camion dans le fichier 
     * cible "resultats1camionEquipe2Groupe4.csv". Ajoute l'en-tête automatiquement 
     * si le fichier est vierge.
     *
     * @param listeOpti    La liste ordonnée des objets {@link CentreSante} représentant le trajet optimisé.
     * @param dureeGlouton La durée initiale calculée par l'algorithme glouton (avant optimisation éventuelle).
     * @param carte        La {@link Carte} (graphe) sur laquelle la tournée est effectuée.
     * @param nom          Le nom textuel de la configuration ou du fichier testé.
     */
    public static void ExporteCSV1c(ArrayList<CentreSante> listeOpti, Integer dureeGlouton, Carte carte, String nom) {

        File file = new File("resultats1camionEquipe2Groupe4.csv");

        boolean besoinEnTete = !file.exists() || file.length() == 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true),
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            if (besoinEnTete) {
                String[] enTete = { "nomFichier", "duréeAlgoGlouton1camion", "meilleureDurée1camion",
                        "ordreMeilleureDurée" };
                writer.writeNext(enTete);
            }

            ArrayList<String> ligne = new ArrayList<>();

            ligne.add(nom);
            ligne.add(dureeGlouton.toString());
            ligne.add(String.valueOf(TourneeCamion.getDuree(carte, listeOpti)));

            for (CentreSante centre : listeOpti) {
                ligne.add(String.valueOf(centre.getIdCentre()));
            }

            writer.writeNext(ligne.toArray(new String[0]));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'export, avez-vous les droits sur le fichier ?", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Exporte les résultats d'une tournée divisée et effectuée par deux camions 
     * dans le fichier cible "resultats2camionsEquipe2Groupe4.csv".
     *
     * @param list  Un tableau contenant exactement deux listes d'objets {@link CentreSante}, 
     * représentant les trajets respectifs du Camion 1 et du Camion 2.
     * @param carte La {@link Carte} (graphe) sur laquelle la tournée est effectuée.
     * @param nom   Le nom textuel de la configuration ou du fichier testé.
     */
    public static void ExporteCSV2c(ArrayList<CentreSante>[] list, Carte carte, String nom) {

        File file = new File("resultats2camionsEquipe2Groupe4.csv");

        boolean besoinEnTete = !file.exists() || file.length() == 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true),
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            if (besoinEnTete) {
                String[] enTete = { "nomFichier", "DuréeCamion1", "DuréeCamion2",
                        "ordreMeilleureDurée" };
                writer.writeNext(enTete);
            }

            ArrayList<String> ligne = new ArrayList<>();

            ligne.add(nom);
            ligne.add(String.valueOf(TourneeCamion.getDuree(carte, list[0])));
            ligne.add(String.valueOf(TourneeCamion.getDuree(carte, list[1])));

            for (ArrayList<CentreSante> camion : list) {
                for (CentreSante centre : camion) {
                    ligne.add(String.valueOf(centre.getIdCentre()));
                }
            }

            writer.writeNext(ligne.toArray(new String[0]));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'export, avez-vous les droits sur le fichier ?", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }
}