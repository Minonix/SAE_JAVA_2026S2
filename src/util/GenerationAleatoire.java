package util;
import java.util.Random;


public class GenerationAleatoire {
    public static void construireGrapheAleatoire(int nbSommets){
        Random alea = new Random();
        for (int i=0; i<100; i++) {
            int proba = alea.nextInt(5);
            System.out.println(proba);
        }
    }
}
