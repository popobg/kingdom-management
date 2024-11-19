package fr.digi.cda2024.dal;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConnexionMongoDB {
    private MongoClient mongoClient;
    private MongoDatabase database;

    /**
     * Constructeur vide établissant la connexion
     */
    public ConnexionMongoDB() {
        // Connexion au serveur MongoDB
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Accès à la base de données "Royaume"
        this.database = mongoClient.getDatabase("Royaume");

        System.out.println("Connexion réussie à la base de données : " + database.getName());
    }

    /**
     * Constructeur avec chaîne de connexion passée en argument.
     * @param connectionString chaîne de connexion à la base de données
     */
    public ConnexionMongoDB(String connectionString) {
        // Connexion au serveur MongoDB
        this.mongoClient = MongoClients.create(connectionString);
        // Accès à la base de données "Royaume"
        this.database = mongoClient.getDatabase("Royaume");

        System.out.println("Connexion réussie à la base de données : " + database.getName());
    }

    /**
     * Retourne la base de données
     * @return MongoDatabase, base de données
     */
    public MongoDatabase getDatabase() {
        return database;
    }

    /**
     * Fermer la connexion à la base de données
     */
    public void closeConnection() {
        mongoClient.close();

        System.out.println("Connexion fermée.");
    }
}
