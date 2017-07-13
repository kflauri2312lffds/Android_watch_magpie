package hevs.aislab.magpie.watch.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.DaoException;

/**
 * Rule object. It will be manage by GreenDAO orm
 */

@Entity(
        nameInDb = "Rules",
        indexes = {
                @Index(value = "category", unique = true)
        }
)
public class CustomRules  {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String category;

    private Double val_1_min;

    private Double val_1_max;

    private Double val__2_min;

    private Double val_2_max;

    private String constraint_1;

    private String constraint_2;

    private String constraint_3;

    private Long timeWindow;

    //RELATIONSHIP
    @ToMany(referencedJoinProperty = "rule_id")
    private List<Alertes> alertes;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 80030747)
    private transient CustomRulesDao myDao;


    @Generated(hash = 1519990713)
    public CustomRules(Long id, @NotNull String category, Double val_1_min,
            Double val_1_max, Double val__2_min, Double val_2_max,
            String constraint_1, String constraint_2, String constraint_3,
            Long timeWindow) {
        this.id = id;
        this.category = category;
        this.val_1_min = val_1_min;
        this.val_1_max = val_1_max;
        this.val__2_min = val__2_min;
        this.val_2_max = val_2_max;
        this.constraint_1 = constraint_1;
        this.constraint_2 = constraint_2;
        this.constraint_3 = constraint_3;
        this.timeWindow = timeWindow;
    }

    @Generated(hash = 922913921)
    public CustomRules() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getVal_1_min() {
        return val_1_min;
    }

    public void setVal_1_min(Double val_1_min) {
        this.val_1_min = val_1_min;
    }

    public Double getVal_1_max() {
        return val_1_max;
    }

    public void setVal_1_max(Double val_1_max) {
        this.val_1_max = val_1_max;
    }

    public Double getVal__2_min() {
        return val__2_min;
    }

    public void setVal__2_min(Double val__2_min) {
        this.val__2_min = val__2_min;
    }

    public Double getVal_2_max() {
        return val_2_max;
    }

    public void setVal_2_max(Double val_2_max) {
        this.val_2_max = val_2_max;
    }

    public String getConstraint_1() {
        return constraint_1;
    }

    public void setConstraint_1(String constraint_1) {
        this.constraint_1 = constraint_1;
    }

    public String getConstraint_2() {
        return constraint_2;
    }

    public void setConstraint_2(String constraint_2) {
        this.constraint_2 = constraint_2;
    }

    public String getConstraint_3() {
        return constraint_3;
    }

    public void setConstraint_3(String constraint_3) {
        this.constraint_3 = constraint_3;
    }


    public Long getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(Long timeWindow) {
        this.timeWindow = timeWindow;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1351549838)
    public List<Alertes> getAlertes() {
        if (alertes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AlertesDao targetDao = daoSession.getAlertesDao();
            List<Alertes> alertesNew = targetDao._queryCustomRules_Alertes(id);
            synchronized (this) {
                if (alertes == null) {
                    alertes = alertesNew;
                }
            }
        }
        return alertes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2079891135)
    public synchronized void resetAlertes() {
        alertes = null;
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
    @Generated(hash = 1390785370)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCustomRulesDao() : null;
    }
}
