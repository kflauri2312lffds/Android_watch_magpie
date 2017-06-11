package hevs.aislab.magpie.watch.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;

/**
 * Created by teuft on 30.05.2017.
 * This will be the main page were we will list all rules
 */

//initial fragment that display user states
public class FragmentHome extends Fragment {

    private TextView txtViewPulse;
    private TextView txtViewGlucose;
    private ImageView imgSeverity_glucose;
    private ImageButton imgSeverity_pulse;
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
        imgSeverity_glucose=(ImageView)view.findViewById(R.id.img_severity_glucose);
        imgSeverity_pulse=(ImageButton) view.findViewById(R.id.img_severity_pulse);



        //retrive data from the bundle and set it to texte view
        Bundle bundle=this.getArguments();
        String value=bundle.getString("glucoseValue","");
        if (!value.equals(""))
            txtViewGlucose.setText(value);
        return view;
    }

    public void setPulseValue(String value)
    {
        if (txtViewPulse!=null)
        txtViewPulse.setText(value);
    }

    public void setGlucoseValue(String value)
    {
        if (txtViewGlucose!=null)
        txtViewGlucose.setText(value);
    }

    public void setSeverity(String category, int severity)
    {
        //TODO IF ELSE FOR THE SEVERITY
        Drawable img=null;
        img = getDrawable(category,severity);
        if (category.equals("pulse"))
        {
            if (imgSeverity_pulse==null)
                return;
            imgSeverity_pulse.setImageDrawable(img);
            return ;
        }

        if (category.equals("glucose"))
        {

        }
    }

    private Drawable getDrawable(String category,int severity) {
        //pulse_lv3
        Log.d("RessourceName",category+"_lv"+severity);
        Context context = view.getContext();
        //Get the image with the named based on category and based on name of the category
        int id = context.getResources().getIdentifier(category+"_lv"+severity, "drawable", context.getPackageName());
        Drawable img=ContextCompat.getDrawable(getContext(), id);
        return img;
    }
}
