package hevs.aislab.magpie.watch.repository;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.Alertes;
import hevs.aislab.magpie.watch.models.AlertesDao;

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
                .list();
    }




}
