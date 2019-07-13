package android.train.mipt_school.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTools {
    private static DateFormat dayMonth = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private static DateFormat hourMinute = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private static final String[] monthName = new String[]{
            "января", "февраля", "марта",
            "апреля", "мая", "июня",
            "июля", "августа", "сентября",
            "октября", "ноября", "декабря"};
    private static final String[] dayOfWeekName = new String[]{
            "Вс", "Пн", "Вт", "Ср", "Чт", "Пт", "Cб"
    };

    public static final long DAY = 86400000;

    public static String dayMonthFormat(Date date) {
        // Форматирование времени как "день.месяц"
        return dayMonth.format(date);
    }

    public static String hourMinuteFormat(Date date) {
        // Форматирование времени как "час:минута"
        return hourMinute.format(date);
    }

    public static String dayOfWeekFormat(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String dayOfWeek = dayOfWeekName[calendar.get(Calendar.DAY_OF_WEEK) - 1];

        return dayOfWeek;
    }

    public static boolean sameDay(Date a, Date b) {
        Calendar first = Calendar.getInstance();
        first.setTime(a);

        Calendar second = Calendar.getInstance();
        second.setTime(b);

        return (first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
                first.get(Calendar.MONTH) == second.get(Calendar.MONTH) &&
                first.get(Calendar.DAY_OF_MONTH) == second.get(Calendar.DAY_OF_MONTH));
    }

    public static String dayMonthToString(int day, int month) {
        return Integer.toString(day) + " " + monthName[month];
    }

    public static long getTimeSinceMidnight() {
        Calendar date = Calendar.getInstance();
        long currentTime = date.getTime().getTime();

        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        long midnightTime = date.getTime().getTime();

        return currentTime - midnightTime;
    }

    public static Date getCurrentDayStart() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTime();
    }
}
