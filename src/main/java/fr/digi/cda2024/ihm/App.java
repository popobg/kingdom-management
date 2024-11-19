package fr.digi.cda2024.ihm;

import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import fr.digi.cda2024.dal.ConnexionMongoDB;
import fr.digi.cda2024.dal.GestionCitoyens;
import fr.digi.cda2024.dal.GestionRessources;

public class App {
    public static void main(String[] args) {
        try {
            ConnexionMongoDB databaseConnection = new ConnexionMongoDB();
            MongoDatabase db = databaseConnection.getDatabase();

            // CRUD Ressources
            GestionRessources ressources = new GestionRessources(db.getCollection("Ressources"));

            ressources.ajouterRessource("Argent", 500);
            ressources.afficherRessources();

            ressources.mettreAJourRessource("Argent", 700);
            ressources.afficherRessources();

            ressources.supprimerRessource("Argent");
            ressources.afficherRessources();

            System.out.println();

            // CRUD Citoyens
            GestionCitoyens citoyens = new GestionCitoyens(db.getCollection("Citoyens"));

            citoyens.ajouterCitoyen("fermier", 12, "Elevage");
            citoyens.afficherCitoyens();

            citoyens.mettreAJourCitoyen("Elevage", 14);
            citoyens.afficherCitoyens();

            citoyens.mettreAJourCitoyen("fermier","Elevage", "Elevage de bêtes");
            citoyens.afficherCitoyens();

            citoyens.supprimerCitoyen("fermier");
            citoyens.afficherCitoyens();

            citoyens.supprimerCitoyen("fermier");
            citoyens.afficherCitoyens();

            databaseConnection.closeConnection();
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }
}