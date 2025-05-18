package com.bibliotheque.ui;

import com.bibliotheque.config.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Fenêtre principale de l'application de gestion de bibliothèque
 */
public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private LivrePanel livrePanel;
    private UtilisateurPanel utilisateurPanel;
    private EmpruntPanel empruntPanel;

    public MainFrame() {
        // Configuration de la fenêtre
        setTitle("Gestion de Bibliothèque");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre
        
        // Icône de l'application (si disponible)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }
        
        // Configuration du panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Ajout de l'en-tête avec le logo
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Création des onglets
        tabbedPane = new JTabbedPane();
        
        // Création des panneaux pour chaque fonctionnalité
        livrePanel = new LivrePanel();
        utilisateurPanel = new UtilisateurPanel();
        empruntPanel = new EmpruntPanel(livrePanel, utilisateurPanel);
        
        // Ajouter les panneaux aux onglets avec des icônes
        tabbedPane.addTab("Livres", createIcon("/images/book_icon.png"), livrePanel, "Gestion des livres");
        tabbedPane.addTab("Utilisateurs", createIcon("/images/user_icon.png"), utilisateurPanel, "Gestion des utilisateurs");
        tabbedPane.addTab("Emprunts", createIcon("/images/borrow_icon.png"), empruntPanel, "Gestion des emprunts");
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Ajouter le panneau principal à la fenêtre
        setContentPane(mainPanel);
        
        // Ajouter un écouteur pour fermer la connexion à la base de données lors de la fermeture de l'application
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DatabaseConnection.closeConnection();
            }
        });
    }
    
    /**
     * Crée le panneau d'en-tête avec le logo et le titre
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Logo à gauche
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
            Image logoImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
            headerPanel.add(logoLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du logo: " + e.getMessage());
        }
        
        // Titre au centre
        JLabel titleLabel = new JLabel("Système de Gestion de Bibliothèque", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée une icône à partir d'un chemin de ressource
     */
    private ImageIcon createIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône " + path + ": " + e.getMessage());
            return null;
        }
    }
}