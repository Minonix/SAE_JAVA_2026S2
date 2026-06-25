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
import java.util.Iterator;

import javax.swing.*;

/**
 * Fenêtre principale de l'application (héritant de {@link JFrame}).
 * Cette classe agit comme le chef d'orchestre de l'interface graphique.
 * Elle intègre le rendu visuel de la carte (via GraphStream), les panneaux
 * d'informations, les menus de filtrage et d'affichage, ainsi que les outils
 * permettant de calculer des itinéraires (Dijkstra) ou de générer des tournées
 * de camions.
 */
public class MainWindow extends JFrame {
    // vue principale
    MyPanel pnlInfoBox, pnlInfoBoxSec, pnlSelectTrajet, pnlMap, pnlStatistique, pnlBoxBtnTourne, pnlFiltre;
    Viewer vwrCarte;

    // menu
    JMenuBar bar;
    JMenu mSecteur, mAffichage, smCentre, smRoute;
    JMenuItem miNouveau;
    JCheckBoxMenuItem mixAfficherId, mixAfficherNom, mixAfficherType;
    JCheckBoxMenuItem mixAfficherDistance, mixAfficherDuree, mixAfficherFiabilite, mixCacherRoutes;

    ArrayList<Carte> secteurs;

    public MainWindow() {
        super("MediCeler");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(1920, 1080));
        this.setMinimumSize(new Dimension(800, 600));

        secteurs = new ArrayList<>();
        try {
            secteurs.add(NewSecteurWindow.NewSecteur());
        } catch (NoChangeException ignored) {}

        initComponent();
        initMenu();
        initListener();

        this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    private Carte getSecteurCourant() {
        if (secteurs.isEmpty()) {
            return null;
        }
        int index = cbSecteur.getSelectedIndex();
        if (index >= 0 && index < secteurs.size()) {
            return secteurs.get(index);
        }
        return null;
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
        this.getContentPane().add(initFiltre(), gc); // Initialise cbSecteur

        Carte mapSelectionne = getSecteurCourant();

        gc.gridy = 1;
        gc.gridheight = 2;
        gc.weighty = 0.3;
        Route routeInitiale = (mapSelectionne != null && mapSelectionne.getEdgeCount() > 0) ? mapSelectionne.getRoute(0, 1)
                : null;
        this.getContentPane().add(initInfoBox(routeInitiale), gc);

        gc.gridy = 3;
        gc.gridheight = 2;
        gc.weighty = 0.3;
        this.getContentPane().add(initSelectTrajet(mapSelectionne), gc);

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
        this.getContentPane().add(initMap(mapSelectionne), gc);

        gc.fill = GridBagConstraints.VERTICAL;
        gc.gridy = 5;
        gc.gridheight = 1;
        gc.weighty = 0.1;
        this.getContentPane().add(initStatistique(mapSelectionne), gc);

        this.getContentPane().setBackground(Color.WHITE);
        this.pack();
    }

    JLabel titreInfoBox, infoCentre1, infoCentre2, SoustitreInfoBox, infoRoute1, infoRoute2, infoRoute3;
    JComboBox<String> cbRoutes;
    ArrayList<Route> listRoute;
    Route routeMiseEnEvidence = null;

