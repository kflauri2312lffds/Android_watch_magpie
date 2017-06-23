package hevs.aislab.magpie.watch.gui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.models.Alertes;

/**
 * Created by teuft on 23.06.2017.
 */

public class AlertAdapter extends ArrayAdapter<Alertes> {
    public AlertAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public AlertAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Alertes> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Log.d("passage_adapter","appel first methode");

        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.adapter_alert, null);
        }

        Alertes anAlert=getItem(position);

        if (anAlert==null)
        {
            return view;
        }

        Log.d("passage_adapter","Alert is not null");
        TextView txtViewTimeStamp=(TextView)view.findViewById(R.id.alert_column_timeStamp);
        TextView txtViewCategory=(TextView)view.findViewById(R.id.alert_column_category);
        TextView txtViewmessage=(TextView)view.findViewById(R.id.alert_column_message);

        if (txtViewTimeStamp!=null)     //TODO PROCESS THE TIMESTAMP AND SHOW A DATE
            txtViewTimeStamp.setText(anAlert.getMeasure().getTimeStamp()+"");
        if (txtViewmessage!=null)
            txtViewmessage.setText(anAlert.getMessage());
        if (txtViewCategory!=null)
            txtViewCategory.setText(anAlert.getMeasure().getCategory());




        return view;
    }
}
