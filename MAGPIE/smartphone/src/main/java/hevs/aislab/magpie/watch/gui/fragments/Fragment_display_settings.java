package hevs.aislab.magpie.watch.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.ButtonsManager;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;
import hevs.aislab.magpie.watch_library.lib.NumberFormater;
import hevs.aislab.magpie.watch_library.lib.Validator;

/**
 * Created by teuft on 07.07.2017.
 */

public class Fragment_display_settings extends Fragment {



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

    String category_fragment;
    //listener class
    CustomRules currentRules;

    /**
     * This class is used to display other category
     */
    private class ListenerButtonCategory implements View.OnClickListener
    {
        String currentCategory;

        public ListenerButtonCategory(String currentCategory)
        {
            this.currentCategory=currentCategory;
            category_fragment=currentCategory;
        }

        @Override
        public void onClick(View view) {

            //we retrieve the rules from the database
            currentRules= RulesRepository.getInstance().getByCategory(currentCategory);

            String constraint_1=currentRules.getConstraint_1();
            String constraint_2=currentRules.getConstraint_2();
            String constraint_3=currentRules.getConstraint_3();

            constraint_1=constraint_1!=null ? constraint_1 : "";
            constraint_2=constraint_2!=null ? constraint_2 : "";
            constraint_3=constraint_3!=null ? constraint_3 : "";

            String value1_min=currentRules.getVal_1_min()!= null ?currentRules.getVal_1_min()+"" :"";
            String value1_max=currentRules.getVal_1_max()!= null ?currentRules.getVal_1_max()+"" :"";
            String value2_min=currentRules.getVal__2_min()!= null ?currentRules.getVal__2_min()+"" :"";
            String value_2max=currentRules.getVal_2_max()!= null ?currentRules.getVal_2_max()+"" :"";

            //format the value based on the category

            if (currentRules.getCategory().equals(Const.CATEGORY_GLUCOSE) || currentRules.getCategory().equals(Const.CATEGORY_WEIGHT))
            {
                value1_min= NumberFormater.getInstance().formatWith1Digit(value1_min);
                value1_max=NumberFormater.getInstance().formatWith1Digit(value1_max);
                value2_min=NumberFormater.getInstance().formatWith1Digit(value2_min);
                value_2max=NumberFormater.getInstance().formatWith1Digit(value_2max);
            }
            else
            {
                value1_min=NumberFormater.getInstance().formatWithNoDigit(value1_min);
                value1_max=NumberFormater.getInstance().formatWithNoDigit(value1_max);
                value2_min=NumberFormater.getInstance().formatWithNoDigit(value2_min);
                value_2max=NumberFormater.getInstance().formatWithNoDigit(value_2max);
            }

            //set the constraints
            constraint_1=formatConstraints(constraint_1,currentRules);
            constraint_2=formatConstraints(constraint_2,currentRules);
            constraint_3=formatConstraints(constraint_3,currentRules);

            //display constraint in text view
            txtConstraint1.setText(constraint_1);
            txtConstraint2.setText(constraint_2);
            txtConstraint3.setText(constraint_3);

            //set the values based on the category
            edit_value1min.setText(value1_min);
            edit_value1Max.setText(value1_max);
            edit_value2Min.setText(value2_min);
            edit_value2Max.setText(value_2max);

            //disable the text view with no value (user can't add a value
            String mergedRules=currentRules.getConstraint_1()+currentRules.getConstraint_2()+currentRules.getConstraint_3();

            //by default, we disable the value editing of all fields
            hideEditText(edit_value1min,edit_value1Max,edit_value2Min,edit_value2Max);
            hideButtonSave();
            //and we enable the value editing only for the pulse and step category
            if (currentCategory.equals(Const.CATEGORY_PULSE) || currentCategory.equals(Const.CATEGORY_STEP))
            {
                enableValuesEditingIfExistInRules(mergedRules);
                displayButtonSave();
            }


            //***********************USED TO CHANGE THE DISPLAY (RED AND GRREN BUTTON)
            //Create map with all the imageButton



            buttonsManager.setAllButtonToGreen();
            buttonsManager.setButtonToRed(currentCategory);
        }
    }



    //************************************LISTNER FOR EDITTEXT****************************

