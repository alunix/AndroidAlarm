package com.fsilberberg.arduinoalarm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fsilberberg.arduinoalarm.models.AlarmModel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by 333fr_000 on 7/5/14.
 */
public class AlarmOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "alarms.db";

    private Dao<AlarmDbModel, Integer> db;

    public AlarmOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(AlarmOpenHelper.class.getName(), "Creating Alarms Database version " + DATABASE_VERSION);
            TableUtils.createTable(connectionSource, AlarmDbModel.class);
        } catch (SQLException e) {
            Log.i(AlarmOpenHelper.class.getName(), "Could not create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(AlarmOpenHelper.class.getName(), "Upgraging Alarms Database version from " + oldVersion + " to " + newVersion);
            TableUtils.dropTable(connectionSource, AlarmDbModel.class, true);
            TableUtils.createTable(connectionSource, AlarmDbModel.class);
        } catch (SQLException e) {
            Log.i(AlarmOpenHelper.class.getName(), "Could not upgrade database", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<AlarmDbModel, Integer> getAlarmDao() throws SQLException {
        if (db == null) {
            db = getDao(AlarmDbModel.class);
        }
        return db;
    }
}
