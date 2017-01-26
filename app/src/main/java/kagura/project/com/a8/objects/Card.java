package kagura.project.com.a8.objects;

import android.view.View;

public class Card {

    public View viewFront;
    public View viewBack;
    public View view;
    public int position;

    public Card(View viewFront, View viewBack, int position) {
        this.viewFront = viewFront;
        this.viewBack = viewBack;
        this.position = position;
    }

    public Card(View view, int position){
        this.view = view;
        this.position = position;
    }
}

