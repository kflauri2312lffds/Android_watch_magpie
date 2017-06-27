package hevs.aislab.magpie.watch.gui.dialogfragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;
import hevs.aislab.magpie.watch.models.Alertes;

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
        View view = inflater.inflate(R.layout.dialogfragment_set1value, //Fragment that take only one value
                container, false);
        this.view = view;
        return view;
    }


}
