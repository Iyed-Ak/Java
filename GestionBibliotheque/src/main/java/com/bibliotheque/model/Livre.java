package com.bibliotheque.model;

/**
 * Classe représentant un livre dans la bibliothèque
 */
public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private String isbn;
    private int annee;
    private int exemplaires;
    private int disponibles;

    // Constructeur par défaut
    public Livre() {
    }

    // Constructeur pour un nouveau livre (sans ID)
    public Livre(String titre, String auteur, String isbn, int annee, int exemplaires) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.annee = annee;
        this.exemplaires = exemplaires;
        this.disponibles = exemplaires; // Initialement, tous les exemplaires sont disponibles
    }

    // Constructeur complet
    public Livre(int id, String titre, String auteur, String isbn, int annee, int exemplaires, int disponibles) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.annee = annee;
        this.exemplaires = exemplaires;
        this.disponibles = disponibles;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public int getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(int exemplaires) {
        this.exemplaires = exemplaires;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    @Override
    public String toString() {
        return titre + " par " + auteur + " (" + annee + ")";
    }
}