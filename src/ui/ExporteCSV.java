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

public class ExporteCSV {

    public ExporteCSV() {
    }

    public static void ExporteCSV1c(ArrayList<CentreSante> list, Carte carte, String nom) {

        File file = new File("resultats1camionEquipe2Groupe4.csv");

        boolean besoinEnTete = !file.exists() || file.length() == 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true),
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            if (besoinEnTete) {
                String[] enTete = { "nomFichier", "dur�eAlgoGlouton1camion", "meilleureDur�e1camion",
                        "ordreMeilleureDur�e" };
                writer.writeNext(enTete);
            }

            ArrayList<String> ligne = new ArrayList<>();

            ligne.add(nom);
            ligne.add(String.valueOf(TourneeCamion.getDuree(carte, list)));
            ligne.add("x");

            for (CentreSante centre : list) {
                ligne.add(String.valueOf(centre.getIdCentre()));
            }

            writer.writeNext(ligne.toArray(new String[0]));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'export, avez-vous les droits sur le ficheier ?", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }

    public static void ExporteCSV2c(ArrayList<CentreSante>[] list, Carte carte, String nom) {

        File file = new File("resultats2camionsEquipe2Groupe4.csv");

        boolean besoinEnTete = !file.exists() || file.length() == 0;

        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true),
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            if (besoinEnTete) {
                String[] enTete = { "nomFichier", "Dur�eCamion1", "Dur�eCamion2",
                        "ordreMeilleureDur�e" };
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
            JOptionPane.showMessageDialog(null, "Erreur lors de l'export, avez-vous les droits sur le ficheier ?", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }
}
