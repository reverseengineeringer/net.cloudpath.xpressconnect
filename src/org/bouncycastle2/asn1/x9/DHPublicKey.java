package org.bouncycastle2.asn1.x9;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class DHPublicKey extends ASN1Encodable
{
  private DERInteger y;

  public DHPublicKey(DERInteger paramDERInteger)
  {
    if (paramDERInteger == null)
      throw new IllegalArgumentException("'y' cannot be null");
    this.y = paramDERInteger;
  }

  public static DHPublicKey getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DHPublicKey)))
      return (DHPublicKey)paramObject;
    if ((paramObject instanceof DERInteger))
      return new DHPublicKey((DERInteger)paramObject);
    throw new IllegalArgumentException("Invalid DHPublicKey: " + paramObject.getClass().getName());
  }

  public static DHPublicKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(DERInteger.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERInteger getY()
  {
    return this.y;
  }

  public DERObject toASN1Object()
  {
    return this.y;
  }
}