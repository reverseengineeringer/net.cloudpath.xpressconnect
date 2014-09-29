package org.xbill.DNS;

public final class TTL
{
  public static final long MAX_VALUE = 2147483647L;

  static void check(long paramLong)
  {
    if ((paramLong < 0L) || (paramLong > 2147483647L))
      throw new InvalidTTLException(paramLong);
  }

  public static String format(long paramLong)
  {
    check(paramLong);
    StringBuffer localStringBuffer = new StringBuffer();
    long l1 = paramLong % 60L;
    long l2 = paramLong / 60L;
    long l3 = l2 % 60L;
    long l4 = l2 / 60L;
    long l5 = l4 % 24L;
    long l6 = l4 / 24L;
    long l7 = l6 % 7L;
    long l8 = l6 / 7L;
    if (l8 > 0L)
      localStringBuffer.append(l8 + "W");
    if (l7 > 0L)
      localStringBuffer.append(l7 + "D");
    if (l5 > 0L)
      localStringBuffer.append(l5 + "H");
    if (l3 > 0L)
      localStringBuffer.append(l3 + "M");
    if ((l1 > 0L) || ((l8 == 0L) && (l7 == 0L) && (l5 == 0L) && (l3 == 0L)))
      localStringBuffer.append(l1 + "S");
    return localStringBuffer.toString();
  }

  public static long parse(String paramString, boolean paramBoolean)
  {
    if ((paramString == null) || (paramString.length() == 0) || (!Character.isDigit(paramString.charAt(0))))
      throw new NumberFormatException();
    long l1 = 0L;
    long l2 = 0L;
    for (int i = 0; i < paramString.length(); i++)
    {
      char c = paramString.charAt(i);
      long l3 = l1;
      if (Character.isDigit(c))
      {
        l1 = 10L * l1 + Character.getNumericValue(c);
        if (l1 < l3)
          throw new NumberFormatException();
      }
      else
      {
        switch (Character.toUpperCase(c))
        {
        default:
          throw new NumberFormatException();
        case 'W':
          l1 *= 7L;
        case 'D':
          l1 *= 24L;
        case 'H':
          l1 *= 60L;
        case 'M':
          l1 *= 60L;
        case 'S':
        }
        l2 += l1;
        l1 = 0L;
        if (l2 > 4294967295L)
          throw new NumberFormatException();
      }
    }
    if (l2 == 0L)
      l2 = l1;
    if (l2 > 4294967295L)
      throw new NumberFormatException();
    if ((l2 > 2147483647L) && (paramBoolean))
      l2 = 2147483647L;
    return l2;
  }

  public static long parseTTL(String paramString)
  {
    return parse(paramString, true);
  }
}