package kagura.project.com.a8;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AssociationChoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AssociationChoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssociationChoiceFragment extends Fragment {
    int level;
    ImageButton buttonNextLevel, buttonLevelSelection, buttonHome;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        level = this.getArguments().getInt("level");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    return null;
    }
}
