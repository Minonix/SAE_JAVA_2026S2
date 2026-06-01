package ui;

import org.graphstream.ui.view.Viewer;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    // vue principale
    JPanel plMain;
    Viewer vwrCarte;
    JButton btLogo;
    JComboBox<String> cbSelectCarte;

    // menu
    JMenu mFichier, mAjDistrict, mbarMain, mAffichage;
    JMenuItem miImporter, miAleatoire;
    JCheckBoxMenuItem mixAfficherInfosCarte, mixAfficherRoutesDangereuses;

    public MainWindow(){
        super("NOM");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1920, 1080));
        
        initComponent();
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponent() {
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        gc.fill = GridBagConstraints.BOTH;

        gc.weightx = 0.2; 
        gc.gridx = 0;

        gc.gridy = 0;
        gc.gridheight = 2;
        gc.weighty = 0.3; 
        this.getContentPane().add(initInfoBox(), gc);

        gc.gridy = 2;
        gc.gridheight = 2;
        gc.weighty = 0.3; 
        this.getContentPane().add(initSelectTrajet(), gc);

        gc.gridy = 4;
        gc.gridheight = 1;
        gc.weighty = 0.2; 
        this.getContentPane().add(initChoixPalier(), gc);

        gc.gridy = 5;
        gc.gridheight = 1;
        gc.weighty = 0.2; 
        this.getContentPane().add(initBoxBtnTourne(), gc);

        gc.weightx = 0.8; 
        gc.gridx = 1;

        gc.gridy = 0;
        gc.gridheight = 5; 
        gc.gridwidth = 1;
        gc.weighty = 0.8; 
        this.getContentPane().add(initMap(), gc);

        gc.gridy = 5;
        gc.gridheight = 1;
        gc.weighty = 0.2; 
        this.getContentPane().add(initStatistique(), gc);

        this.pack();
    }

    private JPanel initInfoBox() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        return panel;
    }

    private JPanel initSelectTrajet() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.GRAY); 
        return panel;
    }

    private JPanel initChoixPalier() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.DARK_GRAY); 
        return panel;
    }

    private JPanel initBoxBtnTourne() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.ORANGE); 
        return panel;
    }

    private JPanel initMap() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE); 
        return panel;
    }

    private JPanel initStatistique() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.CYAN); 
        return panel;
    }
}