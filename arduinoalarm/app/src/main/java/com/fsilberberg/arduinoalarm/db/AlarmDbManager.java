package com.fsilberberg.arduinoalarm.db;

import android.content.Context;
import android.util.Log;

import com.fsilberberg.arduinoalarm.models.AlarmModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by 333fr_000 on 7/5/14.
 */
public class AlarmDbManager {

    private static AlarmDbManager instance;

    public static AlarmDbManager getInstance() {
        if (instance == null) {
            Log.e(AlarmDbManager.class.getName(), "Database manager accessed without initialization");
            throw new RuntimeException("Error, database manager has not been initialized!");
        }
        return instance;
    }

    public static void initialize(Context context) {
        if (instance != null) {
            instance = new AlarmDbManager(context);
        }
    }

    public static void close() {
        if (instance != null && instance.helper != null) {
            instance.helper.close();
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

    public Collection<AlarmModel> getAllAlarms() {
        try {
            Collection<AlarmDbModel> dbModels = db.queryForAll();
            Log.d(AlarmDbManager.class.getName(), "Retrieved " + dbModels.size() + " alarm models from the database");
            return AlarmDbModel.getModels(dbModels);
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Encountered error when querying for all alarms", e);
            return new LinkedList<>();
        }
    }

    public void createAlarm(AlarmModel toSave) {
        try {
            db.createIfNotExists(new AlarmDbModel(toSave));
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Error when saving new model to database", e);
        }
    }

    public void updateAlarm(AlarmModel toUpdate) {
        try {
            if (db.idExists(toUpdate.getId())) {
                db.update(new AlarmDbModel(toUpdate));
            }
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Error when querying for existing models", e);
        }
    }

    public void deleteAlarm(AlarmModel toDelete) {
        try {
            if (db.idExists(toDelete.getId())) {
                db.deleteById(toDelete.getId());
            }
        } catch (SQLException e) {
            Log.w(AlarmDbManager.class.getName(), "Error when deleting object from database", e);
        }
    }
}
