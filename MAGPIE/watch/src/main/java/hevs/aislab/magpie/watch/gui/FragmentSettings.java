package hevs.aislab.magpie.watch.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hevs.aislab.magpie.watch.R;

/**
 * Created by teuft on 04.06.2017.
 */

public class FragmentSettings extends Fragment {

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_settings, container, false);
        return view;
    }
}
