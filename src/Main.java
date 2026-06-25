import ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        // Utilisation du moteur de rendu avancé nécessaire pour le CSS
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        new MainWindow();
    }
}