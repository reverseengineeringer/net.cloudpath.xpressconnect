package org.bouncycastle2.asn1.x9;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Null;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;

public class X962Parameters extends ASN1Encodable
  implements ASN1Choice
{
  private DERObject params = null;

  public X962Parameters(DERObject paramDERObject)
  {
    this.params = paramDERObject;
  }

  public X962Parameters(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.params = paramDERObjectIdentifier;
  }

  public X962Parameters(X9ECParameters paramX9ECParameters)
  {
    this.params = paramX9ECParameters.getDERObject();
  }

  public static X962Parameters getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof X962Parameters)))
      return (X962Parameters)paramObject;
    if ((paramObject instanceof DERObject))
      return new X962Parameters((DERObject)paramObject);
    throw new IllegalArgumentException("unknown object in getInstance()");
  }

  public static X962Parameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DERObject getParameters()
  {
    return this.params;
  }

  public boolean isImplicitlyCA()
  {
    return this.params instanceof ASN1Null;
  }

  public boolean isNamedCurve()
  {
    return this.params instanceof DERObjectIdentifier;
  }

  public DERObject toASN1Object()
  {
    return this.params;
  }
}