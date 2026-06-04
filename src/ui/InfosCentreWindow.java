package ui;

import elements.Carte;
import elements.CentreSante;
import elements.Route;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class InfosCentreWindow extends JFrame{
    private CentreSante c;

    private ArrayList<CentreSante> centresM;
    private ArrayList<CentreSante> centresO;
    private ArrayList<CentreSante> centresN;

    private GridBagConstraints gc;

    private final String[] typeCentres= {"maternités", "blocs opératoires", "centres de nutritions"};
    private final Color[] lstColors = {new Color(0x24283B), new Color(0x24283B), new Color(0xB4C7E7)};
    private ArrayList<ArrayList<CentreSante>> lstCentresTypes = new ArrayList<>();

    private JPanel pnlMain;
    private JTabbedPane pnlCard;

    private JPanel pnlInfos;
    private JPanel pnlPlusProches;
    private JPanel pnlDistance;
    private JPanel pnlDuree;

    public InfosCentreWindow(CentreSante c) {
        super("Informations sur le centre");
        this.c = c;

        initVoisins();
        initComposant();
        this.pack();
        this.setVisible(true);
    }

    private void initComposant() {
        pnlCard = new JTabbedPane();
        pnlMain = new JPanel(new GridBagLayout());
        gc = new GridBagConstraints();

        initTabs();
        initTabDistance();
        initTabDuree();
        initInfos();

        GridBagConstraints gcMain = new GridBagConstraints();
        gcMain.gridx = gcMain.gridy = 0;
        pnlMain.add(pnlInfos, gcMain);
        gc.gridheight = 6;
        gcMain.gridy = 1;
        pnlMain.add(pnlCard, gcMain);

        pnlCard.addTab("Plus proche", pnlPlusProches);
        pnlCard.addTab("Distances", pnlDistance);
        pnlCard.addTab("Durées", pnlDuree);
//        pnlCard.setPreferredSize(new Dimension(200, 1000));
        this.setContentPane(pnlMain);
    }

    private void initTabs() {
        gc.gridx = gc.gridy = 0;
        gc.insets = new Insets(3, 3, 3, 3);

        lstCentresTypes.add(centresM);
        lstCentresTypes.add(centresO);
        lstCentresTypes.add(centresN);
    }

    private void initInfos() {
        pnlInfos = new JPanel(new GridBagLayout());
        pnlInfos.setPreferredSize(new Dimension(200, 20));

        gc.gridy = gc.gridx = 0;

        pnlInfos.add(new JLabel("Nom : "+c.getNomCentre()), gc);
        gc.gridx++;
        pnlInfos.add(new JLabel("ID : " + c.getIdCentre()+""), gc);
        gc.gridx++;
        pnlInfos.add(new JLabel("Type : "+c.getCenterType()+""), gc);
    }

    private void initPlusProches() {
        pnlPlusProches = new JPanel(new GridBagLayout());

        gc.gridx = gc.gridy = 0;

        pnlPlusProches.add(new JLabel("D"), gc);
    }

    private void initVoisins() {
        centresM = new ArrayList<CentreSante>();
        centresO = new ArrayList<CentreSante>();
        centresN = new ArrayList<CentreSante>();

        Carte carte = (Carte) c.getGraph();
        CentreSante centre;
        for (Node node : c.getGraph().getNodeSet()) {
            centre = (CentreSante) node;
            if (carte.getRoute(centre.getIdCentre(), c.getIdCentre()) != null || carte.getRoute(c.getIdCentre(), centre.getIdCentre()) != null ) {
                switch (centre.getCenterType()) {
                    case 'M':
                        centresM.add(centre);
                        break;
                    case 'O':
                        centresO.add(centre);
                        break;
                    case 'N':
                        centresN.add(centre);
                        break;
                }
            }
        }
    }

    private void initTabDistance() {
        pnlDistance = new JPanel(new GridBagLayout());

        Route r;
        for (int i = 0; i < 3; i++) {
            pnlDistance.add(new JLabel("Distance aux " + typeCentres[i]), gc);
            gc.gridx++;
            pnlDistance.add(new JLabel("Id"), gc);
            gc.gridx++;
            pnlDistance.add(new JLabel("Distance (km)"), gc);

            gc.gridy++;
            gc.gridx = 1;

            for (CentreSante voisinType : lstCentresTypes.get(i)) {
                pnlDistance.add(new JLabel(""+voisinType.getIdCentre()), gc);
                gc.gridx++;
                if (((Carte) c.getGraph()).getRoute(voisinType.getIdCentre(), c.getIdCentre()) != null)
                    r = ((Carte) c.getGraph()).getRoute(voisinType.getIdCentre(), c.getIdCentre());
                else
                    r = ((Carte) c.getGraph()).getRoute(c.getIdCentre(), voisinType.getIdCentre());

                pnlDistance.add(new JLabel("" + r.getDistance()), gc);
                gc.gridx = 1;
                gc.gridy++;
            }
            gc.gridx = 0;
            gc.gridy++;
        }

    }

    private void initTabDuree() {
        pnlDuree = new JPanel(new GridBagLayout());

        Route r;
        for (int i = 0; i < 3; i++) {
            pnlDuree.add(new JLabel("Durée aux " + typeCentres[i]), gc);
            gc.gridx++;
            pnlDuree.add(new JLabel("Id"), gc);
            gc.gridx++;
            pnlDuree.add(new JLabel("Durée (minutes)"), gc);

            gc.gridy++;
            gc.gridx = 1;

            for (CentreSante voisinType : lstCentresTypes.get(i)) {
                pnlDuree.add(new JLabel(""+voisinType.getIdCentre()), gc);
                gc.gridx++;
                if (((Carte) c.getGraph()).getRoute(voisinType.getIdCentre(), c.getIdCentre()) != null)
                    r = ((Carte) c.getGraph()).getRoute(voisinType.getIdCentre(), c.getIdCentre());
                else
                    r = ((Carte) c.getGraph()).getRoute(c.getIdCentre(), voisinType.getIdCentre());

                pnlDuree.add(new JLabel("" + r.getDuree()), gc);
                gc.gridx = 1;
                gc.gridy++;
            }
            gc.gridx = 0;
            gc.gridy++;
        }

    }

    // TODO : initInfos, initPlusProche, gérer le spacing dans pnlMain
}
