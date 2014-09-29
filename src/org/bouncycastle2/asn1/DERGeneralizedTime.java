package org.bouncycastle2.asn1;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DERGeneralizedTime extends ASN1Object
{
  String time;

  public DERGeneralizedTime(String paramString)
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

  public DERGeneralizedTime(Date paramDate)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
    localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
    this.time = localSimpleDateFormat.format(paramDate);
  }

  DERGeneralizedTime(byte[] paramArrayOfByte)
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

  private String calculateGMTOffset()
  {
    String str = "+";
    TimeZone localTimeZone = TimeZone.getDefault();
    int i = localTimeZone.getRawOffset();
    if (i < 0)
    {
      str = "-";
      i = -i;
    }
    int j = i / 3600000;
    int k = (i - 1000 * (60 * (j * 60))) / 60000;
    try
    {
      if ((localTimeZone.useDaylightTime()) && (localTimeZone.inDaylightTime(getDate())))
      {
        boolean bool = str.equals("+");
        if (!bool)
          break label128;
      }
      label128: for (int m = 1; ; m = -1)
      {
        j += m;
        label88: return "GMT" + str + convert(j) + ":" + convert(k);
      }
    }
    catch (ParseException localParseException)
    {
      break label88;
    }
  }

  private String convert(int paramInt)
  {
    if (paramInt < 10)
      return "0" + paramInt;
    return Integer.toString(paramInt);
  }

  public static DERGeneralizedTime getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERGeneralizedTime)))
      return (DERGeneralizedTime)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERGeneralizedTime getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERGeneralizedTime)))
      return getInstance(localDERObject);
    return new DERGeneralizedTime(((ASN1OctetString)localDERObject).getOctets());
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

  private boolean hasFractionalSeconds()
  {
    return this.time.indexOf('.') == 14;
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERGeneralizedTime))
      return false;
    return this.time.equals(((DERGeneralizedTime)paramDERObject).time);
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(24, getOctets());
  }

  public Date getDate()
    throws ParseException
  {
    String str1 = this.time;
    SimpleDateFormat localSimpleDateFormat;
    String str2;
    int i;
    if (this.time.endsWith("Z"))
      if (hasFractionalSeconds())
      {
        localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS'Z'");
        localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
        if (hasFractionalSeconds())
        {
          str2 = str1.substring(14);
          i = 1;
          label65: if (i < str2.length())
            break label284;
          label74: if (i - 1 <= 3)
            break label312;
          String str5 = str2.substring(0, 4) + str2.substring(i);
          str1 = str1.substring(0, 14) + str5;
        }
      }
    while (true)
    {
      return localSimpleDateFormat.parse(str1);
      localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
      break;
      if ((this.time.indexOf('-') > 0) || (this.time.indexOf('+') > 0))
      {
        str1 = getTime();
        if (hasFractionalSeconds());
        for (localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSSz"); ; localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssz"))
        {
          localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
          break;
        }
      }
      if (hasFractionalSeconds());
      for (localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss.SSS"); ; localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"))
      {
        localSimpleDateFormat.setTimeZone(new SimpleTimeZone(0, TimeZone.getDefault().getID()));
        break;
      }
      label284: int j = str2.charAt(i);
      if ((48 > j) || (j > 57))
        break label74;
      i++;
      break label65;
      label312: if (i - 1 == 1)
      {
        String str4 = str2.substring(0, i) + "00" + str2.substring(i);
        str1 = str1.substring(0, 14) + str4;
      }
      else if (i - 1 == 2)
      {
        String str3 = str2.substring(0, i) + "0" + str2.substring(i);
        str1 = str1.substring(0, 14) + str3;
      }
    }
  }

  public String getTime()
  {
    if (this.time.charAt(-1 + this.time.length()) == 'Z')
      return this.time.substring(0, -1 + this.time.length()) + "GMT+00:00";
    int i = -5 + this.time.length();
    int j = this.time.charAt(i);
    if ((j == 45) || (j == 43))
      return this.time.substring(0, i) + "GMT" + this.time.substring(i, i + 3) + ":" + this.time.substring(i + 3);
    int k = -3 + this.time.length();
    int m = this.time.charAt(k);
    if ((m == 45) || (m == 43))
      return this.time.substring(0, k) + "GMT" + this.time.substring(k) + ":00";
    return this.time + calculateGMTOffset();
  }

  public String getTimeString()
  {
    return this.time;
  }

  public int hashCode()
  {
    return this.time.hashCode();
  }
}