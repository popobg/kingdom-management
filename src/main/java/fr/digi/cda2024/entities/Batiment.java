package fr.digi.cda2024.entities;

public class Batiment {
    private int etages;
    private String type;
    private String fonction;

    /**
     * Constructeur
     * @param etages nombre d'étages
     * @param type type de bâtiment
     * @param fonction fonction du bâtiment
     */
    public Batiment(int etages, String type, String fonction) {
        this.etages = etages;
        this.type = type;
        this.fonction = fonction;
    }

    /**
     * Retourne les informations de l'objet Batiment
     * @return String
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Batiment{");
        sb.append("etages=").append(etages);
        sb.append(", type='").append(type).append('\'');
        sb.append(", fonction='").append(fonction).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Getter
     * @return etages
     */
    public int getEtages() {
        return etages;
    }

    /**
     * Setter
     * @param etages etages
     */
    public void setEtages(int etages) {
        this.etages = etages;
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
     * @return fonction
     */
    public String getFonction() {
        return fonction;
    }

    /**
     * Setter
     * @param fonction fonction
     */
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}
