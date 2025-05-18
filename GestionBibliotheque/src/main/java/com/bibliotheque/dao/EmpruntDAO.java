package com.bibliotheque.dao;

import com.bibliotheque.config.DatabaseConnection;
import com.bibliotheque.model.Emprunt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Classe DAO pour gérer les opérations CRUD sur les emprunts
 */
public class EmpruntDAO {
    
    /**
     * Ajoute un nouvel emprunt dans la base de données
     * @param emprunt L'emprunt à ajouter
     * @return L'ID de l'emprunt ajouté, ou -1 en cas d'erreur
     */
    public int ajouter(Emprunt emprunt) {
        String sql = "INSERT INTO emprunts (livre_id, utilisateur_id, date_emprunt, date_retour_prevue) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, emprunt.getLivreId());
            pstmt.setInt(2, emprunt.getUtilisateurId());
            pstmt.setDate(3, new java.sql.Date(emprunt.getDateEmprunt().getTime()));
            pstmt.setDate(4, new java.sql.Date(emprunt.getDateRetourPrevue().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        emprunt.setId(rs.getInt(1)); // Met à jour l'ID de l'emprunt
                        
                        // Mettre à jour le nombre d'exemplaires disponibles du livre
                        LivreDAO livreDAO = new LivreDAO();
                        livreDAO.updateDisponibles(emprunt.getLivreId(), false);
                        
                        return emprunt.getId();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'emprunt : " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1; // Échec de l'ajout
    }
    
    /**
     * Marque un emprunt comme retourné
     * @param id L'ID de l'emprunt à marquer comme retourné
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean marquerCommeRetourne(int id) {
        String sql = "UPDATE emprunts SET date_retour_effective = ? WHERE id = ? AND date_retour_effective IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Date de retour = aujourd'hui
            pstmt.setDate(1, new java.sql.Date(new Date().getTime()));
            pstmt.setInt(2, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Récupérer l'ID du livre associé à cet emprunt
                Emprunt emprunt = getById(id);
                if (emprunt != null) {
                    // Mettre à jour le nombre d'exemplaires disponibles du livre
                    LivreDAO livreDAO = new LivreDAO();
                    livreDAO.updateDisponibles(emprunt.getLivreId(), true);
                }
                return true;
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du marquage de l'emprunt comme retourné : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprime un emprunt de la base de données
     * @param id L'ID de l'emprunt à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimer(int id) {
        // Récupérer l'emprunt avant de le supprimer pour obtenir l'ID du livre
        Emprunt emprunt = getById(id);
        if (emprunt == null) {
            return false;
        }
        
        String sql = "DELETE FROM emprunts WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0 && emprunt.getDateRetourEffective() == null) {
                // Si l'emprunt n'avait pas encore été retourné, mettre à jour le nombre d'exemplaires disponibles
                LivreDAO livreDAO = new LivreDAO();
                livreDAO.updateDisponibles(emprunt.getLivreId(), true);
            }
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'emprunt : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Récupère un emprunt par son ID
     * @param id L'ID de l'emprunt à récupérer
     * @return L'emprunt correspondant, ou null s'il n'existe pas
     */
    public Emprunt getById(int id) {
        String sql = "SELECT e.*, l.titre, l.auteur, u.nom, u.prenom FROM emprunts e " +
                     "JOIN livres l ON e.livre_id = l.id " +
                     "JOIN utilisateurs u ON e.utilisateur_id = u.id " +
                     "WHERE e.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEmpruntFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'emprunt : " + e.getMessage());
            e.printStackTrace();
        }
        
        return null; // Emprunt non trouvé
    }
    
