package org.xbill.DNS;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class LOCRecord extends Record
{
  private static final long serialVersionUID = 9058224788126750409L;
  private static NumberFormat w2 = new DecimalFormat();
  private static NumberFormat w3;
  private long altitude;
  private long hPrecision;
  private long latitude;
  private long longitude;
  private long size;
  private long vPrecision;

  static
  {
    w2.setMinimumIntegerDigits(2);
    w3 = new DecimalFormat();
    w3.setMinimumIntegerDigits(3);
  }

  LOCRecord()
  {
  }

  public LOCRecord(Name paramName, int paramInt, long paramLong, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
  {
    super(paramName, 29, paramInt, paramLong);
    this.latitude = (()(2147483648.0D + 1000.0D * (3600.0D * paramDouble1)));
    this.longitude = (()(2147483648.0D + 1000.0D * (3600.0D * paramDouble2)));
    this.altitude = (()(100.0D * (100000.0D + paramDouble3)));
    this.size = (()(100.0D * paramDouble4));
    this.hPrecision = (()(100.0D * paramDouble5));
    this.vPrecision = (()(100.0D * paramDouble6));
  }

  private long parseDouble(Tokenizer paramTokenizer, String paramString, boolean paramBoolean, long paramLong1, long paramLong2, long paramLong3)
    throws IOException
  {
    Tokenizer.Token localToken = paramTokenizer.get();
    if (localToken.isEOL())
    {
      if (paramBoolean)
        throw paramTokenizer.exception("Invalid LOC " + paramString);
      paramTokenizer.unget();
      return paramLong3;
    }
    String str = localToken.value;
    if ((str.length() > 1) && (str.charAt(-1 + str.length()) == 'm'))
      str = str.substring(0, -1 + str.length());
    long l;
    try
    {
      l = ()(100.0D * parseFixedPoint(str));
      if ((l < paramLong1) || (l > paramLong2))
        throw paramTokenizer.exception("Invalid LOC " + paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw paramTokenizer.exception("Invalid LOC " + paramString);
    }
    return l;
  }

  private double parseFixedPoint(String paramString)
  {
    if (paramString.matches("^-?\\d+$"))
      return Integer.parseInt(paramString);
    if (paramString.matches("^-?\\d+\\.\\d*$"))
    {
      String[] arrayOfString = paramString.split("\\.");
      double d1 = Integer.parseInt(arrayOfString[0]);
      double d2 = Integer.parseInt(arrayOfString[1]);
      if (d1 < 0.0D)
        d2 *= -1.0D;
      return d1 + d2 / Math.pow(10.0D, arrayOfString[1].length());
    }
    throw new NumberFormatException();
  }

  private static long parseLOCformat(int paramInt)
    throws WireParseException
  {
    long l = paramInt >> 4;
    int i = paramInt & 0xF;
    if ((l > 9L) || (i > 9))
      throw new WireParseException("Invalid LOC Encoding");
    while (true)
    {
      int j;
      int k = j - 1;
      if (j > 0)
      {
        l *= 10L;
        j = k;
      }
      else
      {
        return l;
        j = i;
      }
    }
  }

  private long parsePosition(Tokenizer paramTokenizer, String paramString)
    throws IOException
  {
    boolean bool = paramString.equals("latitude");
    int i = 0;
    double d = 0.0D;
    int j = paramTokenizer.getUInt16();
    if ((j > 180) || ((j > 90) && (bool)))
      throw paramTokenizer.exception("Invalid LOC " + paramString + " degrees");
    Object localObject = paramTokenizer.getString();
    try
    {
      i = Integer.parseInt((String)localObject);
      if ((i < 0) || (i > 59))
        throw paramTokenizer.exception("Invalid LOC " + paramString + " minutes");
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (((String)localObject).length() != 1)
      {
        throw paramTokenizer.exception("Invalid LOC " + paramString);
        localObject = paramTokenizer.getString();
        d = parseFixedPoint((String)localObject);
        if ((d < 0.0D) || (d >= 60.0D))
          throw paramTokenizer.exception("Invalid LOC " + paramString + " seconds");
        String str = paramTokenizer.getString();
        localObject = str;
      }
      long l = ()(1000.0D * (d + 60L * (i + 60L * j)));
      int k = Character.toUpperCase(((String)localObject).charAt(0));
      if (((bool) && (k == 83)) || ((!bool) && (k == 87)))
        l = -l;
      do
      {
        return l + 2147483648L;
        if ((bool) && (k != 78))
          break;
      }
      while ((bool) || (k == 69));
    }
    throw paramTokenizer.exception("Invalid LOC " + paramString);
  }

  private String positionToString(long paramLong, char paramChar1, char paramChar2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    long l1 = paramLong - 2147483648L;
    if (l1 < 0L)
      l1 = -l1;
    for (char c = paramChar2; ; c = paramChar1)
    {
      localStringBuffer.append(l1 / 3600000L);
      long l2 = l1 % 3600000L;
      localStringBuffer.append(" ");
      localStringBuffer.append(l2 / 60000L);
      long l3 = l2 % 60000L;
      localStringBuffer.append(" ");
      renderFixedPoint(localStringBuffer, w3, l3, 1000L);
      localStringBuffer.append(" ");
      localStringBuffer.append(c);
      return localStringBuffer.toString();
    }
  }

  private void renderFixedPoint(StringBuffer paramStringBuffer, NumberFormat paramNumberFormat, long paramLong1, long paramLong2)
  {
    paramStringBuffer.append(paramLong1 / paramLong2);
    long l = paramLong1 % paramLong2;
    if (l != 0L)
    {
      paramStringBuffer.append(".");
      paramStringBuffer.append(paramNumberFormat.format(l));
    }
  }

  private int toLOCformat(long paramLong)
  {
    int i = 0;
    while (paramLong > 9L)
    {
      i = (byte)(i + 1);
      paramLong /= 10L;
    }
    return (int)((paramLong << 4) + i);
  }

  public double getAltitude()
  {
    return (this.altitude - 10000000L) / 100.0D;
  }

  public double getHPrecision()
  {
    return this.hPrecision / 100.0D;
  }

  public double getLatitude()
  {
    return (this.latitude - 2147483648L) / 3600000.0D;
  }

  public double getLongitude()
  {
    return (this.longitude - 2147483648L) / 3600000.0D;
  }

  Record getObject()
  {
    return new LOCRecord();
  }

  public double getSize()
  {
    return this.size / 100.0D;
  }

  public double getVPrecision()
  {
    return this.vPrecision / 100.0D;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.latitude = parsePosition(paramTokenizer, "latitude");
    this.longitude = parsePosition(paramTokenizer, "longitude");
    this.altitude = (10000000L + parseDouble(paramTokenizer, "altitude", true, -10000000L, 4284967295L, 0L));
    this.size = parseDouble(paramTokenizer, "size", false, 0L, 9000000000L, 100L);
    this.hPrecision = parseDouble(paramTokenizer, "horizontal precision", false, 0L, 9000000000L, 1000000L);
    this.vPrecision = parseDouble(paramTokenizer, "vertical precision", false, 0L, 9000000000L, 1000L);
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    if (paramDNSInput.readU8() != 0)
      throw new WireParseException("Invalid LOC version");
    this.size = parseLOCformat(paramDNSInput.readU8());
    this.hPrecision = parseLOCformat(paramDNSInput.readU8());
    this.vPrecision = parseLOCformat(paramDNSInput.readU8());
    this.latitude = paramDNSInput.readU32();
    this.longitude = paramDNSInput.readU32();
    this.altitude = paramDNSInput.readU32();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(positionToString(this.latitude, 'N', 'S'));
    localStringBuffer.append(" ");
    localStringBuffer.append(positionToString(this.longitude, 'E', 'W'));
    localStringBuffer.append(" ");
    renderFixedPoint(localStringBuffer, w2, this.altitude - 10000000L, 100L);
    localStringBuffer.append("m ");
    renderFixedPoint(localStringBuffer, w2, this.size, 100L);
    localStringBuffer.append("m ");
    renderFixedPoint(localStringBuffer, w2, this.hPrecision, 100L);
    localStringBuffer.append("m ");
    renderFixedPoint(localStringBuffer, w2, this.vPrecision, 100L);
    localStringBuffer.append("m");
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU8(0);
    paramDNSOutput.writeU8(toLOCformat(this.size));
    paramDNSOutput.writeU8(toLOCformat(this.hPrecision));
    paramDNSOutput.writeU8(toLOCformat(this.vPrecision));
    paramDNSOutput.writeU32(this.latitude);
    paramDNSOutput.writeU32(this.longitude);
    paramDNSOutput.writeU32(this.altitude);
  }
}