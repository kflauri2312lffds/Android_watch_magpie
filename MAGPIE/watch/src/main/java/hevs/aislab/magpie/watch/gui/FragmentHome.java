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

import org.w3c.dom.Text;

import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;

/**
 * Created by teuft on 30.05.2017.
 * This will be the main page were we will list all rules
 */

//initial fragment that display user states
public class FragmentHome extends Fragment {

    private TextView txtViewPulse;
    private TextView txtViewGlucose;
    private TextView txtViewSystol;
    private TextView txtViewDiastol;
    private TextView txtViewSteps;
    private TextView txtViewWeight;

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
        txtViewSystol=(TextView)view.findViewById(R.id.txtView_systolValue);
        txtViewDiastol=(TextView)view.findViewById(R.id.txtView_DiastolValue);
        txtViewSteps=(TextView)view.findViewById(R.id.txtView_stepValue);
        txtViewWeight=(TextView)view.findViewById(R.id.txtView_weightValue);



        imgSeverity_glucose=(ImageView)view.findViewById(R.id.img_severity_glucose);
        imgSeverity_pulse=(ImageButton) view.findViewById(R.id.img_severity_pulse);

        //define the format of integer and numeric number
        String format1Digit="%(,.1f";
        String formatNodigit="%(,.0f";


        //get all measure and display. Measure are sorted alphabeticaly by category
        List<Measure>measureList=MeasuresRepository.getInstance().getLastMeasure();
         txtViewGlucose.setText( measureList.size()>=1 ? String.format( format1Digit,measureList.get(0).getValue1()) : "/");
        txtViewSystol.setText( measureList.size()>=2 ?  String.format(formatNodigit, measureList.get(1).getValue1()):"/");
        txtViewDiastol.setText( measureList.size()>=2 ? String.format(formatNodigit, measureList.get(1).getValue2()):"/");
        txtViewPulse.setText( measureList.size()>=3 ?   String.format(formatNodigit, measureList.get(2).getValue1()):"/");
        txtViewWeight.setText(measureList.size()>=4 ?   String.format(format1Digit,  measureList.get(3).getValue1()) :"/");
 


;


        return view;
    }

    public void setGlucoseValue(String value)
    {
        if (txtViewGlucose!=null)
            txtViewGlucose.setText(value);
    }

    public void setSystolValue(String value)
    {
        if (txtViewDiastol!=null)
            txtViewSystol.setText(value);
    }

    public void setDiastolValue(String value)
    {
        if (txtViewDiastol!=null)
            txtViewDiastol.setText(value);
    }

    public void setStepValue(String value)
    {
        if (txtViewSteps!=null)
            txtViewSteps.setText(value);
    }

    public void setPulseValue(String value)
    {
        if (txtViewPulse!=null)
        txtViewPulse.setText(value);
    }
    public void setWeightValue(String value)
    {
        if (txtViewWeight!=null)
            txtViewWeight.setText(value);
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

        Log.d("RessourceName",category+"_lv"+severity);
        Context context = view.getContext();
        //Get the image with the named based on category and based on name of the category
        int id = context.getResources().getIdentifier(category+"_lv"+severity, "drawable", context.getPackageName());
        Drawable img=ContextCompat.getDrawable(getContext(), id);
        return img;
    }
    //set the values of the GUI with the last values in the databse. each time the framgnet is created
    private void setGuiValues()
    {

    }

}
