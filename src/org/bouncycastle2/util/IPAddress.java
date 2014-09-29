package org.bouncycastle2.util;

public class IPAddress
{
  private static boolean isMaskValue(String paramString, int paramInt)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      boolean bool = false;
      if (i >= 0)
      {
        bool = false;
        if (i <= paramInt)
          bool = true;
      }
      return bool;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return false;
  }

  public static boolean isValid(String paramString)
  {
    return (isValidIPv4(paramString)) || (isValidIPv6(paramString));
  }

  public static boolean isValidIPv4(String paramString)
  {
    if (paramString.length() == 0);
    while (true)
    {
      return false;
      int i = 0;
      String str = paramString + ".";
      int j = 0;
      int k;
      if (j < str.length())
      {
        k = str.indexOf('.', j);
        if (k > j);
      }
      else
      {
        if (i != 4)
          continue;
        return true;
      }
      if (i != 4)
        try
        {
          int m = Integer.parseInt(str.substring(j, k));
          if ((m >= 0) && (m <= 255))
          {
            j = k + 1;
            i++;
          }
        }
        catch (NumberFormatException localNumberFormatException)
        {
        }
    }
    return false;
  }

  public static boolean isValidIPv4WithNetmask(String paramString)
  {
    int i = paramString.indexOf("/");
    String str = paramString.substring(i + 1);
    boolean bool1 = false;
    if (i > 0)
    {
      boolean bool2 = isValidIPv4(paramString.substring(0, i));
      bool1 = false;
      if (bool2)
        if (!isValidIPv4(str))
        {
          boolean bool3 = isMaskValue(str, 32);
          bool1 = false;
          if (!bool3);
        }
        else
        {
          bool1 = true;
        }
    }
    return bool1;
  }

  public static boolean isValidIPv6(String paramString)
  {
    if (paramString.length() == 0);
    int i;
    String str1;
    int j;
    int k;
    label36: int m;
    String str2;
    do
    {
      do
      {
        do
        {
          return false;
          i = 0;
          str1 = paramString + ":";
          j = 0;
          k = 0;
          if (k < str1.length())
          {
            m = str1.indexOf(':', k);
            if (m >= k)
              break;
          }
        }
        while ((i != 8) && (j == 0));
        return true;
      }
      while (i == 8);
      if (k == m)
        break label174;
      str2 = str1.substring(k, m);
      if ((m != -1 + str1.length()) || (str2.indexOf('.') <= 0))
        break;
    }
    while (!isValidIPv4(str2));
    i++;
    while (true)
    {
      while (true)
      {
        k = m + 1;
        i++;
        break label36;
        try
        {
          int n = Integer.parseInt(str1.substring(k, m), 16);
          if (n < 0)
            break;
          if (n > 65535)
            return false;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          return false;
        }
      }
      label174: if ((m != 1) && (m != -1 + str1.length()) && (j != 0))
        break;
      j = 1;
    }
  }

  public static boolean isValidIPv6WithNetmask(String paramString)
  {
    int i = paramString.indexOf("/");
    String str = paramString.substring(i + 1);
    boolean bool1 = false;
    if (i > 0)
    {
      boolean bool2 = isValidIPv6(paramString.substring(0, i));
      bool1 = false;
      if (bool2)
        if (!isValidIPv6(str))
        {
          boolean bool3 = isMaskValue(str, 128);
          bool1 = false;
          if (!bool3);
        }
        else
        {
          bool1 = true;
        }
    }
    return bool1;
  }

  public static boolean isValidWithNetMask(String paramString)
  {
    return (isValidIPv4WithNetmask(paramString)) || (isValidIPv6WithNetmask(paramString));
  }
}