package com.example.facebookapp.activity.activity.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgoDateParse {


    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long MONTHS_MILLIS = 30 * DAY_MILLIS;
    private static final long YEARS_MILLIS = 12 * MONTHS_MILLIS;


    public static String getTimeAgo(long time) {

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {
            return "";
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a min ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " min ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hrs ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hrs ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else if (diff < 30 * DAY_MILLIS) {
            return diff / DAY_MILLIS + " days ago";
        } else if (diff < 12 * MONTHS_MILLIS) {
            return diff / MONTHS_MILLIS + " months ago";
        } else {


            return diff / YEARS_MILLIS + " yeas ago";
        }
    }


    public static long getTimeInMillsecond(String givenDateString) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(givenDateString);
        return  date.getTime();

    }
}