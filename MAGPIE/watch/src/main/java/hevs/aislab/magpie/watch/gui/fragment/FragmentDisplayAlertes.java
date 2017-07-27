package hevs.aislab.magpie.watch.gui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import hevs.aislab.magpie.watch.HomeActivity;
import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.notification.CustomToast;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch_library.gui.ButtonsManager;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * Fragment that display all alert if no category is specified, or display alert based on the category
 */

public class FragmentDisplayAlertes extends Fragment {
    View view;
    AlertAdapter alertAdapter;
    ListView listViewAlert;
    List<Alertes>alertesList;

    ButtonsManager buttonsManager;


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
            buttonsManager.setAllButtonToGreen();
            buttonsManager.setButtonToRed(category);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            contextActivity = (HomeActivity) context;
        } catch (ClassCastException castException) {
         //
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_display_alert, container, false);
        buttonsManager=new ButtonsManager(getContext());
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
        buttonsManager.getButtonByCategory(Const.CATEGORY_GLUCOSE).setOnClickListener(new ListnerButton(Const.CATEGORY_GLUCOSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PRESSURE).setOnClickListener(new ListnerButton(Const.CATEGORY_PRESSURE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PULSE).setOnClickListener(new ListnerButton(Const.CATEGORY_PULSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_STEP).setOnClickListener(new ListnerButton(Const.CATEGORY_STEP));
        buttonsManager.getButtonByCategory(Const.CATEGORY_WEIGHT).setOnClickListener(new ListnerButton(Const.CATEGORY_WEIGHT));
    }

    private void instanciateView() {
        buttonsManager.addButtonByCategory(Const.CATEGORY_GLUCOSE,(ImageButton)view.findViewById(R.id.button_display_alert_glucose));
        buttonsManager.addButtonByCategory(Const.CATEGORY_PRESSURE,(ImageButton)view.findViewById(R.id.button_display_alert_pressure));
        buttonsManager.addButtonByCategory(Const.CATEGORY_PULSE,(ImageButton)view.findViewById(R.id.button_display_alert_pulse));
        buttonsManager.addButtonByCategory(Const.CATEGORY_STEP,(ImageButton)view.findViewById(R.id.button_display_alert_step));
        buttonsManager.addButtonByCategory(Const.CATEGORY_WEIGHT,(ImageButton)view.findViewById(R.id.button_display_alert_weight));
    }


    private void updateViewList() {
        if (alertesList.size()==0)
            CustomToast.getInstance().warningToast(getString(R.string.toast_noalert),getActivity());
        alertAdapter.notifyDataSetChanged();
    }

}
