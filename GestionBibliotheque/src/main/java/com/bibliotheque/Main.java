package com.bibliotheque;

import com.bibliotheque.config.DatabaseConnection;
import com.bibliotheque.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principale de l'application de gestion de bibliothèque
 */
public class Main {
    public static void main(String[] args) {
        // Configurez le look and feel pour une meilleure apparence
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Assurez-vous que les composants GUI sont créés dans l'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                // Établir la connexion à la base de données
                DatabaseConnection.getConnection();
                
                // Créer et afficher la fenêtre principale
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors du démarrage de l'application: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}