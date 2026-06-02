package ui;

import elements.CentreSante;
import elements.Route;
import elements.Carte;
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

    Carte map;

    public MainWindow() {
        super("NOM");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1920, 1080));

        map = new Carte("5");
        map.ajouterCentre("S1", 'C');
        map.ajouterCentre("S2", 'M');
        map.ajouterRoute(0, 1, 8, 12, 87);
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
        gc.gridheight = 1;
        gc.weighty = 0.2;
        this.getContentPane().add(initFiltre(), gc);

        gc.gridy = 1;
        gc.gridheight = 2;
        gc.weighty = 0.3;
        this.getContentPane().add(initInfoBox(map.getRoute(0, 1)), gc);

        gc.gridy = 3;
        gc.gridheight = 2;
        gc.weighty = 0.3;
        this.getContentPane().add(initSelectTrajet(map), gc);

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

    JLabel titreInfoBox, info1, info2, info3;

    private JPanel initInfoBox(Object select) {
        //TODO: aspect visuel a revoir, tout et centrer en petit au centre
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;

        switch (select) {
            case CentreSante centreSante -> {
                titreInfoBox = new JLabel("Centre");
                info1 = new JLabel("Id : " + centreSante.getIdCentre());
                info2 = new JLabel("Nom : " + centreSante.getNomCentre());
                
                panel.add(titreInfoBox, gc);
                gc.gridy = 1;
                panel.add(info1, gc);
                gc.gridy = 2;
                panel.add(info2, gc);
            }
            case Route route -> {
                titreInfoBox = new JLabel("Route");
                info1 = new JLabel("Fiabilité : " + route.getFiabilite());
                info2 = new JLabel("Distance : " + route.getDistance());
                info3 = new JLabel("Durée : " + route.getDuree());
                
                panel.add(titreInfoBox, gc);
                gc.gridy = 1;
                panel.add(info1, gc);
                gc.gridy = 2;
                panel.add(info2, gc);
                gc.gridy = 3;
                panel.add(info3, gc);
            }
            default -> {
                titreInfoBox = new JLabel("Aucun élément Sélectionner");
                
                panel.add(titreInfoBox, gc);
            }
        }

        return panel;

    }

    JLabel depLab, arrLab;
    JButton btnchercher;
    JComboBox<String> depCB, arrCB;
    private JPanel initSelectTrajet(Carte carte) {
        //TODO: aspect visuel a revoir, tout et centrer en petit au centre
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        panel.setBackground(Color.GRAY);

        depLab = new JLabel("Départ : ");
        arrLab = new JLabel("Arrivée : ");
        btnchercher = new JButton("Chercher");
        depCB = new JComboBox<>();
        arrCB = new JComboBox<>();

        for (int i = 0; i < carte.getNbTotalCentres(); i++) {
            depCB.addItem(carte.getCentre(i).getNomCentre());
            arrCB.addItem(carte.getCentre(i).getNomCentre());
        }
        
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(depLab, gc);
        gc.gridy = 1;
        panel.add(arrLab, gc);
        gc.gridwidth = 2;
        gc.gridy = 2;
        panel.add(btnchercher, gc);
        gc.gridwidth = 1;
        gc.gridy = 0;
        gc.gridx = 1;
        panel.add(depCB, gc);
        gc.gridy = 1;
        panel.add(arrCB, gc);

        return panel;
    }

    private JPanel initFiltre() {
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