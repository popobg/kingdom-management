package fr.digi.cda2024.entities;

/**
 * Classe entité représentant les ressources du royaume
 */
public class Ressource {
    /** Type de ressource */
    private String type;
    /** Quantité de la ressource */
    private int quantite;

    /**
     * Constructeur
     * @param type type
     * @param quantite quantité
     */
    public Ressource(String type, int quantite) {
        this.type = type;
        this.quantite = quantite;
    }

    /**
     * Afficher les informations liées à un objet Ressource dans la console.
     */
    public void afficherDetails() {
        System.out.println(this.toString());
    }

    /**
     * Retourne une chaîne de caractères contenant les informations
     * d'un objet Ressource.
     * @return String, infos de l'objet
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ressource{");
        sb.append("type='").append(type).append('\'');
        sb.append(", quantite=").append(quantite);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
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
}
