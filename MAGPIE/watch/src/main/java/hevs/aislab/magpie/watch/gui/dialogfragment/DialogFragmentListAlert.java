package hevs.aislab.magpie.watch.gui.dialogfragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.repository.AlertRepository;

/**
 * Created by teuft on 27.06.2017.
 */

public class DialogFragmentListAlert extends DialogFragment {

    View view;

    AlertAdapter alertAdapter;
    ListView listViewAlert;
    List<Alertes>alertesList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogfragment_listalert, //Fragment that take only one value
                container, false);
        this.view = view;


        //Get the argument
        if (getArguments()==null)
        {
            Log.d("BundleIsNull","BundielFDsfads");
        }



        String category=getArguments().getString("category");
       // String category= Const.CATEGORY_GLUCOSE;

        //init the var for the listview
        listViewAlert=(ListView)view.findViewById(R.id.listview_alert) ;
        if (category==null || category.equals(""))
            alertesList= AlertRepository.getINSTANCE().getAll();
        else
            alertesList=AlertRepository.getINSTANCE().getAllByCategory(category);


        alertAdapter=new AlertAdapter(view.getContext(),R.layout.adapter_alert,alertesList);
        listViewAlert.setAdapter(alertAdapter);


        return view;
    }




}
