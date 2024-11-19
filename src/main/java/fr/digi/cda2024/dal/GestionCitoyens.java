package fr.digi.cda2024.dal;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

/**
 * Classe de gestion de la collection "Citoyens"
 * issue d'une base de données MongoDB
 */
public class GestionCitoyens {
    /** Ensemble d'objets d'une collection "Citoyens" MongoDB */
    private MongoCollection<Document> citoyens;

    /**
     * Constructeur
     * @param citoyens collection Citoyens MongoDB
     */
    public GestionCitoyens(MongoCollection<Document> citoyens) {
        this.citoyens = citoyens;
    }

    /**
     * Créer un nouveau type de citoyen dans la collection
     * à partir d'un objet entité Citoyen.
     * Met-à-jour les informations si ce type de citoyen existe déjà.
     * @param nom nom du métier des citoyens
     * @param quantite nombre de citoyens ayant ce nom et rôle
     * @param role rôle du citoyen
     */
    // CREATE du CRUD
    // On identifie le citoyen par son rôle
    public void ajouterCitoyen(String nom, int quantite, String role) {
        Document citoyen = citoyens.find(new Document("role",role)).first();
        if (citoyen!= null) {
            mettreAJourCitoyen(role, citoyen.getInteger("quantite", 0)+quantite);
        } else {
            Document nouveauCitoyen = new Document("role", role).append("quantite", quantite).append("nom", nom);
            citoyens.insertOne(nouveauCitoyen);
            System.out.println("Citoyen ajouté : " + nom + ", " + role);
        }
    }

    /**
     * Afficher tous les citoyens de la collection.
     */
    // READ du CRUD
    public void afficherCitoyens() {
        for (Document citoyen : citoyens.find()) {
            System.out.println(citoyen.toJson());
        }
    }

    /**
     * Mettre-à-jour la quantité de citoyens assurant un rôle particulier.
     * Crée le citoyen si il n'existe pas.
     * @param role rôle du citoyen
     * @param changementQuantite nouvelle quantité
     */
    // UPDATE du CRUD
    // Ici on incrémente avec la nouvelle quantité (ancienne + nouvelle)
    public void mettreAJourCitoyen(String role, int changementQuantite) {
        citoyens.updateOne(new Document("role", role), new Document("$set", new Document("quantite", changementQuantite)));
        System.out.println("Quantité mise à jour pour le rôle : " + role);
    }

    /**
     * Mettre à jour le rôle d'un citoyen existant. Gère la mise-à-jour des quantités
     * de citoyens pour chaque rôle.
     * Crée le nouveau rôle s'il n'existe pas.
     * @param nom nom du type de citoyen
     * @param ancienRole ancien rôle
     * @param nouveauRole nouveau rôle
     */
    // UPDATE du CRUD
    // mise à jour du rôle d'un citoyen existant
    public void mettreAJourCitoyen(String nom, String ancienRole, String nouveauRole) {
        // Un type de citoyen ayant le nouveau rôle existe-t-il déjà ?
        Document citoyenNouveauRole = citoyens.find(new Document("role", nouveauRole)).first();

        if (citoyenNouveauRole != null) {
            // On prend le type de citoyen existant et on incrémente son nombre
            int qteNouveauRole = citoyenNouveauRole.getInteger("quantite") + 1;
            mettreAJourCitoyen(nouveauRole, qteNouveauRole);
        } else {
            // On crée le citoyen du type donné avec le nouveau rôle
            Document citoyen = new Document("role", nouveauRole)
                    .append("quantite", 1)
                    .append("nom", nom);
            citoyens.insertOne(citoyen);
        }

        // L'ancien citoyen est décrémenté de 1
        Document citoyenAncienRole = citoyens.find(new Document("role", ancienRole)).first();
        int qteAncienRole = citoyenAncienRole.getInteger("quantite", 0) - 1;
        mettreAJourCitoyen(nouveauRole, qteAncienRole);

        System.out.println("Role mis à jour pour le citoyen : " + nom + " (" + ancienRole + " -> " + nouveauRole + ")");
    }

    /**
     * Supprimer un type de citoyen de la collection.
     * @param nom nom du type de citoyen
     */
    // DELETE du CRUD
    public void supprimerCitoyen(String nom, String role) {
        citoyens.deleteOne(new Document("nom", nom).append("role", role));
        System.out.println("Citoyen supprimé : " + nom);
    }

    /**
     * Vérifier si le nombre de citoyens d'un type est suffisant par rapport au besoin.
     * @param role rôle du citoyen
     * @param nbNecessaire nombre de citoyens nécessaires
     * @return boolean, true si nombre suffisant, sinon false
     */
    public boolean verifierCitoyens(String role, int nbNecessaire) {
        Document citoyen = citoyens.find(new Document("role", role))
                .sort(Sorts.descending("_id"))
                .first();

        if (citoyen != null) {
            int nbDisponible = citoyen.getInteger("quantite", 0);
            System.out.println("Nombre de " + role + " disponible : " + nbDisponible);
            System.out.println("Nombre de " + role + " nécessaire : " + nbNecessaire);
            return nbDisponible >= nbNecessaire;
        }
        return false;
    }

    /**
     * Retourne le citoyen du nom et rôle demandé
     * @param nom nom du citoyen
     * @param role rôle
     * @return Document ressource
     */
    public Document getCitoyen(String nom, String role) {
        return citoyens.find(new Document("nom", nom).append("role", role)).first();
    }
}