    /**
     * This internal class will manage the interaction of the user with the edit text. It will upload the display of the rules after modification,
     * allow the value (min and max)
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
            //check the value enter by the user. IF the value is out of the range, we change it

            if (currentEditText==null)
                return;
            //check if the value is correct

            updateDisplayOfConstraints();

        }

        private void updateDisplayOfConstraints() {
            //update the display of the constraints, only if the constraint exist (not null)
            if (currentRules.getConstraint_1()!=null)
            { //if the constraint don't contain the number we just change, we don't change anythings
                if (isRulesContainsDesignation(currentRules.getConstraint_1(),valueCode))
                { //replace the value in the constraints by the number we just change
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



    View view;
    ButtonsManager buttonsManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_settings, container, false);

        buttonsManager=new ButtonsManager(getContext());
        initView();
        addListenerToButton();
        setListenerOnButtonSaveValue();
        addListenerToEditText();



        return view;
    }
    /**
     * this methode will assign the lister to the save button (save value in the db)
     */
    private void setListenerOnButtonSaveValue() {
        buttonSaveValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //Get all the fields
                String value1_min=edit_value1min.getText().toString();
                String value1_max=edit_value1Max.getText().toString();
                String value2_min=edit_value2Min.getText().toString();
                String value2_max=edit_value2Max.getText().toString();





                //we insertOrUpdate only the values from the editText that are visible and activated
                if ( edit_value1min.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value1_min) || TextUtils.isEmpty(value1_min) || !Validator.isRuleValueValide(category_fragment,Double.parseDouble(value1_min)))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    //Check if the values of the user are not valide (out of a certain range)
                    currentRules.setVal_1_min(Double.parseDouble(value1_min));
                }

                if ( edit_value1Max.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value1_max) || TextUtils.isEmpty(value1_max) ||  !Validator.isRuleValueValide(category_fragment,Double.parseDouble(value1_max)))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal_1_max(Double.parseDouble(value1_max));
                }

                if (edit_value2Min.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value2_min) || TextUtils.isEmpty(value2_min) ||  !Validator.isRuleValueValide(category_fragment,Double.parseDouble(value2_min)))
                    {
                        //check on the value

                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal__2_min(Double.parseDouble(value2_min));
                }

                if (edit_value2Max.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value2_max) || TextUtils.isEmpty(value2_max) ||  !Validator.isRuleValueValide(category_fragment,Double.parseDouble(value2_max)))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal_2_max(Double.parseDouble(value2_max));
                }


                //now we update the rules in the database


                //TODO SEND THE RULES TO THE WATCH
                RulesRepository.getInstance().insertOrUpdate(currentRules);
                CustomToast.getInstance().confirmToast(getContext().getString(R.string.unit_measure_diastol),getActivity());
                //change the display of the rule in the home fragment

            }
        });
    }


    public void initView()
    {
        buttonsManager.addButtonByCategory(Const.CATEGORY_GLUCOSE,(ImageButton)view.findViewById(R.id.button_glucose));
        buttonsManager.addButtonByCategory(Const.CATEGORY_PULSE,(ImageButton)view.findViewById(R.id.button_pulse));
        buttonsManager.addButtonByCategory(Const.CATEGORY_PRESSURE,(ImageButton)view.findViewById(R.id.button_pressure));
        buttonsManager.addButtonByCategory(Const.CATEGORY_WEIGHT,(ImageButton)view.findViewById(R.id.button_weight));
        buttonsManager.addButtonByCategory(Const.CATEGORY_STEP,(ImageButton)view.findViewById(R.id.button_step));

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
    }
    private void addListenerToButton()
    {
        buttonsManager.getButtonByCategory(Const.CATEGORY_GLUCOSE).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_GLUCOSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PULSE).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_PULSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PRESSURE).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_PRESSURE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_WEIGHT).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_WEIGHT));
        buttonsManager.getButtonByCategory(Const.CATEGORY_STEP).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_STEP));
    }

    private void addListenerToEditText()
    {
        //add listener to edit Text
        edit_value1min.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_1Min,edit_value1min));
        edit_value1Max.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_1Max,edit_value1Max));
        edit_value2Min.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_2Min,edit_value2Min));
        edit_value2Max.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_2Max,edit_value2Max));
    }

    private String formatConstraints(String constraints, CustomRules rule)
    {

        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_1Min,rule.getVal_1_min()+"");
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_1Max,rule.getVal_1_max()+"");
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_2Min,rule.getVal__2_min()+"");
        constraints=replaceValueByNumber(constraints,Const.VALUE_Value_2Max,rule.getVal_2_max()+"");
        return constraints;
    }

    private String replaceValueByNumber(String rules, String desingation, String value )
    {
        return rules.replace(desingation,value);
    }
    private boolean isRulesContainsDesignation(String rules, String designation)
    {
        return rules.contains(designation);

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



