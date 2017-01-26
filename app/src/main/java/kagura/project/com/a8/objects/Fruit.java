package kagura.project.com.a8.objects;

public class Fruit {

    private String nom;
    private int fruit_plein_id;
    private int fruit_coupe_id;
    private int fruit_arbre_id;
    private int fruit_graine_id;

    public Fruit(){}

    public int getFruit_graine_id() {
        return fruit_graine_id;
    }

    public void setFruit_graine_id(int fruit_graine_id) {
        this.fruit_graine_id = fruit_graine_id;
    }

    public int getFruit_arbre_id() {
        return fruit_arbre_id;
    }

    public void setFruit_arbre_id(int fruit_arbre_id) {
        this.fruit_arbre_id = fruit_arbre_id;
    }

    public int getFruit_coupe_id() {
        return fruit_coupe_id;
    }

    public void setFruit_coupe_id(int fruit_coupe_id) {
        this.fruit_coupe_id = fruit_coupe_id;
    }

    public int getFruit_plein_id() {
        return fruit_plein_id;
    }

    public void setFruit_plein_id(int fruit_plein_id) {
        this.fruit_plein_id = fruit_plein_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