    /**
     * Récupère tous les emprunts de la base de données
     * @return Une liste de tous les emprunts
     */
    public List<Emprunt> getAll() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre, l.auteur, u.nom, u.prenom FROM emprunts e " +
                     "JOIN livres l ON e.livre_id = l.id " +
                     "JOIN utilisateurs u ON e.utilisateur_id = u.id " +
                     "ORDER BY e.date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                emprunts.add(extractEmpruntFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts : " + e.getMessage());
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    /**
     * Récupère tous les emprunts en cours (non retournés)
     * @return Une liste des emprunts en cours
     */
    public List<Emprunt> getEmpruntsEnCours() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre, l.auteur, u.nom, u.prenom FROM emprunts e " +
                     "JOIN livres l ON e.livre_id = l.id " +
                     "JOIN utilisateurs u ON e.utilisateur_id = u.id " +
                     "WHERE e.date_retour_effective IS NULL " +
                     "ORDER BY e.date_retour_prevue ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                emprunts.add(extractEmpruntFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts en cours : " + e.getMessage());
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    /**
     * Récupère tous les emprunts en retard (date de retour prévue dépassée)
     * @return Une liste des emprunts en retard
     */
    public List<Emprunt> getEmpruntsEnRetard() {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre, l.auteur, u.nom, u.prenom FROM emprunts e " +
                     "JOIN livres l ON e.livre_id = l.id " +
                     "JOIN utilisateurs u ON e.utilisateur_id = u.id " +
                     "WHERE e.date_retour_effective IS NULL AND e.date_retour_prevue < ? " +
                     "ORDER BY e.date_retour_prevue ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(new Date().getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(extractEmpruntFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts en retard : " + e.getMessage());
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    /**
     * Récupère les emprunts d'un utilisateur spécifique
     * @param utilisateurId L'ID de l'utilisateur
     * @return Une liste des emprunts de l'utilisateur
     */
    public List<Emprunt> getEmpruntsByUtilisateur(int utilisateurId) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre, l.auteur, u.nom, u.prenom FROM emprunts e " +
                     "JOIN livres l ON e.livre_id = l.id " +
                     "JOIN utilisateurs u ON e.utilisateur_id = u.id " +
                     "WHERE e.utilisateur_id = ? " +
                     "ORDER BY e.date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, utilisateurId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(extractEmpruntFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    /**
     * Récupère l'historique des emprunts d'un livre spécifique
     * @param livreId L'ID du livre
     * @return Une liste des emprunts du livre
     */
    public List<Emprunt> getEmpruntsByLivre(int livreId) {
        List<Emprunt> emprunts = new ArrayList<>();
        String sql = "SELECT e.*, l.titre, l.auteur, u.nom, u.prenom FROM emprunts e " +
                     "JOIN livres l ON e.livre_id = l.id " +
                     "JOIN utilisateurs u ON e.utilisateur_id = u.id " +
                     "WHERE e.livre_id = ? " +
                     "ORDER BY e.date_emprunt DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, livreId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    emprunts.add(extractEmpruntFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emprunts du livre : " + e.getMessage());
            e.printStackTrace();
        }
        
        return emprunts;
    }
    
    /**
     * Méthode utilitaire pour calculer la date de retour prévue (généralement 2 semaines après l'emprunt)
     * @param dateEmprunt La date d'emprunt
     * @return La date de retour prévue
     */
    public static Date calculerDateRetourPrevue(Date dateEmprunt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEmprunt);
        calendar.add(Calendar.DAY_OF_MONTH, 14); // +14 jours = 2 semaines
        return calendar.getTime();
    }
    
    /**
     * Méthode utilitaire pour extraire un emprunt d'un ResultSet
     * @param rs Le ResultSet contenant les données de l'emprunt
     * @return Un objet Emprunt avec les données extraites
     * @throws SQLException En cas d'erreur lors de l'extraction
     */
    private Emprunt extractEmpruntFromResultSet(ResultSet rs) throws SQLException {
        Emprunt emprunt = new Emprunt();
        emprunt.setId(rs.getInt("id"));
        emprunt.setLivreId(rs.getInt("livre_id"));
        emprunt.setUtilisateurId(rs.getInt("utilisateur_id"));
        emprunt.setDateEmprunt(rs.getDate("date_emprunt"));
        emprunt.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
        emprunt.setDateRetourEffective(rs.getDate("date_retour_effective"));
        
        // Informations supplémentaires pour l'affichage
        emprunt.setTitreLivre(rs.getString("titre"));
        emprunt.setAuteurLivre(rs.getString("auteur"));
        emprunt.setNomUtilisateur(rs.getString("nom"));
        emprunt.setPrenomUtilisateur(rs.getString("prenom"));
        
        return emprunt;
    }
}