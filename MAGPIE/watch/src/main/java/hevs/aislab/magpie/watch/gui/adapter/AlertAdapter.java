package hevs.aislab.magpie.watch.gui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hevs.aislab.magpie.watch.R;

import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch_library.lib.Const;
import hevs.aislab.magpie.watch_library.lib.DateFormater;
import hevs.aislab.magpie.watch_library.lib.NumberFormater;

/**
 * adapter that will display the alert inside the fragment displayAlert
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

        TextView txtViewTimeStamp=(TextView)view.findViewById(R.id.alert_column_timeStamp);
        TextView txtViewmessage=(TextView)view.findViewById(R.id.alert_column_message);
        TextView txtViewValue=(TextView)view.findViewById(R.id.alert_column_values);
        TextView txtViewValue2=(TextView)view.findViewById(R.id.alert_column_values2);
        ImageView imageCategory=(ImageView)view.findViewById(R.id.alert_column_category);

        if (txtViewTimeStamp!=null)
        {
            //get the date of the time stamp
           String date= DateFormater.getInstance().getDateTime(anAlert.getMeasure().getTimeStamp());
            txtViewTimeStamp.setText(date);
        }
        if (txtViewmessage!=null)
            txtViewmessage.setText("");

        if (txtViewValue!=null)
        {
            String value;
            if (anAlert.getMeasure().getCategory().equals(Const.CATEGORY_GLUCOSE) ||anAlert.getMeasure().getCategory().equals(Const.CATEGORY_WEIGHT))
                value= NumberFormater.getInstance().formatWith1Digit( anAlert.getMeasure().getValue1());
            else
                value=NumberFormater.getInstance().formatWithNoDigit( anAlert.getMeasure().getValue1());
            txtViewValue.setText(value);
        }

        if (txtViewValue2!=null )
        {
            String value;
            if (anAlert.getMeasure().getValue2()!=null)
            {
                //if the alert is a  weight category, we have to process the weight to get the percentage
                if (anAlert.getMeasure().getCategory().equals(Const.CATEGORY_WEIGHT))
                {
                    Double variation=(anAlert.getMeasure().getValue2()*100);
                    value =NumberFormater.getInstance().formatWithNoDigit(variation)+"%";
                    value=value.replace("(","");
                    value=value.replace(")","");

                    if (variation<0)
                        value="-"+value;
                    else
                        value="+"+value;
                }

                else
                    value=NumberFormater.getInstance().formatWithNoDigit(anAlert.getMeasure().getValue2());
            }
            else
                value="";
            txtViewValue2.setText(value);
        }

        if (imageCategory!=null)
        {
            //set the image based on the category
           imageCategory.setImageDrawable(getDrawable(anAlert.getMeasure().getCategory()));
        }


        return view;
    }

    private Drawable getDrawable(String category) {

        String ressourceName="small_"+category;
        Context context = getContext();
        //Get the image with the named based on category and based on name of the category
        int id = context.getResources().getIdentifier(ressourceName, "drawable", context.getPackageName());
        Drawable img= ContextCompat.getDrawable(getContext(), id);
        return img;
    }
}
