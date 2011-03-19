package fmiprojects.weather.util;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateUtil {
  public static String now(String format) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(cal.getTime());
  }
  
  public static String format(Date date, String format) {
	    SimpleDateFormat sdf = new SimpleDateFormat(format);
	    return sdf.format(date.getTime());	  
  }
  
}
