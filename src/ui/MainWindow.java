package ui;

import elements.CentreSante;
import elements.Route;
import elements.Carte;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.util.ArrayList;
import java.io.FileNotFoundException;

import javax.swing.*;

public class MainWindow extends JFrame {
    // vue principale
    JPanel pnlInfoBox, pnlSelectTrajet, pnlMap, pnlStatistique, pnlBoxBtnTourne, pnlFiltre;
    Viewer vwrCarte;
    JButton btLogo;
    JComboBox<String> cbSelectCarte;

    // menu
    JMenu mFichier, mAjDistrict, mbarMain, mAffichage;
    JMenuItem miImporter, miAleatoire;
    JCheckBoxMenuItem mixAfficherInfosCarte, mixAfficherRoutesDangereuses;

    ArrayList<Carte> secteurs;

    public MainWindow() {
        super("NOM");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1920, 1080));

        Carte map = new Carte("5");
        map.ajouterCentre("S1", 'C');
        map.ajouterCentre("S2", 'M');
        map.ajouterRoute(0, 1, 8, 12, 87);

        Carte map2;
        try {
            map2 = Carte.Parsing("Bonjour", "data/graphe2.csv");
        } catch (FileNotFoundException e) {
            System.err.println("Fichier introuvable : data/graphe2.csv");
            map2 = new Carte("Bonjour"); // On crée une carte vide en secours pour éviter un crash
        }
        
        secteurs = new ArrayList<>();
        secteurs.add(map2);
        secteurs.add(map);

        initComponent();
        initListener();

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
        this.getContentPane().add(initInfoBox(secteurs.get(cbSecteur.getSelectedIndex()).getRoute(0, 1)), gc);

        gc.gridy = 3;
        gc.gridheight = 2;
        gc.weighty = 0.3;
        this.getContentPane().add(initSelectTrajet(secteurs.get(cbSecteur.getSelectedIndex())), gc);

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
        this.getContentPane().add(initMap(secteurs.get(cbSecteur.getSelectedIndex())), gc);

        gc.gridy = 5;
        gc.gridheight = 1;
        gc.weighty = 0.2;
        this.getContentPane().add(initStatistique(secteurs.get(cbSecteur.getSelectedIndex())), gc);

        this.pack();
    }

    JLabel titreInfoBox, info1, info2, info3;

    private JPanel initInfoBox(Object select) {
        if (pnlInfoBox == null) {
            pnlInfoBox = new JPanel(new GridBagLayout());
            pnlInfoBox.setBackground(Color.LIGHT_GRAY);
        } else {
            pnlInfoBox.removeAll();
        }
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;

        switch (select) {
            case CentreSante centreSante -> {
                titreInfoBox = new JLabel("Centre");
                info1 = new JLabel("Id : " + centreSante.getIdCentre());
                info2 = new JLabel("Nom : " + centreSante.getNomCentre());

                pnlInfoBox.add(titreInfoBox, gc);
                gc.gridy = 1;
                pnlInfoBox.add(info1, gc);
                gc.gridy = 2;
                pnlInfoBox.add(info2, gc);
            }
            case Route route -> {
                titreInfoBox = new JLabel("Route");
                info1 = new JLabel("Fiabilité : " + route.getFiabilite());
                info2 = new JLabel("Distance : " + route.getDistance());
                info3 = new JLabel("Durée : " + route.getDuree());

                pnlInfoBox.add(titreInfoBox, gc);
                gc.gridy = 1;
                pnlInfoBox.add(info1, gc);
                gc.gridy = 2;
                pnlInfoBox.add(info2, gc);
                gc.gridy = 3;
                pnlInfoBox.add(info3, gc);
            }
            default -> {
                titreInfoBox = new JLabel("Aucun élément Sélectionner");

                pnlInfoBox.add(titreInfoBox, gc);
            }
        }

        pnlInfoBox.revalidate();
        pnlInfoBox.repaint();
        return pnlInfoBox;

    }

    JLabel depLab, arrLab;
    JButton btnchercher;
    JComboBox<String> cbDep, cbArr;

    private JPanel initSelectTrajet(Carte carte) {
        if (pnlSelectTrajet == null) {
            pnlSelectTrajet = new JPanel(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();
            pnlSelectTrajet.setBackground(Color.GRAY);

            depLab = new JLabel("Départ : ");
            arrLab = new JLabel("Arrivée : ");
            btnchercher = new JButton("Chercher");
            cbDep = new JComboBox<>();
            cbArr = new JComboBox<>();

            gc.gridx = 0;
            gc.gridy = 0;
            pnlSelectTrajet.add(depLab, gc);
            gc.gridy = 1;
            pnlSelectTrajet.add(arrLab, gc);
            gc.gridwidth = 2;
            gc.gridy = 2;
            pnlSelectTrajet.add(btnchercher, gc);
            gc.gridwidth = 1;
            gc.gridy = 0;
            gc.gridx = 1;
            pnlSelectTrajet.add(cbDep, gc);
            gc.gridy = 1;
            pnlSelectTrajet.add(cbArr, gc);
        }

        cbDep.removeAllItems();
        cbArr.removeAllItems();
        for (int i = 0; i < carte.getNbTotalCentres(); i++) {
            cbDep.addItem(carte.getCentre(i).getNomCentre());
            cbArr.addItem(carte.getCentre(i).getNomCentre());
        }

        return pnlSelectTrajet;
    }

    JLabel lbSeuil, lbChxSecteur;
    JTextField txSeuil;
    JComboBox<String> cbSecteur;
    private JPanel initFiltre() {
        if (pnlFiltre == null) {
            pnlFiltre = new JPanel(new GridBagLayout());
            pnlFiltre.setBackground(Color.DARK_GRAY);

            lbSeuil = new JLabel("Seuil de dangereusité : ");
            lbChxSecteur = new JLabel("Secteur : ");
            txSeuil = new JTextField(20);
            cbSecteur = new JComboBox<>();

            GridBagConstraints gc = new GridBagConstraints();

            gc.gridx = 0;
            gc.gridy = 0;
            pnlFiltre.add(lbSeuil, gc);
            gc.gridy = 1;
            pnlFiltre.add(lbChxSecteur, gc);
            gc.gridx = 1;
            pnlFiltre.add(cbSecteur, gc);
            gc.gridy = 0;
            pnlFiltre.add(txSeuil, gc);
        }

        cbSecteur.removeAllItems();
        for (Carte carte : secteurs) {
            cbSecteur.addItem(carte.toString());
        }
        cbSecteur.addItem("+ Ajouter un Secteur");

        return pnlFiltre;
    }

    JButton btTourne;

    private JPanel initBoxBtnTourne() {
        pnlBoxBtnTourne = new JPanel(new BorderLayout());

        pnlBoxBtnTourne.setBackground(Color.ORANGE);

        btTourne = new JButton("Créer une Tournée");

        pnlBoxBtnTourne.add(btTourne, BorderLayout.CENTER);

        return pnlBoxBtnTourne;
    }

    private JPanel initMap(Carte map) {
        if (pnlMap == null) {
            pnlMap = new JPanel(new BorderLayout());
            pnlMap.setBackground(Color.WHITE);
        } else {
            pnlMap.removeAll();
            if (vwrCarte != null) {
                vwrCarte.close();
            }
        }

        vwrCarte = new Viewer(map, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        vwrCarte.enableAutoLayout();

        Component view = (Component) vwrCarte.addDefaultView(false);
        pnlMap.add(view, BorderLayout.CENTER);
        pnlMap.revalidate();
        pnlMap.repaint();

        return pnlMap;
    }

    JLabel lbNbCentre, lbNbMaternitee, lbNbOperatoire;

    private JPanel initStatistique(Carte map) {
        if (pnlStatistique == null) {
            pnlStatistique = new JPanel(new BorderLayout(100, 10));
            pnlStatistique.setBackground(Color.CYAN);

            lbNbCentre = new JLabel();
            lbNbMaternitee = new JLabel();
            lbNbOperatoire = new JLabel();

            pnlStatistique.add(lbNbCentre, BorderLayout.WEST);
            pnlStatistique.add(lbNbMaternitee, BorderLayout.CENTER);
            pnlStatistique.add(lbNbOperatoire, BorderLayout.EAST);
        }
        
        lbNbCentre.setText("Centres de Nutrition : " + map.getNbNutri());
        lbNbMaternitee.setText("Maternités : " + map.getNbMat());
        lbNbOperatoire.setText("Blocs Opératoires : " + map.getNbBloc());

        return pnlStatistique;
    }

    private void initListener() {
        cbSecteur.addActionListener((e) -> {
            if (((JComboBox)e.getSource()).getSelectedIndex() == ((JComboBox)e.getSource()).getItemCount() - 1) {
                //TODO: ouverture d'une fenetre pour les details de la nouvelle carte + ajout dans la liste
                Carte nCarte = NewSecteurWindow.NewSecteur();
                
                secteurs.add(nCarte);
                cbSecteur.insertItemAt(nCarte.toString(), cbSecteur.getItemCount() - 1);
                
                cbSecteur.setSelectedIndex(cbSecteur.getItemCount() - 2);
            }
            Carte mapSelectionnee = secteurs.get(cbSecteur.getSelectedIndex());
            initMap(mapSelectionnee);
            initStatistique(mapSelectionnee);
            
            // TODO: a changer, soit au clique de l'user soit par default
            initInfoBox(mapSelectionnee.getNbTotalCentres() > 0 ? mapSelectionnee.getCentre(0) : null);
            initSelectTrajet(mapSelectionnee);
            System.out.println("test");
        });
    }
}