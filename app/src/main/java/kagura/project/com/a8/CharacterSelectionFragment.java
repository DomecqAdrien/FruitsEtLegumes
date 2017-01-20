package kagura.project.com.a8;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class CharacterSelectionFragment extends Fragment implements View.OnClickListener {


    TextView name;
    int avatar;
    Button start;
    ImageButton fraise;
    ImageButton pomme;

    public CharacterSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //name = (TextView) getView().findViewById(R.id.name);
        fraise = (ImageButton) getView().findViewById(R.id.buttonFraise);
        fraise.setOnClickListener(this);
        pomme = (ImageButton) getView().findViewById(R.id.buttonPomme);
        pomme.setOnClickListener(this);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_character_selection, container, false);
    }

    @Override
    public void onClick(View v) {
        avatar = v.getId();
        Log.i("id", Integer.toString(avatar));
    }
}
