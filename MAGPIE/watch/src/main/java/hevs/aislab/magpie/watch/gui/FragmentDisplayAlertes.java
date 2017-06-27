package hevs.aislab.magpie.watch.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;
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
            alertesList.clear();
            alertesList.addAll(AlertRepository.getINSTANCE().getAllByCategory(category));
            alertAdapter.notifyDataSetChanged();
            Log.d("sizOfArray",alertesList.size()+"");
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
        Log.d("displayedAlert","DidplaysedAlert");
        listViewAlert.setAdapter(alertAdapter);

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


    /**
     * This method is used to display alert based on the button pressed. We will specify the category of alert
     * @param view
     * @param category
     */


}
