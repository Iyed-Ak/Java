package com.bibliotheque.dao;

import com.bibliotheque.config.DatabaseConnection;
import com.bibliotheque.model.Livre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations CRUD sur les livres
 */
public class LivreDAO {
    
    /**
     * Ajoute un nouveau livre dans la base de données
     * @param livre Le livre à ajouter
     * @return L'ID du livre ajouté, ou -1 en cas d'erreur
     */
    public int ajouter(Livre livre) {
        String sql = "INSERT INTO livres (titre, auteur, isbn, annee, exemplaires, disponibles) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getIsbn());
            pstmt.setInt(4, livre.getAnnee());
            pstmt.setInt(5, livre.getExemplaires());
            pstmt.setInt(6, livre.getDisponibles());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        livre.setId(rs.getInt(1)); // Met à jour l'ID du livre avec celui généré
                        return livre.getId();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du livre : " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1; // Échec de l'ajout
    }
    
    /**
     * Met à jour un livre existant dans la base de données
     * @param livre Le livre à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean modifier(Livre livre) {
        String sql = "UPDATE livres SET titre = ?, auteur = ?, isbn = ?, annee = ?, exemplaires = ?, disponibles = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, livre.getTitre());
            pstmt.setString(2, livre.getAuteur());
            pstmt.setString(3, livre.getIsbn());
            pstmt.setInt(4, livre.getAnnee());
            pstmt.setInt(5, livre.getExemplaires());
            pstmt.setInt(6, livre.getDisponibles());
            pstmt.setInt(7, livre.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du livre : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprime un livre de la base de données
     * @param id L'ID du livre à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimer(int id) {
        String sql = "DELETE FROM livres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du livre : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Récupère un livre par son ID
     * @param id L'ID du livre à récupérer
     * @return Le livre correspondant, ou null s'il n'existe pas
     */
    public Livre getById(int id) {
        String sql = "SELECT * FROM livres WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractLivreFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du livre : " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // Livre non trouvé
    }
    
    /**
     * Récupère tous les livres de la base de données
     * @return Une liste de tous les livres
     */
    public List<Livre> getAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres ORDER BY titre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                livres.add(extractLivreFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des livres : " + e.getMessage());
            e.printStackTrace();
        }
        
        return livres;
    }
    
    /**
     * Recherche des livres par titre ou auteur
     * @param recherche Le texte à rechercher dans les titres et noms d'auteurs
     * @return Une liste des livres correspondant à la recherche
     */
    public List<Livre> rechercherParTitreOuAuteur(String recherche) {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livres WHERE titre LIKE ? OR auteur LIKE ? ORDER BY titre";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchTerm = "%" + recherche + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    livres.add(extractLivreFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de livres : " + e.getMessage());
            e.printStackTrace();
        }
        
        return livres;
    }
    
    /**
     * Met à jour le nombre d'exemplaires disponibles d'un livre
     * @param livreId L'ID du livre à mettre à jour
     * @param increment true pour incrémenter (retour de livre), false pour décrémenter (emprunt)
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateDisponibles(int livreId, boolean increment) {
        String sql = "UPDATE livres SET disponibles = disponibles " + (increment ? "+ 1" : "- 1") + 
                     " WHERE id = ? AND " + (increment ? "disponibles < exemplaires" : "disponibles > 0");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, livreId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour des exemplaires disponibles : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Méthode utilitaire pour extraire un livre d'un ResultSet
     * @param rs Le ResultSet contenant les données du livre
     * @return Un objet Livre avec les données extraites
     * @throws SQLException En cas d'erreur lors de l'extraction
     */
    private Livre extractLivreFromResultSet(ResultSet rs) throws SQLException {
        Livre livre = new Livre();
        livre.setId(rs.getInt("id"));
        livre.setTitre(rs.getString("titre"));
        livre.setAuteur(rs.getString("auteur"));
        livre.setIsbn(rs.getString("isbn"));
        livre.setAnnee(rs.getInt("annee"));
        livre.setExemplaires(rs.getInt("exemplaires"));
        livre.setDisponibles(rs.getInt("disponibles"));
        return livre;
    }
}