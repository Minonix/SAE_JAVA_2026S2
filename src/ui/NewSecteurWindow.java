package ui;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import elements.Carte;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class NewSecteurWindow extends JDialog {
    
    // L'objet que nous allons construire et retourner
    private Carte carteCreee;
    
    // Les composants interactifs à partager entre initComponent et initListener
    private JTextField txNom;
    private JTextField txParam;
    private JButton btRetour;
    private JButton btAlea;
    private JButton btChemin;

    public NewSecteurWindow() {
        super();
        this.setTitle("Création d'un Nouveau Secteur");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setSize(new Dimension(600, 400));
        this.setLocationRelativeTo(null);
        
        initComponent();
        initListener();
    }

    private void initComponent() {
        JPanel panelFormulaire = new JPanel(new GridLayout(3, 2, 10, 10));
        
        JLabel lblNom = new JLabel("Nom du secteur :");
        txNom = new JTextField(); // Utilisation de l'attribut de classe
        
        JLabel lblParam = new JLabel("Chemin CSV ou Nb sommets :");
        txParam = new JTextField(); // Utilisation de l'attribut de classe
        
        panelFormulaire.add(lblNom);
        panelFormulaire.add(txNom);
        panelFormulaire.add(lblParam);
        panelFormulaire.add(txParam);

        // Panneau pour les boutons
        JPanel panelBoutons = new JPanel();
        btRetour = new JButton("Retour");
        btAlea = new JButton("Créer aléatoire");
        btChemin = new JButton("Créer depuis CSV");
        
        panelBoutons.add(btRetour);
        panelBoutons.add(btAlea);
        panelBoutons.add(btChemin);
        
        this.add(panelFormulaire, BorderLayout.NORTH);
        this.add(panelBoutons, BorderLayout.SOUTH);
    }

    private void initListener() {
        btRetour.addActionListener(e -> {
            this.dispose();
        });
        btAlea.addActionListener(e -> {
            try {
                int nbSommets = Integer.parseInt(txParam.getText().trim());
                carteCreee = Carte.construireGrapheAleatoire(txNom.getText(), nbSommets);
                this.dispose(); // Ferme la fenêtre si tout s'est bien passé
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Pour une génération aléatoire, veuillez entrer un nombre de sommets valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        btChemin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    carteCreee = Carte.Parsing(txNom.getText(), txParam.getText().trim());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }   NewSecteurWindow.this.dispose();
            }
        });
    }

    public static Carte NewSecteur() {
        NewSecteurWindow nSect = new NewSecteurWindow();
        nSect.setVisible(true); // L'exécution du code s'arrête ici jusqu'au this.dispose()
        
        return nSect.carteCreee; // Une fois fermé, on renvoie l'objet nouvellement créé
    }
}
