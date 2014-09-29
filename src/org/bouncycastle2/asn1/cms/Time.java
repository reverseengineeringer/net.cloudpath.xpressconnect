package org.bouncycastle2.asn1.cms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERUTCTime;

public class Time extends ASN1Encodable
  implements ASN1Choice
{
  DERObject time;

  public Time(Date paramDate)
  {
    SimpleTimeZone localSimpleTimeZone = new SimpleTimeZone(0, "Z");
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    localSimpleDateFormat.setTimeZone(localSimpleTimeZone);
    String str = localSimpleDateFormat.format(paramDate) + "Z";
    int i = Integer.parseInt(str.substring(0, 4));
    if ((i < 1950) || (i > 2049))
    {
      this.time = new DERGeneralizedTime(str);
      return;
    }
    this.time = new DERUTCTime(str.substring(2));
  }

  public Time(DERObject paramDERObject)
  {
    if ((!(paramDERObject instanceof DERUTCTime)) && (!(paramDERObject instanceof DERGeneralizedTime)))
      throw new IllegalArgumentException("unknown object passed to Time");
    this.time = paramDERObject;
  }

  public static Time getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof Time)))
      return (Time)paramObject;
    if ((paramObject instanceof DERUTCTime))
      return new Time((DERUTCTime)paramObject);
    if ((paramObject instanceof DERGeneralizedTime))
      return new Time((DERGeneralizedTime)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static Time getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public Date getDate()
  {
    try
    {
      if ((this.time instanceof DERUTCTime))
        return ((DERUTCTime)this.time).getAdjustedDate();
      Date localDate = ((DERGeneralizedTime)this.time).getDate();
      return localDate;
    }
    catch (ParseException localParseException)
    {
      throw new IllegalStateException("invalid date string: " + localParseException.getMessage());
    }
  }

  public String getTime()
  {
    if ((this.time instanceof DERUTCTime))
      return ((DERUTCTime)this.time).getAdjustedTime();
    return ((DERGeneralizedTime)this.time).getTime();
  }

  public DERObject toASN1Object()
  {
    return this.time;
  }
}