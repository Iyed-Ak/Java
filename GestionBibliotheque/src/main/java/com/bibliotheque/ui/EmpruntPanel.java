package com.bibliotheque.ui;

import com.bibliotheque.dao.EmpruntDAO;
import com.bibliotheque.model.Emprunt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;

public class EmpruntPanel extends JPanel {

    private LivrePanel livrePanel;
    private UtilisateurPanel utilisateurPanel;

    private EmpruntDAO empruntDAO;
    private JTable tableEmprunts;
    private DefaultTableModel tableModel;
    private JButton btnRetourner, btnSupprimer, btnRefresh, btnEmprunter;

    // ✅ Constructeur avec paramètres
    public EmpruntPanel(LivrePanel livrePanel, UtilisateurPanel utilisateurPanel) {
        this.livrePanel = livrePanel;
        this.utilisateurPanel = utilisateurPanel;
        this.empruntDAO = new EmpruntDAO();

        initComponents();
        loadEmprunts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        String[] colonnes = {"ID", "Livre", "Auteur", "Utilisateur", "Date emprunt", "Date retour prévue", "Date retour effective"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableEmprunts = new JTable(tableModel);
        tableEmprunts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tableEmprunts);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoutons = new JPanel();
        btnRetourner = new JButton("Marquer comme retourné");
        btnSupprimer = new JButton("Supprimer");
        btnRefresh = new JButton("Actualiser");
        btnEmprunter = new JButton("Emprunter");

        panelBoutons.add(btnEmprunter);
        panelBoutons.add(btnRetourner);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnRefresh);

        add(panelBoutons, BorderLayout.SOUTH);

        btnRetourner.addActionListener(this::onMarquerCommeRetourne);
        btnSupprimer.addActionListener(this::onSupprimer);
        btnRefresh.addActionListener(e -> loadEmprunts());
        btnEmprunter.addActionListener(e -> onEmprunter());
    }

    private void loadEmprunts() {
        List<Emprunt> emprunts = empruntDAO.getAll();
        tableModel.setRowCount(0);
        for (Emprunt e : emprunts) {
            tableModel.addRow(new Object[]{
                e.getId(),
                e.getTitreLivre(),
                e.getAuteurLivre(),
                e.getNomUtilisateur() + " " + e.getPrenomUtilisateur(),
                e.getDateEmprunt(),
                e.getDateRetourPrevue(),
                e.getDateRetourEffective() != null ? e.getDateRetourEffective() : ""
            });
        }
    }

    private void onMarquerCommeRetourne(ActionEvent event) {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int empruntId = (int) tableModel.getValueAt(selectedRow, 0);
        String dateRetourEffective = (String) tableModel.getValueAt(selectedRow, 6);

        if (!dateRetourEffective.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cet emprunt est déjà retourné.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean success = empruntDAO.marquerCommeRetourne(empruntId);
        if (success) {
            JOptionPane.showMessageDialog(this, "Emprunt marqué comme retourné avec succès.");
            loadEmprunts();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSupprimer(ActionEvent event) {
        int selectedRow = tableEmprunts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un emprunt à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int empruntId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Confirmez-vous la suppression de l'emprunt sélectionné ?", "Confirmer la suppression", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            boolean success = empruntDAO.supprimer(empruntId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Emprunt supprimé avec succès.");
                loadEmprunts();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void onEmprunter() {
        List<String> utilisateurs = utilisateurPanel.getUtilisateursAsStringList();
        List<String> livres = livrePanel.getLivresDisponiblesAsStringList();

        if (utilisateurs.isEmpty() || livres.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ajoutez au moins un utilisateur et un livre disponible.");
            return;
        }

        JComboBox<String> comboUtil = new JComboBox<>(utilisateurs.toArray(new String[0]));
        JComboBox<String> comboLivre = new JComboBox<>(livres.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Utilisateur :"));
        panel.add(comboUtil);
        panel.add(new JLabel("Livre :"));
        panel.add(comboLivre);

        int result = JOptionPane.showConfirmDialog(this, panel, "Nouvel emprunt", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int utilisateurId = utilisateurPanel.getUtilisateurIdByString((String) comboUtil.getSelectedItem());
            int livreId = livrePanel.getLivreIdByString((String) comboLivre.getSelectedItem());

            if (utilisateurId != -1 && livreId != -1) {
                Emprunt emprunt = new Emprunt();
                emprunt.setLivreId(livreId);
                emprunt.setUtilisateurId(utilisateurId);
                emprunt.setDateEmprunt(new Date());
                emprunt.setDateRetourPrevue(EmpruntDAO.calculerDateRetourPrevue(emprunt.getDateEmprunt()));

                int id = empruntDAO.ajouter(emprunt);
                if (id != -1) {
                    JOptionPane.showMessageDialog(this, "Emprunt enregistré avec succès !");
                    loadEmprunts();
                    livrePanel.refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Échec de l'enregistrement de l'emprunt.");
                }
            }
        }
    }
}
