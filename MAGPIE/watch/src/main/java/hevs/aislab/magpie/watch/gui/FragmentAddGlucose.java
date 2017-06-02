package hevs.aislab.magpie.watch.gui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hevs.aislab.magpie.watch.R;

/**
 * Created by teuft on 02.06.2017.
 */

public class FragmentAddGlucose extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_glucose, container, false);
    }
}
