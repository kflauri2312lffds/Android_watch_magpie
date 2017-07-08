package hevs.aislab.magpie.smartphone.db;

import android.database.sqlite.SQLiteDatabase;

import hevs.aislab.magpie.smartphone.models.DaoMaster;
import hevs.aislab.magpie.smartphone.models.DaoSession;

//import hevs.aislab.magpie.smartphone.models.DaoMaster;
//import hevs.aislab.magpie.smartphone.models.DaoSession;

/**
 * This class is used to initiliaze the the DB & the ORM GreenDAO. Initialize it directly in the your first activity.
 * To initi the database in an activity, do the following method:
 *
 *           Core.getInstance().setHelper(new DaoMaster.DevOpenHelper(this,"{nameOfYourDB}",null));
 *           Core.getInstance().setDb(Core.getInstance().getHelper().getWritableDatabase());
 *           Core.getInstance().setDaoMaster(new DaoMaster(Core.getInstance().getDb()));
 *           Core.getInstance().setDaoSession( (Core.getInstance().getDaoMaster().newSession()));
 */


//singletone pattern
public class Core {

    private static Core instance;

    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static void setInstance(Core instance) {
        Core.instance = instance;
    }


    private Core() {}

    public static  Core getInstance()
    {
        if (instance==null)
            instance=new Core();
        return instance;
    }


    public DaoMaster.DevOpenHelper getHelper() {
        return helper;
    }

    public void setHelper(DaoMaster.DevOpenHelper helper) {
        this.helper = helper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public void setDaoMaster(DaoMaster daoMaster) {
        this.daoMaster = daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

}
