package hevs.aislab.magpie.watch.gui;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch.shared_pref.PrefAccessor;
import hevs.aislab.magpie.watch_library.lib.Const;
import hevs.aislab.magpie.watch_library.lib.NumberFormater;

/**
 *
 * In this page, we will display the current status of all rules and the user will have possiblity to add a value
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

    //image button to set the severity

    private ImageButton imgGlucose;
    private ImageButton imgStep;
    private ImageButton imgWeight;
    private ImageButton imgPulse;
    private ImageButton imgPressure;




    //min and max value foreach category
    float maxPulse=220;
    float maxGlucose=20;
    //max weight is a variation (10 % higher, 10% lower)
    float maxWeightVariation =10;
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

        //set the max variation


//        CustomRules
        //now we get the number based on the rules. The value are in % (ex 98 or 101)





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

        imgGlucose=(ImageButton) view.findViewById(R.id.img_severity_glucose);
        imgPulse=(ImageButton) view.findViewById(R.id.img_severity_pulse);
        imgPressure=(ImageButton)view.findViewById(R.id.img_severity_pressure);
        imgStep=(ImageButton)view.findViewById(R.id.img_severity_step);
        imgWeight=(ImageButton)view.findViewById(R.id.img_severity_weight);
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
            setDiastolValue(measureList.get(Const.CATEGORY_PRESSURE).getValue2());
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
            txtViewGlucose.setText(NumberFormater.getInstance().formatWith1Digit(value));
        if (barGlucose!=null)
            setCursor(Const.CATEGORY_GLUCOSE, value.floatValue());

        setIconColor(Const.CATEGORY_GLUCOSE,value);
    }

    public void setSystolValue(Double value)
    {
        if (txtViewDiastol!=null)
            txtViewSystol.setText(NumberFormater.getInstance().formatWithNoDigit(value));
        if (barSystol!=null)
            setCursor(Const.CATEGORY_SYSTOL,value.floatValue());

        setIconColor(Const.CATEGORY_SYSTOL,value);
    }

    public void setDiastolValue(Double value)
    {
        if (txtViewDiastol!=null)
            txtViewDiastol.setText(NumberFormater.getInstance().formatWithNoDigit(value));
        if (barDiastol!=null)
            setCursor(Const.CATEGORY_DIASTOL,value.floatValue());

        setIconColor(Const.CATEGORY_DIASTOL,value);

    }

    public void setStepValue(Double value)
    {
        if (txtViewSteps!=null)
            txtViewSteps.setText(NumberFormater.getInstance().formatWithNoDigit(value));
        if (barStep!=null)
            setCursor(Const.CATEGORY_STEP, value.floatValue());

        setIconColor(Const.CATEGORY_STEP,value);
    }

    public void setPulseValue(Double value)
    {
        if (txtViewPulse!=null)
        txtViewPulse.setText(NumberFormater.getInstance().formatWithNoDigit(value));
        if (barPulse!=null)
            setCursor(Const.CATEGORY_PULSE, value.floatValue());

        setIconColor(Const.CATEGORY_PULSE,value);
    }
    public void setWeightValue(Double value, Double variation)
    {


        if (txtViewWeight!=null)
            txtViewWeight.setText(NumberFormater.getInstance().formatWith1Digit(value));

        if (barWeight!=null)
            setCursor(Const.CATEGORY_WEIGHT, value.floatValue(),variation.floatValue());

            setIconColor(Const.CATEGORY_WEIGHT,variation);
    }


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
                    //this value will be formated with the variation only. This is why, it will take the second value in the array (valuie[1])
                    double cursorPosition = formatValueForWeightBar(value[1]);
                setLevel(barWeight,(float)cursorPosition);
                break;

        }
    }

    private double formatValueForWeightBar(float value) {
        //set the max value and the min value of the weight



        double variation= value;
        double variationWithMaxWeight=variation*maxWeightVariation/2;
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
                float maxVariationValue=rule.getVal_2_max().floatValue()-100;
                float goodValue=minVariationValue+maxVariationValue;

                //the area 2 correspond to the "good" zone (green)
                weightArea2=goodValue;
                //are 1 correspond to a important weight loss
                weightArea3=this.maxWeightVariation-(goodValue+maxVariationValue);
                //area 3 correspond to an important gain of weight
                weightArea1=this.maxWeightVariation-(goodValue+minVariationValue);
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
                break;

            case Const.CATEGORY_SYSTOL :
                area1=(LinearLayout) barSystol.findViewById(R.id.bar_level_area1);
                area2=(LinearLayout) barSystol.findViewById(R.id.bar_level_area2);
                area3=(LinearLayout) barSystol.findViewById(R.id.bar_level_area3);
                //this category will contains only 2 area: low is green (good) high is bad (red)
                weightArea1=0F;
                weightArea2=rule.getVal_1_max().floatValue();
                weightArea3=this.maxSystol-rule.getVal_1_max().floatValue();
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

    /**
     * This method will change the color of the icon based on the value and the rule
     */
    private void setIconColor(String category, double value)
    {
        String cateSearch=category;
        if (category.equals(Const.CATEGORY_SYSTOL) || category.equals(Const.CATEGORY_DIASTOL))
            cateSearch=Const.CATEGORY_PRESSURE;

        CustomRules rule=RulesRepository.getInstance().getByCategory(cateSearch);
        boolean isSever=false;
        switch (category)
        {
            case Const.CATEGORY_WEIGHT :
                //val 2 min and val2 max
                //get the max increase of weight allowed
                Double increaseMaxVariation=(rule.getVal_2_max()-100)/100;
                Double minWeightLoosVariation=-((100-rule.getVal__2_min())/100);

                if (value>increaseMaxVariation ||value<minWeightLoosVariation)
                    isSever=true;
                    changeIconColor(imgWeight,Const.CATEGORY_WEIGHT,isSever);

                break;
            case Const.CATEGORY_PULSE:

                if (value<rule.getVal_1_min() ||value>rule.getVal_1_max())
                    isSever=true;
                changeIconColor(imgPulse,Const.CATEGORY_PULSE,isSever);

                break;
            case Const.CATEGORY_GLUCOSE :
                //val_1_min val_2_max
                if (value<rule.getVal_1_min() ||value>rule.getVal_2_max())
                    isSever=true;
                changeIconColor(imgGlucose,Const.CATEGORY_GLUCOSE,isSever);

                break;
            case Const.CATEGORY_STEP :

                if (value<rule.getVal_1_min() ||value>rule.getVal_1_max())
                    isSever=true;
                changeIconColor(imgStep,Const.CATEGORY_STEP,isSever);
                break;
            case Const.CATEGORY_SYSTOL :
                if (value>rule.getVal_1_max())
                    isSever=true;
                changeIconColor(imgPressure,Const.CATEGORY_PRESSURE,isSever);

                break;
            case Const.CATEGORY_DIASTOL :

                if (value>rule.getVal_2_max())
                    isSever=true;
                changeIconColor(imgPressure,Const.CATEGORY_PRESSURE,isSever);
                break;

        }
    }
    private void changeIconColor(ImageButton imagebutton, String category, boolean isSever)
    {
        if (imagebutton==null)
            return;

        String ressourceName=category;

        if (isSever)
            ressourceName+="_red";
        else
            ressourceName+="_green";

        imagebutton.setImageDrawable(getDrawable(ressourceName));

    }

    private Drawable getDrawable(String ressourceName) {

        Context context = getContext();
        //Get the image with the named based on category and based on name of the category
        int id = context.getResources().getIdentifier(ressourceName, "drawable", context.getPackageName());
        Drawable img= ContextCompat.getDrawable(getContext(), id);
        return img;
    }

}
