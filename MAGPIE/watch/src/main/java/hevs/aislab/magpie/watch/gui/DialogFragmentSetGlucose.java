package hevs.aislab.magpie.watch.gui;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;

/**
 * Created by teuft on 08.06.2017.
 */

public class DialogFragmentSetGlucose extends DialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_setglucose, //nomn du layout frag dialogue
                container, false);

        getDialog().getWindow().setLayout(300,300);
        //set the backcolor to transparent
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

}
