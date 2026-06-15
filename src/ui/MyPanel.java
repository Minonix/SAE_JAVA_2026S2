package ui;

import java.awt.*;
import javax.swing.*;

public class MyPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;

    // Constructeur
    public MyPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        
        // CRUCIAL : Indique à Swing de ne pas dessiner le fond carré par défaut
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // On utilise Graphics2D pour avoir accès à des fonctions de dessin avancées
        Graphics2D g2 = (Graphics2D) g.create();
        
        // CRUCIAL : Active l'antialiasing pour que les bords arrondis soient lisses (pas d'effet "escalier")
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // On dessine le fond avec les bords arrondis
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        g2.dispose(); // Libère les ressources mémoire
    }
}