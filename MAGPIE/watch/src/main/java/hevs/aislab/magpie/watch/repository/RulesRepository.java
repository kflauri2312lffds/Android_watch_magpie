package hevs.aislab.magpie.watch.repository;

import android.database.sqlite.SQLiteConstraintException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.CustomRulesDao;

/**
 * Created by teuft on 05.06.2017.
 */

public class RulesRepository {

    private static RulesRepository INSTANCE;

    private CustomRulesDao rulesDao;

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

    public Map<String, CustomRules> getAllRules()
    {
        List<CustomRules> rules = rulesDao.queryBuilder()
                .list();
        if (rules.size()==0)
            return null;


        //Create the map for the return

        Map<String, CustomRules>mapRules=new HashMap<>();

        for (CustomRules aRules : rules)
        {
            mapRules.put(aRules.getCategory(),aRules);
        }
        return mapRules;
    }

    public CustomRules getByCategory(String category)
    {

        return rulesDao.queryBuilder()
                .where(CustomRulesDao.Properties.Category.eq(category))
                .unique();
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

    public void update(CustomRules rule)
    {
        rule.update();
    }


}