    private MyPanel initInfoBox(Object select) {
        if (pnlInfoBox == null) {
            pnlInfoBox = new MyPanel(0, new Color(0xededed));
            pnlInfoBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            cbRoutes = new JComboBox<>();
            listRoute = new ArrayList<>();

            pnlInfoBox.setLayout(new GridBagLayout());
        } else {
            pnlInfoBox.removeAll();
            cbRoutes.removeAllItems();
            listRoute.clear();
        }
        GridBagConstraints gc = new GridBagConstraints();
        Insets inset = new Insets(10, 0, 0, 0);
        gc.insets = inset;
        gc.gridx = 0;
        gc.gridy = 0;

        if (select instanceof CentreSante centreSante) {
            titreInfoBox = new JLabel("Centre");
            infoCentre1 = new JLabel("Id : " + centreSante.getIdCentre());
            infoCentre2 = new JLabel("Nom : " + centreSante.getNomCentre());

            inset.set(10, 0, 10, 0);
            pnlInfoBox.add(titreInfoBox, gc);
            gc.gridy = 1;
            inset.set(0, 0, 5, 0);
            pnlInfoBox.add(infoCentre1, gc);
            gc.gridy = 2;
            pnlInfoBox.add(infoCentre2, gc);

            Iterator<org.graphstream.graph.Edge> it = centreSante.getEdgeIterator();

            cbRoutes.addItem("Selectionner une route");
            while (it.hasNext()) {
                Route route = (Route) it.next();

                CentreSante centreOppose;
                if (route.getSource().getIdCentre() == centreSante.getIdCentre()) {
                    centreOppose = route.getTarget();
                } else {
                    centreOppose = route.getSource();
                }

                listRoute.add(route);
                cbRoutes.addItem("Vers " + centreOppose.getNomCentre());
            }

            gc.gridy = 3;
            pnlInfoBox.add(cbRoutes, gc);

            gc.gridy = 4;
            gc.gridwidth = 1;
            gc.gridheight = 4;

            if (cbRoutes.getItemCount() <= 1) {
                cbRoutes.removeAllItems();
                cbRoutes.addItem("Aucune route");
                cbRoutes.setEnabled(false);
            } else {
                cbRoutes.setEnabled(true);
            }

            MyPanel sousPanel = initInfoBoxSec(null);
            pnlInfoBox.add(sousPanel, gc);

        } else {
            titreInfoBox = new JLabel("Aucun élément sélectionné");
            pnlInfoBox.add(titreInfoBox, gc);
        }

        pnlInfoBox.revalidate();
        pnlInfoBox.repaint();
        return pnlInfoBox;
    }

    private MyPanel initInfoBoxSec(Route route) {
        if (pnlInfoBoxSec == null) {
            pnlInfoBoxSec = new MyPanel(50, new Color(0xededef));
            pnlInfoBoxSec.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            pnlInfoBoxSec.setLayout(new GridBagLayout());
        } else {
            pnlInfoBoxSec.removeAll();
        }

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10, 20, 0, 20);
        gc.gridx = 0;
        gc.gridy = 0;

        if (route != null) {
            SoustitreInfoBox = new JLabel("Route");
            infoRoute1 = new JLabel("Fiabilité : " + route.getFiabilite());
            infoRoute2 = new JLabel("Distance : " + route.getDistance());
            infoRoute3 = new JLabel("Durée : " + route.getDuree());

            pnlInfoBoxSec.add(SoustitreInfoBox, gc);
            gc.gridy = 1;
            pnlInfoBoxSec.add(infoRoute1, gc);
            gc.gridy = 2;
            pnlInfoBoxSec.add(infoRoute2, gc);
            gc.gridy = 3;
            pnlInfoBoxSec.add(infoRoute3, gc);
        } else {
            SoustitreInfoBox = new JLabel("Aucune route");
            pnlInfoBoxSec.add(SoustitreInfoBox, gc);
        }

        pnlInfoBoxSec.revalidate();
        pnlInfoBoxSec.repaint();
        return pnlInfoBoxSec;
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
            btRetourNormal = new JButton("Retour à la normale");
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

        if (carte != null) {
            for (int i = 0; i < carte.getNbTotalCentres(); i++) {
                cbDep.addItem(carte.getCentre(i).getNomCentre());
                cbArr.addItem(carte.getCentre(i).getNomCentre());
            }
            btnchercherDuree.setEnabled(true);
            btnchercherEstimation.setEnabled(true);
            btRetourNormal.setEnabled(true);
            cbDep.setEnabled(true);
            cbArr.setEnabled(true);
        } else {
            cbDep.addItem("Aucun");
            cbArr.addItem("Aucun");
            btnchercherDuree.setEnabled(false);
            btnchercherEstimation.setEnabled(false);
            btRetourNormal.setEnabled(false);
            cbDep.setEnabled(false);
            cbArr.setEnabled(false);
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
            txSeuil = new JTextField("5", 6);
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

        btSeuil.setEnabled(!secteurs.isEmpty());
        txSeuil.setEnabled(!secteurs.isEmpty());

        return pnlFiltre;
    }

    JButton btTourne;

