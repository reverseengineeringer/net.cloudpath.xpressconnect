package org.bouncycastle2.asn1.x500;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;

public class RDN extends ASN1Encodable
{
  private ASN1Set values;

  public RDN(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramASN1ObjectIdentifier);
    localASN1EncodableVector.add(paramASN1Encodable);
    this.values = new DERSet(new DERSequence(localASN1EncodableVector));
  }

  private RDN(ASN1Set paramASN1Set)
  {
    this.values = paramASN1Set;
  }

  public RDN(AttributeTypeAndValue paramAttributeTypeAndValue)
  {
    this.values = new DERSet(paramAttributeTypeAndValue);
  }

  public RDN(AttributeTypeAndValue[] paramArrayOfAttributeTypeAndValue)
  {
    this.values = new DERSet(paramArrayOfAttributeTypeAndValue);
  }

  public static RDN getInstance(Object paramObject)
  {
    if ((paramObject instanceof RDN))
      return (RDN)paramObject;
    if (paramObject != null)
      return new RDN(ASN1Set.getInstance(paramObject));
    return null;
  }

  public AttributeTypeAndValue getFirst()
  {
    if (this.values.size() == 0)
      return null;
    return AttributeTypeAndValue.getInstance(this.values.getObjectAt(0));
  }

  public AttributeTypeAndValue[] getTypesAndValues()
  {
    AttributeTypeAndValue[] arrayOfAttributeTypeAndValue = new AttributeTypeAndValue[this.values.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfAttributeTypeAndValue.length)
        return arrayOfAttributeTypeAndValue;
      arrayOfAttributeTypeAndValue[i] = AttributeTypeAndValue.getInstance(this.values.getObjectAt(i));
    }
  }

  public boolean isMultiValued()
  {
    return this.values.size() > 1;
  }

  public DERObject toASN1Object()
  {
    return this.values;
  }
}