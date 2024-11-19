package fr.digi.cda2024.dal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class GestionBatiments {
    private MongoCollection<Document> batiments;
    private GestionRessources ressources;

    /**
     * Constructeur
     * @param batiments collection Batiments de MongoDB
     * @param ressources objet gestionRessources permettant de communiquer
     *                  avec le gestionnaire de la collection Ressources
     *                  en base de données
     */
    public GestionBatiments(MongoCollection<Document> batiments, GestionRessources ressources) {
        this.batiments = batiments;
        this.ressources = ressources;
    }

    /**
     * Ajouter bâtiment
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param etages nombre d'étages
     */
    public void ajouterBatiment(String type, String fonction, int etages) {
        batiments.insertOne(new Document("type", type).append("fonction", fonction).append("etages", etages));
    }

    /**
     * Construit le bâtiment voulu, avec le nombre d'étages souhaité,
     * Si les ressources sont en quantité suffisante.
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param coutBois coût d'un étage en bois
     * @param coutPierre coût d'un étage en pierre
     */
    public void construireBatiment(String type, String fonction, int coutBois, int coutPierre) {
        if (ressources.verifierRessource("Bois", coutBois) && ressources.verifierRessource("Pierre", coutPierre)) {
            // On décrémente les ressources utilisées
            ressources.mettreAJourRessource("Bois", ressources.getRessource("Bois").getInteger("quantite", 1) - coutBois);
            ressources.mettreAJourRessource("Pierre", ressources.getRessource("Pierre").getInteger("quantite", 1) - coutPierre);

            // On ajoute un nouveau bâtiment à un étage
            ajouterBatiment(type, fonction, 1);
            System.out.println("Bâtiment construit " + type + ", fonction : " + fonction);
        }
        else {
            System.out.println("Pas assez de ressources pour construire le bâtiment.");
        }
    }

    /**
     * Afficher tous les bâtiments de la collection.
     */
    public void afficherBatiments() {
        for (Document batiment : batiments.find()) {
            System.out.println(batiment.toJson());
        }
    }

    /**
     * Mettre à jour le nombre d'étages du bâtiment.
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param nouvelEtage nombre d'étages à ajouter
     */
    public void mettreAJourBatiment(String type, String fonction, int nouvelEtage) {
        batiments.updateOne(new Document("type", type).append("fonction", fonction), new Document("$set", new Document("etages", nouvelEtage)));
        System.out.println("Etages mis à jour pour le bâtiment : " + type + ", " + fonction + " -> " + nouvelEtage + " étages.");
    }

    /**
     * Augmenter le nombre d'étages du bâtiment
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     */
    public void ameliorerBatiment(String type, String fonction) {
        // Récupérer le bâtiment à l'aide de son type et de sa fonction
        Document batimentExistant = batiments.find(new Document("type", type).append("fonction", fonction)).first();

        if (batimentExistant == null) {
            System.out.println("Ce bâtiment " + type + ", fonction : " + fonction + " n'existe pas.");
        }
        else {
            int nbEtagesActuel = batimentExistant.getInteger("etages", 1);
            int nbEtagesSouhaite = nbEtagesActuel + 1;

            int coutBois = 100 * nbEtagesSouhaite;
            int coutPierre = 50 * nbEtagesSouhaite;

            if (ressources.verifierRessource("Bois", coutBois) && ressources.verifierRessource("Pierre", coutPierre)) {
                // On décrémente les ressources utilisées
                ressources.mettreAJourRessource("Bois", ressources.getRessource("Bois").getInteger("quantite", 1) - coutBois);
                ressources.mettreAJourRessource("Pierre", ressources.getRessource("Pierre").getInteger("quantite", 1) - coutPierre);

                // mise-à-jour du nombre d'étages du bâtiment
                mettreAJourBatiment(type, fonction, nbEtagesSouhaite);
                System.out.println("Bâtiment amélioré !");
            }
            else {
                System.out.println("Pas assez de ressources pour améliorer le bâtiment.");
            }
        }
    }

    /**
     * Supprimer un bâtiment de la collection
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     */
    public void supprimerBatiment(String type, String fonction) {
        batiments.deleteOne(new Document("type", type).append("fonction", fonction));
        System.out.println("Bâtiment supprimé : " + type + ", fonction : " + fonction);
    }
}
