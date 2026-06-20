package ui;

import elements.Carte;
import elements.CentreSante;
import org.graphstream.graph.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class InfosCentreWindow extends JFrame{
    private final CentreSante c;
    private final Carte g;

    public ArrayList<ArrayList<Integer>> allData;

    private ArrayList<CentreSante> centresM;
    private ArrayList<CentreSante> centresO;
    private ArrayList<CentreSante> centresN;

    private GridBagConstraints gc;

    private final String[] typeCentres= {"maternité(s)", "bloc(s) opératoire(s)", "centre(s) de nutrition"};
    private final ArrayList<ArrayList<CentreSante>> lstCentresTypes = new ArrayList<>();

    private JPanel pnlInfos;
    private JPanel pnlPlusProches;
    private JPanel pnlDistance;
    private JPanel pnlDuree;

    public InfosCentreWindow(CentreSante c) {
        super("Informations sur le centre");
        this.c = c;
        this.g = (Carte) c.getGraph();

        allData = g.dijkstraInfos(c);

        initCentres();
        initComposant();
        this.pack();
        this.setVisible(true);
    }

    private void initComposant() {
        JTabbedPane pnlCard = new JTabbedPane();
        JPanel pnlMain = new JPanel(new GridBagLayout());
        gc = new GridBagConstraints();

        initTabs();
        initTabDistance();
        initTabDuree();
        initInfos();
        initPlusProches();

        GridBagConstraints gcMain = new GridBagConstraints();
        gcMain.gridx = gcMain.gridy = 0;
        pnlMain.add(pnlInfos, gcMain);
        gc.gridheight = 6;
        gcMain.gridy = 1;
        pnlMain.add(pnlCard, gcMain);

//        pnlCard.addTab("Plus proche", pnlPlusProches);

        pnlCard.setPreferredSize(new Dimension(450, 200));

        pnlCard.addTab("Plus proche", new JScrollPane(pnlPlusProches));
        pnlCard.addTab("Distances", new JScrollPane(pnlDistance));
        pnlCard.addTab("Durées", new JScrollPane(pnlDuree));


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
        pnlInfos.add(new JLabel("ID : " + c.getIdCentre()), gc);
        gc.gridx++;
        pnlInfos.add(new JLabel("Type : "+c.getCenterType()), gc);
    }

    private void initPlusProches() {
        pnlPlusProches = new JPanel(new GridBagLayout());
        ArrayList<ArrayList<CentreSante>> lists = new ArrayList<>();
        lists.add(centresM);
        lists.add(centresO);
        lists.add(centresN);

        CentreSante tmp;

        gc.gridy = 0;

        for (int i = 0; i < 3; i++) {
            gc.gridx = 0;
            pnlPlusProches.add(new JLabel("Plus proche "+typeCentres[i]), gc);
            gc.gridx++;
            pnlPlusProches.add(new JLabel("ID"), gc);
            gc.gridx++;
            pnlPlusProches.add(new JLabel("Durée"), gc);
            gc.gridx++;
            pnlPlusProches.add(new JLabel("Durée avec la fiabilité"), gc);

            gc.gridy++;

            gc.gridx = 1;

            tmp = plusProcheCentre(lists.get(i));
            pnlPlusProches.add(new JLabel(""+(tmp.getIdCentre())), gc);
            gc.gridx++;
            pnlPlusProches.add(new JLabel(""+allData.get(tmp.getIdCentre()-1).get(0)), gc);
            gc.gridx++;
            pnlPlusProches.add(new JLabel(""+allData.get(tmp.getIdCentre()-1).get(1)), gc);

            gc.gridy++;

        }
    }

    private void initCentres() {
        centresM = new ArrayList<>();
        centresO = new ArrayList<>();
        centresN = new ArrayList<>();

        CentreSante centre;
        for (Node node : g.getNodeSet()) {
            centre = (CentreSante) node;
            if (centre != c) {
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

    // TODO : Fusionner initTabDistance et initTabDuree en modulaire

    private void initTabDistance() {
        pnlDistance = new JPanel(new GridBagLayout());

        for (int i = 0; i < 3; i++) {
            pnlDistance.add(new JLabel("Distance aux " + typeCentres[i]), gc);
            gc.gridx++;
            pnlDistance.add(new JLabel("Id"), gc);
            gc.gridx++;
            pnlDistance.add(new JLabel("Distance (km)"), gc);

            gc.gridy++;
            gc.gridx = 1;

            for (CentreSante centreType : lstCentresTypes.get(i)) {
                pnlDistance.add(new JLabel(""+(centreType.getIdCentre())), gc);
                gc.gridx++;

                pnlDistance.add(new JLabel("" + allData.get(centreType.getIdCentre()-1).get(2)), gc);
                gc.gridx = 1;
                gc.gridy++;
            }
            gc.gridx = 0;
            gc.gridy++;
        }

    }

    private void initTabDuree() {
        pnlDuree = new JPanel(new GridBagLayout());
        JLabel tmpLabel;

        for (int i = 0; i < 3; i++) {
            // TODO : ne marche pas pour le background, marche pour le texte ?
            tmpLabel = new JLabel("Durée aux " + typeCentres[i]);
            tmpLabel.setBackground(Color.ORANGE);
            pnlDuree.add(tmpLabel, gc);
            gc.gridx++;
            pnlDuree.add(new JLabel("Id"), gc);
            gc.gridx++;
            pnlDuree.add(new JLabel("Durée (minutes)"), gc);

            gc.gridy++;
            gc.gridx = 1;

            for (CentreSante centreType : lstCentresTypes.get(i)) {
                pnlDuree.add(new JLabel(""+(centreType.getIdCentre())), gc);
                gc.gridx++;

                pnlDuree.add(new JLabel("" + allData.get(centreType.getIdCentre()-1).getFirst()), gc);
                gc.gridx = 1;
                gc.gridy++;
            }

            gc.gridx = 0;
            gc.gridy++;
        }

    }

    private CentreSante plusProcheCentre(ArrayList<CentreSante> liste) {
        CentreSante res = liste.getFirst();
        int dureeEstimeeMax = allData.get(res.getIdCentre()-1).get(1);

        for (CentreSante centre : liste) {
            if (allData.get(centre.getIdCentre()-1).get(1) < dureeEstimeeMax) {
                dureeEstimeeMax = allData.get(centre.getIdCentre()-1).get(1);
                res = centre;
            }
        }
        return res;
    }
}
