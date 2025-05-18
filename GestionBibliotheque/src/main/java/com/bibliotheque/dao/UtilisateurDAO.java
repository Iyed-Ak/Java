package com.bibliotheque.dao;

import com.bibliotheque.config.DatabaseConnection;
import com.bibliotheque.model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations CRUD sur les utilisateurs
 */
public class UtilisateurDAO {
    
    /**
     * Ajoute un nouvel utilisateur dans la base de données
     * @param utilisateur L'utilisateur à ajouter
     * @return L'ID de l'utilisateur ajouté, ou -1 en cas d'erreur
     */
    public int ajouter(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (nom, prenom, numero_adherent, date_inscription) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getNumeroAdherent());
            pstmt.setDate(4, new java.sql.Date(utilisateur.getDateInscription().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        utilisateur.setId(rs.getInt(1)); // Met à jour l'ID de l'utilisateur
                        return utilisateur.getId();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1; // Échec de l'ajout
    }
    
    /**
     * Met à jour un utilisateur existant dans la base de données
     * @param utilisateur L'utilisateur à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean modifier(Utilisateur utilisateur) {
        String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, numero_adherent = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getNumeroAdherent());
            pstmt.setInt(4, utilisateur.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprime un utilisateur de la base de données
     * @param id L'ID de l'utilisateur à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Récupère un utilisateur par son ID
     * @param id L'ID de l'utilisateur à récupérer
     * @return L'utilisateur correspondant, ou null s'il n'existe pas
     */
    public Utilisateur getById(int id) {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUtilisateurFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // Utilisateur non trouvé
    }
    
    /**
     * Récupère tous les utilisateurs de la base de données
     * @return Une liste de tous les utilisateurs
     */
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurs.add(extractUtilisateurFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
            e.printStackTrace();
        }
        
        return utilisateurs;
    }
    
    /**
     * Recherche des utilisateurs par nom, prénom ou numéro d'adhérent
     * @param recherche Le texte à rechercher
     * @return Une liste des utilisateurs correspondant à la recherche
     */
    public List<Utilisateur> rechercherParNomOuNumero(String recherche) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE nom LIKE ? OR prenom LIKE ? OR numero_adherent LIKE ? ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + recherche + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(extractUtilisateurFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche d'utilisateurs : " + e.getMessage());
            e.printStackTrace();
        }
        
        return utilisateurs;
    }
    
    /**
     * Méthode utilitaire pour extraire un utilisateur d'un ResultSet
     * @param rs Le ResultSet contenant les données de l'utilisateur
     * @return Un objet Utilisateur avec les données extraites
     * @throws SQLException En cas d'erreur lors de l'extraction
     */
    private Utilisateur extractUtilisateurFromResultSet(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setNumeroAdherent(rs.getString("numero_adherent"));
        utilisateur.setDateInscription(rs.getDate("date_inscription"));
        return utilisateur;
    }
}