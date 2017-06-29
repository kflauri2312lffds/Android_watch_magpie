package hevs.aislab.magpie.watch.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.repository.RulesRepository;

/**
 * Created by teuft on 04.06.2017.
 */

public class FragmentSettings extends Fragment {

    View view;

    //button
    ImageButton button_displayPulse;
    ImageButton button_display_glucose;
    ImageButton button_display_weight;
    ImageButton button_display_steps;
    ImageButton button_display_pressure;

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
    //--------------------------LISTENER FOR BUTTONS-----------------------------
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



        }

    }

    //************************************LISTNER FOR EDITTEXT
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
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            //SET THE VALUE TO THE DISPLAY TEXT
            Log.d("passageDansMethodeChan","fdsafasdfds");
            if (currentEditText==null)
                return;

            String   constraint_1="";
            String   constraint_2="";
            String   constraint_3="";

            if (currentRules.getConstraint_1()!=null)
            {
                if (isRulesContainsDesignation(currentRules.getConstraint_1(),valueCode))
                {
                    constraint_1=replaceValueByNumber(currentRules.getConstraint_1(),valueCode,currentEditText.getText().toString());
                    txtConstraint1.setText(constraint_1);
                }
            }

            if (currentRules.getConstraint_2()!=null)
            {
                if (isRulesContainsDesignation(currentRules.getConstraint_2(),valueCode))
                {
                    constraint_2=replaceValueByNumber(currentRules.getConstraint_2(),valueCode,currentEditText.getText().toString());
                    txtConstraint2.setText(constraint_2);
                }

            }

            if (currentRules.getConstraint_3()!=null)
            {
                if (isRulesContainsDesignation(currentRules.getConstraint_3(),valueCode))
                {
                    constraint_3=replaceValueByNumber(currentRules.getConstraint_3(),valueCode,currentEditText.getText().toString());
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
        button_displayPulse.setOnClickListener(new ListenerButton(Const.CATEGORY_PULSE));
        button_display_glucose.setOnClickListener(new ListenerButton(Const.CATEGORY_GLUCOSE));
        button_display_weight.setOnClickListener(new ListenerButton(Const.CATEGORY_WEIGHT));
        button_display_steps.setOnClickListener(new ListenerButton(Const.CATEGORY_STEP));
        button_display_pressure.setOnClickListener(new ListenerButton(Const.CATEGORY_PRESSURE));

        //add listener to edit Text
        edit_value1min.addTextChangedListener(new ListenerEditText("Value_1Min",edit_value1min));
        edit_value1Max.addTextChangedListener(new ListenerEditText("Value_1Max",edit_value1Max));
        edit_value2Min.addTextChangedListener(new ListenerEditText("Value_2Min",edit_value2Min));
        edit_value2Max.addTextChangedListener(new ListenerEditText("Value_2Max",edit_value2Max));

        return view;
    }

    private void initView() {
        button_displayPulse=(ImageButton)view.findViewById(R.id.button_display_rules_pulse);
        button_display_glucose=(ImageButton)view.findViewById(R.id.button_display_rules_glucose);
        button_display_weight=(ImageButton)view.findViewById(R.id.button_display_rules_weight);
        button_display_steps=(ImageButton)view.findViewById(R.id.button_display_rules_step);
        button_display_pressure=(ImageButton)view.findViewById(R.id.button_display_rules_pressure);

        txtConstraint1 =(TextView)view.findViewById(R.id.txtView_constraint1);
        txtConstraint2 =(TextView)view.findViewById(R.id.txtView_constraint2);
        txtConstraint3 =(TextView)view.findViewById(R.id.txtView_constraint3);

        edit_value1min =(EditText)view.findViewById(R.id.edittxt_value1min);
        edit_value1Max =(EditText)view.findViewById(R.id.edittxt_value1max);
        edit_value2Min =(EditText)view.findViewById(R.id.edittxt_value2min);
        edit_value2Max =(EditText)view.findViewById(R.id.edittxt_value2max);
    }
    private String formatConstraints(String constraints, CustomRules rule)
    {
        String bulletPoint="\u2022";
        constraints=replaceValueByNumber(constraints,"Value_1Min",rule.getVal_1_min()+"");
        constraints=replaceValueByNumber(constraints,"Value_1Max",rule.getVal_1_max()+"");
        constraints=replaceValueByNumber(constraints,"Value_2Min",rule.getVal__2_min()+"");
        constraints=replaceValueByNumber(constraints,"Value_2Max",rule.getVal_2_max()+"");
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
}
