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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ch.hevs.aislab.magpie.support.Rule;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.libs.Lib;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;

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

        initViews();
        HashMap<String, Measure>measureList=MeasuresRepository.getInstance().getLastMeasure();
        displayLastMeasure(measureList);
        setBarLevelArea();


        return view;
    }

    /**
     * This method will create the view object from XLM to Java and instanciate objects. Used when the user reboots it's device
     *
     */
    private void initViews() {
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
    }

    /**
     * This methode will display the last measure for all category. Used when the user reboot it's device
     * @param measureList
     */
    private void displayLastMeasure(HashMap<String, Measure> measureList) {
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

        //the step work differently. We won't take data directly from the data base. We will check if it's a new day. If it's, we take 0 as value
        //TODO IMPLEMENT THE NEW DAY CHECK
        //get the step
       double currentStep= PrefAccessor.getInstance().getLong(getContext(),Const.KEY_CURRENT_STEP);
        setStepValue(currentStep);

    }

    /**
     * This method will set the area (green and red) on each bar level. Used when the user reboot the system
     */
    private void setBarLevelArea() {
        //get all the rules
        Map<String, CustomRules> rulesMap= RulesRepository.getInstance().getAllRules();
        //set all the progress bar depending on the rules

        ajustBarLevel(rulesMap.get(Const.CATEGORY_GLUCOSE),Const.CATEGORY_GLUCOSE);
        ajustBarLevel(rulesMap.get(Const.CATEGORY_WEIGHT),Const.CATEGORY_WEIGHT);
        ajustBarLevel(rulesMap.get(Const.CATEGORY_PULSE),Const.CATEGORY_PULSE);
        ajustBarLevel(rulesMap.get(Const.CATEGORY_PRESSURE),Const.CATEGORY_SYSTOL);
        ajustBarLevel(rulesMap.get(Const.CATEGORY_PRESSURE),Const.CATEGORY_DIASTOL);
        ajustBarLevel(rulesMap.get(Const.CATEGORY_STEP),Const.CATEGORY_STEP);
    }


    public void setGlucoseValue(Double value)
    {
        if (txtViewGlucose!=null)
            txtViewGlucose.setText(Lib.getInstance().formatWith1Digit(value));
        if (barGlucose!=null)
            setCursor(Const.CATEGORY_GLUCOSE, value.floatValue());
    }

    public void setSystolValue(Double value)
    {
        if (txtViewDiastol!=null)
            txtViewSystol.setText(Lib.getInstance().formatWithNoDigit(value));
        if (barSystol!=null)
            setCursor(Const.CATEGORY_SYSTOL,value.floatValue());
    }

    public void setDiastolValue(Double value)
    {
        if (txtViewDiastol!=null)
            txtViewDiastol.setText(Lib.getInstance().formatWithNoDigit(value));
        if (barDiastol!=null)
            setCursor(Const.CATEGORY_DIASTOL,value.floatValue());
    }

    public void setStepValue(Double value)
    {
        if (txtViewSteps!=null)
            txtViewSteps.setText(Lib.getInstance().formatWithNoDigit(value));
        if (barStep!=null)
            setCursor(Const.CATEGORY_STEP, value.floatValue());
    }

    public void setPulseValue(Double value)
    {
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
        Drawable img;
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

        return ContextCompat.getDrawable(getContext(), id);
    }

    //set the cursore to display the current level


    /**
     * Set the current level of measure on the bar level. The max and minumum value are fixed and cannot be changed. however, the red and green
     * value can be changed when rules are changed
     * @param category
     * @param value
     */
    private void setCursor(String category, float ... value)
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
    public void ajustBarLevel( CustomRules rule, String category)
    {

        /*
        the display of the area inside the layout  is the followings. we will specify the weight (space that take) for each layout

        *****************
        *     area 3  RED  *
        *****************
        *****************
        *     area 2 GREEN   *
        *****************
        *****************
        *     area 1  RED  *
        *****************
         */


        //define the layout
        LinearLayout area1=null;
        LinearLayout area2=null;
        LinearLayout area3=null;

        float weightArea1=0F;
        float weightArea2=0F;
        float weightArea3=0F;

        switch (category)
        {
            //get the value and the layout based on the category
            case Const.CATEGORY_WEIGHT :
                //Get the 3 area
                 area1=(LinearLayout) barWeight.findViewById(R.id.bar_level_area1);
                 area2=(LinearLayout) barWeight.findViewById(R.id.bar_level_area2);
                 area3=(LinearLayout) barWeight.findViewById(R.id.bar_level_area3);

                //now we get the number based on the rules. The value are in % (ex 98 or 101)
                float minVariationValue=100-rule.getVal__2_min().floatValue();
                float maxValueVariationValue=rule.getVal_2_max().floatValue()-100;

                //the area 2 correspond to the "good" zone (green)
                weightArea2=maxValueVariationValue+minVariationValue;
                //are 1 correspond to a important weight loss
                weightArea1=this.maxWeightVariation- minVariationValue;
                //area 3 correspond to an important gain of weight
                weightArea3=this.maxWeightVariation-maxValueVariationValue;
                break;

            case Const.CATEGORY_GLUCOSE :
                area1=(LinearLayout) barGlucose.findViewById(R.id.bar_level_area1);
                area2=(LinearLayout) barGlucose.findViewById(R.id.bar_level_area2);
                area3=(LinearLayout) barGlucose.findViewById(R.id.bar_level_area3);

                float minValueGlucose=rule.getVal_1_min().floatValue();
                float maxValueGlucose=rule.getVal_2_max().floatValue();

                weightArea1=minValueGlucose;
                weightArea2=maxValueGlucose-minValueGlucose;
                weightArea3=this.maxGlucose-maxValueGlucose;
                break;

            case Const.CATEGORY_PULSE :
                area1=(LinearLayout) barPulse.findViewById(R.id.bar_level_area1);
                area2=(LinearLayout) barPulse.findViewById(R.id.bar_level_area2);
                area3=(LinearLayout) barPulse.findViewById(R.id.bar_level_area3);

                float minValuePulse=rule.getVal_1_min().floatValue();
                float maxValuePulse=rule.getVal_1_max().floatValue();

                weightArea1=minValuePulse;
                weightArea2=maxValuePulse-minValuePulse;
                weightArea3=this.maxPulse-maxValuePulse;

                Log.d("weightArea__1",weightArea1+"");
                Log.d("weightArea__2",weightArea2+"");
                Log.d("weightArea__3",weightArea3+"");
                break;

            case Const.CATEGORY_SYSTOL :
                area1=(LinearLayout) barSystol.findViewById(R.id.bar_level_area1);
                area2=(LinearLayout) barSystol.findViewById(R.id.bar_level_area2);
                area3=(LinearLayout) barSystol.findViewById(R.id.bar_level_area3);
                //this category will contains only 2 area: low is green (good) high is bad (red)
                weightArea1=0F;
                weightArea2=rule.getVal_1_max().floatValue();
                weightArea3=this.maxSystol-rule.getVal_1_max().floatValue();
                Log.d("PassageDansSystolCase","passage");
                break;

            case Const.CATEGORY_DIASTOL :
                area1=(LinearLayout) barDiastol.findViewById(R.id.bar_level_area1);
                area2=(LinearLayout) barDiastol.findViewById(R.id.bar_level_area2);
                area3=(LinearLayout) barDiastol.findViewById(R.id.bar_level_area3);
                //same principe as diastol
                //this category will contains only 2 area: low is green (good) high is bad (red)
                weightArea1=0F;
                weightArea2=rule.getVal_2_max().floatValue();
                weightArea3=this.maxDiastol-rule.getVal_2_max().floatValue();
                Log.d("PassageDansSystolCase","passage");
                break;
            case Const.CATEGORY_STEP :

                area1=(LinearLayout) barStep.findViewById(R.id.bar_level_area1);
                area2=(LinearLayout) barStep.findViewById(R.id.bar_level_area2);
                area3=(LinearLayout) barStep.findViewById(R.id.bar_level_area3);

                float minValueStep=rule.getVal_1_min().floatValue();
                float maxValueStep=rule.getVal_1_max().floatValue();

                weightArea1=minValueStep;
                weightArea2=maxValueStep-minValueStep;
                weightArea3=this.maxStep-maxValueStep;

                break;



           // default:
              //  return;
        }

        //we set the values
        ajustArea(area1,weightArea1);
        ajustArea(area2, weightArea2);
        ajustArea(area3,weightArea3);


    }
    private void ajustArea(LinearLayout area, float weight)
    {
        LinearLayout.LayoutParams param =new LinearLayout.LayoutParams(area.getLayoutParams());
        param.weight=weight;
        area.setLayoutParams(param);
    }



}
