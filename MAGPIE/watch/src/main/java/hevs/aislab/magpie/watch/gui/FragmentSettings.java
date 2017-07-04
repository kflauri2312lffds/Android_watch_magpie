package hevs.aislab.magpie.watch.gui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.repository.RulesRepository;

/**
 * Created by teuft on 04.06.2017.
 */

public class FragmentSettings extends Fragment {

    View view;

    //button


    Map<String, ImageButton> buttonMap=new HashMap<>();


    ImageButton buttonSaveValues;

    // textview of the constraints
    TextView txtConstraint1;
    TextView txtConstraint2;
    TextView txtConstraint3;

    //edit text of values
    EditText edit_value1min;
    EditText edit_value1Max;
    EditText edit_value2Min;
    EditText edit_value2Max;




    String currentCategory;
    //listener class
    CustomRules currentRules;
    //--------------------------LISTENER FOR BUTTONS, CHANGE THE CATEGORY DISPLAYED -----------------------------
    private class ListenerButton implements View.OnClickListener
    {
        String category;
        public ListenerButton (String category)
        {
            this.category=category;
        }

        @Override
        public void onClick(View view) {
            currentCategory=category;
            //we retrieve the rules from the database
            currentRules= RulesRepository.getInstance().getByCategory(category);
            //assign the values to the txtview

            String constraint_1=currentRules.getConstraint_1();
            String constraint_2=currentRules.getConstraint_2();
            String constraint_3=currentRules.getConstraint_3();

            constraint_1=constraint_1!=null ? constraint_1 : "-";
            constraint_2=constraint_2!=null ? constraint_2 : "-";
            constraint_3=constraint_3!=null ? constraint_3 : "-";

            String value1_min=currentRules.getVal_1_min()!= null ?currentRules.getVal_1_min()+"" :"";
            String value1_max=currentRules.getVal_1_max()!= null ?currentRules.getVal_1_max()+"" :"";
            String value2_min=currentRules.getVal__2_min()!= null ?currentRules.getVal__2_min()+"" :"";
            String value_2max=currentRules.getVal_2_max()!= null ?currentRules.getVal_2_max()+"" :"";

            //replace the value in the constraints by the values of the rules


            //set the values to the view element

            //set the constraints
            constraint_1=formatConstraints(constraint_1,currentRules);
            constraint_2=formatConstraints(constraint_2,currentRules);
            constraint_3=formatConstraints(constraint_3,currentRules);

            txtConstraint1.setText(constraint_1);
            txtConstraint2.setText(constraint_2);
            txtConstraint3.setText(constraint_3);

            //set the values
            edit_value1min.setText(value1_min);
            edit_value1Max.setText(value1_max);
            edit_value2Min.setText(value2_min);
            edit_value2Max.setText(value_2max);

            //disable the text view with no value

            String mergedRules=currentRules.getConstraint_1()+currentRules.getConstraint_2()+currentRules.getConstraint_3();

           //by default, we disable the value editing of all fields
            hideEditText(edit_value1min,edit_value1Max,edit_value2Min,edit_value2Max);
            hideButtonSave();

            if (category.equals(Const.CATEGORY_PULSE) || category.equals(Const.CATEGORY_STEP))
            {
                enableValuesEditingIfExistInRules(mergedRules);
                displayButtonSave();
            }


            //***********************USED TO CHANGE THE DISPLAY (RED AND GRREN BUTTON)
            //Create map with all the imageButton


            setButtonsToGreen(buttonMap);

            setButtonToRed(buttonMap.get(category));

        }






        /**
         * Set the color of the category displayed to green. It's mean the category is not displayed
         *
         */
        private void setButtonsToGreen(Map<String, ImageButton> imageButtonMap)
        {
            for (Map.Entry<String, ImageButton> entry : imageButtonMap.entrySet())
            {
                int id = getContext().getResources().getIdentifier(entry.getKey()+"_green", "drawable", getContext().getPackageName());
                Drawable img= ContextCompat.getDrawable(getContext(), id);
                entry.getValue().setImageDrawable(img);
            }
        }

        /**
         * Set the color of the category displayed to green. It's mean the category is displayed
         *
         */
        private void setButtonToRed(ImageButton imagebutton)
        {
            int id = getContext().getResources().getIdentifier(category+"_red", "drawable", getContext().getPackageName());
            Drawable img= ContextCompat.getDrawable(getContext(), id);
            imagebutton.setImageDrawable(img);
        }
    }

