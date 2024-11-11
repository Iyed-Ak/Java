import java.util.ArrayList;
import java.util.List;

public class Magasin {
    private static int nombreTotalProduits = 0;
    private int id;
    private String adresse;
    private int capacite;
    private List<Produit> produits;

    public Magasin(int id, String adresse, int capacite) {
        this.id = id;
        this.adresse = adresse;
        this.capacite = capacite;
        this.produits = new ArrayList<>();
    }

    public void ajouterProduit(Produit produit) {
        if (rechercherProduit(produit) == -1) { // Vérifie si le produit n'est pas déjà présent
            if (produits.size() < capacite) {
                produits.add(produit);
                nombreTotalProduits++;
            } else {
                System.out.println("Le magasin a atteint sa capacité maximale de produits.");
            }
        } else {
            System.out.println("Le produit est déjà présent dans le magasin.");
        }
    }

    public int rechercherProduit(Produit produit) {
        for (int i = 0; i < produits.size(); i++) {
            if (produits.get(i).comparer(produit)) {
                return i;
            }
        }
        return -1;
    }

    public void supprimerProduit(Produit produit) {
        int index = rechercherProduit(produit);
        if (index != -1) {
            produits.remove(index);
            nombreTotalProduits--;
            System.out.println("Produit supprimé.");
        } else {
            System.out.println("Produit introuvable.");
        }
    }

    public void afficherCaracteristiques() {
        System.out.println("Magasin ID: " + id);
        System.out.println("Adresse: " + adresse);
        System.out.println("Capacité: " + capacite);
        System.out.println("Produits :");
        for (Produit produit : produits) {
            System.out.println("Nom: " + produit.getLibelle() + ", Prix: " + produit.getPrix());
        }
    }

    public static Magasin magasinAvecPlusDeProduits(Magasin magasin1, Magasin magasin2) {
        return magasin1.produits.size() > magasin2.produits.size() ? magasin1 : magasin2;
    }

    public static int getNombreTotalProduits() {
        return nombreTotalProduits;
    }
}
