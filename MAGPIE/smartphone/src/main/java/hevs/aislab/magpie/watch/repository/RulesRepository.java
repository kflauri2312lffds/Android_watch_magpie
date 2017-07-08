package hevs.aislab.magpie.watch.repository;

import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.CustomRules;
import hevs.aislab.magpie.watch.models.CustomRulesDao;

/**
 * SINGLE TON CLASS TO HANDLE ACCESS TO THE RULES MODEL
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

    public void insert(CustomRules rule)
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


    }
