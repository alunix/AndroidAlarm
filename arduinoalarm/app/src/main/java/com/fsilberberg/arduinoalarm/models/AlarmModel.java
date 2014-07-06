package com.fsilberberg.arduinoalarm.models;

import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.Date;

/**
 * Created by 333fr_000 on 7/5/14.
 */
public class AlarmModel {
    // Default id, never set intentionally in AlarmModel
    private int m_id = 0;
    private Date m_alarmTime;
    private Collection<Days> m_activeDays;
    private boolean m_oneOff;

    public Date getAlarmTime() {
        return m_alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        m_alarmTime = alarmTime;
    }

    public Collection<Days> getActiveDays() {
        return m_activeDays;
    }

    public void setActiveDays(Collection<Days> activeDays) {
        m_activeDays = activeDays;
    }

    public boolean isOneOff() {
        return m_oneOff;
    }

    public void setOneOff(boolean oneOff) {
        m_oneOff = oneOff;
    }

    public int getId() {
        return m_id;
    }

    public void setId(int id) {
        m_id = id;
    }

    /**
     * Gets the UTC wall clock time of the next alarm for this model
     * @return
     */
    public long getMillis() {
        // TODO: Implement
        return 0;
    }
}
