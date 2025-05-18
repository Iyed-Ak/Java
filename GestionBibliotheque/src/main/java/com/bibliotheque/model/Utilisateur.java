package com.bibliotheque.model;

import java.util.Date;

/**
 * Classe représentant un utilisateur de la bibliothèque
 */
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;
    private String numeroAdherent;
    private Date dateInscription;

    // Constructeur par défaut
    public Utilisateur() {
    }

    // Constructeur pour un nouvel utilisateur (sans ID)
    public Utilisateur(String nom, String prenom, String numeroAdherent) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroAdherent = numeroAdherent;
        this.dateInscription = new Date(); // Date d'inscription = aujourd'hui
    }

    // Constructeur complet
    public Utilisateur(int id, String nom, String prenom, String numeroAdherent, Date dateInscription) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroAdherent = numeroAdherent;
        this.dateInscription = dateInscription;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroAdherent() {
        return numeroAdherent;
    }

    public void setNumeroAdherent(String numeroAdherent) {
        this.numeroAdherent = numeroAdherent;
    }

    public Date getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date dateInscription) {
        this.dateInscription = dateInscription;
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (N° " + numeroAdherent + ")";
    }
}