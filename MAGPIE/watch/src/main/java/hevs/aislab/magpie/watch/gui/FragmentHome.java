package hevs.aislab.magpie.watch.gui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;

/**
 * Created by teuft on 30.05.2017.
 * This will be the gf
 */

//initial fragment that display user states
public class FragmentHome extends Fragment {

   private TextView txtViewPulse;
   private TextView txtViewGlucose;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
       view=lf.inflate(R.layout.fragment_home, container, false);

        //init graphical element
        txtViewGlucose=(TextView)view.findViewById(R.id.txtView_GlucoseValue);
        txtViewPulse=(TextView)view.findViewById(R.id.txtView_pulseValue);

        //retrive data from the bundle and set it to texte view
        Bundle bundle=this.getArguments();
        String value=bundle.getString("glucoseValue","");
        if (!value.equals(""))
            txtViewGlucose.setText(value);
        return view;
    }

    public void setPulseValue(String value) throws Exception
    {
            txtViewPulse.setText(value);
    }

    public void setGlucoseValue(String value)throws Exception
    {
        txtViewGlucose.setText(value);
    }



}
