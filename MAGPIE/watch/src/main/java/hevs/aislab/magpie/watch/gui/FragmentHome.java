package hevs.aislab.magpie.watch.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import java.util.HashMap;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.libs.Lib;
import hevs.aislab.magpie.watch.models.CustomRules;
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
    private ConstraintLayout barGlucose;
    private ConstraintLayout barWeight;
    private ConstraintLayout barStep;
    private ConstraintLayout barSystol;
    private ConstraintLayout barDiastol;


    private ImageView imgSeverity_glucose;
    private ImageButton imgSeverity_pulse;


    //min and max value foreach category
    float maxPulse=220;
    float maxGlucose=20;
    //max weight is a variation (10 % higher, 10% lower)
    float maxWeightVariation =5;
    float maxStep=20000;
    float maxSystol=200;
    float maxDiastol=150;

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

        //create the bar object
        barPulse=(ConstraintLayout)view.findViewById(R.id.bar_pulse);
        barGlucose=(ConstraintLayout)view.findViewById(R.id.bar_glucose);
        barSystol=(ConstraintLayout)view.findViewById(R.id.bar_sys);
        barDiastol=(ConstraintLayout)view.findViewById(R.id.bar_dias);
        barStep=(ConstraintLayout)view.findViewById(R.id.bar_step);
        barWeight=(ConstraintLayout)view.findViewById(R.id.bar_weight);


        imgSeverity_glucose=(ImageView)view.findViewById(R.id.img_severity_glucose);
        imgSeverity_pulse=(ImageButton) view.findViewById(R.id.img_severity_pulse);

        //define the format of integer and numeric number
        String format1Digit="%(,.1f";
        String formatNodigit="%(,.0f";


        //get all measure and display.
        HashMap<String, Measure>measureList=MeasuresRepository.getInstance().getLastMeasure();


        //set all values and display it
        if (measureList.containsKey(Const.CATEGORY_GLUCOSE))
        {
            setGlucoseValue(measureList.get(Const.CATEGORY_GLUCOSE).getValue1());
        }
        if (measureList.containsKey(Const.CATEGORY_PRESSURE))
        {
            setSystolValue(measureList.get(Const.CATEGORY_PRESSURE).getValue1());
            setDiastolValue(measureList.get(Const.CATEGORY_PRESSURE).getValue1());
        }
        if (measureList.containsKey(Const.CATEGORY_PULSE))
        {
            setPulseValue(measureList.get(Const.CATEGORY_PULSE).getValue1());
        }
        if (measureList.containsKey(Const.CATEGORY_WEIGHT))
        {
            setWeightValue(measureList.get(Const.CATEGORY_WEIGHT).getValue1(),measureList.get(Const.CATEGORY_WEIGHT).getValue2());
        }

        return view;
    }

    public void setGlucoseValue(Double value)
    {
        if (value.equals("/"))
            return;
        if (txtViewGlucose!=null)
            txtViewGlucose.setText(Lib.getInstance().formatWith1Digit(value));
        if (barGlucose!=null)
            setCursor(Const.CATEGORY_GLUCOSE, value.floatValue());
    }

    public void setSystolValue(Double value)
    {
        if (value.equals("/"))
            return;
        if (txtViewDiastol!=null)
            txtViewSystol.setText(Lib.getInstance().formatWith1Digit(value));
        if (barSystol!=null)
            setCursor(Const.CATEGORY_SYSTOL,value.floatValue());
    }

    public void setDiastolValue(Double value)
    {
        if (value.equals("/"))
            return;
        if (txtViewDiastol!=null)
            txtViewDiastol.setText(Lib.getInstance().formatWith1Digit(value));
        if (barDiastol!=null)
            setCursor(Const.CATEGORY_DIASTOL,value.floatValue());
    }

    public void setStepValue(Double value)
    {
        if (value.equals("/"))
            return;
        if (txtViewSteps!=null)
            txtViewSteps.setText(Lib.getInstance().formatWith1Digit(value));
        if (barStep!=null)
            setCursor(Const.CATEGORY_STEP, value.floatValue());
    }

    public void setPulseValue(Double value)
    {
        //check if there is a value. if not, it will be a "/"
        if (value.equals("/"))
            return;

        if (txtViewPulse!=null)
        txtViewPulse.setText(Lib.getInstance().formatWith1Digit(value));
        if (barPulse!=null)
            setCursor(Const.CATEGORY_PULSE, value.floatValue());
    }
    public void setWeightValue(Double value, Double variation)
    {


        if (txtViewWeight!=null)
            txtViewWeight.setText(Lib.getInstance().formatWith1Digit(value));

        if (barWeight!=null)
            setCursor(Const.CATEGORY_WEIGHT, value.floatValue(),variation.floatValue());
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
    public void setCursor(String category, float ... value)
    {

        switch (category) {
            case Const.CATEGORY_PULSE:
                value[0] = formatValueForBarLevel(value[0],maxPulse);
                setLevel(barPulse, value[0]);
                break;

            case   Const.CATEGORY_GLUCOSE :
                value[0]=formatValueForBarLevel(value[0],maxGlucose);
                setLevel(barGlucose,value[0]);
                break;
            //for this one, we have the systol and diastol
            case  Const.CATEGORY_SYSTOL :
                //systol processing
                value[0]=formatValueForBarLevel(value[0],maxSystol);
                setLevel(barSystol,value[0]);
                //diastol processing
                break;
            case Const.CATEGORY_DIASTOL :
                value[0]=formatValueForBarLevel(value[0],maxDiastol);
                setLevel(barDiastol,value[0]);
                break;
            case Const.CATEGORY_STEP :
                value[0]=formatValueForBarLevel(value[0],maxStep);
                setLevel(barStep, value[0]);
                break;
                case Const.CATEGORY_WEIGHT :
                    //this value will be formated with the variation only
                    double cursorPosition = formatValueForWeightBar(value[1]);

                setLevel(barWeight,(float)cursorPosition);
                break;

        }
    }

    private double formatValueForWeightBar(float value) {
        //set the max value and the min value of the weight

        double variation= value;
        Log.d("vaal_debut",variation+"");
        double variationWithMaxWeight=variation*maxWeightVariation;
        double processedValue=1-(0.5+variationWithMaxWeight);

        //don't allow to exceed the range
        if (processedValue<0)
            return 0;
        if (processedValue>1)
            return 1;
        return processedValue;
    }

    /**
     *
     * Format the value in %.
     * @param value
     * @param maxValue
     * @return
     */
    private float formatValueForBarLevel(float value, float maxValue) {

        //format the value based on the max value of the pulse. And take the %
        value /= maxValue;
        //because the order of the layout is inversed (from the top to the bottom and we want from the bottom to the top), we have to take the max and remove the value from the max
        value = 1 - value;
        Log.d("displayValue",value+"---");
        //don't allow to exceed the range
        if (value<0)
            return 0;
        if (value>1)
            return 1;
        return value;
    }




    /**
     * this methode is used to set the level of the cursor (set the current statuts)
     * @param layout
     * @param value
     */
    private void setLevel(ConstraintLayout layout, float value) {
        //get the element we want to change the constraint
        ImageView cursor=(ImageView) layout.findViewById(R.id.cursor);
        //get the actual constraint
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        //change actual constraint
        set.setVerticalBias(cursor.getId(), value);
        // Apply the changes
        set.applyTo(layout);
        layout.refreshDrawableState();
    }

    /**
     * This methode is used to set the diffent bar level (the size of the green and red area into the bar level)
     * it will be used when we initialize the fragment and when we add a new value
     * @param rule
     */
    public void ajustBarLevel( CustomRules rule)
    {
        switch (rule.getCategory())
        {
            case Const.CATEGORY_WEIGHT :
                //Get the 3 area



                break;
        }

    }



}
