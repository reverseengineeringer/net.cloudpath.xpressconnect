package org.xbill.DNS;

import java.util.HashMap;

class Mnemonic
{
  static final int CASE_LOWER = 3;
  static final int CASE_SENSITIVE = 1;
  static final int CASE_UPPER = 2;
  private static Integer[] cachedInts = new Integer[64];
  private String description;
  private int max;
  private boolean numericok;
  private String prefix;
  private HashMap strings;
  private HashMap values;
  private int wordcase;

  static
  {
    for (int i = 0; i < cachedInts.length; i++)
      cachedInts[i] = new Integer(i);
  }

  public Mnemonic(String paramString, int paramInt)
  {
    this.description = paramString;
    this.wordcase = paramInt;
    this.strings = new HashMap();
    this.values = new HashMap();
    this.max = 2147483647;
  }

  private int parseNumeric(String paramString)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      if (i >= 0)
      {
        int j = this.max;
        if (i <= j)
          return i;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return -1;
  }

  private String sanitize(String paramString)
  {
    if (this.wordcase == 2)
      paramString = paramString.toUpperCase();
    while (this.wordcase != 3)
      return paramString;
    return paramString.toLowerCase();
  }

  public static Integer toInteger(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < cachedInts.length))
      return cachedInts[paramInt];
    return new Integer(paramInt);
  }

  public void add(int paramInt, String paramString)
  {
    check(paramInt);
    Integer localInteger = toInteger(paramInt);
    String str = sanitize(paramString);
    this.strings.put(str, localInteger);
    this.values.put(localInteger, str);
  }

  public void addAlias(int paramInt, String paramString)
  {
    check(paramInt);
    Integer localInteger = toInteger(paramInt);
    String str = sanitize(paramString);
    this.strings.put(str, localInteger);
  }

  public void addAll(Mnemonic paramMnemonic)
  {
    if (this.wordcase != paramMnemonic.wordcase)
      throw new IllegalArgumentException(paramMnemonic.description + ": wordcases do not match");
    this.strings.putAll(paramMnemonic.strings);
    this.values.putAll(paramMnemonic.values);
  }

  public void check(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.max))
      throw new IllegalArgumentException(this.description + " " + paramInt + "is out of range");
  }

  public String getText(int paramInt)
  {
    check(paramInt);
    String str1 = (String)this.values.get(toInteger(paramInt));
    if (str1 != null)
      return str1;
    String str2 = Integer.toString(paramInt);
    if (this.prefix != null)
      return this.prefix + str2;
    return str2;
  }

  public int getValue(String paramString)
  {
    String str = sanitize(paramString);
    Integer localInteger = (Integer)this.strings.get(str);
    int i;
    if (localInteger != null)
      i = localInteger.intValue();
    do
    {
      return i;
      if ((this.prefix == null) || (!str.startsWith(this.prefix)))
        break;
      i = parseNumeric(str.substring(this.prefix.length()));
    }
    while (i >= 0);
    if (this.numericok)
      return parseNumeric(str);
    return -1;
  }

  public void setMaximum(int paramInt)
  {
    this.max = paramInt;
  }

  public void setNumericAllowed(boolean paramBoolean)
  {
    this.numericok = paramBoolean;
  }

  public void setPrefix(String paramString)
  {
    this.prefix = sanitize(paramString);
  }
}