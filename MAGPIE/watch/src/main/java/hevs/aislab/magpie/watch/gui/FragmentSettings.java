package hevs.aislab.magpie.watch.gui;

import android.content.Context;
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
import java.util.Locale;
import java.util.Map;

import hevs.aislab.magpie.watch.IdialogToActivity;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Lib;

import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.gui.ButtonsManager;
import hevs.aislab.magpie.watch_library.lib.Const;
import hevs.aislab.magpie.watch_library.lib.NumberFormater;
import hevs.aislab.magpie.watch_library.lib.Validator;

/**
 * Created by teuft on 04.06.2017.
 */

public class FragmentSettings extends Fragment {

    View view;
    //button
    ButtonsManager buttonsManager;
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

    //used to have communication with the activity
    IdialogToActivity homeactivity;

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

            String  constraint_1=currentRules.getConstraint_1();
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

            txtConstraint1.setText(constraint_1);
            txtConstraint2.setText(constraint_2);
            txtConstraint3.setText(constraint_3);

            //set the values based on the category
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
            buttonsManager.setAllButtonToGreen();
            buttonsManager.setButtonToRed(category);
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

        buttonsManager=new ButtonsManager(getContext());
        //init the view
        initView();
        //add the listener to the button
        addListenerToButtonCategory();
        //add listener to edit Text
        addListenerToEditText();
        setListenerOnButtonSaveValue();
        //hide the current edit text
        hideEditText( edit_value1min, edit_value1Max,edit_value2Min,edit_value2Max);
        hideButtonSave();

        return view;
    }

    private void addListenerToEditText() {
        edit_value1min.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_1Min,edit_value1min));
        edit_value1Max.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_1Max,edit_value1Max));
        edit_value2Min.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_2Min,edit_value2Min));
        edit_value2Max.addTextChangedListener(new ListenerEditText(Const.VALUE_Value_2Max,edit_value2Max));
    }

    private void addListenerToButtonCategory() {
        buttonsManager.getButtonByCategory(Const.CATEGORY_PULSE).setOnClickListener(new ListenerButton(Const.CATEGORY_PULSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_GLUCOSE).setOnClickListener(new ListenerButton(Const.CATEGORY_GLUCOSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_WEIGHT).setOnClickListener(new ListenerButton(Const.CATEGORY_WEIGHT));
        buttonsManager.getButtonByCategory(Const.CATEGORY_STEP).setOnClickListener(new ListenerButton(Const.CATEGORY_STEP));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PRESSURE).setOnClickListener(new ListenerButton(Const.CATEGORY_PRESSURE));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            homeactivity = (IdialogToActivity) context;
        } catch (ClassCastException castException) {
        }
    }

    private void initView() {
        ImageButton button_displayPulse=(ImageButton)view.findViewById(R.id.button_display_rules_pulse);
        ImageButton button_display_glucose=(ImageButton)view.findViewById(R.id.button_display_rules_glucose);
        ImageButton button_display_weight=(ImageButton)view.findViewById(R.id.button_display_rules_weight);
        ImageButton button_display_steps=(ImageButton)view.findViewById(R.id.button_display_rules_step);
        ImageButton   button_display_pressure=(ImageButton)view.findViewById(R.id.button_display_rules_pressure);

        buttonsManager.addButtonByCategory(Const.CATEGORY_PULSE,button_displayPulse);
        buttonsManager.addButtonByCategory(Const.CATEGORY_GLUCOSE,button_display_glucose);
        buttonsManager.addButtonByCategory(Const.CATEGORY_WEIGHT,button_display_weight);
        buttonsManager.addButtonByCategory(Const.CATEGORY_STEP,button_display_steps);
        buttonsManager.addButtonByCategory(Const.CATEGORY_PRESSURE,button_display_pressure);

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

                //we insert only the values from the editText that are visible and activated
                if ( edit_value1min.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value1_min) || TextUtils.isEmpty(value1_min) || !Validator.isRuleValueValide(currentCategory,Double.parseDouble(value1_min)))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    //Check if the values of the user are not valide (out of a certain range)
                    currentRules.setVal_1_min(Double.parseDouble(value1_min));
                }

                if ( edit_value1Max.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value1_max) || TextUtils.isEmpty(value1_max) ||  !Validator.isRuleValueValide(currentCategory,Double.parseDouble(value1_max)))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal_1_max(Double.parseDouble(value1_max));
                }

                if (edit_value2Min.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value2_min) || TextUtils.isEmpty(value2_min) ||  !Validator.isRuleValueValide(currentCategory,Double.parseDouble(value2_min)))
                    {
                        //check on the value

                            CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                            return;
                    }
                    currentRules.setVal__2_min(Double.parseDouble(value2_min));
                }

                if (edit_value2Max.getVisibility()==View.VISIBLE)
                {
                    if (!is_aValideNumber(value2_max) || TextUtils.isEmpty(value2_max) ||  !Validator.isRuleValueValide(currentCategory,Double.parseDouble(value2_max)))
                    {
                        CustomToast.getInstance().errorTOast("incorrect value",getActivity());
                        return;
                    }
                    currentRules.setVal_2_max(Double.parseDouble(value2_max));
                }

                //now we update the rules in the database
                RulesRepository.getInstance().update(currentRules);
                CustomToast.getInstance().confirmToast(getContext().getString(R.string.change_saved),getActivity());
                //change the display of the rule in the home fragment
                homeactivity.updateBarArea(currentRules);

            }
        });
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
