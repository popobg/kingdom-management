package fr.digi.cda2024.ihm;

import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import fr.digi.cda2024.dal.*;

public class App {
    public static void main(String[] args) {
        try {
            ConnexionMongoDB databaseConnection = new ConnexionMongoDB();
            MongoDatabase db = databaseConnection.getDatabase();

            // CRUD Ressources
            GestionRessources gestionRessources = new GestionRessources(db.getCollection("Ressources"));

            gestionRessources.ajouterRessource("Argent", 500);
            gestionRessources.afficherRessources();

            gestionRessources.mettreAJourRessource("Argent", 700);
            gestionRessources.afficherRessources();

            gestionRessources.supprimerRessource("Argent");
            gestionRessources.afficherRessources();

            gestionRessources.supprimerRessource("Bois");
            gestionRessources.supprimerRessource("Pierre");
            gestionRessources.ajouterRessource("Bois", 700);
            gestionRessources.ajouterRessource("Pierre", 500);
            gestionRessources.supprimerRessource("Nourriture");
            gestionRessources.ajouterRessource("Nourriture", 200);
            gestionRessources.afficherRessources();

            System.out.println();

            // CRUD Citoyens
            GestionCitoyens gestionCitoyens = new GestionCitoyens(db.getCollection("Citoyens"));

            gestionCitoyens.ajouterCitoyen("fermier", 12, "Elevage");
            gestionCitoyens.afficherCitoyens();

            gestionCitoyens.mettreAJourCitoyen("Elevage", 14);
            gestionCitoyens.afficherCitoyens();

            gestionCitoyens.mettreAJourCitoyen("fermier","Elevage", "Elevage de bêtes");
            gestionCitoyens.afficherCitoyens();

            gestionCitoyens.supprimerCitoyen("fermier", "Elevage");
            gestionCitoyens.afficherCitoyens();

            gestionCitoyens.supprimerCitoyen("fermier", "Elevage de bêtes");
            gestionCitoyens.afficherCitoyens();

            gestionCitoyens.supprimerCitoyen("Soldats", "Attaque");
            gestionCitoyens.ajouterCitoyen("Soldats", 50, "Attaque");

            System.out.println();
            // CRUD Batiments
            GestionBatiments gestionBatiments = new GestionBatiments(db.getCollection("Batiments"), gestionRessources);

            gestionBatiments.construireBatiment("Maison", "Habitat", 50, 80);
            gestionBatiments.supprimerBatiment("Forge", "Armement");
            gestionBatiments.construireBatiment("Forge", "Armement", 15, 100);
            gestionBatiments.afficherBatiments();

            gestionBatiments.ameliorerBatiment("Forge", "Armement");


            gestionBatiments.supprimerBatiment("Maison", "Habitat");
            gestionBatiments.afficherBatiments();

            System.out.println();
            // Mission
            GestionMission gestionMission = new GestionMission(db.getCollection("Missions"), gestionRessources, gestionCitoyens);

            gestionMission.preparerMission("Pillage ville voisine", 8, 50, 100);
            gestionMission.envoyerEnMission("Pillage ville voisine", 8);
            gestionMission.retourMission("Pillage ville voisine");

            databaseConnection.closeConnection();
        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }
}