package hevs.aislab.magpie.watch.gui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hevs.aislab.magpie.watch.R;
import hevs.aislab.magpie.watch.gui.ButtonsManager;
import hevs.aislab.magpie.watch.lib.Const;
import hevs.aislab.magpie.watch.lib.DateFormater;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;

/**
 * Created by teuft on 07.07.2017.
 */

public class Fragment_display_measures extends Fragment {



    View view;
    LineChart chart;




    /**
     * Will handle change the color of the button based on the category we selec
     */
    ButtonsManager buttonsManager;



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

            displayValue(currentCategory);

            buttonsManager.setAllButtonToGreen();
            buttonsManager.setButtonToRed(currentCategory);
        }
    }

    /**
     * This class is used to format the value inside the chart
     */
    private class XaxValueFormater implements IAxisValueFormatter
    {
        private List<Measure>measureList;
        private String dateFormater;


        public XaxValueFormater(List<Measure>measureList, String dateformat)
        {
            this.dateFormater=dateformat;
            this.measureList=measureList;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            try{
                if (dateFormater==null ||dateFormater.equals(""))
                    return DateFormater.getInstance().getDate( measureList.get((int)value).getTimeStamp());
                return DateFormater.getInstance().getDateByFormat( measureList.get((int)value).getTimeStamp(),dateFormater);
            }
            catch (Exception ex)
            {
                return "";
            }
        }
        public int getDecimalDigits() {  return 0; }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        // Inflate the layout for this fragment

        view = lf.inflate(R.layout.fragment_measure, container, false);
        buttonsManager=new ButtonsManager(this.getContext());
        initView();
        addListenerToButton();


        return view;

    }

    private void initView() {
        //for this fragment, we will work with a line chart
        chart =(LineChart) view.findViewById(R.id.chart_measure);
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


    private void displayValue(String category)
    {
        cleanValueFromChart();

        switch (category)
        {
            case Const.CATEGORY_GLUCOSE :
                final List<Measure>glucoseMeasure= MeasuresRepository.getInstance().getByCategory(Const.CATEGORY_GLUCOSE);
                setChart1Value(glucoseMeasure,getString(R.string.weight),"dd-MM-yy HH:mm");
                break;
            case Const.CATEGORY_PULSE :
                final List<Measure>pulseMeasure= MeasuresRepository.getInstance().getByCategory(Const.CATEGORY_PULSE);
                setChart1Value(pulseMeasure,getString(R.string.weight),"dd-MM-yy HH:mm");
                break;
            case Const.CATEGORY_PRESSURE :
                final List<Measure>pressureMeasure= MeasuresRepository.getInstance().getByCategory(Const.CATEGORY_PRESSURE);
                setChart2Value(pressureMeasure,getString(R.string.systol),getString(R.string.diastol),"dd-MM-yy HH:mm");
                break;
            case Const.CATEGORY_WEIGHT :
                final List<Measure>weightMeasure= MeasuresRepository.getInstance().getByCategory(Const.CATEGORY_WEIGHT);
                setChart1Value(weightMeasure,getString(R.string.weight),"dd-MM-yy");
                break;
            case Const.CATEGORY_STEP :
                final List<Measure>stepMeasure= MeasuresRepository.getInstance().getByCategory(Const.CATEGORY_STEP);
                setChart1Value(stepMeasure,getString(R.string.weight),"dd-MM-yy");
                break;
        }
        chart.fitScreen();
        chart.animateXY(3000,3000);
    }

    /**
     * Used to display the value from the measure list into the chart
     * @param measures
     * @param labelName
     */
    private void setChart1Value(List<Measure> measures, String labelName,String dateFormat) {

        if (measures.size()==0)
            return;

        List<Entry> entries=new ArrayList<>();

        for (int k=0;k<measures.size();k++)
        {   //add the entry
            entries.add(new Entry(k,measures.get(k).getValue1().floatValue()));

        }
        //add label instead of number in the axis
        chart.notifyDataSetChanged();

        //sort otherwise we will trigger an error
        Collections.sort(entries, new EntryXComparator());
        //add the entry to a data set (data that belong together), it's a line
        LineDataSet dataSet=new LineDataSet(entries,labelName);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);
        //ad the data set to the line data. Linedata contains all data set
        LineData lineData=new LineData(dataSet);
        chart.setData(lineData);
        //refresh view
        IAxisValueFormatter formatter =new XaxValueFormater(measures,dateFormat);
        //set the gape between value in x axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        //set the label instead of numner
        xAxis.setValueFormatter(formatter);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        chart.invalidate();

    }

    /**
     * this methode is basicaly the same as the setchart1value1, but it will process 2 value
     * @param measures the list of the measure we have to display
     * @param labelValue1 the label of the first data set
     * @param labelValue2 the label of the second data set
     */
    private void setChart2Value(List<Measure> measures, String labelValue1, String labelValue2,String dateformat) {
        if (measures.size()==0)
            return;

        //set the entry for the
        List<Entry> entries_measure1=new ArrayList<>();
        List<Entry>entries_measure2=new ArrayList<>();
        for (int k=0;k<measures.size();k++)
        {   //add the entry
            entries_measure1.add(new Entry(k,measures.get(k).getValue1().floatValue()));
            entries_measure2.add(new Entry(k,measures.get(k).getValue2().floatValue()));
        }

        //add label instead of number in the axis
        chart.notifyDataSetChanged();

        //sort otherwise we will trigger an error
        Collections.sort(entries_measure1, new EntryXComparator());
        Collections.sort(entries_measure2,new EntryXComparator());

        //add the entry to a data set (data that belong together), it's a line

        //************FIRST DATA SET *************************
        LineDataSet dataSet1=new LineDataSet(entries_measure1,labelValue1);
        dataSet1.setColor(Color.BLUE);
        dataSet1.setValueTextColor(Color.BLACK);
        //**************SECOND DATA SET*********************
        LineDataSet dataSet2=new LineDataSet(entries_measure2,labelValue2);
        dataSet2.setColor(Color.GREEN);
        dataSet2.setValueTextColor(Color.BLACK);



        //ad the data set to the line data. Linedata contains all data set
        LineData lineData=new LineData(dataSet1,dataSet2);
        chart.setData(lineData);
        //refresh view
        IAxisValueFormatter formatter =new XaxValueFormater(measures,dateformat);
        //set the gape between value in x axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        //set the label instead of numner
        xAxis.setValueFormatter(formatter);
        dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        chart.invalidate();

    }
    private void cleanValueFromChart()
    {
        try {

            chart.clearValues();
            chart.clear();

        }
        catch (Exception ex)
        {

        }

    }



}
