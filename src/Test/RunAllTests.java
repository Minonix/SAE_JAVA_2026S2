package Test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunAllTests {

    public static void main(String[] args) {
        System.out.println("Lancement de l'ensemble des tests JUnit 4...\n");

        // Avec JUnit 4, il faut lister explicitement les classes à tester
        Result result = JUnitCore.runClasses(
                CentreSanteTest.class,
                RouteTest.class,
                CarteTest.class,
                MyPanelTest.class,
                ExporteCSVTest.class,
                NewSecteurWindowTest.class,
                NewTourneeWindowTest.class
        );

        System.out.println("=========================================");
        System.out.println("           RÉSULTATS DES TESTS           ");
        System.out.println("=========================================");
        System.out.println("Tests exécutés : " + result.getRunCount());
        System.out.println("Tests réussis  : " + (result.getRunCount() - result.getFailureCount() - result.getIgnoreCount()));
        System.out.println("Tests échoués  : " + result.getFailureCount());
        System.out.println("Tests ignorés  : " + result.getIgnoreCount());
        System.out.println("Temps total    : " + result.getRunTime() + " ms");
        System.out.println("=========================================");

        if (!result.wasSuccessful()) {
            System.out.println("\nDÉTAIL DES ÉCHECS :");
            for (Failure failure : result.getFailures()) {
                System.out.println("-> " + failure.toString());
                System.out.println(failure.getTrace());
            }
        } else {
            System.out.println("\n🎉 Succès total ! Tous les tests sont au vert !");
        }

        // Force l'arrêt du programme (utile si des fenêtres Swing tournent en fond)
        System.exit(result.wasSuccessful() ? 0 : 1);
    }
}