package hevs.aislab.magpie.watch.dummy;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.repository.AlertRepository;
import hevs.aislab.magpie.watch.repository.MeasuresRepository;
import hevs.aislab.magpie.watch.repository.RulesRepository;
import hevs.aislab.magpie.watch_library.lib.Const;

/**
 * this class is used to insert dummy data inside the DB
 */

public class DummyValue {


    public void insertDummyMeasure()
    {
        insertDummyPulseMeasure();
        insertDummyWeightMeasure();
        insertDummyPressure();
        insertDummysteps();
        insertDummyGlucose();

    }

    /**
     *
     * @param str_date ex: "13-09-2011 12:44";
     * @return
     */
    private long getTimeStamp(String str_date)
    {
        try {
                DateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
                Date date = formatter.parse(str_date);
                return date.getTime();
            }
            catch (ParseException ex)
            {
                ex.printStackTrace();
            }
        return 0;
    }

    private void insertDummyPulseMeasure()
    {
        List<Measure> measureList=new ArrayList<>();
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,40.0,null,getTimeStamp("13-09-2011 12:44")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,60.00,null,getTimeStamp("13-09-2011 12:56")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,70.00,null,getTimeStamp("13-09-2011 13:21")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,90.00,null,getTimeStamp("13-09-2011 13:44")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,120.00,null,getTimeStamp("13-09-2011 13:56")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,140.00,null,getTimeStamp("13-09-2011 14:02")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,90.00,null,getTimeStamp("13-09-2011 18:32")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,120.00,null,getTimeStamp("14-09-2011 12:21")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,60.00,null,getTimeStamp("14-09-2011 22:32")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,87.00,null,getTimeStamp("14-09-2011 23:44")));
        measureList.add(new Measure(null, Const.CATEGORY_PULSE,113.00,null,getTimeStamp("15-09-2011 18:21")));

        for (Measure aMeasure : measureList)
            MeasuresRepository.getInstance().insert(aMeasure);

        //add 5 alert

        CustomRules rule= RulesRepository.getInstance().getByCategory(Const.CATEGORY_PULSE);

        List<Alertes>alertListe=new ArrayList<>();

        alertListe.add(new Alertes(null,null,measureList.get(3).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(4).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(7).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(8).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(9).getId(),rule.getId()));

        for (Alertes anAlert : alertListe)
        {
            AlertRepository.getINSTANCE().insert(anAlert);
        }


    }

    private void insertDummyWeightMeasure()
    {
        List<Measure> measureList=new ArrayList<>();
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,100.00,null,getTimeStamp("16-09-2011 12:44")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,100.80,0.02,getTimeStamp("17-09-2011 12:56")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,99.00,-0.01,getTimeStamp("18-09-2011 13:21")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,98.00,0.01,getTimeStamp("19-09-2011 13:44")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,101.00,0.04,getTimeStamp("20-09-2011 13:56")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,100.8,-0.02,getTimeStamp("21-09-2011 14:02")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,100.2,-0.06,getTimeStamp("22-09-2011 18:32")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,100.1,-0.01,getTimeStamp("23-09-2011 12:21")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,103.00,0.02,getTimeStamp("24-09-2011 22:32")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,101.5,-0.01,getTimeStamp("25-09-2011 23:44")));
        measureList.add(new Measure(null, Const.CATEGORY_WEIGHT,103.00,0.03,getTimeStamp("26-09-2011 18:21")));

        for (Measure aMeasure : measureList)
            MeasuresRepository.getInstance().insert(aMeasure);



        CustomRules rule= RulesRepository.getInstance().getByCategory(Const.CATEGORY_WEIGHT);

        List<Alertes>alertListe=new ArrayList<>();

        alertListe.add(new Alertes(null,null,measureList.get(3).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(4).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(7).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(8).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(9).getId(),rule.getId()));

        for (Alertes anAlert : alertListe)
        {
            AlertRepository.getINSTANCE().insert(anAlert);
        }
    }

    private void insertDummyPressure()
    {
        List<Measure> measureList=new ArrayList<>();
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,100.00,90.00,getTimeStamp("27-09-2011 12:44")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,110.00,70.00,getTimeStamp("27-09-2011 12:56")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,140.00,60.00,getTimeStamp("27-09-2011 13:21")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,100.00,70.00,getTimeStamp("27-09-2011 13:44")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,140.00,75.00,getTimeStamp("28-09-2011 13:56")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,135.00,82.00,getTimeStamp("28-09-2011 14:02")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,110.00,90.00,getTimeStamp("29-09-2011 18:32")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,135.00,80.00,getTimeStamp("29-09-2011 12:21")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,140.00,90.00,getTimeStamp("30-09-2011 22:32")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,100.00,80.00,getTimeStamp("1-10-2011 23:44")));
        measureList.add(new Measure(null, Const.CATEGORY_PRESSURE,125.00,80.00,getTimeStamp("2-11-2011 18:21")));

        for (Measure aMeasure : measureList)
            MeasuresRepository.getInstance().insert(aMeasure);

        CustomRules rule= RulesRepository.getInstance().getByCategory(Const.CATEGORY_PRESSURE);

        List<Alertes>alertListe=new ArrayList<>();

        alertListe.add(new Alertes(null,null,measureList.get(3).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(4).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(7).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(8).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(9).getId(),rule.getId()));

        for (Alertes anAlert : alertListe)
        {
            AlertRepository.getINSTANCE().insert(anAlert);
        }
    }
    private void insertDummysteps()
    {
        List<Measure> measureList=new ArrayList<>();
        measureList.add(new Measure(null, Const.CATEGORY_STEP,500.00,null,getTimeStamp("3-11-2011 12:44")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,700.00,null,getTimeStamp("4-11-2011 12:56")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,12000.00,null,getTimeStamp("5-11-2011 13:21")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,13000.00,null,getTimeStamp("6-11-2011 13:44")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,5678.00,null,getTimeStamp("7-11-2011 13:56")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,12000.00,null,getTimeStamp("8-11-2011 14:02")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,7500.00,null,getTimeStamp("9-11-2011 18:32")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,6000.00,null,getTimeStamp("10-11-2011 12:21")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,5400.00,null,getTimeStamp("11-11-2011 22:32")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,10018.00,null,getTimeStamp("12-11-2011 23:44")));
        measureList.add(new Measure(null, Const.CATEGORY_STEP,14000.00,null,getTimeStamp("13-11-2011 18:21")));

        for (Measure aMeasure : measureList)
            MeasuresRepository.getInstance().insert(aMeasure);

        CustomRules rule= RulesRepository.getInstance().getByCategory(Const.CATEGORY_STEP);

        List<Alertes>alertListe=new ArrayList<>();

        alertListe.add(new Alertes(null,null,measureList.get(3).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(4).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(7).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(8).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(9).getId(),rule.getId()));

        for (Alertes anAlert : alertListe)
        {
            AlertRepository.getINSTANCE().insert(anAlert);
        }

    }
    private void insertDummyGlucose()
    {
        List<Measure> measureList=new ArrayList<>();
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,2.0,null,getTimeStamp("14-11-2011 12:44")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,3.1,null,getTimeStamp("14-11-2011 12:56")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,5.6,null,getTimeStamp("14-11-2011 13:21")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,7.4,null,getTimeStamp("14-11-2011 13:44")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,10.0,null,getTimeStamp("14-11-2011 13:56")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,9.9,null,getTimeStamp("14-11-2011 14:02")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,8.7,null,getTimeStamp("14-11-2011 18:32")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,15.3,null,getTimeStamp("15-11-2011 12:21")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,4.9,null,getTimeStamp("15-11-2011 22:32")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,5.4,null,getTimeStamp("16-11-2011 23:44")));
        measureList.add(new Measure(null, Const.CATEGORY_GLUCOSE,3.9,null,getTimeStamp("17-11-2011 18:21")));

        for (Measure aMeasure : measureList)
            MeasuresRepository.getInstance().insert(aMeasure);

        CustomRules rule= RulesRepository.getInstance().getByCategory(Const.CATEGORY_GLUCOSE);

        List<Alertes>alertListe=new ArrayList<>();

        alertListe.add(new Alertes(null,null,measureList.get(3).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(4).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(7).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(8).getId(),rule.getId()));
        alertListe.add(new Alertes(null,null,measureList.get(9).getId(),rule.getId()));

        for (Alertes anAlert : alertListe)
        {
            AlertRepository.getINSTANCE().insert(anAlert);
        }
    }
}
