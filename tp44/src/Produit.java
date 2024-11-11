import java.util.Date;

public class Produit {
    private int id;
    private String libelle;
    private String marque;
    private double prix;
    private Date dateExpiration;


    public Produit(int id, String libelle, String marque, double prix) {
        this.id = id;
        this.libelle = libelle;
        this.marque = marque;
        setPrix(prix);
        this.dateExpiration = null;
    }

    // Nouveau constructeur avec date d'expiration
    public Produit(int id, String libelle, String marque, double prix, Date dateExpiration) {
        this(id, libelle, marque, prix);
        this.dateExpiration = dateExpiration;
    }

    public int getId() {
        return id;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getMarque() {
        return marque;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        if (prix >= 0) {
            this.prix = prix;
        } else {
            System.out.println("Le prix ne peut pas être négatif.");
        }
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public boolean comparer(Produit produit) {
        return this.id == produit.getId() &&
                this.libelle.equals(produit.getLibelle()) &&
                this.prix == produit.getPrix();
    }

    public static boolean comparer(Produit produit1, Produit produit2) {
        return produit1.getId() == produit2.getId() &&
                produit1.getLibelle().equals(produit2.getLibelle()) &&
                produit1.getPrix() == produit2.getPrix();
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", marque='" + marque + '\'' +
                ", prix=" + prix +
                ", dateExpiration=" + (dateExpiration != null ? dateExpiration.toString() : "Aucune") +
                '}';
    }
}
