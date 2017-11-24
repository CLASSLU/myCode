package kd.idp.psidpapp.opt.sys.util;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil
{
  public static Calendar getCalendar(Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  public static String getCurrentDate(String pattern)
  {
    Date date = new Date();

    String s = dateToStr(date, pattern);
    return s;
  }

  public static Date parseDate(String source)
  {
    if ((source == null) || (source.length() == 0))
      return null;
    String[] datePattern = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss" };
    Date date = null;
    for (int i = 0; i < datePattern.length; i++)
    {
      date = parseDate(source, datePattern[i]);
      if (date != null)
        break;
    }
    return date;
  }

  public static Calendar parseCal(String source)
  {
    if ((source == null) || (source.length() == 0))
      return null;
    String[] datePattern = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss" };
    Calendar date = null;
    for (int i = 0; i < datePattern.length; i++)
    {
      date = parseCal(source, datePattern[i]);
      if (date != null)
        break;
    }
    return date;
  }

  public static Date parseDate(String dateStr, String format)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Date date = null;
    try
    {
      date = dateFormat.parse(dateStr);
    }
    catch (ParseException localParseException)
    {
    }

    return date;
  }

  public static Calendar parseCal(String dateStr, String format)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Calendar date = Calendar.getInstance();
    try
    {
      date.setTime(dateFormat.parse(dateStr));
    }
    catch (ParseException localParseException)
    {
    }

    return date;
  }

  public static int diffDate(Date end, Date start)
  {
    return (int)(getMillis(end) / 86400000L - getMillis(start) / 86400000L);
  }

  public static long diffSecond(Date end, Date start)
  {
    return (getMillis(end) - getMillis(start)) / 1000L;
  }

  private static long getMillis(Date date)
  {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    return c.getTimeInMillis();
  }

  public static String getNow()
  {
    Date now = new Date();
    return formatDateTime(now);
  }

  public static String getNow(String pattern)
  {
    Date now = new Date();
    return formatDate(now, pattern);
  }

  public static String formatDate(String dateStr, String pattern)
  {
    Date date = parseDate(dateStr);
    return formatDate(date, pattern);
  }

  public static String formatTime(Date date)
  {
    String pattern = "HH:mm:ss";
    return formatDate(date, pattern);
  }

  public static String formatDate(Date date)
  {
    String pattern = "yyyy-MM-dd";
    return formatDate(date, pattern);
  }

  public static String formatDateTime(Date date)
  {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    return formatDate(date, pattern);
  }

  public static String formatDateTime(Calendar cal)
  {
    String pattern = "yyyy-MM-dd HH:mm:ss";
    if (cal == null)
    {
      return "";
    }

    return formatDate(cal.getTime(), pattern);
  }

  public static String formatDate(Date date, String pattern)
  {
    String dateTimePattern = pattern;

    if (date == null)
    {
      if ((dateTimePattern.length() > 2) && (dateTimePattern.substring(dateTimePattern.length() - 2).equals("_e")))
      {
        dateTimePattern = dateTimePattern.replaceAll("(yy|YY){1,2}", "        ");
        dateTimePattern = dateTimePattern.replaceAll("(M){1,2}", "    ");
        dateTimePattern = dateTimePattern.replaceAll("(d|D){1,2}", "    ");
        dateTimePattern = dateTimePattern.replaceAll("(h|H){1,2}", "    ");
        dateTimePattern = dateTimePattern.replaceAll("(m){1,2}", "    ");
        dateTimePattern = dateTimePattern.replaceAll("(s|S){1,2}", "    ");

        return dateTimePattern.substring(0, dateTimePattern.length() - 2);
      }

      return "";
    }

    if ((dateTimePattern.length() > 2) && (dateTimePattern.substring(dateTimePattern.length() - 2).equals("_e")))
    {
      dateTimePattern = dateTimePattern.substring(0, dateTimePattern.length() - 2);
    }

    if ((dateTimePattern == null) || (dateTimePattern.length() == 0)) {
      return formatDateTime(date);
    }
    SimpleDateFormat sdf = new SimpleDateFormat();
    try
    {
      sdf.applyPattern(dateTimePattern);
    }
    catch (IllegalArgumentException exp)
    {
      sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
    }
    return sdf.format(date);
  }

  public static int getCurrYear()
  {
    Calendar cal = Calendar.getInstance();
    return cal.get(1);
  }

  public static int getCurrMonth()
  {
    Calendar cal = Calendar.getInstance();
    return cal.get(2) + 1;
  }

  public static String getCurrtWeekDay()
  {
    Date date = new Date();
    String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int w = cal.get(7) - 1;
    if (w < 0)
      w = 0;
    return weekDays[w];
  }

  public static int getCurrDay()
  {
    Calendar cal = Calendar.getInstance();
    return cal.get(5);
  }

  public static int getWeekOfYear(Date date)
  {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(2);
    c.setMinimalDaysInFirstWeek(7);
    c.setTime(date);

    return c.get(3);
  }

  public static int getWeekOfYear()
  {
    return getWeekOfYear(new Date());
  }

  public static int getMaxWeekNumOfYear(int year)
  {
    Calendar c = new GregorianCalendar();
    c.set(year, 11, 31, 23, 59, 59);

    return getWeekOfYear(c.getTime());
  }

  public static Date getFirstDayOfWeek(int year, int week)
  {
    Calendar c = new GregorianCalendar();
    c.set(1, year);
    c.set(2, 0);
    c.set(5, 1);

    Calendar cal = (GregorianCalendar)c.clone();
    cal.add(5, week * 7);

    return getFirstDayOfWeek(cal.getTime());
  }

  public static Date getLastDayOfWeek(int year, int week)
  {
    Calendar c = new GregorianCalendar();
    c.set(1, year);
    c.set(2, 0);
    c.set(5, 1);

    Calendar cal = (GregorianCalendar)c.clone();
    cal.add(5, week * 7);

    return getLastDayOfWeek(cal.getTime());
  }

  public static Date getFirstDayOfWeek(Date date)
  {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(2);
    c.setTime(date);
    c.set(7, c.getFirstDayOfWeek());
    return c.getTime();
  }

  public static Date getLastDayOfWeek(Date date)
  {
    Calendar c = new GregorianCalendar();
    c.setFirstDayOfWeek(2);
    c.setTime(date);
    c.set(7, c.getFirstDayOfWeek() + 6);
    return c.getTime();
  }

  public static Date getFirstDayOfFirstWeek(int year, int month)
  {
    Calendar c = Calendar.getInstance();
    c.setFirstDayOfWeek(2);
    c.set(year, month, 1);

    c.set(8, 1);
    c.set(7, 2);

    return c.getTime();
  }

  public static Date getYesterday()
  {
    Calendar c = new GregorianCalendar();
    c.set(5, c.get(5) - 1);
    Date date = c.getTime();
    return date;
  }

  public static Date getTomorrow()
  {
    Calendar c = new GregorianCalendar();
    c.set(5, c.get(5) + 1);
    Date date = c.getTime();
    return date;
  }

  public static Date getPreMonth(Date date)
  {
    GregorianCalendar c = new GregorianCalendar();
    c.setTime(date);
    c.add(2, -1);
    return c.getTime();
  }

  public static int getQuarter(Date date)
  {
    int month = date.getMonth();
    if (month < 4)
    {
      return 1;
    }
    if (month < 7)
    {
      return 2;
    }
    if (month < 10)
    {
      return 3;
    }

    return 4;
  }

  public static int getDayCountByTwoDates(String strDate1, String strDate2)
  {
    int differ = 0;
    try
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      Date date1 = format.parse(strDate1);
      Date date2 = format.parse(strDate2);
      differ = (int)((date1.getTime() - date2.getTime()) / 86400000L);
    }
    catch (Exception e)
    {
      System.out.println("比较两个日期相差天数出错：" + e.getMessage());
      differ = -1;
    }

    return differ;
  }

  public static float getDayCountFloatByTwoDates(String strDate1, String strDate2)
  {
    float differ = 0.0F;
    try
    {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
      Date date1 = format.parse(strDate1);
      Date date2 = format.parse(strDate2);
      BigDecimal b1 = new BigDecimal(date1.getTime());
      BigDecimal b2 = new BigDecimal(date2.getTime());
      double b3 = b1.subtract(b2).doubleValue();
      BigDecimal b4 = new BigDecimal(b3);
      BigDecimal b5 = new BigDecimal("86400000");
      double result = b4.divide(b5, 10, 1).doubleValue();
      differ = Float.parseFloat(String.valueOf(result));
    }
    catch (Exception e)
    {
      System.out.println("比较两个日期相差天数出错：" + e.getMessage());
      differ = -1.0F;
    }

    return differ;
  }

  public static String dateToStr(Date date, String pattern)
  {
    if (date == null) {
      return "";
    }
    String pat = pattern;

    if ((pat == null) || (pat.length() == 0)) {
      pat = "yyyy-MM-dd HH:mm:ss";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pat);

    return sdf.format(date);
  }

  public static Calendar addSeconds(Calendar cal, int amount) {
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(cal.getTime());
    cal2.add(13, amount);
    return cal2;
  }
}