package fr.digi.cda2024.dal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class GestionBatiments {
    private MongoCollection<Document> batiments;

    public GestionBatiments(MongoCollection<Document> batiments) {
        this.batiments = batiments;
    }

    /**
     * Ajouter bâtiment
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param etages nombre d'étages
     */
    public void ajouterBatiment(String type, String fonction, int etages) {
        Document batiment = batiments.find(new Document("type", type).append("fonction", fonction)).first();
        if (batiment != null) {
            mettreAJourBatiment(type, fonction, etages);
        } else {
            batiments.insertOne(new Document("type", type).append("fonction", fonction).append("etages", etages));
            System.out.println("Bâtiment ajouté : " + type + ", fonction : " + fonction);
        }
    }

    /**
     * Construit le bâtiment voulu, avec le nombre d'étages souhaité,
     * Si les ressources sont en quantité suffisante.
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param coutBois coût d'un étage en bois
     * @param coutPierre coût d'un étage en pierre
     * @param nbEtages nombre d'étages souhaité
     * @param ressources objet gestionRessources permettant de communiquer
     *                   avec le gestionnaire de la collection Ressources
     *                   en base de données
     */
    public void construireBatiment(String type, String fonction, int coutBois, int coutPierre, int nbEtages, GestionRessources ressources) {
        if (ressources.verifierRessource("Bois", coutBois * nbEtages) && ressources.verifierRessource("Pierre", coutPierre * nbEtages)) {
            // On décrémente les ressources utilisées
            ressources.mettreAJourRessource("Bois", - coutBois * nbEtages);
            ressources.mettreAJourRessource("Pierre", - coutPierre * nbEtages);
            ajouterBatiment(type, fonction, nbEtages);
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
     * Met à jour le nombre d'étages du bâtiment en l'incrémentant du nombre
     * d'étages donné en paramètre.
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param nouvelEtage nombre d'étages à ajouter
     */
    public void mettreAJourBatiment(String type, String fonction, int nouvelEtage) {
        // Le bâtiment existe-t-il déjà ?
        Document batimentNouveauNiveau = batiments.find(new Document("type", type).append("fonction", fonction)).first();

        if (batimentNouveauNiveau != null) {
            // On prend le type de citoyen existant et on incrémente son nombre
            int qteNouveauNiveau = batimentNouveauNiveau.getInteger("etages") + nouvelEtage;
            batiments.updateOne(new Document("type", type).append("fonction", fonction), new Document("$inc", new Document("etages", nouvelEtage)));
        } else {
            // On crée le citoyen du type donné avec le nouveau rôle
            Document batiment = new Document("type", type)
                    .append("fonction", fonction)
                    .append("etages", nouvelEtage);
            batiments.insertOne(batiment);
        }

        System.out.println("Etages mis à jour pour le bâtiment : " + type + ", " + fonction + " -> " + batiments.find(new Document("type", type).append("fonction", fonction)).first().getInteger("etages"));
    }

    /**
     * Augmenter le nombre d'étages du bâtiment
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     * @param nbEtagesAAjouter nombre d'étages à ajouter
     * @param ressources objet gestionRessources permettant de communiquer
     *      *                   avec le gestionnaire de la collection Ressources
     *      *                   en base de données
     */
    public void ameliorerBatiment(String type, String fonction, int nbEtagesAAjouter, GestionRessources ressources) {
        Document batimentExistant = batiments.find(new Document("type", type).append("fonction", fonction)).first();

        if (batimentExistant == null) {
            construireBatiment(type, fonction, 3, 2, nbEtagesAAjouter, ressources);
        }
        else {
            if (ressources.verifierRessource("Bois", 3 * nbEtagesAAjouter) && ressources.verifierRessource("Pierre", 2 * nbEtagesAAjouter)) {
                // On décrémente les ressources utilisées
                ressources.mettreAJourRessource("Bois", - 3 * nbEtagesAAjouter);
                ressources.mettreAJourRessource("Pierre", - 2 * nbEtagesAAjouter);
                mettreAJourBatiment(type, fonction, batimentExistant.getInteger("etages") + nbEtagesAAjouter);
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
        System.out.println("Citoyen supprimé : " + type + ", fonction : " + fonction);
    }
}
