package io.dourl.mqtt.utils;

import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_TIME;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import io.dourl.mqtt.R;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/11/1
 */
public class MessageTimeUtils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static CharSequence formatDateTime(Context context, long then) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime past = LocalDateTime.ofInstant(Instant.ofEpochMilli(then), ZoneId.systemDefault());

        String time = DateUtils.formatDateTime(context, then, FORMAT_SHOW_TIME);
        if (DateUtils.isToday(then)) {
            return time;
        } else {
            if (ChronoUnit.DAYS.between(past, now) <= 1) {
                return context.getString(R.string.yesterday) + " " + time;
            } else if (ChronoUnit.YEARS.between(past, now) < 1) {
                return DateUtils.formatDateTime(context, then, FORMAT_SHOW_DATE | FORMAT_SHOW_TIME);
            } else {
                return DateUtils.formatDateTime(context, then, FORMAT_SHOW_YEAR | FORMAT_SHOW_DATE);
            }
        }
    }
}
