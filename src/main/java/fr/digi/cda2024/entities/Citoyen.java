package fr.digi.cda2024.entities;

/**
 * Classe entité représentant les citoyens du royaume
 */
public class Citoyen {
    /** Nom du ciyoyen */
    private String nom;
    /** Quantité de citoyens d'un nom */
    private int quantite;
    /** Rôle du citoyen */
    private String role;

    /**
     * Constructeur
     * @param nom nom
     * @param quantite quantité
     * @param role rôle
     */
    public Citoyen(String nom, int quantite, String role) {
        this.nom = nom;
        this.quantite = quantite;
        this.role = role;
    }

    /**
     * Afficher les informations liées à un objet Citoyen dans la console.
     */
    public void afficherDetails() {
        System.out.println(this.toString());
    }

    /**
     * Retourne une chaîne de caractères contenant les informations
     * d'un objet Citoyen.
     * @return String, infos de l'objet
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Citoyen{");
        sb.append("nom='").append(nom).append('\'');
        sb.append(", quantite=").append(quantite);
        sb.append(", role='").append(role).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter
     * @param nom nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter
     * @return quantite
     */
    public int getQuantite() {
        return quantite;
    }

    /**
     * Setter
     * @param quantite quantite
     */
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    /**
     * Getter
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * Setter
     * @param role role
     */
    public void setRole(String role) {
        this.role = role;
    }
}
