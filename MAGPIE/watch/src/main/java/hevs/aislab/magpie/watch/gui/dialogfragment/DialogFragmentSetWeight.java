package hevs.aislab.magpie.watch.gui.dialogfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;

/**
 * Created by teuft on 17.06.2017.
 */

public class DialogFragmentSetWeight extends DialogFragmentSetValue {



    EditText textValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialogfragment_set1value, //nomn du layout frag dialogue
                container, false);
        this.view=view;

        ImageButton cancelButton=(ImageButton) view.findViewById(R.id.button_cancel_value);
        ImageButton submitButton=(ImageButton)view.findViewById(R.id.button_submit_value);
        //add listener to the button
        createListenerCancelButton(cancelButton);
        createListenerSubmitButton(submitButton);
        //change the value of the title
        ((TextView)view.findViewById(R.id.title_setvalue)).setText(getString(R.string.weight_level));
        //get the 2 EditText and set the hint
        textValue=(EditText) view.findViewById(R.id.edit_txt_value);
        textValue.setHint(R.string.weight);


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
                    double value=Double.parseDouble(textValue.getText().toString());
                    homeActivity.sendValue(Const.CATEGORY_WEIGHT,value+"");
                    dismiss();
                }
                catch (Exception ex)
                {
                    Toast.makeText(getContext(), getString(R.string.incorrect_value), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
