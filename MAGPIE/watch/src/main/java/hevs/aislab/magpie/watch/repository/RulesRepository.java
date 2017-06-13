package hevs.aislab.magpie.watch.repository;

import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.CustomRulesDao;

/**
 * Created by teuft on 05.06.2017.
 */

public class RulesRepository {

    public static RulesRepository INSTANCE;

    CustomRulesDao rulesDao;

    private RulesRepository(CustomRulesDao rulesDao) {
        this.rulesDao = rulesDao;
    }




    public static RulesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RulesRepository(Core.getInstance().getDaoSession().getCustomRulesDao());
        }
        return INSTANCE;
    }

    public CustomRules getById(long id) {
        List<CustomRules> rules = rulesDao.queryBuilder()
                .where(CustomRulesDao.Properties.Id.eq(id))
                .orderAsc(CustomRulesDao.Properties.Id)
                .list();

        if (rules.size() == 0)
            return null;
        return rules.get(0);
    }

    public List<CustomRules>getAllRules()
    {
        List<CustomRules> rules = rulesDao.queryBuilder()
                .list();
        if (rules.size()==0)
            return null;
        return rules;
    }

    public List<CustomRules>getByCategory(String category)
    {

        List<CustomRules> rules = rulesDao.queryBuilder()
                .where(CustomRulesDao.Properties.Category.eq(category))
                .list();
        if (rules.size()==0)
            return null;
        return rules;
    }



    public void insert(CustomRules rule)
    {
        try
        {
            rulesDao.insert(rule);
        }
        catch (SQLiteConstraintException ex)
        {
            ex.printStackTrace();
        }
    }

    public void delete(CustomRules rule)
    {
        rulesDao.delete(rule);
    }

    public void delete(long id)
    {
        rulesDao.deleteByKey(id);
    }
    public void updateOrCreate(CustomRules rule)
    {
        rulesDao.save(rule);
    }


}
