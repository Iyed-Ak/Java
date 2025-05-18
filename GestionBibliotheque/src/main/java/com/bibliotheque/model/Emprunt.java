package com.bibliotheque.model;

import java.util.Date;

/**
 * Classe représentant un emprunt de livre
 */
public class Emprunt {
    private int id;
    private int livreId;
    private int utilisateurId;
    private Date dateEmprunt;
    private Date dateRetourPrevue;
    private Date dateRetourEffective;
    
    // Informations supplémentaires pour l'affichage (jointures)
    private String titreLivre;
    private String auteurLivre;
    private String nomUtilisateur;
    private String prenomUtilisateur;

    // Constructeur par défaut
    public Emprunt() {
    }

    // Constructeur pour un nouvel emprunt (sans ID et sans date de retour effective)
    public Emprunt(int livreId, int utilisateurId, Date dateEmprunt, Date dateRetourPrevue) {
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
    }

    // Constructeur complet
    public Emprunt(int id, int livreId, int utilisateurId, Date dateEmprunt, Date dateRetourPrevue, Date dateRetourEffective) {
        this.id = id;
        this.livreId = livreId;
        this.utilisateurId = utilisateurId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivreId() {
        return livreId;
    }

    public void setLivreId(int livreId) {
        this.livreId = livreId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Date getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(Date dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Date getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(Date dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public String getTitreLivre() {
        return titreLivre;
    }

    public void setTitreLivre(String titreLivre) {
        this.titreLivre = titreLivre;
    }

    public String getAuteurLivre() {
        return auteurLivre;
    }

    public void setAuteurLivre(String auteurLivre) {
        this.auteurLivre = auteurLivre;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getPrenomUtilisateur() {
        return prenomUtilisateur;
    }

    public void setPrenomUtilisateur(String prenomUtilisateur) {
        this.prenomUtilisateur = prenomUtilisateur;
    }

    @Override
    public String toString() {
        return "Emprunt #" + id + " : " + titreLivre + " par " + prenomUtilisateur + " " + nomUtilisateur;
    }
    
    // Méthode utilitaire pour vérifier si l'emprunt est en cours
    public boolean estEnCours() {
        return dateRetourEffective == null;
    }
    
    // Méthode utilitaire pour vérifier si l'emprunt est en retard
    public boolean estEnRetard() {
        if (!estEnCours()) {
            return false; // Déjà retourné, donc pas en retard
        }
        Date now = new Date();
        return now.after(dateRetourPrevue);
    }
}