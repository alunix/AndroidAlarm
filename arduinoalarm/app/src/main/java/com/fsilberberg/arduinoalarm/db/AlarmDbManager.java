package com.fsilberberg.arduinoalarm.db;

import android.content.Context;
import android.util.Log;

import com.fsilberberg.arduinoalarm.models.AlarmModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Singleton manager for access to the database. This ensures that only one connection to the
 * database is opened, which prevents stale data issues across threads.
 */
public class AlarmDbManager {

    private static AlarmDbManager instance;
    private static final Object _lock = new Object();

    public static AlarmDbManager getInstance() {
        if (instance == null) {
            Log.e(AlarmDbManager.class.getName(), "Database manager accessed without initialization");
            throw new RuntimeException("Error, database manager has not been initialized!");
        }
        return instance;
    }

    public static void initialize(Context context) {
        synchronized (_lock) {
            if (instance != null) {
                instance = new AlarmDbManager(context);
            }
        }
    }

    public static void close() {
        synchronized (_lock) {
            if (instance != null && instance.helper != null) {
                instance.helper.close();
                instance = null;
            }
        }
    }

    private AlarmOpenHelper helper;
    private Dao<AlarmDbModel, Integer> db;

    public AlarmDbManager(Context context) {
        try {
            helper = new AlarmOpenHelper(context);
            db = helper.getAlarmDao();
        } catch (SQLException e) {
            Log.e(AlarmDbManager.class.getName(), "Error getting the dao object", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets all existing alarms
     *
     * @return All alarms in the database. The empty list if none
     */
    public Collection<AlarmModel> getAllAlarms() {
        try {
            Collection<AlarmDbModel> dbModels;
            synchronized (_lock) {
                dbModels = db.queryForAll();
            }
            if (dbModels == null) {
                Log.d(AlarmDbManager.class.getName(), "Retrieved null from the database");
                return new LinkedList<>();
            }
            Log.d(AlarmDbManager.class.getName(), "Retrieved " + dbModels.size() + " alarm models from the database");
            return AlarmDbModel.getModels(dbModels);
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Encountered error when querying for all alarms", e);
            return new LinkedList<>();
        }
    }

    /**
     * Creates an alarm. If the alarm already exists, then nothing will occur
     *
     * @param toSave The alarm to create
     */
    public void createAlarm(AlarmModel toSave) {
        try {
            synchronized (_lock) {
                db.createIfNotExists(new AlarmDbModel(toSave));
            }
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Error when saving new model to database", e);
        }
    }

    /**
     * Updates a prexisting alarm. If the alarm doesn't exist, nothing will occur
     *
     * @param toUpdate The alarm to update
     */
    public void updateAlarm(AlarmModel toUpdate) {
        try {
            synchronized (_lock) {
                if (db.idExists(toUpdate.getId())) {
                    db.update(new AlarmDbModel(toUpdate));
                }
            }
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Error when querying for existing models", e);
        }
    }

    /**
     * Deletes an existing alarm. If the alarm doesn't exist, nothing will occur
     *
     * @param toDelete The alarm to delete
     */
    public void deleteAlarm(AlarmModel toDelete) {
        try {
            synchronized (_lock) {
                if (db.idExists(toDelete.getId())) {
                    db.deleteById(toDelete.getId());
                }
            }
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Error when deleting object from database", e);
        }
    }
}
