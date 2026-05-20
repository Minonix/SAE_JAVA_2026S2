package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.graphstream.graph.implementations.SingleGraph;


public class Parsing {
    private BufferedReader reader;

    public Parsing(String chemin, SingleGraph carte) {
        try {
            reader = new BufferedReader(new FileReader(chemin));

            String line = reader.readLine();
            
            while (line != null) {
                if (!(line.charAt(0) == '/' && line.charAt(1) == '/')) {
                // affichage de la ligne
                System.out.println(line);
                // lecture de la prochaine ligne
            }
            line = reader.readLine();
            }
            reader.close();

        } catch (IOException  e) {
            System.out.println("problem dans l'ouverture du fichier : " + e);
        }
    }
}
