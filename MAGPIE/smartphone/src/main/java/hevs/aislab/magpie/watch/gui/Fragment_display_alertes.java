package hevs.aislab.magpie.watch.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import hevs.aislab.magpie.watch.R;
/**
 * Created by teuft on 07.07.2017.
 */

public class Fragment_display_alertes extends Fragment {


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_alert, container, false);
        return view;

    }
}