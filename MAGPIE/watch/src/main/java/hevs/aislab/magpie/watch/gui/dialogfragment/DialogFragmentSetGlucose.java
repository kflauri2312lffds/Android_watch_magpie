package hevs.aislab.magpie.watch.gui.dialogfragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.libs.Validator;
import hevs.aislab.magpie.watch.notification.CustomToast;

/**
 * Created by teuft on 08.06.2017.
 */

public class DialogFragmentSetGlucose extends DialogFragmentSetValue {

    EditText txtValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_set1value, //Fragment that take only one value
                container, false);
        this.view=view;

       // getDialog().getWindow().setLayout(300,300);
        //set the backcolor to transparent
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //get the button an create the click listener for each button
        ImageButton submit=(ImageButton) view.findViewById(R.id.button_submit_value);
        ImageButton cancel=(ImageButton)view.findViewById(R.id.button_cancel_value);
        txtValue=(EditText)view.findViewById(R.id.edit_txt_value);

        createListenerCancelButton(cancel);
        createListenerSubmitButton(submit);

        //set the value of the textView
        TextView title=(TextView)view.findViewById(R.id.title_setvalue);
        title.setText(getString(R.string.glucose_level));

        return view;
    }

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
                    double value=Double.parseDouble(txtValue.getText().toString());
                    //check if the entry is valide (range)
                    if (!Validator.isEntryValueValide(Const.CATEGORY_GLUCOSE,value))
                    {
                        CustomToast.getInstance().errorTOast(getString(R.string.incorrect_value),getActivity());
                        return;
                    }
                    homeActivity.sendValue(Const.CATEGORY_GLUCOSE,value+"");
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
