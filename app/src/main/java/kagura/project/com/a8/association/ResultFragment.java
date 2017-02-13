package kagura.project.com.a8.association;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kagura.project.com.a8.R;

import static kagura.project.com.a8.R.id.tries;


public class ResultFragment extends Fragment {

    int level;
    ImageView buttonNextLevel, buttonLevelSelection, buttonHome;

    public ResultFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        level = this.getArguments().getInt("level");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_result_game, container, false);
        buttonNextLevel = (ImageView) rl.findViewById(R.id.buttonNextLevel);
        if(level == 3){
            buttonNextLevel.setEnabled(false);
            buttonNextLevel.setImageDrawable(getResources().getDrawable(R.drawable.nextgrey));
        }
        buttonLevelSelection = (ImageView) rl.findViewById(R.id.buttonLevelSelection);
        buttonLevelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Log.i("nameActivity", getActivity().getClass().getSimpleName());
                switch (getActivity().getClass().getSimpleName()){
                    case "SemantiqueGame":
                        getActivity().overridePendingTransition(R.anim.right_start, R.anim.right_end);
                        break;
                    case "MemoryGame":
                        getActivity().overridePendingTransition(R.anim.left_start, R.anim.left_end);
                        break;
                }

            }
        });

        buttonHome = (ImageView) rl.findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHomeMenu = new Intent();
                getActivity().setResult(66, intentHomeMenu);
                getActivity().finish();
            }
        });

        return rl;
    }
}
