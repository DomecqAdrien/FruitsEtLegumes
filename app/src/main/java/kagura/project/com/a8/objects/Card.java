package kagura.project.com.a8.objects;

import android.view.View;

public class Card {

    public View viewFront;
    public View viewBack;
    public int position;

    public Card(View viewFront, View viewBack, int position) {
        this.viewFront = viewFront;
        this.viewBack = viewBack;
        this.position = position;
    }
}

