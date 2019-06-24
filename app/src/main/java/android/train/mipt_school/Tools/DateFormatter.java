package android.train.mipt_school.Tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {
    private static DateFormat dayMonth = new SimpleDateFormat("dd.MM", Locale.getDefault());

    public static String dayMonthFormat(Date date) {
        // Форматирование времени как "день.месяц"
        return dayMonth.format(date);
    }
}
