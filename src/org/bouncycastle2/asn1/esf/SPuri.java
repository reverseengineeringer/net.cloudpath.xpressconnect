package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;

public class SPuri
{
  private DERIA5String uri;

  public SPuri(DERIA5String paramDERIA5String)
  {
    this.uri = paramDERIA5String;
  }

  public static SPuri getInstance(Object paramObject)
  {
    if ((paramObject instanceof SPuri))
      return (SPuri)paramObject;
    if ((paramObject instanceof DERIA5String))
      return new SPuri((DERIA5String)paramObject);
    throw new IllegalArgumentException("unknown object in 'SPuri' factory: " + paramObject.getClass().getName() + ".");
  }

  public DERIA5String getUri()
  {
    return this.uri;
  }

  public DERObject toASN1Object()
  {
    return this.uri.getDERObject();
  }
}