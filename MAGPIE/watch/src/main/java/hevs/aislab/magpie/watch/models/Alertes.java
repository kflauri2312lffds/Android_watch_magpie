package hevs.aislab.magpie.watch.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by teuft on 13.06.2017.
 */

@Entity(
        nameInDb = "Alertes",
        indexes = {
        @Index(value = "measure_id, rule_id", unique = true)
}
)
public class Alertes {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private long measure_id;
    @NotNull
    private long rule_id;

    @ToOne(joinProperty = "measure_id")
    private Measure measure;


    @ToOne(joinProperty = "rule_id")
    private CustomRules rule;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1259768704)
    private transient AlertesDao myDao;







    @Generated(hash = 550830157)
    public Alertes(Long id, long measure_id, long rule_id) {
        this.id = id;
        this.measure_id = measure_id;
        this.rule_id = rule_id;
    }

    @Generated(hash = 1614529119)
    public Alertes() {
    }

    @Generated(hash = 2039194280)
    private transient Long measure__resolvedKey;
    @Generated(hash = 1580548308)
    private transient Long rule__resolvedKey;







    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeasure_id() {
        return measure_id;
    }

    public void setMeasure_id(Long measure_id) {
        this.measure_id = measure_id;
    }

    public long getRules_id() {
        return rule_id;
    }

    public void setRules_id(long rule_id) {
        this.rule_id = rule_id;
    }

    public long getRule_id() {
        return this.rule_id;
    }

    public void setRule_id(long rule_id) {
        this.rule_id = rule_id;
    }

    public void setMeasure_id(long measure_id) {
        this.measure_id = measure_id;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1530824084)
    public Measure getMeasure() {
        long __key = this.measure_id;
        if (measure__resolvedKey == null || !measure__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MeasureDao targetDao = daoSession.getMeasureDao();
            Measure measureNew = targetDao.load(__key);
            synchronized (this) {
                measure = measureNew;
                measure__resolvedKey = __key;
            }
        }
        return measure;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 406434750)
    public void setMeasure(@NotNull Measure measure) {
        if (measure == null) {
            throw new DaoException(
                    "To-one property 'measure_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.measure = measure;
            measure_id = measure.getId();
            measure__resolvedKey = measure_id;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 752533316)
    public CustomRules getRule() {
        long __key = this.rule_id;
        if (rule__resolvedKey == null || !rule__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CustomRulesDao targetDao = daoSession.getCustomRulesDao();
            CustomRules ruleNew = targetDao.load(__key);
            synchronized (this) {
                rule = ruleNew;
                rule__resolvedKey = __key;
            }
        }
        return rule;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1626686981)
    public void setRule(@NotNull CustomRules rule) {
        if (rule == null) {
            throw new DaoException(
                    "To-one property 'rule_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.rule = rule;
            rule_id = rule.getId();
            rule__resolvedKey = rule_id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1523232790)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlertesDao() : null;
    }


   



}
