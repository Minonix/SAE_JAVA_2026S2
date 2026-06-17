package ui;

import Exception.NoChangeException;
import elements.Carte;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileNotFoundException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class NewSecteurWindow extends JDialog {

    private Carte carteCreee;
    private JTextField txNom;
    private JTextField txParam;
    private JButton btRetour, btAlea, btChemin;

    public NewSecteurWindow(){
        super();
        this.setTitle("Création d'un Nouveau Secteur");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(600, 200));
        this.setLocationRelativeTo(null);

        initComponent();
        initListener();
    }

    private void initComponent() {
        JPanel panelFormulaire = new JPanel(new GridBagLayout());

        JLabel lblNom = new JLabel("Nom du secteur :");
        txNom = new JTextField(50);

        JLabel lblParam = new JLabel("Nb sommets :");
        txParam = new JTextField(20);

        JLabel lblFile = new JLabel("Selectionner un fichier");

        btAlea = new JButton("Créer aléatoire");
        btChemin = new JButton("Importer depuis le CSV");

        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.BOTH;
        gc.gridx = 0;
        gc.gridy = 0;
        panelFormulaire.add(lblNom, gc);
        gc.gridx = 1;
        gc.gridwidth = 2;
        panelFormulaire.add(txNom, gc);
        gc.gridwidth = 1;

        gc.gridy = 1;
        gc.gridx = 0;
        panelFormulaire.add(lblParam, gc);
        gc.gridx = 1;
        panelFormulaire.add(txParam, gc);
        gc.gridx = 2;
        panelFormulaire.add(btAlea, gc);

        gc.gridy = 2;
        gc.gridx = 0;
        panelFormulaire.add(lblFile, gc);
        gc.gridx = 1;
        panelFormulaire.add(btChemin, gc);

        btRetour = new JButton("Retour");

        this.add(panelFormulaire, BorderLayout.NORTH);
        this.add(btRetour, BorderLayout.SOUTH);
    }

    private void initListener() {
        btRetour.addActionListener(e -> {
            NewSecteurWindow.this.dispose();
        });

        btAlea.addActionListener(e -> {
            if (!txNom.getText().isBlank()) {
                try {
                    int nbSommets = Integer.parseInt(txParam.getText().trim());
                    carteCreee = Carte.construireGrapheAleatoire(txNom.getText(), nbSommets);
                    this.dispose(); // Ferme la fenêtre si tout s'est bien passé
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Pour une génération aléatoire, veuillez entrer un nombre de sommets valide.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Veuillez ajouter un titre au Secteur", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btChemin.addActionListener((e) -> {
            JFileChooser choose = new JFileChooser(
                    FileSystemView
                            .getFileSystemView());

            choose.setDialogTitle("Selectionnez une image");

            choose.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier CSV", "csv");
            choose.addChoosableFileFilter(filter);

            choose.showOpenDialog(this);

            if (choose.getSelectedFile() != null) {
                try {
                    String name;
                    if (txNom.getText().isBlank()) {
                        name = choose.getSelectedFile().getName().substring(0, choose.getSelectedFile().getName().length()-4);
                    }
                    else {
                        name = txNom.getText();
                    }
                    carteCreee = Carte.Parsing(name, choose.getSelectedFile().getPath());
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(this,
                        "Erreur dans la recherche du fichier : " + e1, "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception e2) {
                    JOptionPane.showMessageDialog(this,
                        "Autre erreur : " + e2, "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
                NewSecteurWindow.this.dispose();
            }
        });
    }

    public static Carte NewSecteur() throws NoChangeException{
        NewSecteurWindow nSect = new NewSecteurWindow();
        nSect.setVisible(true); // L'exécution du code s'arrête ici jusqu'au this.dispose()
        if (nSect.carteCreee == null) {
            throw new NoChangeException();
        } else {
            return nSect.carteCreee; // Une fois fermé, on renvoie l'objet nouvellement créé
        }
    }
}
