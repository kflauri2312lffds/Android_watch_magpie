package hevs.aislab.magpie.watch.repository;

import java.util.List;

import ch.hevs.aislab.magpie.support.Rule;
import hevs.aislab.magpie.watch.db.Core;
import hevs.aislab.magpie.watch.models.Rules;
import hevs.aislab.magpie.watch.models.RulesDao;

/**
 * Created by teuft on 05.06.2017.
 */

public class RulesRepository {

    public static RulesRepository INSTANCE;

    RulesDao rulesDao;

    private RulesRepository(RulesDao rulesDao) {
        this.rulesDao = rulesDao;
    }

    ;


    public static RulesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RulesRepository(Core.getInstance().getDaoSession().getRulesDao());
        }
        return INSTANCE;
    }

    public Rules getById(long id) {
        List<Rules> rules = rulesDao.queryBuilder()
                .where(RulesDao.Properties.Id.eq(id))
                .orderAsc(RulesDao.Properties.Id)
                .list();

        if (rules.size() == 0)
            return null;
        return rules.get(0);
    }

    public List<Rules>getAllRules()
    {
        List<Rules> rules = rulesDao.queryBuilder()
                .list();
        if (rules.size()==0)
            return null;
        return rules;
    }

    public List<Rules>getByCategory(String category)
    {
        List<Rules> rules = rulesDao.queryBuilder()
                .where(RulesDao.Properties.Category.eq(category))
                .list();
        if (rules.size()==0)
            return null;
        return rules;
    }



    public void insert(Rules rule)
    {
        rulesDao.insert(rule);
    }

    public void delete(Rules rules)
    {
        rulesDao.delete(rules);
    }

    public void delete(long id)
    {
        rulesDao.deleteByKey(id);
    }
    public void updateOrCreate(Rules rule)
    {
        rulesDao.save(rule);

    }

}
