import java.util.Date;

public class Main {
    public static void main(String[] args) {

        Produit lait = new Produit(1021, "Lait", "Delice", 0.700);
        Produit yaourt = new Produit(2510, "Yaourt", "Vitalait", 1.200);
        Produit tomate = new Produit(3250, "Tomate", "Sicam", 1.500);
        Produit fromage = new Produit(1022, "Fromage", "Président", 2.500, new Date());


        Magasin magasin1 = new Magasin(1, "Avenue de la liberté", 50);
        Magasin magasin2 = new Magasin(2, "Boulevard des Nations", 50);


        magasin1.ajouterProduit(lait);
        magasin1.ajouterProduit(yaourt);
        magasin1.ajouterProduit(tomate);
        magasin1.ajouterProduit(lait);
        magasin1.supprimerProduit(yaourt);

        magasin2.ajouterProduit(fromage);
        magasin2.ajouterProduit(tomate);


        magasin1.afficherCaracteristiques();
        magasin2.afficherCaracteristiques();


        Magasin magasinPlusDeProduits = Magasin.magasinAvecPlusDeProduits(magasin1, magasin2);
        System.out.println("Le magasin avec le plus de produits est : " + magasinPlusDeProduits);


        System.out.println("Nombre total de produits dans tous les magasins: " + Magasin.getNombreTotalProduits());
    }
}
