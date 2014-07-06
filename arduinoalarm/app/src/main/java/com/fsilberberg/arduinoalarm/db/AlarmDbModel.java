package com.fsilberberg.arduinoalarm.db;

import com.fsilberberg.arduinoalarm.models.AlarmModel;
import com.fsilberberg.arduinoalarm.models.Days;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by 333fr_000 on 7/5/14.
 */
@DatabaseTable(tableName = "alarms")
public class AlarmDbModel {

    public static final String ID_COLUMN_NAME = "_id";
    public static final String TIME_COLUMN_NAME = "alarmTime";
    public static final String SUNDAY_COLUMN_NAME = "alarmSunday";
    public static final String MONDAY_COLUMN_NAME = "alarmMonday";
    public static final String TUESDAY_COLUMN_NAME = "alarmTuesday";
    public static final String WEDNESDAY_COLUMN_NAME = "alarmWednesday";
    public static final String THURSDAY_COLUMN_NAME = "alarmThursday";
    public static final String FRIDAY_COLUMN_NAME = "alarmFriday";
    public static final String SATURDAY_COLUMN_NAME = "alarmSaturday";
    public static final String ONE_OFF_COLUMN_NAME = "alarmOneOff";

    @DatabaseField(generatedId = true, columnName = ID_COLUMN_NAME)
    private int id;
    @DatabaseField(columnName = TIME_COLUMN_NAME)
    private Date alarmTime;
    @DatabaseField(columnName = SUNDAY_COLUMN_NAME)
    private boolean sunday;
    @DatabaseField(columnName = MONDAY_COLUMN_NAME)
    private boolean monday;
    @DatabaseField(columnName = TUESDAY_COLUMN_NAME)
    private boolean tuesday;
    @DatabaseField(columnName = WEDNESDAY_COLUMN_NAME)
    private boolean wednesday;
    @DatabaseField(columnName = THURSDAY_COLUMN_NAME)
    private boolean thursday;
    @DatabaseField(columnName = FRIDAY_COLUMN_NAME)
    private boolean friday;
    @DatabaseField(columnName = SATURDAY_COLUMN_NAME)
    private boolean saturday;
    @DatabaseField(columnName = ONE_OFF_COLUMN_NAME)
    private boolean oneOff;

    public AlarmDbModel() {
        // Default constructor for the database
    }

    public AlarmDbModel(AlarmModel model) {
        this.alarmTime = model.getAlarmTime();
        this.oneOff = model.isOneOff();
        Collection<Days> days = model.getActiveDays();
        if (model.getId() != 0) id = model.getId();
        for (Days d : days) {
            switch (d) {
                case SUNDAY:
                    sunday = true;
                    break;
                case MONDAY:
                    monday = true;
                    break;
                case TUESDAY:
                    tuesday = true;
                    break;
                case WEDNESDAY:
                    wednesday = true;
                    break;
                case THURSDAY:
                    thursday = true;
                    break;
                case FRIDAY:
                    friday = true;
                    break;
                case SATURDAY:
                    saturday = true;
                    break;
            }
        }
    }

    public static Collection<AlarmModel> getModels(Collection<AlarmDbModel> dbModels) {
        Collection<AlarmModel> models = new LinkedList<>();
        for (AlarmDbModel dbModel : dbModels) {
            models.add(getModel(dbModel));
        }
        return models;
    }

    public static AlarmModel getModel(AlarmDbModel dbModel) {
        AlarmModel model = new AlarmModel();
        model.setAlarmTime(dbModel.alarmTime);
        model.setOneOff(dbModel.oneOff);
        model.setId(dbModel.id);
        Collection<Days> activeDays = new LinkedList<>();
        addIfActive(dbModel.sunday, Days.SUNDAY, activeDays);
        addIfActive(dbModel.monday, Days.MONDAY, activeDays);
        addIfActive(dbModel.tuesday, Days.TUESDAY, activeDays);
        addIfActive(dbModel.wednesday, Days.WEDNESDAY, activeDays);
        addIfActive(dbModel.thursday, Days.THURSDAY, activeDays);
        addIfActive(dbModel.friday, Days.FRIDAY, activeDays);
        addIfActive(dbModel.saturday, Days.SATURDAY, activeDays);
        model.setActiveDays(activeDays);
        return model;
    }

    private static void addIfActive(boolean active, Days day, Collection<Days> list) {
        if (active) {
            list.add(day);
        }
    }
}
