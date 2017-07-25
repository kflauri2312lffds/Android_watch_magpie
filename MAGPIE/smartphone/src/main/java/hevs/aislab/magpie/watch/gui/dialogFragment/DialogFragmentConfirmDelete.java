package hevs.aislab.magpie.watch.gui.dialogFragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.repository.AlertesRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;

/**
 * Fragment that will ask the confirmation when we want to delete the DB data
 */

public class DialogFragmentConfirmDelete extends DialogFragment {

    View view;
    TextView textTitle;

    ImageButton buttonConfirm;
    ImageButton buttonCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_confirm_delete, //nomn du layout frag dialogue
                container, false);
        this.view = view;

        //init the view
        textTitle=(TextView)view.findViewById(R.id.txtview_confirm_delete);
        buttonCancel=(ImageButton)view.findViewById(R.id.button_cancel);
        buttonConfirm=(ImageButton)view.findViewById(R.id.button_confirm);

        //add the listener to the button

        //close the dialog fragment without delete anythings (cancel)
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //confirm
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertesRepository.getINSTANCE().deleteAll();
                MeasuresRepository.getInstance().deleteAll();
                CustomToast.getInstance().confirmToast(getString(R.string.notif_data_deleted),getActivity());
                dismiss();
            }
        });

        return view;
    }


    //set the full size of the screen
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
