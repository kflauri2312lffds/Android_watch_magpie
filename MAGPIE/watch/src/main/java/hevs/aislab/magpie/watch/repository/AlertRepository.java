package hevs.aislab.magpie.watch.repository;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.AlertesDao;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.Measure;
import hevs.aislab.magpie.watch.models.MeasureDao;

/**
 * Created by teuft on 13.06.2017.
 */

public class AlertRepository {

    private AlertesDao alertesDao;
    private static AlertRepository INSTANCE;

    private AlertRepository(AlertesDao alertesDao)
    {
        this.alertesDao=alertesDao;
    }

    public static AlertRepository getINSTANCE()
    {
        if (INSTANCE==null)
            INSTANCE=new AlertRepository(Core.getInstance().getDaoSession().getAlertesDao());
        return INSTANCE;
    }

    public void insert(Alertes alerte)
    {
        alertesDao.insert(alerte);
    }
    public Alertes getByIdWithRelations(long id)
    {
        return  alertesDao.queryBuilder()
                .where(AlertesDao.Properties.Id.eq(id)
                ).unique();

    }

    public List<Alertes> getAll()
    {
        return  alertesDao.queryBuilder()
                .orderDesc(AlertesDao.Properties.Measure_id)
                .list();
    }

    public List<Alertes>getAllByCategory(String category)
    {
        QueryBuilder<Alertes> queryBuilder = alertesDao.queryBuilder();
        queryBuilder.join(AlertesDao.Properties.Measure_id,Measure.class)
                .where(MeasureDao.Properties.Category.eq(category));
        return queryBuilder
                .orderDesc(AlertesDao.Properties.Measure_id)
                .list();
    }

    public List<Alertes>getAllByCategoryBetweenTimeStamp(String category, long startTimeStamp, long endTimeStamp )
    {
        QueryBuilder<Alertes> queryBuilder = alertesDao.queryBuilder();
        queryBuilder.join(AlertesDao.Properties.Measure_id,Measure.class)
                .where(MeasureDao.Properties.Category.eq(category))
                .where(MeasureDao.Properties.TimeStamp.between(startTimeStamp,endTimeStamp));
        return queryBuilder.list();

    }




}
