package hevs.aislab.magpie.watch.db;

import android.database.sqlite.SQLiteDatabase;

import hevs.aislab.magpie.watch.models.DaoMaster;
import hevs.aislab.magpie.watch.models.DaoSession;

/**
 * Created by teuft on 31.05.2017.
 */


//singletone pattern
public class Core {

    private static Core instance;

    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;


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
