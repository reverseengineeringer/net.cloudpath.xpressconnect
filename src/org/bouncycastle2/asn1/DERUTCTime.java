package org.bouncycastle2.asn1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DERUTCTime extends ASN1Object
{
  String time;

  public DERUTCTime(String paramString)
  {
    this.time = paramString;
    try
    {
      getDate();
      return;
    }
    catch (ParseException localParseException)
    {
      throw new IllegalArgumentException("invalid date string: " + localParseException.getMessage());
    }
  }

  public DERUTCTime(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    this.time = localSimpleDateFormat.format(paramDate);
  }

  DERUTCTime(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
      {
        this.time = new String(arrayOfChar);
        return;
      }
      arrayOfChar[i] = ((char)(0xFF & paramArrayOfByte[i]));
    }
  }

  public static DERUTCTime getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERUTCTime)))
      return (DERUTCTime)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERUTCTime getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERUTCTime)))
      return getInstance(localDERObject);
    return new DERUTCTime(((ASN1OctetString)localDERObject).getOctets());
  }

  private byte[] getOctets()
  {
    char[] arrayOfChar = this.time.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)arrayOfChar[i]);
    }
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERUTCTime))
      return false;
    return this.time.equals(((DERUTCTime)paramDERObject).time);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(23, getOctets());
  }

  public Date getAdjustedDate()
    throws ParseException
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    return localSimpleDateFormat.parse(getAdjustedTime());
  }

  public String getAdjustedTime()
  {
    String str = getTime();
    if (str.charAt(0) < '5')
      return "20" + str;
    return "19" + str;
  }

  public Date getDate()
    throws ParseException
  {
    return new SimpleDateFormat("yyMMddHHmmssz").parse(getTime());
  }

  public String getTime()
  {
    if ((this.time.indexOf('-') < 0) && (this.time.indexOf('+') < 0))
    {
      if (this.time.length() == 11)
        return this.time.substring(0, 10) + "00GMT+00:00";
      return this.time.substring(0, 12) + "GMT+00:00";
    }
    int i = this.time.indexOf('-');
    if (i < 0)
      i = this.time.indexOf('+');
    String str = this.time;
    if (i == -3 + this.time.length())
      str = str + "00";
    if (i == 10)
      return str.substring(0, 10) + "00GMT" + str.substring(10, 13) + ":" + str.substring(13, 15);
    return str.substring(0, 12) + "GMT" + str.substring(12, 15) + ":" + str.substring(15, 17);
  }

  public int hashCode()
  {
    return this.time.hashCode();
  }

  public String toString()
  {
    return this.time;
  }
}