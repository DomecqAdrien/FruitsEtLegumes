package kagura.project.com.a8.collections;


abstract  class Aliment {

    private String nom;

     Aliment(){}

     Aliment(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
