package hevs.aislab.magpie.watch.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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

import java.util.HashMap;
import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;
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

    private ConstraintLayout barPulse;

    private ImageView imgSeverity_glucose;
    private ImageButton imgSeverity_pulse;


    //min and max value foreach category
    double minPulse=0;
    float maxPulse=220;

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

        barPulse=(ConstraintLayout)view.findViewById(R.id.bar_pulse);


        imgSeverity_glucose=(ImageView)view.findViewById(R.id.img_severity_glucose);
        imgSeverity_pulse=(ImageButton) view.findViewById(R.id.img_severity_pulse);

        //define the format of integer and numeric number
        String format1Digit="%(,.1f";
        String formatNodigit="%(,.0f";


        //get all measure and display.
        HashMap<String, Measure>measureList=MeasuresRepository.getInstance().getLastMeasure();

         txtViewGlucose.setText( measureList.containsKey(Const.CATEGORY_GLUCOSE) ? String.format( format1Digit,measureList.get(Const.CATEGORY_GLUCOSE).getValue1()) : "/");
        txtViewSystol.setText(  measureList.containsKey(Const.CATEGORY_PRESSURE)?  String.format(formatNodigit, measureList.get(Const.CATEGORY_PRESSURE).getValue1()):"/");
        txtViewDiastol.setText( measureList.containsKey(Const.CATEGORY_PRESSURE) ? String.format(formatNodigit, measureList.get(Const.CATEGORY_PRESSURE).getValue2()):"/");
        txtViewPulse.setText( measureList.containsKey(Const.CATEGORY_PULSE) ?   String.format(formatNodigit, measureList.get(Const.CATEGORY_PULSE).getValue1()):"/");
        txtViewWeight.setText(measureList.containsKey(Const.CATEGORY_WEIGHT) ?   String.format(format1Digit,  measureList.get(Const.CATEGORY_WEIGHT).getValue1()) :"/");

        setBarLevel(Const.CATEGORY_PULSE, 90);
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

    //set the cursore to display the current level


    /**
     * Set the current level of measure on the bar level. The max and minumum value are fixed and cannot be changed. however, the red and green
     * value can be changed when rules are changed
     * @param category
     * @param value
     */
    public void setBarLevel(String category, float value)
    {
        if (category.equals(Const.CATEGORY_PULSE))
        {
            //format the value based on the max value of the pulse. And take the %

            value/=maxPulse;

            //because the order of the layout is inversed (from the top to the bottom and we want from the bottom to the top), we have to take the max and remove the value from the max
            value=1-value;
            setLevel(barPulse,value);
        }
    }

    private void setLevel(ConstraintLayout layout, float value) {
        //get the element we want to change the constraint
        ImageView cursor=(ImageView) layout.findViewById(R.id.cursor);
        //get the actual constraint
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        //change actual constraint
        set.setVerticalBias(cursor.getId(), value);
        // Apply the changes
        set.applyTo(layout); // this is my… (ConstraintLayout) findViewById(R.id.rootLayout);
    }


}
