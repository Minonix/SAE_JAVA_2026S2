package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.implementations.SingleGraph;


public class Parsing {
    private BufferedReader reader;
    private int nbLigne;

    public Parsing(String chemin, SingleGraph carte) {
        try {
            //dénombre le nombre de ligne
            reader = new BufferedReader(new FileReader(chemin));

            String line = reader.readLine();
            
            this.nbLigne = 0;
            while (line != null) {
                String trimming = line.trim();
                if (!(trimming.startsWith("//"))) {
                    this.nbLigne++;
                }
            line = reader.readLine();
            }
            reader.close();

            reader = new BufferedReader(new FileReader(chemin));

            line = reader.readLine();
            
            while (line != null) {
                String trimming = line.trim();
                if (!(trimming.startsWith("//"))) {
                    //TODO: logique de parsing
                }
            line = reader.readLine();
            }
            reader.close();

        } catch (IOException  e) {
            System.out.println("problem dans l'ouverture du fichier : " + e);
        }
    }
}
