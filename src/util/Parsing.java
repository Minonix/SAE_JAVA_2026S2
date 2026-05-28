package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import elements.*;
import java.io.StringReader;
import java.util.List;

public class Parsing {
    private BufferedReader reader;
    private int nbLigne;
    Carte carte;

    public Parsing(String chemin, Carte carte) {
        this.carte = carte;
        try {
            List<List<String>> recordsCSV = new ArrayList<List<String>>();

            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            CSVReader csvReader = new CSVReaderBuilder(new FileReader(chemin)).withCSVParser(parser).build();

            String[] values =  csvReader.readNext();
            while (values != null) {
                if (!(values.length <= 1 || values[0].trim().startsWith("//") )) {
                    recordsCSV.add(Arrays.asList(values));
                    this.carte.ajouterCentre(values[0], values[1].charAt(0));
                }
                values = csvReader.readNext();
            }
            nbLigne = recordsCSV.size();
            for (int i = 0; i < nbLigne; i++) {
                //Pour chaque ligne :
                for (int y = 2; y < nbLigne + 1; y++) {
                    parsingConexion(recordsCSV.get(i).get(y), i, y - 2);
                }
            }

        } catch (Exception e) {
            System.out.println("l'erreur est : " + e);
        }
    }

    private void parsingConexion(String ligne, int a, int b) {
        try {
            CSVReader LigneReader = new CSVReader(new StringReader(ligne));

            String[] values =  LigneReader.readNext();
                if (!(values == null || values.length <= 2)) {
                    this.carte.ajouterRoute(a, b, Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                }
                try {
                values = LigneReader.readNext();
                } catch(Exception e) {}
        } catch (Exception e) {
            System.out.println("l'erreur est : " + e);
        }
    }
}
