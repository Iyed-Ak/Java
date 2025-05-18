package com.bibliotheque.ui;

import com.bibliotheque.dao.LivreDAO;
import com.bibliotheque.model.Livre;
import com.bibliotheque.ui.components.CustomButton;
import com.bibliotheque.ui.components.CustomPanel;
import com.bibliotheque.ui.components.CustomTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panneau pour la gestion des livres
 */
public class LivrePanel extends JPanel {
    private CustomTable tableLivres;
    private JTextField txtRecherche;
    private JTextField txtTitre;
    private JTextField txtAuteur;
    private JTextField txtIsbn;
    private JTextField txtAnnee;
    private JTextField txtExemplaires;
    private JButton btnAjouter;
    private JButton btnModifier;
    private JButton btnSupprimer;
    private JButton btnEffacer;
    
    private LivreDAO livreDAO;
    private Livre livreSelectionne;

    /**
     * Constructeur du panneau de gestion des livres
     */
    public LivrePanel() {
        livreDAO = new LivreDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(createSearchPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 0));
        centerPanel.add(createTablePanel(), BorderLayout.CENTER);
        centerPanel.add(createFormPanel(), BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        refreshTable();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new CustomPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel lblRecherche = new JLabel("Rechercher:");
        searchPanel.add(lblRecherche, BorderLayout.WEST);

        txtRecherche = new JTextField(20);
        searchPanel.add(txtRecherche, BorderLayout.CENTER);

        JButton btnRechercher = new CustomButton("Rechercher");
        btnRechercher.addActionListener(e -> rechercherLivres());
        searchPanel.add(btnRechercher, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new CustomPanel(new BorderLayout());

        tablePanel.add(CustomPanel.createTitlePanel("Liste des livres"), BorderLayout.NORTH);

        String[] columnNames = {"ID", "Titre", "Auteur", "ISBN", "Année", "Exemplaires", "Disponibles"};
        tableLivres = new CustomTable(columnNames);

        tableLivres.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableLivres.getSelectedRowIndex();
                if (selectedRow >= 0) {
                    int id = Integer.parseInt(tableLivres.getModel().getValueAt(selectedRow, 0).toString());
                    livreSelectionne = livreDAO.getById(id);
                    remplirFormulaire(livreSelectionne);
                    btnModifier.setEnabled(true);
                    btnSupprimer.setEnabled(true);
                }
            }
        });

        tablePanel.add(new JScrollPane(tableLivres), BorderLayout.CENTER);
        return tablePanel;
    }


    /**
     * Crée le panneau de formulaire pour les détails du livre
     */
    private JPanel createFormPanel() {
        JPanel formContainer = new CustomPanel(new BorderLayout());
        formContainer.setPreferredSize(new Dimension(300, 400));
        
        // En-tête
        formContainer.add(CustomPanel.createTitlePanel("Détails du livre"), BorderLayout.NORTH);
        
        // Champs du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        
        // Titre
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Titre:"), gbc);
        gbc.gridy = 1;
        txtTitre = new JTextField(20);
        formPanel.add(txtTitre, gbc);
        
        // Auteur
        gbc.gridy = 2;
        formPanel.add(new JLabel("Auteur:"), gbc);
        gbc.gridy = 3;
        txtAuteur = new JTextField(20);
        formPanel.add(txtAuteur, gbc);
        
        // ISBN
        gbc.gridy = 4;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridy = 5;
        txtIsbn = new JTextField(20);
        formPanel.add(txtIsbn, gbc);
        
        // Année
        gbc.gridy = 6;
        formPanel.add(new JLabel("Année:"), gbc);
        gbc.gridy = 7;
        txtAnnee = new JTextField(20);
        formPanel.add(txtAnnee, gbc);
        
        // Exemplaires
        gbc.gridy = 8;
        formPanel.add(new JLabel("Exemplaires:"), gbc);
        gbc.gridy = 9;
        txtExemplaires = new JTextField(20);
        formPanel.add(txtExemplaires, gbc);
        
        formContainer.add(formPanel, BorderLayout.CENTER);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnAjouter = new CustomButton("Ajouter");
        btnAjouter.addActionListener(e -> ajouterLivre());
        
        btnModifier = new CustomButton("Modifier", CustomButton.TYPE_PRIMARY);
        btnModifier.setEnabled(false);
        btnModifier.addActionListener(e -> modifierLivre());
        
        btnSupprimer = new CustomButton("Supprimer", CustomButton.TYPE_DANGER);
        btnSupprimer.setEnabled(false);
        btnSupprimer.addActionListener(e -> supprimerLivre());
        
        btnEffacer = new CustomButton("Effacer", CustomButton.TYPE_SECONDARY);
        btnEffacer.addActionListener(e -> effacerFormulaire());
        
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);
        buttonPanel.add(btnEffacer);
        
        formContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        return formContainer;
    }
    
    /**
     * Remplit le formulaire avec les détails d'un livre
     */
    private void remplirFormulaire(Livre livre) {
        if (livre != null) {
            txtTitre.setText(livre.getTitre());
            txtAuteur.setText(livre.getAuteur());
            txtIsbn.setText(livre.getIsbn());
            txtAnnee.setText(String.valueOf(livre.getAnnee()));
            txtExemplaires.setText(String.valueOf(livre.getExemplaires()));
        }
    }
    
    /**
     * Efface le formulaire et réinitialise la sélection
     */
    private void effacerFormulaire() {
        txtTitre.setText("");
        txtAuteur.setText("");
        txtIsbn.setText("");
        txtAnnee.setText("");
        txtExemplaires.setText("");
        livreSelectionne = null;
        btnModifier.setEnabled(false);
        btnSupprimer.setEnabled(false);
        tableLivres.clearSelection();
    }
    
    /**
     * Rafraîchit la table avec les données actuelles
     */
    public void refreshTable() {
        tableLivres.clearTable();
        List<Livre> livres = livreDAO.getAll();
        for (Livre livre : livres) {
            tableLivres.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getIsbn(),
                livre.getAnnee(),
                livre.getExemplaires(),
                livre.getDisponibles()
            });
        }
    }
    
    
    /**
     * Ajoute un nouveau livre
     */
    private void ajouterLivre() {
        try {
            // Validation
            if (txtTitre.getText().trim().isEmpty() || txtAuteur.getText().trim().isEmpty() || 
                txtIsbn.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Veuillez remplir tous les champs obligatoires (Titre, Auteur, ISBN).",
                    "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Création du livre
            String titre = txtTitre.getText().trim();
            String auteur = txtAuteur.getText().trim();
            String isbn = txtIsbn.getText().trim();
            int annee = txtAnnee.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAnnee.getText().trim());
            int exemplaires = txtExemplaires.getText().trim().isEmpty() ? 1 : Integer.parseInt(txtExemplaires.getText().trim());
            
            Livre livre = new Livre(titre, auteur, isbn, annee, exemplaires);
            
            // Ajout dans la base de données
            int id = livreDAO.ajouter(livre);
            if (id > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Livre ajouté avec succès!", 
                    "Ajout réussi", JOptionPane.INFORMATION_MESSAGE);
                effacerFormulaire();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de l'ajout du livre.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer des valeurs numériques valides pour l'année et le nombre d'exemplaires.",
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Une erreur est survenue: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Modifie le livre sélectionné
     */
    private void modifierLivre() {
        if (livreSelectionne == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un livre à modifier.", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Validation
            if (txtTitre.getText().trim().isEmpty() || txtAuteur.getText().trim().isEmpty() || 
                txtIsbn.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Veuillez remplir tous les champs obligatoires (Titre, Auteur, ISBN).",
                    "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Mise à jour du livre
            livreSelectionne.setTitre(txtTitre.getText().trim());
            livreSelectionne.setAuteur(txtAuteur.getText().trim());
            livreSelectionne.setIsbn(txtIsbn.getText().trim());
            livreSelectionne.setAnnee(txtAnnee.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAnnee.getText().trim()));
            
            int nouveauxExemplaires = txtExemplaires.getText().trim().isEmpty() ? 1 : Integer.parseInt(txtExemplaires.getText().trim());
            int diff = nouveauxExemplaires - livreSelectionne.getExemplaires();
            livreSelectionne.setExemplaires(nouveauxExemplaires);
            livreSelectionne.setDisponibles(Math.max(0, livreSelectionne.getDisponibles() + diff));
            
            // Mise à jour dans la base de données
            boolean success = livreDAO.modifier(livreSelectionne);
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Livre modifié avec succès!", 
                    "Modification réussie", JOptionPane.INFORMATION_MESSAGE);
                effacerFormulaire();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la modification du livre.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer des valeurs numériques valides pour l'année et le nombre d'exemplaires.",
                "Erreur de format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Une erreur est survenue: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    public List<String> getLivresDisponiblesAsStringList() {
    List<Livre> livres = livreDAO.getAll();
    List<String> result = new ArrayList<>();
    for (Livre l : livres) {
        if (l.getDisponibles() > 0) {
            result.add(l.getId() + " - " + l.getTitre());
        }
    }
    return result;
}

    public int getLivreIdByString(String s) {
        try {
            return Integer.parseInt(s.split(" - ")[0]);
        } catch (Exception e) {
            return -1;
        }
    }

    
    /**
     * Supprime le livre sélectionné
     */
    private void supprimerLivre() {
        if (livreSelectionne == null) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner un livre à supprimer.", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmation = JOptionPane.showConfirmDialog(this,
            "Êtes-vous sûr de vouloir supprimer le livre \"" + livreSelectionne.getTitre() + "\" ?",
            "Confirmation de suppression", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirmation == JOptionPane.YES_OPTION) {
            boolean success = livreDAO.supprimer(livreSelectionne.getId());
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Livre supprimé avec succès!", 
                    "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
                effacerFormulaire();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la suppression du livre.", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Recherche des livres par titre ou auteur
     */
    private void rechercherLivres() {
        String recherche = txtRecherche.getText().trim();
        
        if (recherche.isEmpty()) {
            refreshTable();
            return;
        }
        
        List<Livre> livres = livreDAO.rechercherParTitreOuAuteur(recherche);
        tableLivres.clearTable();
        
        for (Livre livre : livres) {
            tableLivres.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getIsbn(),
                livre.getAnnee(),
                livre.getExemplaires(),
                livre.getDisponibles()
            });
        }
    }
    
    /**
     * Retourne le livre actuellement sélectionné
     */
    public Livre getLivreSelectionne() {
        return livreSelectionne;
    }
}