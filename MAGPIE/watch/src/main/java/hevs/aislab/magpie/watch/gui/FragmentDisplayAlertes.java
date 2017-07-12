package hevs.aislab.magpie.watch.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;

import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * Created by teuft on 23.06.2017.
 */

public class FragmentDisplayAlertes extends Fragment {
    View view;
    AlertAdapter alertAdapter;
    ListView listViewAlert;
    List<Alertes>alertesList;
    Map<String, ImageButton> mapButton=new HashMap<>();

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
                    alertesList.clear();
                    alertesList.addAll(AlertRepository.getINSTANCE().getAllByCategory(category));
                    updateViewList();

            //change the button selected
            setButtonsToGreen(mapButton);
            setButtonToRed(mapButton.get(category));
        }


        /**
         * Set the color of the category displayed to green. It's mean the category is not displayed
         *
         */
        private void setButtonsToGreen(Map<String, ImageButton> imageButtonMap)
        {
            for (Map.Entry<String, ImageButton> entry : imageButtonMap.entrySet())
            {
                int id = getContext().getResources().getIdentifier(entry.getKey()+"_green", "drawable", getContext().getPackageName());
                Drawable img= ContextCompat.getDrawable(getContext(), id);
                entry.getValue().setImageDrawable(img);
            }
        }

        /**
         * Set the color of the category displayed to green. It's mean the category is displayed
         *
         */
        private void setButtonToRed(ImageButton imagebutton)
        {
            int id = getContext().getResources().getIdentifier(category+"_red", "drawable", getContext().getPackageName());
            Drawable img= ContextCompat.getDrawable(getContext(), id);
            imagebutton.setImageDrawable(img);
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
        alertAdapter=new AlertAdapter(view.getContext(),R.layout.adapter_alert,alertesList);

        instanciateView();
        addListenerToButton();
        listViewAlert.setAdapter(alertAdapter);
        updateViewList();


        return view;
    }

    private void addListenerToButton() {
        mapButton.get(Const.CATEGORY_GLUCOSE).setOnClickListener(new ListnerButton(Const.CATEGORY_GLUCOSE));
        mapButton.get(Const.CATEGORY_PRESSURE).setOnClickListener(new ListnerButton(Const.CATEGORY_PRESSURE));
        mapButton.get(Const.CATEGORY_PULSE).setOnClickListener(new ListnerButton(Const.CATEGORY_PULSE));
        mapButton.get(Const.CATEGORY_STEP).setOnClickListener(new ListnerButton(Const.CATEGORY_STEP));
        mapButton.get(Const.CATEGORY_WEIGHT).setOnClickListener(new ListnerButton(Const.CATEGORY_WEIGHT));
    }

    private void instanciateView() {
        mapButton.put(Const.CATEGORY_GLUCOSE,(ImageButton)view.findViewById(R.id.button_display_alert_glucose));
        mapButton.put(Const.CATEGORY_PRESSURE,(ImageButton)view.findViewById(R.id.button_display_alert_pressure));
        mapButton.put(Const.CATEGORY_PULSE,(ImageButton)view.findViewById(R.id.button_display_alert_pulse));
        mapButton.put(Const.CATEGORY_STEP,(ImageButton)view.findViewById(R.id.button_display_alert_step));
        mapButton.put(Const.CATEGORY_WEIGHT,(ImageButton)view.findViewById(R.id.button_display_alert_weight));
    }


    private void updateViewList() {
        if (alertesList.size()==0)
            CustomToast.getInstance().warningToast(getString(R.string.toast_noalert),getActivity());
        alertAdapter.notifyDataSetChanged();
    }



}
