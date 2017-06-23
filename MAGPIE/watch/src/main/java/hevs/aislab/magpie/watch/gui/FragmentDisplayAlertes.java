package hevs.aislab.magpie.watch.gui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_display_alert, container, false);

        ListView listViewAlert=(ListView)view.findViewById(R.id.list_alert) ;
        List<Alertes> listAlert= AlertRepository.getINSTANCE().getAllByCategory(Const.CATEGORY_GLUCOSE);
        Log.d("Adapter_Creation",listAlert.size()+"");
        AlertAdapter alertAdapter=new AlertAdapter(view.getContext(),R.layout.adapter_alert,listAlert);

        listViewAlert.setAdapter(alertAdapter);
        return view;
    }
}
