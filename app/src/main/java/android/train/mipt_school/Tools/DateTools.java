package android.train.mipt_school.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DateTools {
    private static DateFormat dayMonth = new SimpleDateFormat("dd.MM", Locale.getDefault());
    private static DateFormat hourMinute = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static String[] monthName = new String[]{
            "января", "февраля", "марта",
            "апреля", "мая", "июня",
            "июля", "августа", "сентября",
            "октября", "ноября", "декабря"};

    public static final long DAY = 86400000;

    public static String dayMonthFormat(Date date) {
        // Форматирование времени как "день.месяц"
        return dayMonth.format(date);
    }

    public static String hourMinuteFormat(Date date) {
        // Форматирование времени как "час:минута"
        return hourMinute.format(date);
    }

    public static boolean sameDay(Date a, Date b) {
        // проверяет, что дата a, задающая начало дня, и дата b, находятся в одном дне
        long first = a.getTime();
        long second = b.getTime();
        return (second - first < DAY && second > first);
    }

    public static String dayMonthToString(int day, int month) {
        return Integer.toString(day) + " " + monthName[month];
    }
}
