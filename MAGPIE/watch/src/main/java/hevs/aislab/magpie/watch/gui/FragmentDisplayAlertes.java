package hevs.aislab.magpie.watch.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentListAlert;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetGlucose;
import hevs.aislab.magpie.watch.gui.dialogfragment.DialogFragmentSetValue;
import hevs.aislab.magpie.watch.libs.Const;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.repository.AlertRepository;

/**
 * Created by teuft on 23.06.2017.
 */

public class FragmentDisplayAlertes extends Fragment {
    View view;
    AlertAdapter alertAdapter;
    ListView listViewAlert;
    List<Alertes>alertesList;

    //BUtton
    ImageButton buttonPulse;
    ImageButton buttonGlucose;
    ImageButton buttonWeight;
    ImageButton buttonPressure;
    ImageButton buttonStep;

    private FragmentActivity contextActivity;

    //listner for button
    private class ListnerButton implements View.OnClickListener
    {
        String category;
        public ListnerButton(String category)
        {
            this.category=category;
        }
        @Override
        public void onClick(View view) {
            //change the value in the
            ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertesList.clear();
                    alertesList.addAll(AlertRepository.getINSTANCE().getAllByCategory(category));

                    listViewAlert.setAdapter(alertAdapter);
                    alertAdapter.notifyDataSetChanged();
                    listViewAlert.refreshDrawableState();
                }
            });




//            //create the bundle for the fragment and set the bundle
//            DialogFragmentListAlert dialogFragmentListAlert = new DialogFragmentListAlert();
//            Bundle bundle=new Bundle();
//            bundle.putString("category",category);
//            dialogFragmentListAlert.setArguments(bundle);
//
//            //show the fragment
//            FragmentManager fm = contextActivity.getSupportFragmentManager();
//            dialogFragmentListAlert.show(fm,"tag");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            contextActivity = (HomeActivity) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_display_alert, container, false);

        listViewAlert=(ListView)view.findViewById(R.id.list_alert) ;
        alertesList= AlertRepository.getINSTANCE().getAll();
        Log.d("Adapter_Creation",alertesList.size()+"");
        alertAdapter=new AlertAdapter(view.getContext(),R.layout.adapter_alert,alertesList);

        ((HomeActivity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewAlert.setAdapter(alertAdapter);
                alertAdapter.notifyDataSetChanged();
                listViewAlert.refreshDrawableState();
            }
        });

        //generate the button
        buttonGlucose=(ImageButton)view.findViewById(R.id.button_display_alert_glucose);
        buttonPressure=(ImageButton)view.findViewById(R.id.button_display_alert_pressure);
        buttonPulse=(ImageButton)view.findViewById(R.id.button_display_alert_pulse);
        buttonStep=(ImageButton)view.findViewById(R.id.button_display_alert_step);
        buttonWeight=(ImageButton)view.findViewById(R.id.button_display_alert_weight);

        buttonGlucose.setOnClickListener(new ListnerButton(Const.CATEGORY_GLUCOSE));
        buttonPressure.setOnClickListener(new ListnerButton(Const.CATEGORY_PRESSURE));
        buttonPulse.setOnClickListener(new ListnerButton(Const.CATEGORY_PULSE));
        buttonStep.setOnClickListener(new ListnerButton(Const.CATEGORY_STEP));
        buttonWeight.setOnClickListener(new ListnerButton(Const.CATEGORY_WEIGHT));


        return view;
    }




}
