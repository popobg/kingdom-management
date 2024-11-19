package fr.digi.cda2024.dal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Random;

/**
 * Classe de gestion de la collection "Missions"
 * issue d'une base de données MongoDB
 */
public class GestionMission {
    /** Ensemble de documents d'une collection "Missions" MongoDB */
    private MongoCollection<Document> missions;
    /** Gestionnaire de la collection "Citoyens" */
    private GestionCitoyens gestionCitoyens;
    /** Gestionnaire de la collection "Ressources" */
    private GestionRessources gestionRessources;

    /**
     * Constructeur
     * @param missions collection Missions MongoDB
     * @param citoyens objet gestionRessources permettant de communiquer
     *                 avec le gestionnaire de la collection Citoyens
     *                 en base de données
     * @param ressources objet gestionRessources permettant de communiquer
     *                  avec le gestionnaire de la collection Ressources
     *                  en base de données
     */
    public GestionMission(MongoCollection<Document> missions, GestionRessources ressources, GestionCitoyens citoyens) {
        this.missions = missions;
        this.gestionCitoyens = citoyens;
        this.gestionRessources = ressources;
    }

    /**
     * Vérifier les stocks et les hommes disponibles avant le lancement d'une mission.
     * @param nomMission nom de la mission
     * @param soldatsNecessaires citoyens soldats nécessaires
     * @param boisNecessaire ressource bois nécessaires
     * @param nourritureNecessaire ressource nourriture nécessaire
     * @return boolean, true si les stocks sont suffisants, sinon false
     */
    public boolean preparerMission(String nomMission, int soldatsNecessaires, int boisNecessaire, int nourritureNecessaire) {
        System.out.println("Préparation de la mission.");
        // Vérification des stocks de ressources
        if (gestionRessources.verifierRessource("Bois", boisNecessaire)
                && gestionRessources.verifierRessource("Nourriture", nourritureNecessaire)) {

            // Vérification du nombre de citoyens
            if (gestionCitoyens.verifierCitoyens("Attaque", soldatsNecessaires)) {
                // Décrémente les ressources utilisées
                gestionRessources.mettreAJourRessource("Bois", gestionRessources.getRessource("Bois").getInteger("quantite", 1) - boisNecessaire);
                gestionRessources.mettreAJourRessource("Nourriture", gestionRessources.getRessource("Nourriture").getInteger("quantite", 1) - nourritureNecessaire);

                // Recrute des soldats
                gestionCitoyens.mettreAJourCitoyen("Attaque", gestionCitoyens.getCitoyen("Soldats", "Attaque").getInteger("quantite", 1) - soldatsNecessaires);
                System.out.println("Mission " + nomMission + " prête avec " + soldatsNecessaires + " soldats.");
                return true;
            }
            else {
                System.out.println("Nombre de soldats insuffisants pour partir en mission.");
                return false;
            }
        }
        else {
            System.out.println("Ressources insuffisantes pour partir en mission.");
            return false;
        }
    }

    /**
     * Soldats envoyés en mission + création de la mission
     * dans la base de données MongoDB
     * @param nomMission nom de la mission
     * @param soldatsNecessaires nombre desoldats nécessaires
     */
    public void envoyerEnMission(String nomMission, int soldatsNecessaires){
        System.out.println("Envoi de la mission " + nomMission);

        Document mission = new Document("nom", nomMission)
                .append("Soldats envoyés", soldatsNecessaires)
                .append("Statut", "En cours");

        missions.insertOne(mission);

        System.out.println("La mission " + nomMission + " a été envoyée avec " + soldatsNecessaires + " soldats.");
    }

    /**
     * Gère le retour de missions des soldats : calcul des gains/pertes,
     * compte le nombre de soldats revenus, change le statut de la mission.
     * @param nomMission nom de la mission
     */
    public void retourMission(String nomMission) {
        int soldatsEnvoyes = gestionCitoyens.getCitoyen("Soldats", "Attaque").getInteger("quantite", 1);

        Random rand = new Random();
        String statutMission = rand.nextBoolean()? "Réussie": "Echouée";

        // Mise à jour statut mission
        missions.updateOne(new Document("nom", nomMission), new Document("$set", new Document("Statut", statutMission)));
        System.out.println("Mission " + nomMission + " " + statutMission);

        // Calcul des gains
        calculerGain(statutMission, rand, soldatsEnvoyes);

        // Nombre de soldats de retour
        retourSoldats(nomMission, rand);
    }

    /**
     * Calculer les gains ou les pertes apportées par la mission.
     */
    public void calculerGain(String statutMission, Random rand, int soldatsEnvoyes) {
        if (statutMission.equals("Réussie")) {
            int gainOr = rand.nextInt(1000);
            int gainBois = soldatsEnvoyes * 20;
            int gainNourriture = soldatsEnvoyes * 10;

            gestionRessources.mettreAJourRessource("Or", gestionRessources.getRessource("Or").getInteger("quantite", 1) + gainOr);
            gestionRessources.mettreAJourRessource("Bois", gestionRessources.getRessource("Bois").getInteger("quantite", 1) + gainBois);
            gestionRessources.mettreAJourRessource("Nourriture", gestionRessources.getRessource("Nourriture").getInteger("quantite", 1) + gainNourriture);

            System.out.println("Félicitations, la mission est un succès ! Vous gagnez " + gainOr + " or, " + gainBois + " bois, " + gainNourriture + " unités de nourriture.");
        }
        else {
            int perteOr = rand.nextInt(500);

            System.out.println("La mission est un échec... Vous avez perdu " + perteOr + " or.");
            int orActuel = gestionRessources.getRessource("Or").getInteger("quantite", 1);

            if (orActuel - perteOr <= 0) {
                gestionRessources.mettreAJourRessource("Or", 0);
            }
            else {
                gestionRessources.mettreAJourRessource("Or", orActuel - perteOr);
            }
        }
    }

    /**
     * Détermine combien de soldats rentrent de mission et s'ajoute à
     * @param nomMission nom de la mission
     */
    public void retourSoldats(String nomMission, Random rand) {
        int soldatsEnvoyes = missions.find(new Document("nom", nomMission)).first().getInteger("Soldats envoyés");
        int perteSoldats = rand.nextInt(soldatsEnvoyes);

        int nbAttaquantActuel = gestionCitoyens.getCitoyen("Soldats", "Attaque").getInteger("quantite", 1);
        if (nbAttaquantActuel - perteSoldats <= 0) {
            gestionCitoyens.mettreAJourCitoyen("Attaque", 0);
        }
        else {
            gestionCitoyens.mettreAJourCitoyen("Attaque", nbAttaquantActuel - perteSoldats);
        }
    }
}
