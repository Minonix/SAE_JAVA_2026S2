import elements.Carte;
import util.*;

public class Main {
    public static void main(String[] args) {
        Carte carte = new Carte("12");
        Parsing t = new Parsing("data/graphe5.csv", carte);
        carte.display();
    }
}