import elements.Carte;
import ui.MainWindow;
import util.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Carte g = Carte.construireGrapheAleatoire("Test", 20);
        g.display();
        ArrayList<Integer> t = g.dijkstraDuree(g.getCentre(0), g.getCentre(16));
        System.out.println(t);


    }
}