    private MyPanel initBoxBtnTourne() {
        if (pnlBoxBtnTourne == null) {
            pnlBoxBtnTourne = new MyPanel(0, new Color(0xededed));
            pnlBoxBtnTourne.setLayout(new BorderLayout());
            pnlBoxBtnTourne.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            btTourne = new JButton("Créer une Tournée");
            pnlBoxBtnTourne.add(btTourne, BorderLayout.CENTER);
        }

        btTourne.setEnabled(!secteurs.isEmpty());
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

        if (map != null) {
            vwrCarte = new Viewer(map, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
            vwrCarte.enableAutoLayout();
            Component view = vwrCarte.addDefaultView(false);

            vwrCarte.getDefaultView().setMouseManager(new DefaultMouseManager() {
                public void mouseDragged(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);

                    Carte mapSelectionne = getSecteurCourant();
                    if (mapSelectionne == null)
                        return;

                    GraphicsConfiguration gc = vwrCarte.getDefaultView().getGraphicsConfiguration();
                    AffineTransform tx = gc.getDefaultTransform();

                    float x = (float) (e.getX() * tx.getScaleX());
                    float y = (float) (e.getY() * tx.getScaleY());

                    GraphicElement element = vwrCarte.getDefaultView().findNodeOrSpriteAt(x, y);

                    if (element != null) {
                        String idSommet = element.getId();
                        Object n = mapSelectionne.getCentre(idSommet);
                        if (SwingUtilities.isRightMouseButton(e)) {
                            InfosCentreWindow window = new InfosCentreWindow((CentreSante) n);
                            window.setVisible(true);
                        } else {
                            initInfoBox(n);
                        }
                    }
                }
            });
            pnlMap.add(view, BorderLayout.CENTER);
        } else {
            JLabel emptyLabel = new JLabel("Aucune carte chargée. Veuillez ajouter un secteur.", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Arial", Font.BOLD, 18));
            emptyLabel.setForeground(Color.GRAY);
            pnlMap.add(emptyLabel, BorderLayout.CENTER);
        }

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

        if (map != null) {
            lbNbCentre.setText("Centres de Nutrition : " + map.getNbNutri());
            lbNbMaternitee.setText("Maternités : " + map.getNbMat());
            lbNbOperatoire.setText("Blocs Opératoires : " + map.getNbBloc());
        } else {
            lbNbMaternitee.setText("Aucun Secteur sélectionné");
        }

        lbNbCentre.setForeground(Color.green);
        lbNbMaternitee.setForeground(Color.blue);
        lbNbOperatoire.setForeground(Color.red);

        return pnlStatistique;
    }

    private void initListener() {
        cbSecteur.addActionListener((e) -> {
            int selectedIndex = cbSecteur.getSelectedIndex();
            if (selectedIndex == cbSecteur.getItemCount() - 1) {
                Carte nCarte;
                try {
                    nCarte = NewSecteurWindow.NewSecteur();
                } catch (NoChangeException e1) {
                    if (!secteurs.isEmpty()) {
                        cbSecteur.setSelectedIndex(secteurs.size() - 1);
                    }
                    return;
                }

                if (nCarte != null) {
                    secteurs.add(nCarte);
                    cbSecteur.insertItemAt(nCarte.toString(), cbSecteur.getItemCount() - 1);
                    cbSecteur.setSelectedIndex(cbSecteur.getItemCount() - 2);
                }
            }

            Carte mapSelectionnee = getSecteurCourant();
            initMap(mapSelectionnee);
            initStatistique(mapSelectionnee);
            initInfoBox(null);
            initSelectTrajet(mapSelectionnee);

            boolean hasMap = (mapSelectionnee != null);
            btSeuil.setEnabled(hasMap);
            txSeuil.setEnabled(hasMap);
            if (btTourne != null) 
                btTourne.setEnabled(hasMap);
        });

        btnchercherDuree.addActionListener((e) -> {
            Carte mapSelectionne = getSecteurCourant();
            if (mapSelectionne == null)
                return;

            mapSelectionne.resetCouleursRoutes();
            mapSelectionne.colorerChemin(
                    mapSelectionne.dijkstraDuree(
                            mapSelectionne.getCentre(cbDep.getSelectedIndex()),
                            mapSelectionne.getCentre(cbArr.getSelectedIndex())),
                    Color.BLUE);
        });

        btnchercherEstimation.addActionListener((e) -> {
            Carte mapSelectionne = getSecteurCourant();
            if (mapSelectionne == null)
                return;

            mapSelectionne.resetCouleursRoutes();
            mapSelectionne.colorerChemin(
                    mapSelectionne.dijkstraEstimation(
                            mapSelectionne.getCentre(cbDep.getSelectedIndex()),
                            mapSelectionne.getCentre(cbArr.getSelectedIndex())),
                    Color.GREEN);
        });

        btRetourNormal.addActionListener((e) -> {
            Carte mapSelectionne = getSecteurCourant();
            if (mapSelectionne == null)
                return;

            mapSelectionne.resetCouleursRoutes();
        });

        btTourne.addActionListener((e) -> {
            Carte mapSelectionne = getSecteurCourant();
            if (mapSelectionne == null)
                return;

            try {
                ArrayList<Object> params = NewTourneeWindow.newTournee();
                if ((Integer) params.getFirst() == 1) {
                    ArrayList<CentreSante> resultGlouton;
                    if (params.getLast() != null) {
                        resultGlouton = TourneeCamion.tourneeGlouton(mapSelectionne, (Character) params.getLast());
                    } else {
                        resultGlouton = TourneeCamion.tourneeGlouton(mapSelectionne);
                    }
                    ArrayList<CentreSante> resultOpti;
                    if (mapSelectionne.getNbTotalCentres() < 30) {
                        resultOpti = TourneeCamion.optimisation2Opt(mapSelectionne, resultGlouton);
                    }
                    else {
                        resultOpti = resultGlouton;
                    }
                    int dureeGlouton = TourneeCamion.getDuree(mapSelectionne, resultGlouton);

                    ExporteCSV.ExporteCSV1c(resultOpti, dureeGlouton, mapSelectionne,
                            (String) cbSecteur.getSelectedItem());
                } else {
                    ArrayList<CentreSante>[] resultTournee;
                    if (params.getLast() != null) {
                        resultTournee = TourneeCamion.tourneeGlouton2C(mapSelectionne, (Character) params.getLast());
                    } else {
                        resultTournee = TourneeCamion.tourneeGlouton2C(mapSelectionne);
                    }
                    ExporteCSV.ExporteCSV2c(resultTournee, mapSelectionne, (String) cbSecteur.getSelectedItem());
                }
                JOptionPane.showMessageDialog(this,
                        "La tournée a bien été ajoutée, consultez votre répertoire courant.");
            } catch (NoChangeException ignored) {
            }
        });

        btSeuil.addActionListener((e) -> {
            Carte mapSelectionne = getSecteurCourant();
            if (mapSelectionne == null)
                return;

            mapSelectionne.resetCouleursRoutes();
            try {
                int seuil = Integer.parseInt(txSeuil.getText());
                if (seuil < 0 || seuil > 10)
                    throw new NumberFormatException();
                mapSelectionne.colorerSeuil(seuil, Color.RED);
            } catch (NumberFormatException err) {
                JOptionPane.showMessageDialog(this, "Entrée du seuil invalide (doit être un nombre entre 0 et 10)",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        cbRoutes.addActionListener(e -> {
            Carte mapSelectionnee = getSecteurCourant();
            if (mapSelectionnee == null)
                return;

            int indexSelectionne = cbRoutes.getSelectedIndex();

            if (routeMiseEnEvidence != null) {
                mapSelectionnee.resetCouleurRoute(routeMiseEnEvidence);
                routeMiseEnEvidence = null;
            }

            if (indexSelectionne > 0 && (indexSelectionne - 1) < listRoute.size()) {
                Route routeSelectionnee = listRoute.get(indexSelectionne - 1);
                routeSelectionnee.setAttribute("ui.style", "fill-color: cyan; size: 4; visibility-mode: normal;");
                routeMiseEnEvidence = routeSelectionnee;
                initInfoBoxSec(routeSelectionnee);
            } else {
                initInfoBoxSec(null);
            }
        });

        miNouveau.addActionListener(e -> {
            try {
                Carte nCarte = NewSecteurWindow.NewSecteur();
                secteurs.add(nCarte);
                cbSecteur.insertItemAt(nCarte.toString(), cbSecteur.getItemCount() - 1);
                cbSecteur.setSelectedIndex(cbSecteur.getItemCount() - 2);
            } catch (NoChangeException ignored) {
            }
        });

        mixAfficherId.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchAffIDs();
        });
        mixAfficherNom.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchAffNoms();
        });
        mixAfficherType.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchAffTypes();
        });
        mixCacherRoutes.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchCacherRoutes();
        });
        mixAfficherDistance.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchAffDistance();
        });
        mixAfficherDuree.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchAffDuree();
        });
        mixAfficherFiabilite.addActionListener(e -> {
            for (Carte s : secteurs)
                s.switchAffFiabilite();
        });
    }
}