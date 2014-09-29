package org.xbill.DNS;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public final class Options
{
  private static Map table;

  static
  {
    try
    {
      refresh();
      return;
    }
    catch (SecurityException localSecurityException)
    {
    }
  }

  public static boolean check(String paramString)
  {
    if (table == null);
    while (table.get(paramString.toLowerCase()) == null)
      return false;
    return true;
  }

  public static void clear()
  {
    table = null;
  }

  public static int intValue(String paramString)
  {
    String str = value(paramString);
    if (str != null)
      try
      {
        int i = Integer.parseInt(str);
        if (i > 0)
          return i;
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
    return -1;
  }

  public static void refresh()
  {
    String str1 = System.getProperty("dnsjava.options");
    if (str1 != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",");
      while (localStringTokenizer.hasMoreTokens())
      {
        String str2 = localStringTokenizer.nextToken();
        int i = str2.indexOf('=');
        if (i == -1)
          set(str2);
        else
          set(str2.substring(0, i), str2.substring(i + 1));
      }
    }
  }

  public static void set(String paramString)
  {
    if (table == null)
      table = new HashMap();
    table.put(paramString.toLowerCase(), "true");
  }

  public static void set(String paramString1, String paramString2)
  {
    if (table == null)
      table = new HashMap();
    table.put(paramString1.toLowerCase(), paramString2.toLowerCase());
  }

  public static void unset(String paramString)
  {
    if (table == null)
      return;
    table.remove(paramString.toLowerCase());
  }

  public static String value(String paramString)
  {
    if (table == null)
      return null;
    return (String)table.get(paramString.toLowerCase());
  }
}