package hevs.aislab.magpie.watch.gui;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private ImageView imgSeverity_glucose;
    private ImageView imgSeverity_pulse;
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
        imgSeverity_pulse=(ImageView)view.findViewById(R.id.img_severity_pulse);

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
    public void setSeverity(String category, int severity)
    {
        //TODO IF ELSE FOR THE SEVERITY
        Drawable img=null;
         img = getDrawable(severity, img);

        if (category.equals("pulse")) {
            imgSeverity_pulse.setImageDrawable(img);
            return;
        }

        if (category.equals("glucose"))
        {
            imgSeverity_glucose.setImageDrawable(img);
            return;
        }





    }

    private Drawable getDrawable(int severity, Drawable img) {
        switch (severity)
        {
            case 0 :
                img= ContextCompat.getDrawable(getContext(), R.drawable.alert_lv0);
                break;
            case 1 :
                img=ContextCompat.getDrawable(getContext(),R.drawable.alert_lv0);
                break;
            case 2:
                img=ContextCompat.getDrawable(getContext(),R.drawable.alert_lv2);
                break;
            case 3:
                img=ContextCompat.getDrawable(getContext(),R.drawable.alert_lv3);
                break;
            default:
                img= ContextCompat.getDrawable(getContext(), R.drawable.alert_lv0);
                break;
        }
        return img;
    }


}
