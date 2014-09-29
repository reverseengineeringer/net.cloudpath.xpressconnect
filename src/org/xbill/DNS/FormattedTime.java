package org.xbill.DNS;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

final class FormattedTime
{
  private static NumberFormat w2 = new DecimalFormat();
  private static NumberFormat w4;

  static
  {
    w2.setMinimumIntegerDigits(2);
    w4 = new DecimalFormat();
    w4.setMinimumIntegerDigits(4);
    w4.setGroupingUsed(false);
  }

  public static String format(Date paramDate)
  {
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
    StringBuffer localStringBuffer = new StringBuffer();
    localGregorianCalendar.setTime(paramDate);
    localStringBuffer.append(w4.format(localGregorianCalendar.get(1)));
    localStringBuffer.append(w2.format(1 + localGregorianCalendar.get(2)));
    localStringBuffer.append(w2.format(localGregorianCalendar.get(5)));
    localStringBuffer.append(w2.format(localGregorianCalendar.get(11)));
    localStringBuffer.append(w2.format(localGregorianCalendar.get(12)));
    localStringBuffer.append(w2.format(localGregorianCalendar.get(13)));
    return localStringBuffer.toString();
  }

  public static Date parse(String paramString)
    throws TextParseException
  {
    if (paramString.length() != 14)
      throw new TextParseException("Invalid time encoding: " + paramString);
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
    localGregorianCalendar.clear();
    try
    {
      localGregorianCalendar.set(Integer.parseInt(paramString.substring(0, 4)), -1 + Integer.parseInt(paramString.substring(4, 6)), Integer.parseInt(paramString.substring(6, 8)), Integer.parseInt(paramString.substring(8, 10)), Integer.parseInt(paramString.substring(10, 12)), Integer.parseInt(paramString.substring(12, 14)));
      return localGregorianCalendar.getTime();
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw new TextParseException("Invalid time encoding: " + paramString);
  }
}