    //************************************LISTNER FOR EDITTEXT****************************

    /**
     * This internal class will manage the interaction of the user with the edit text. It will upload the display of the rules after modification,
     * allow the value (min and max) and save it in the database if the value is valide
     */
    class ListenerEditText implements TextWatcher
    {
        String valueCode;
        EditText currentEditText;

        public ListenerEditText (String valueCode, EditText currentEditText)
        {
            this.valueCode=valueCode;
            this.currentEditText=currentEditText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            //SET THE VALUE TO THE DISPLAY TEXT
            Log.d("pppp_passage","after text changed");
            if (currentEditText==null)
                return;
            //check if the value is correct

            updateDisplayOfConstraints();

        }

        private void updateDisplayOfConstraints() {
            if (currentRules.getConstraint_1()!=null)
            {
                if (isRulesContainsDesignation(currentRules.getConstraint_1(),valueCode))
                {
                   String constraint_1=replaceValueByNumber(currentRules.getConstraint_1(),valueCode,currentEditText.getText().toString());
                    txtConstraint1.setText(constraint_1);
                }
            }

            if (currentRules.getConstraint_2()!=null)
            {
                if (isRulesContainsDesignation(currentRules.getConstraint_2(),valueCode))
                {
                    String constraint_2=replaceValueByNumber(currentRules.getConstraint_2(),valueCode,currentEditText.getText().toString());
                    txtConstraint2.setText(constraint_2);
                }
            }

            if (currentRules.getConstraint_3()!=null)
            {
                if (isRulesContainsDesignation(currentRules.getConstraint_3(),valueCode))
                {
                    String constraint_3=replaceValueByNumber(currentRules.getConstraint_3(),valueCode,currentEditText.getText().toString());
                    txtConstraint3.setText(constraint_3);
                }
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_settings, container, false);

        //init the view
        initView();

        //add the listener to the button
        buttonMap.get(Const.CATEGORY_PULSE).setOnClickListener(new ListenerButton(Const.CATEGORY_PULSE));
        buttonMap.get(Const.CATEGORY_GLUCOSE).setOnClickListener(new ListenerButton(Const.CATEGORY_GLUCOSE));
        buttonMap.get(Const.CATEGORY_WEIGHT).setOnClickListener(new ListenerButton(Const.CATEGORY_WEIGHT));
        buttonMap.get(Const.CATEGORY_STEP).setOnClickListener(new ListenerButton(Const.CATEGORY_STEP));
        buttonMap.get(Const.CATEGORY_PRESSURE).setOnClickListener(new ListenerButton(Const.CATEGORY_PRESSURE));

        //add listener to edit Text
        edit_value1min.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_1Min,edit_value1min));
        edit_value1Max.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_1Max,edit_value1Max));
        edit_value2Min.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_2Min,edit_value2Min));
        edit_value2Max.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_2Max,edit_value2Max));

        return view;
    }

    private void initView() {
        ImageButton button_displayPulse=(ImageButton)view.findViewById(R.id.button_display_rules_pulse);
        ImageButton button_display_glucose=(ImageButton)view.findViewById(R.id.button_display_rules_glucose);
        ImageButton button_display_weight=(ImageButton)view.findViewById(R.id.button_display_rules_weight);
        ImageButton button_display_steps=(ImageButton)view.findViewById(R.id.button_display_rules_step);
        ImageButton   button_display_pressure=(ImageButton)view.findViewById(R.id.button_display_rules_pressure);

        buttonMap.put(Const.CATEGORY_PULSE,button_displayPulse);
        buttonMap.put(Const.CATEGORY_GLUCOSE,button_display_glucose);
        buttonMap.put(Const.CATEGORY_WEIGHT,button_display_weight);
        buttonMap.put(Const.CATEGORY_STEP,button_display_steps);
        buttonMap.put(Const.CATEGORY_PRESSURE,button_display_pressure);

        txtConstraint1 =(TextView)view.findViewById(R.id.txtView_constraint1);
        txtConstraint2 =(TextView)view.findViewById(R.id.txtView_constraint2);
        txtConstraint3 =(TextView)view.findViewById(R.id.txtView_constraint3);

        edit_value1min =(EditText)view.findViewById(R.id.edittxt_value1min);
        edit_value1Max =(EditText)view.findViewById(R.id.edittxt_value1max);
        edit_value2Min =(EditText)view.findViewById(R.id.edittxt_value2min);
        edit_value2Max =(EditText)view.findViewById(R.id.edittxt_value2max);


        //init the imageButton
        //add the listener for the button save
         buttonSaveValues =(ImageButton)view.findViewById(R.id.button_update_rules);
        buttonSaveValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //TODO CHECK IF THE ENTRIES ARE VALIDE

                //Get all the fields
                String value1_min=edit_value1min.getText().toString();
                String value1_max=edit_value1Max.getText().toString();
                String value2_min=edit_value2Min.getText().toString();
                String value2_max=edit_value2Max.getText().toString();

            //try catch to handle number format exceptino


                //we insert only the values from the editText that are visible and activated
                if ( edit_value1min.getVisibility()==View.VISIBLE)
                   {
                       if (!is_aValideNumber(value1_min) || TextUtils.isEmpty(value1_min))
                       {
                           CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                           return;
                       }

                       //Check if the values of the user are not valide (out of a certain range)



                       currentRules.setVal_1_min(Double.parseDouble(value1_min));
                   }


                if ( edit_value1Max.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value1_max) || TextUtils.isEmpty(value1_max) )
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal_1_max(Double.parseDouble(value1_max));
                }


                if (edit_value2Min.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value2_min) || TextUtils.isEmpty(value2_min))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal__2_min(Double.parseDouble(value2_min));
                }

                if (edit_value2Max.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value2_max) || TextUtils.isEmpty(value2_max))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal_2_max(Double.parseDouble(value2_max));
                }

                //now we update the rules in the database
                RulesRepository.getInstance().update(currentRules);

                CustomToast.getInstance().confirmToast(getContext().getString(R.string.change_saved),getActivity());

            }
        });

        //hide the current edit text
       hideEditText( edit_value1min, edit_value1Max,edit_value2Min,edit_value2Max);
        hideButtonSave();
    }
    private String formatConstraints(String constraints, CustomRules rule)
    {
        String bulletPoint="\u2022";
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_1Min,rule.getVal_1_min()+"");
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_1Max,rule.getVal_1_max()+"");
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_2Min,rule.getVal__2_min()+"");
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_2Max,rule.getVal_2_max()+"");
        return bulletPoint+constraints;
    }

    private String replaceValueByNumber(String rules, String desingation, String value )
    {

        return rules.replace(desingation,value);
    }
    private boolean isRulesContainsDesignation(String rules, String designation)
    {
        return rules.contains(designation);

    }

    /**
     * this method will enable the values eiditing. it will check if the values exist in the rules and activate the editText
     * @param mergedRules
     */
    private void enableValuesEditingIfExistInRules(String mergedRules)
    {

        if (mergedRules.contains(Const.VALUE_Value_1Min))
            showEditText( edit_value1min);

        if (mergedRules.contains(Const.VALUE_Value_1Max))
            showEditText( edit_value1Max);

        if (mergedRules.contains(Const.VALUE_Value_2Min))
            showEditText( edit_value2Min);

        if (mergedRules.contains(Const.VALUE_Value_2Max))
            showEditText( edit_value2Max);

    }
    private void showEditText(EditText ... editTexts)
    {
        for (EditText anEditText : editTexts)
        {
            anEditText.setEnabled(true);
            anEditText.setClickable(true);

            anEditText.setVisibility(View.VISIBLE);
        }
    }
    private void hideEditText(EditText ... editTexts)
    {
        for (EditText anEditText : editTexts)
        {
            anEditText.setEnabled(false);
            anEditText.setClickable(false);
            anEditText.setVisibility(View.GONE);
        }
    }
    private void hideButtonSave()
    {
        buttonSaveValues.setVisibility(View.INVISIBLE);
    }
    private void displayButtonSave()
    {
        buttonSaveValues.setVisibility(View.VISIBLE);
    }
    private boolean is_aValideNumber(String ... number)
    {
        try
        {
            for (String aNumber : number) {
                Double.parseDouble(aNumber);
            }
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }
}
