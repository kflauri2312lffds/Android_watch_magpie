package hevs.aislab.magpie.watch.gui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.ButtonsManager;
import hevs.aislab.magpie.watch.gui.adapter.AlertAdapter;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.repository.AlertesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * Created by teuft on 07.07.2017.
 */

public class Fragment_display_alertes extends Fragment {


    AlertAdapter alertAdapter;
    ListView listViewAlert;
    List<Alertes> alertesList;

    /**
     * This class is used to display other category
     */
    private class ListenerButtonCategory implements View.OnClickListener
    {
        String currentCategory;

        public ListenerButtonCategory(String currentCategory)
        {
            this.currentCategory=currentCategory;
        }

        @Override
        public void onClick(View view) {
            alertesList.clear();
            alertesList.addAll(AlertesRepository.getINSTANCE().getAllByCategory(currentCategory));
            updateViewList();

            buttonsManager.setAllButtonToGreen();
            buttonsManager.setButtonToRed(currentCategory);
        }
    }

    ButtonsManager buttonsManager;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment
        view = lf.inflate(R.layout.fragment_alert, container, false);


        buttonsManager=new ButtonsManager(getContext());
        initView();
        addListenerToButton();
        //listview part

        listViewAlert=(ListView)view.findViewById(R.id.list_alert) ;

        displayAll();
        return view;
    }

    public void displayAll() {
        alertesList= AlertesRepository.getINSTANCE().getAll();
        Log.d("Adapter_Creation",alertesList.size()+"");
        alertAdapter=new AlertAdapter(view.getContext(), R.layout.adapter_alert,alertesList);
        listViewAlert.setAdapter(alertAdapter);
        updateViewList();
    }

    public void initView()
    {

        buttonsManager.addButtonByCategory(Const.CATEGORY_GLUCOSE,(ImageButton)view.findViewById(R.id.button_glucose));
        buttonsManager.addButtonByCategory(Const.CATEGORY_PULSE,(ImageButton)view.findViewById(R.id.button_pulse));
        buttonsManager.addButtonByCategory(Const.CATEGORY_PRESSURE,(ImageButton)view.findViewById(R.id.button_pressure));
        buttonsManager.addButtonByCategory(Const.CATEGORY_WEIGHT,(ImageButton)view.findViewById(R.id.button_weight));
        buttonsManager.addButtonByCategory(Const.CATEGORY_STEP,(ImageButton)view.findViewById(R.id.button_step));
    }
    private void addListenerToButton()
    {
        buttonsManager.getButtonByCategory(Const.CATEGORY_GLUCOSE).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_GLUCOSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PULSE).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_PULSE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_PRESSURE).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_PRESSURE));
        buttonsManager.getButtonByCategory(Const.CATEGORY_WEIGHT).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_WEIGHT));
        buttonsManager.getButtonByCategory(Const.CATEGORY_STEP).setOnClickListener(new ListenerButtonCategory(Const.CATEGORY_STEP));
    }

    private void updateViewList() {
        alertAdapter.notifyDataSetChanged();

    }
}