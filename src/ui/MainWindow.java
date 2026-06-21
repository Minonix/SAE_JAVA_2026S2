package ui;

import elements.CentreSante;
import elements.Route;
import util.TourneeCamion;
import elements.Carte;

import org.graphstream.ui.graphicGraph.GraphicElement;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.util.DefaultMouseManager;

import Exception.NoChangeException;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.io.FileNotFoundException;

import javax.swing.*;

public class MainWindow extends JFrame {
    // vue principale
    MyPanel pnlInfoBox, pnlSelectTrajet, pnlMap, pnlStatistique, pnlBoxBtnTourne, pnlFiltre;
    Viewer vwrCarte;

    // menu
    JMenuBar bar;
    JMenu mSecteur, mAffichage, smCentre, smRoute;
    JMenuItem miNouveau;
    JCheckBoxMenuItem mixAfficherId, mixAfficherNom, mixAfficherType;
    JCheckBoxMenuItem mixAfficherDistance, mixAfficherDuree, mixAfficherFiabilite, mixCacherRoutes;


    ArrayList<Carte> secteurs;

    public MainWindow() {
        super("NOM");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1920, 1080));
        this.setMinimumSize(new Dimension(800, 600));

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
        initMenu();
        initListener();

        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    private void initMenu() {
        bar = new JMenuBar();
        mSecteur = new JMenu("Secteur");
        mAffichage = new JMenu("Affichage");
        smCentre = new JMenu("Centres");
        smRoute = new JMenu("Routes");
        miNouveau = new JMenuItem("Nouveau...");
        mixAfficherId = new JCheckBoxMenuItem("IDs");
        mixAfficherNom = new JCheckBoxMenuItem("Noms");
        mixAfficherType = new JCheckBoxMenuItem("Types");
        mixAfficherDistance = new JCheckBoxMenuItem("Longueur/Distance");
        mixAfficherDuree = new JCheckBoxMenuItem("Durée de parcours");
        mixAfficherFiabilite = new JCheckBoxMenuItem("Fiabilité");
        mixCacherRoutes = new JCheckBoxMenuItem("Cacher les routes hors trajet");

        mSecteur.add(miNouveau);
        mAffichage.add(smCentre);
        smCentre.add(mixAfficherId);
        smCentre.add(mixAfficherNom);
        smCentre.add(mixAfficherType);

        mAffichage.add(smRoute);
        smRoute.add(mixCacherRoutes);
        smRoute.add(mixAfficherDistance);
        smRoute.add(mixAfficherDuree);
        smRoute.add(mixAfficherFiabilite);

        mixAfficherNom.setSelected(true);
        mixAfficherType.setSelected(true);
        mixAfficherId.setSelected(true);

        bar.add(mSecteur);
        bar.add(mAffichage);

        this.setJMenuBar(bar);
    }

    private void initComponent() {
        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        gc.anchor = GridBagConstraints.CENTER;

        gc.weightx = 0.1;
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
        gc.weighty = 0.1;
        this.getContentPane().add(initBoxBtnTourne(), gc);

        gc.weightx = 0.9;
        gc.gridx = 1;

        gc.gridy = 0;
        gc.gridheight = 5;
        gc.gridwidth = 1;
        gc.weighty = 0.9;
        this.getContentPane().add(initMap(secteurs.get(cbSecteur.getSelectedIndex())), gc);

        gc.fill = GridBagConstraints.VERTICAL;
        gc.gridy = 5;
        gc.gridheight = 1;
        gc.weighty = 0.1;
        this.getContentPane().add(initStatistique(secteurs.get(cbSecteur.getSelectedIndex())), gc);

        this.getContentPane().setBackground(Color.WHITE);
        this.pack();
    }

    JLabel titreInfoBox, info1, info2, info3;

    private MyPanel initInfoBox(Object select) {
        if (pnlInfoBox == null) {
            pnlInfoBox = new MyPanel(0, new Color(0xededed));
            pnlInfoBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            pnlInfoBox.setLayout(new GridBagLayout());
        } else {
            pnlInfoBox.removeAll();
        }
        GridBagConstraints gc = new GridBagConstraints();
        Insets inset = new Insets(10, 0, 0, 0);
        gc.insets = inset;
        gc.gridx = 0;
        gc.gridy = 0;

        if (select == null) {
            titreInfoBox = new JLabel("Aucun élément Sélectionner");

            pnlInfoBox.add(titreInfoBox, gc);
        } else {
            switch (select) {
                case CentreSante centreSante -> {
                    titreInfoBox = new JLabel("Centre");
                    info1 = new JLabel("Id : " + centreSante.getIdCentre());
                    info2 = new JLabel("Nom : " + centreSante.getNomCentre());

                    inset.set(10, 0, 10, 0);
                    pnlInfoBox.add(titreInfoBox, gc);
                    gc.gridy = 1;
                    inset.set(0, 0, 5, 0);
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
        }

        pnlInfoBox.revalidate();
        pnlInfoBox.repaint();
        return pnlInfoBox;

    }

    JLabel depLab, arrLab;
    JButton btnchercherDuree, btnchercherEstimation, btRetourNormal;
    JComboBox<String> cbDep, cbArr;

    private MyPanel initSelectTrajet(Carte carte) {
        if (pnlSelectTrajet == null) {
            pnlSelectTrajet = new MyPanel(0, new Color(0xededed));
            pnlSelectTrajet.setLayout(new GridBagLayout());
            pnlSelectTrajet.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(10, 0, 10, 10);

            depLab = new JLabel("Départ : ");
            arrLab = new JLabel("Arrivée : ");
            btnchercherDuree = new JButton("Le plus rapide");
            btnchercherEstimation = new JButton("Sûr et rapide");
            btRetourNormal = new JButton("Retour à la normal");
            cbDep = new JComboBox<>();
            cbArr = new JComboBox<>();

            gc.gridx = 0;
            gc.gridy = 0;
            pnlSelectTrajet.add(depLab, gc);
            gc.gridy = 1;
            pnlSelectTrajet.add(arrLab, gc);
            gc.gridy = 2;
            pnlSelectTrajet.add(btnchercherDuree, gc);
            gc.gridy = 0;
            gc.gridx = 1;
            pnlSelectTrajet.add(cbDep, gc);
            gc.gridy = 1;
            pnlSelectTrajet.add(cbArr, gc);
            gc.gridy = 2;
            pnlSelectTrajet.add(btnchercherEstimation, gc);
            gc.gridwidth = 2;
            gc.gridx = 0;
            gc.gridy = 3;
            pnlSelectTrajet.add(btRetourNormal, gc);
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
    JButton btSeuil;

    private MyPanel initFiltre() {
        if (pnlFiltre == null) {
            pnlFiltre = new MyPanel(0, new Color(0xededed));
            pnlFiltre.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            pnlFiltre.setLayout(new GridBagLayout());

            lbSeuil = new JLabel("Seuil (unité entre 1 et 10) : ");
            lbChxSecteur = new JLabel("Secteur : ");
            txSeuil = new JTextField(6);
            cbSecteur = new JComboBox<>();
            btSeuil = new JButton("Valider");

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(10, 0, 10, 10);

            gc.gridx = 0;
            gc.gridy = 0;
            pnlFiltre.add(lbChxSecteur, gc);
            gc.gridy = 1;
            pnlFiltre.add(lbSeuil, gc);
            gc.gridx = 1;
            gc.gridy = 0;
            pnlFiltre.add(cbSecteur, gc);
            gc.gridy = 1;
            pnlFiltre.add(txSeuil, gc);
            gc.gridx = 0;
            gc.gridy = 2;
            gc.gridwidth = 2;
            pnlFiltre.add(btSeuil, gc);
        }

        cbSecteur.removeAllItems();
        for (Carte carte : secteurs) {
            cbSecteur.addItem(carte.toString());
        }
        cbSecteur.addItem("+ Ajouter un Secteur");

        return pnlFiltre;
    }

    JButton btTourne;

    private MyPanel initBoxBtnTourne() {
        pnlBoxBtnTourne = new MyPanel(0, new Color(0xededed));
        pnlBoxBtnTourne.setLayout(new BorderLayout());
        pnlBoxBtnTourne.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // pnlBoxBtnTourne.setPreferredSize(new Dimension(30, 50));

        btTourne = new JButton("Créer une Tournée");

        pnlBoxBtnTourne.add(btTourne, BorderLayout.CENTER);

        return pnlBoxBtnTourne;
    }

    private MyPanel initMap(Carte map) {
        if (pnlMap == null) {
            pnlMap = new MyPanel(0, new Color(0xededed));
            pnlMap.setLayout(new BorderLayout());
        } else {
            pnlMap.removeAll();
            if (vwrCarte != null) {
                vwrCarte.close();
            }
        }

        vwrCarte = new Viewer(map, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);

        vwrCarte.enableAutoLayout();

        Component view = vwrCarte.addDefaultView(false);

        vwrCarte.getDefaultView().setMouseManager(new DefaultMouseManager() {

            public void mouseDragged(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                Carte mapSelcetione = secteurs.get(cbSecteur.getSelectedIndex());
                GraphicsConfiguration gc = vwrCarte.getDefaultView().getGraphicsConfiguration();
                AffineTransform tx = gc.getDefaultTransform();

                float x = (float) (e.getX() * tx.getScaleX());
                float y = (float) (e.getY() * tx.getScaleY());

                GraphicElement element = vwrCarte.getDefaultView().findNodeOrSpriteAt(x, y);

                if (element != null) {
                    String idSommet = element.getId();
                    Object n = mapSelcetione.getCentre(idSommet);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        new InfosCentreWindow((CentreSante)n);
                    } else {
                        initInfoBox(n);
                    }
                }
            }
        });

        pnlMap.add(view, BorderLayout.CENTER);
        pnlMap.revalidate();
        pnlMap.repaint();

        return pnlMap;
    }

    JLabel lbNbCentre, lbNbMaternitee, lbNbOperatoire;

    private MyPanel initStatistique(Carte map) {
        if (pnlStatistique == null) {
            pnlStatistique = new MyPanel(0, Color.white);
            pnlStatistique.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            pnlStatistique.setLayout(new GridBagLayout());

            lbNbCentre = new JLabel();
            lbNbMaternitee = new JLabel();
            lbNbOperatoire = new JLabel();

            GridBagConstraints gc = new GridBagConstraints();
            gc.insets = new Insets(0, 20, 0, 20);

            gc.gridy = 0;
            gc.gridx = 0;
            pnlStatistique.add(lbNbCentre, gc);
            gc.gridx = 1;
            pnlStatistique.add(lbNbMaternitee, gc);
            gc.gridx = 2;
            pnlStatistique.add(lbNbOperatoire, gc);
        }

        lbNbCentre.setText("Centres de Nutrition : " + map.getNbNutri());
        lbNbMaternitee.setText("Maternités : " + map.getNbMat());
        lbNbOperatoire.setText("Blocs Opératoires : " + map.getNbBloc());

        lbNbCentre.setForeground(Color.green);
        lbNbMaternitee.setForeground(Color.blue);
        lbNbOperatoire.setForeground(Color.red);

        return pnlStatistique;
    }

    private void initListener() {
        cbSecteur.addActionListener((e) -> {
            if (((JComboBox) e.getSource()).getSelectedIndex() == ((JComboBox) e.getSource()).getItemCount() - 1) {
                Carte nCarte;
                try {
                    nCarte = NewSecteurWindow.NewSecteur();
                } catch (NoChangeException e1) {
                    return;
                }
                if (nCarte != null) {
                    secteurs.add(nCarte);
                    cbSecteur.insertItemAt(nCarte.toString(), cbSecteur.getItemCount() - 1);

                }
                cbSecteur.setSelectedIndex(cbSecteur.getItemCount() - 2);
            }
            Carte mapSelectionnee = secteurs.get(cbSecteur.getSelectedIndex());
            initMap(mapSelectionnee);
            initStatistique(mapSelectionnee);

            // initInfoBox(mapSelectionnee.getCentre(0));
            initInfoBox(null);
            initSelectTrajet(mapSelectionnee);
        });

        btnchercherDuree.addActionListener((e) -> {
            Carte mapSelcetione = secteurs.get(cbSecteur.getSelectedIndex());
            mapSelcetione.resetCouleursRoutes();
            mapSelcetione.colorerChemin(mapSelcetione.dijkstraDuree(mapSelcetione.getCentre(cbDep.getSelectedIndex()),
                    mapSelcetione.getCentre(cbArr.getSelectedIndex())), Color.BLUE);
        });

        btnchercherEstimation.addActionListener((e) -> {
            Carte mapSelcetione = secteurs.get(cbSecteur.getSelectedIndex());
            mapSelcetione.resetCouleursRoutes();
            mapSelcetione
                    .colorerChemin(mapSelcetione.dijkstraEstimation(mapSelcetione.getCentre(cbDep.getSelectedIndex()),
                            mapSelcetione.getCentre(cbArr.getSelectedIndex())), Color.GREEN);
        });

        btRetourNormal.addActionListener((e) -> {
            Carte mapSelcetione = secteurs.get(cbSecteur.getSelectedIndex());
            mapSelcetione.resetCouleursRoutes();
        });

        btTourne.addActionListener((e) -> {
            Carte mapSelcetione = secteurs.get(cbSecteur.getSelectedIndex());
            try {
                ArrayList<Object> params = NewTourneeWindow.newTournee();
                if ((Integer) params.getFirst() == 1) {
                    ArrayList<CentreSante> resultTournee;

                    if (params.getLast() != null)
                        resultTournee = TourneeCamion.tourneeGlouton(mapSelcetione,(Character) params.getLast());
                    else
                        resultTournee = TourneeCamion.tourneeGlouton(mapSelcetione);

                    ExporteCSV.ExporteCSV1c(resultTournee, mapSelcetione, (String) cbSecteur.getSelectedItem());
                }
                else {
                    ArrayList<CentreSante>[] resultTournee;

                    if (params.getLast() != null)
                        resultTournee = TourneeCamion.tourneeGlouton2C(mapSelcetione,(Character) params.getLast());
                    else
                        resultTournee = TourneeCamion.tourneeGlouton2C(mapSelcetione);

                    ExporteCSV.ExporteCSV2c(resultTournee, mapSelcetione, (String) cbSecteur.getSelectedItem());
                }
                JOptionPane.showMessageDialog(this, "La tournée à bien été ajoutée, consultez votre répertoire courant.");
            } catch (NoChangeException ignored){}
        });

        btSeuil.addActionListener((e) -> {
            int seuil = 0;
            secteurs.get(cbSecteur.getSelectedIndex()).resetCouleursRoutes();
            try {
                seuil = Integer.parseInt(txSeuil.getText());
                if (seuil < 0 || seuil > 10)
                    throw new NumberFormatException();
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this,
                        "Entrée du seuil invalide", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
            secteurs.get(cbSecteur.getSelectedIndex()).colorerSeuil(seuil, Color.RED);
        });

        miNouveau.addActionListener(e -> {
            Carte nCarte;
            try {
                nCarte = NewSecteurWindow.NewSecteur();
                secteurs.add(nCarte);
                cbSecteur.insertItemAt(nCarte.toString(), cbSecteur.getItemCount() - 1);
                cbSecteur.setSelectedIndex(cbSecteur.getItemCount() - 2);
            } catch (NoChangeException ignored) {}
        });

        mixAfficherId.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchAffIDs();
            }
        });

        mixAfficherNom.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchAffNoms();
            }
        });

        mixAfficherType.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchAffTypes();
            }
        });

        mixCacherRoutes.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchCacherRoutes();
            }
        });

        mixAfficherDistance.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchAffDistance();
            }
        });

        mixAfficherDuree.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchAffDuree();
            }
        });

        mixAfficherFiabilite.addActionListener(e -> {
            for (Carte secteur : secteurs) {
                secteur.switchAffFiabilite();
            }
        });
    }
}