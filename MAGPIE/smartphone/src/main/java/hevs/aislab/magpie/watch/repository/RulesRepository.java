package hevs.aislab.magpie.watch.repository;

import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.CustomRulesDao;

/**
 * this class will take car of the rule CRUD access. Querry are made by the ORM. It's a singletone class
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

    public void insertOrUpdate(CustomRules rule)
    {
        try
        {
            rulesDao.insertOrReplace(rule);
        }
        catch (SQLiteConstraintException ex)
        {
            ex.printStackTrace();
        }
    }

    public List<CustomRules> getAll()
    {
        return rulesDao.queryBuilder()
                .list();
    }

    public CustomRules getByCategory(String category)
    {

        return rulesDao.queryBuilder()
                .where(CustomRulesDao.Properties.Category.eq(category))
                .unique();
    }




    }

