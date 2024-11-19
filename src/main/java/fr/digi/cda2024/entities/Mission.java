package fr.digi.cda2024.entities;

public class Mission {
    private String nom;

    public Mission(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Mission{");
        sb.append("nom='").append(nom).append('\'');
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
}
