package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.BERSet;
import org.bouncycastle2.asn1.DERObject;

public class Attributes extends ASN1Encodable
{
  private ASN1Set attributes;

  public Attributes(ASN1EncodableVector paramASN1EncodableVector)
  {
    this.attributes = new BERSet(paramASN1EncodableVector);
  }

  private Attributes(ASN1Set paramASN1Set)
  {
    this.attributes = paramASN1Set;
  }

  public static Attributes getInstance(Object paramObject)
  {
    if ((paramObject instanceof Attributes))
      return (Attributes)paramObject;
    if (paramObject != null)
      return new Attributes(ASN1Set.getInstance(paramObject));
    return null;
  }

  public DERObject toASN1Object()
  {
    return this.attributes;
  }
}