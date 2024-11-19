package fr.digi.cda2024.dal;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

/**
 * Classe de gestion de la collection "Ressources"
 * issue d'une base de données MongoDB
 */
public class GestionRessources {
    /** Ensemble d'objets d'une collection "Ressources" MongoDB */
    private MongoCollection<Document> ressources;

    /**
     * Constructeur
     * @param ressources collection Ressources MongoDB
     */
    public GestionRessources(MongoCollection<Document> ressources) {
        this.ressources = ressources;
    }

    /**
     * Créer une nouvelle ressource dans la collection
     * à partir d'un objet entité Ressource.
     * Met-à-jour les informations si la ressource est déjà existante.
     * @param type type
     * @param quantite quantité de ressource
     */
    // CREATE du CRUD
    public void ajouterRessource(String type, int quantite) {
        Document ressource = ressources.find(new Document("type", type)).first();
        if (ressource != null) {
            int nouvelleQuantite = quantite + ressource.getInteger("quantite", 0);
            mettreAJourRessource(type, nouvelleQuantite);
        } else {
            ressources.insertOne(new Document("type", type).append("quantite", quantite));
            System.out.println("Ressource ajoutée : " + type + ", quantite " + quantite);
        }
    }

    /**
     * Afficher toutes les ressources de la collection.
     */
    // READ du CRUD
    public void afficherRessources() {
        for (Document ressource : ressources.find()) {
            System.out.println(ressource.toJson());
        }
    }

    /**
     * Mettre-à-jour la quantité d'une ressource.
     * Crée la ressource si elle n'existe pas.
     * @param type type de ressource
     * @param nouvelleQuantite nouvelle quantité de ressource
     */
    // UPDATE du CRUD
    public void mettreAJourRessource(String type, int nouvelleQuantite) {
        ressources.updateOne(new Document("type", type), new Document("$set", new Document("quantite", nouvelleQuantite)));
        System.out.println("Quantité mise à jour pour : " + type + " -> " + nouvelleQuantite);
    }

    /**
     * Supprimer une ressource de la collection.
     * @param type type de ressource à supprimer
     */
    // DELETE du CRUD
    public void supprimerRessource(String type) {
        ressources.deleteOne(new Document("type", type));
        System.out.println("Ressource supprimée : " + type);
    }

    /**
     * Vérifier si le stock de la ressource est suffisant par rapport au besoin.
     * @param type type de ressource
     * @param quantiteNecessaire quantité nécessaire
     * @return boolean, true si stock suffisant, sinon false
     */
    public boolean verifierRessource(String type, int quantiteNecessaire) {
        // On récupère le document le plus récent
        Document ressource = ressources.find(new Document("type", type))
                .sort(Sorts.descending("_id"))
                .first();

        if (ressource != null) {
            int quantiteDisponible = ressource.getInteger("quantite", 0);
            System.out.println("Quantité disponible de " + type + " : " + quantiteDisponible);
            System.out.println("Quantité nécessaire de " + type + " : " + quantiteNecessaire);
            return quantiteDisponible >= quantiteNecessaire;
        }
        return false;
    }

    /**
     * Retourne la ressource de type demandée
     * @param type type de ressource
     * @return Document ressource
     */
    public Document getRessource(String type) {
        return ressources.find(new Document("type", type)).first();
    }
}
