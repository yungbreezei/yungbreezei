import java.util.Calendar;

public class ClimateInfo {
        public static boolean isColdestMonth(int currentMonth) {
        // JANUARY is the coldest month in London
        return currentMonth == Calendar.JANUARY;
    }
        public static boolean isWettestMonth(int currentMonth) {
        // NOVEMBER is the coldest month in London
        return currentMonth == Calendar.NOVEMBER;
    }
}
