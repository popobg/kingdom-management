package fr.digi.cda2024.ihm;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import fr.digi.cda2024.dal.ConnexionMongoDB;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class TestDB {
    // Avant création de la DB
    public static void main(String[] args) {
        ConnexionMongoDB databaseConnection = new ConnexionMongoDB();
        // Récupérer base de données
        MongoDatabase db = databaseConnection.getDatabase();

        // Avant création de la classe gestionRessources
        // CREATE
        try {
            db.getCollection("Ressources").insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("type", "Fer")
                    .append("quantite", 150));

            System.out.println("Fer ajouté aux ressources.");
        } catch (MongoException e) {
            System.out.println("Impossible d'ajouter le fer aux Ressources.");
        }

        // READ
        try {
            MongoCollection<Document> ressources = db.getCollection("Ressources");

            FindIterable<Document> fi = ressources.find();

            // ferme le cursor à la fin du try
            try (MongoCursor<Document> cursor = fi.iterator()) {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            }

            System.out.println("Récupération des ressources.");
        } catch (MongoException e) {
            System.out.println("Impossible de récupérer les ressources.");
        }

        // UPDATE
        Document queryUpdate = new Document().append("type", "Bois");
        Bson updates = Updates.combine(
                Updates.set("quantite", 400));
        UpdateOptions options = new UpdateOptions().upsert(true);

        try {
            UpdateResult result = db.getCollection("Ressources").updateOne(queryUpdate, updates, options);
            System.out.println("Modification de Bois" + result.getModifiedCount());
        } catch (MongoException e) {
            System.out.println("Impossible de mettre à jour le bois.");
        }

        // DELETE
        Bson queryDelete = eq("type", "Fer");

        try {
            DeleteResult result = db.getCollection("Ressources").deleteOne(queryDelete);
            System.out.println("Fer supprimé des ressources.");
        } catch (MongoException e) {
            System.out.println("Impossible de supprimer le bois.");
        }
        databaseConnection.closeConnection();
    }
}
