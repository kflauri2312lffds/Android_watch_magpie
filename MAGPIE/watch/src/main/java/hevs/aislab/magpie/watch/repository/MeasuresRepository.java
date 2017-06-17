package hevs.aislab.magpie.watch.repository;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.CustomRulesDao;
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
}
