package com.fsilberberg.arduinoalarm.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fsilberberg.arduinoalarm.db.AlarmDbManager;
import com.fsilberberg.arduinoalarm.models.AlarmModel;

import java.util.Collection;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Re-enable alarms
        enableAlarms(context);

        // TODO: Start arduino service
    }

    private void enableAlarms(Context context) {
        Collection<AlarmModel> alarms = AlarmDbManager.getInstance().getAllAlarms();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (AlarmModel alarm : alarms) {
            PendingIntent pi = PendingIntent.getBroadcast(context, alarm.getId(),
                    new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getNextTime().getMillis(), pi);
        }
    }
}
