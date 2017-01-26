package kagura.project.com.a8.objects;

public class Fruit {

    private String nom;
    private String fruit_plein;
    private String fruit_coupe;
    private String fruit_arbre;
    private String fruit_graine;
    private int fruit_plein_id;
    private int fruit_coupe_id;
    private int fruit_arbre_id;

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

    private int fruit_graine_id;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getFruit_plein() {
        return fruit_plein;
    }

    public void setFruit_plein(String fruit_plein) {
        this.fruit_plein = fruit_plein;
    }

    public String getFruit_coupe() {
        return fruit_coupe;
    }

    public void setFruit_coupe(String fruit_coupe) {
        this.fruit_coupe = fruit_coupe;
    }

    public String getFruit_arbre() {
        return fruit_arbre;
    }

    public void setFruit_arbre(String fruit_arbre) {
        this.fruit_arbre = fruit_arbre;
    }

    public String getFruit_graine() {
        return fruit_graine;
    }

    public void setFruit_graine(String fruit_graine) {
        this.fruit_graine = fruit_graine;
    }
}
