package com.bibliotheque.ui;

import com.bibliotheque.dao.UtilisateurDAO;
import com.bibliotheque.model.Utilisateur;
import com.bibliotheque.ui.components.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurPanel extends CustomPanel {
    private CustomTable table;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private UtilisateurDAO utilisateurDAO;

    public UtilisateurPanel() {
        super(new BorderLayout());
        utilisateurDAO = new UtilisateurDAO();

        // Créer la table et l'encapsuler dans un JScrollPane
        table = new CustomTable(new String[]{"ID", "Nom", "Prénom", "N° Adhérent", "Inscription"});
        scrollPane = new JScrollPane(table);

        refreshTable();

        // Recherche
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterTable();
            }
        });

        // Boutons
        CustomButton addBtn = new CustomButton("Ajouter", CustomButton.TYPE_PRIMARY);
        addBtn.addActionListener(e -> ajouterUtilisateur());

        CustomButton deleteBtn = new CustomButton("Supprimer", CustomButton.TYPE_DANGER);
        deleteBtn.addActionListener(e -> supprimerUtilisateur());

        // Haut (titre + recherche)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(CustomPanel.createTitlePanel("Gestion des Utilisateurs"), BorderLayout.NORTH);
        topPanel.add(searchField, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(CustomPanel.createButtonPanel(addBtn, deleteBtn), BorderLayout.SOUTH);
    }

    private void refreshTable() {
        table.clearTable();
        List<Utilisateur> utilisateurs = utilisateurDAO.getAll();
        for (Utilisateur u : utilisateurs) {
            table.addRow(new Object[]{
                    u.getId(), u.getNom(), u.getPrenom(), u.getNumeroAdherent(), u.getDateInscription()
            });
        }
    }

    private void filterTable() {
        table.setFilter(searchField.getText(), new int[]{1, 2, 3});
    }

    private void ajouterUtilisateur() {
        JTextField nom = new JTextField();
        JTextField prenom = new JTextField();
        JTextField numero = new JTextField();

        JComponent[] fields = new JComponent[]{nom, prenom, numero};
        String[] labels = new String[]{"Nom", "Prénom", "N° Adhérent"};

        int result = JOptionPane.showConfirmDialog(this,
                CustomPanel.createFormPanel(labels, fields),
                "Ajouter un utilisateur", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Utilisateur utilisateur = new Utilisateur(
                    nom.getText(), prenom.getText(), numero.getText()
            );
            utilisateurDAO.ajouter(utilisateur);
            refreshTable();
        }
    }
    public List<String> getUtilisateursAsStringList() {
    List<Utilisateur> utilisateurs = utilisateurDAO.getAll();
    List<String> result = new ArrayList<>();
    for (Utilisateur u : utilisateurs) {
        result.add(u.getId() + " - " + u.getPrenom() + " " + u.getNom());
    }
    return result;
}

    public int getUtilisateurIdByString(String s) {
        try {
            return Integer.parseInt(s.split(" - ")[0]);
        } catch (Exception e) {
            return -1;
        }
    }


    private void supprimerUtilisateur() {
        int selectedRow = table.getSelectedRowIndex();
        if (selectedRow == -1) return;

        int id = (int) table.getModel().getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            utilisateurDAO.supprimer(id);
            refreshTable();
        }
    }

    public Utilisateur getUtilisateurSelectionne() {
        int selectedRow = table.getSelectedRowIndex();
        if (selectedRow == -1) return null;

        int id = (int) table.getModel().getValueAt(selectedRow, 0);
        return utilisateurDAO.getById(id);
    }
}
