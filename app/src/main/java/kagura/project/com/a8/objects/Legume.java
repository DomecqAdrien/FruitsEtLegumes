package kagura.project.com.a8.objects;


public class Legume extends Aliment {

    private int legume_id;

    public Legume(){}

    public Legume(String nom, int id) {
        super(nom);
        this.legume_id = id;
    }

    public int getLegume_id() {
        return legume_id;
    }

    public void setLegume_id(int legume_id) {
        this.legume_id = legume_id;
    }
}
