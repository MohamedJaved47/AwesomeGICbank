package util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static LocalDate convertStringToDate(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, formatter);
    }

    public static String convertDateToString(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(date);
    }

    public static YearMonth convertStringToYearMonth(String dateString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return YearMonth.parse(dateString, formatter);
    }

    public static String formatDecimal(Double input, String format) {
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.UP);
        return df.format(input);
    }
}
