package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Parsing {
    private BufferedReader f;

    public Parsing(String chemin, SingleGraph carte) {
        try {
            f = new BufferedReader(new FileReader(chemin));
        } catch (IOException  e) {
            System.out.println("problem dans l'ouverture du fichier : " + e);
        }
    }
}
