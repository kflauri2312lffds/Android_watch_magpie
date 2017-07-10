package hevs.aislab.magpie.watch.gui.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;

import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch_library.lib.Const;
import hevs.aislab.magpie.watch_library.lib.Validator;

/**
 * Created by teuft on 16.06.2017.
 */

    public class DialogFragmentSetPressure extends DialogFragmentSetValue {


    EditText textDiastol;
    EditText textSystol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialogfragment_set2value, //nomn du layout frag dialogue
                container, false);
        this.view=view;

        ImageButton cancelButton=(ImageButton) view.findViewById(R.id.button_cancel_value);
        ImageButton submitButton=(ImageButton)view.findViewById(R.id.button_submit_value);
        //add listener to the button
        createListenerCancelButton(cancelButton);
        createListenerSubmitButton(submitButton);
        //change the value of the title
        ((TextView)view.findViewById(R.id.title_2_setvalue)).setText(getString(R.string.pressure_level));
        //get the 2 EditText and set the hint
        textSystol=(EditText) view.findViewById(R.id.edit_txt_value1);
        textDiastol=(EditText)view.findViewById(R.id.edit_txt_value2);
        textSystol.setHint(R.string.systol);
        textDiastol.setHint(R.string.diastol);


        return view;
    }

    //USED TO SEND THE VALUE BACK TO THE ACTIVITY
    @Override
    protected void createListenerSubmitButton(ImageButton submit) {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the value from the editText
                // EditText textValue=(EditText)view.findViewById(R.id.edit_txt_value);
                //convert it to double
                try
                {
                    double valueSystol=Double.parseDouble(textSystol.getText().toString());
                    double valueDiastol=Double.parseDouble(textDiastol.getText().toString());
                    if (!Validator.isEntryValueValide(Const.CATEGORY_PRESSURE,valueSystol,valueDiastol))
                    {
                        CustomToast.getInstance().errorTOast(getString(R.string.incorrect_value),getActivity());
                        return;
                    }
                    homeActivity.sendValue(Const.CATEGORY_PRESSURE,valueSystol+"",valueDiastol+"");
                    dismiss();
                }
                catch (Exception ex)
                {
                    CustomToast.getInstance().errorTOast(getString(R.string.incorrect_value),getActivity());
                }
            }
        });
    }
}
