package ui;

import Exception.NoChangeException;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Fenêtre de dialogue (JDialog) permettant à l'utilisateur de configurer les paramètres 
 * d'une nouvelle tournée de véhicules.
 * L'utilisateur peut définir le nombre de camions alloués à la tournée et filtrer 
 * le type de centres de santé à desservir (tous, blocs, maternités ou nutrition).
 */
public class NewTourneeWindow extends JDialog {
    private JComboBox<Integer> cbNbCamions;
    private JComboBox<String> cbTypeCentre;
    private JLabel lbNbCamions, lbTypeCentre;

    private JButton btConfirmer, btRetour;

    private ArrayList<Object> parameters;

    public NewTourneeWindow() {
        super();

        this.parameters = null;

        this.setTitle("Options de la tournée");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(600, 200));
        this.setLocationRelativeTo(null);

        initComponent();
        initListener();
    }

    private void initListener() {
        btRetour.addActionListener(e -> {
            this.dispose();
        });
        btConfirmer.addActionListener(e -> {
            parameters = new ArrayList<>();
            parameters.add(cbNbCamions.getSelectedItem());
            switch (cbTypeCentre.getSelectedIndex()) {
                case 0 -> parameters.add(1, null);
                case 1 -> parameters.add(1, 'O');
                case 2 -> parameters.add(1, 'M');
                case 3 -> parameters.add(1, 'N');
            }
            this.dispose();
        });
    }

    private void initComponent() {
        cbNbCamions = new JComboBox<>(new Integer[]{1, 2});
        cbTypeCentre = new JComboBox<>(new String[]{"Tous", "Blocs opératoires", "Maternités", "Centres de nutrition"});
        lbNbCamions = new JLabel("Nombre de camions :");
        lbTypeCentre = new JLabel("Type de centres à desservir :");
        btConfirmer = new JButton("Confirmer");
        btRetour = new JButton("Retour");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(5, 2, 5, 2);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = gc.gridy = 0;
        mainPanel.add(lbNbCamions, gc);
        gc.gridx++;
        mainPanel.add(cbNbCamions, gc);
        gc.gridy++;
        gc.gridx = 0;
        mainPanel.add(lbTypeCentre, gc);
        gc.gridx++;
        mainPanel.add(cbTypeCentre, gc);
        gc.gridy++;
        gc.gridx = 0;
        mainPanel.add(btRetour, gc);
        gc.gridx++;
        mainPanel.add(btConfirmer, gc);

        setContentPane(mainPanel);
    }

    /**
     * Ouvre la fenêtre de paramétrage de la tournée et bloque l'exécution jusqu'à la validation.
     * Convertit les choix de l'utilisateur (menus déroulants) en variables exploitables par l'algorithme.
     *
     * @return Une liste contenant les paramètres configurés par l'utilisateur :
     * - Index 0 (Integer) : Le nombre de camions (ex: 1 ou 2).
     * - Index 1 (Character) : Le caractère symbolisant le type de centre ciblé 
     * ('O', 'M', 'N') ou {@code null} si l'utilisateur souhaite desservir tous les centres.
     * @throws NoChangeException Si l'utilisateur ferme la fenêtre via la croix ou le bouton "Retour".
     */
    public static ArrayList<Object> newTournee() throws NoChangeException {
        NewTourneeWindow nTour = new NewTourneeWindow();
        nTour.setVisible(true); // L'exécution du code s'arrête ici jusqu'au this.dispose()
        if (nTour.parameters == null) {
            throw new NoChangeException();
        } else {
            return nTour.parameters; // Une fois fermé, on renvoie l'objet nouvellement créé
        }
    }
}