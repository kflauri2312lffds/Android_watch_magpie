package hevs.aislab.magpie.watch.repository;

import android.database.Cursor;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.CustomRulesDao;
import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.models.MeasureDao;


/**
 * Created by teuft on 05.06.2017.
 */

public class MeasuresRepository {

    private static MeasuresRepository INSTANCE;
    private MeasureDao measuresDao;

    private MeasuresRepository(MeasureDao measuresDao) {
        this.measuresDao = measuresDao;
    }

    public static MeasuresRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MeasuresRepository(Core.getInstance().getDaoSession().getMeasureDao());
        }
        return INSTANCE;
    }

    public List<Measure> getByCategoryWhereTimeStampBetween(String category, long startTimeStamp, long endTimeStamp)
    {
        //we ORDER BY ASC. THEN, WE WON'T NEED TO ORDER IT IN THE ALGORITHME FOR THE GLUCOSE. USEFULLL FOR RULES LIKE TEV2>TEV1
       return measuresDao.queryBuilder()
                .where(MeasureDao.Properties.Category.eq(category))
                .where(MeasureDao.Properties.TimeStamp.between(startTimeStamp,endTimeStamp))
               .orderAsc(MeasureDao.Properties.TimeStamp)
                .list();
    }

    public void insert(Measure measure)
    {
        measuresDao.insert(measure);
    }

    public void update(Measure measure)
    {
        measuresDao.update(measure);
    }

    public Measure getById(long id)
    {
        return  measuresDao.queryBuilder()
                .where(MeasureDao.Properties.Id.eq(id))
                .orderAsc(MeasureDao.Properties.Id)
                .unique();
    }

    /**
     * Get all measure in the database
     * @return List<Measure>
     */
    public List<Measure>getAll()
    {
        return measuresDao.queryBuilder()
                .list();
    }
    public List<Measure>getByCategory(String category)
    {
        return measuresDao.queryBuilder()
                .where(MeasureDao.Properties.Category.eq(category))
                .list();
    }


    /**
     * this methode will return the last measure stored in the database of each category, and then
     * sotre it by the name of the category in a hash map
     * @return lastMeasure
     */
    public HashMap<String,Measure>getLastMeasure()
    {
      String SQL_DISTINCT_ENAME = "SELECT max("+MeasureDao.Properties.TimeStamp.columnName+"),"
              +MeasureDao.Properties.Id.columnName+","
              +MeasureDao.Properties.Value1.columnName+","
              +MeasureDao.Properties.Value2.columnName+","
              +MeasureDao.Properties.Category.columnName
              +" FROM "+MeasureDao.TABLENAME
              +" GROUP BY  "+MeasureDao.Properties.Category.columnName
              +" ORDER BY "+MeasureDao.Properties.Category.columnName;


        HashMap<String,Measure> result = new HashMap<String, Measure>();
        Cursor c = Core.getInstance().getDaoSession().getDatabase().rawQuery(SQL_DISTINCT_ENAME, null);
        try{
            if (c.moveToFirst()) {
                do {
                    Measure measure=new Measure();
                    measure.setTimeStamp(c.getLong(0));
                    measure.setId(c.getLong(1));
                    measure.setValue1(c.getDouble(2));
                    measure.setValue2(c.getDouble(3));
                    measure.setCategory(c.getString(4));
                    result.put(measure.getCategory(),measure);
                } while (c.moveToNext());
            }
        } finally {
            c.close();

        return result;
        }
    }

    public Measure getLastMeasureFromCategory(String category) {
        String querry = "SELECT max(" + MeasureDao.Properties.TimeStamp.columnName + "),"
                + MeasureDao.Properties.Id.columnName + ","
                + MeasureDao.Properties.Value1.columnName + ","
                + MeasureDao.Properties.Value2.columnName + ","
                + MeasureDao.Properties.Category.columnName
                + " FROM " + MeasureDao.TABLENAME
                +" WHERE "+ MeasureDao.Properties.Category.columnName +" = "+ "'"+category+"'"
                + " ORDER BY " + MeasureDao.Properties.TimeStamp.columnName;

        Cursor c = Core.getInstance().getDaoSession().getDatabase().rawQuery(querry, null);
        Measure measure = null;
        try
        {
            if (c.moveToFirst())
            {
                measure=new Measure();
                do {

                    measure.setTimeStamp(c.getLong(0));
                    measure.setId(c.getLong(1));
                    measure.setValue1(c.getDouble(2));
                    measure.setValue2(c.getDouble(3));
                    measure.setCategory(c.getString(4));
                } while (c.moveToNext());
            }
        }
        finally {
            c.close();

        }

        return measure;
    }

    public void deleteAll()
    {
        measuresDao.deleteAll();
    }
}
