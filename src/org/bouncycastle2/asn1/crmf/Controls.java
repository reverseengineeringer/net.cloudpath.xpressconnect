package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class Controls extends ASN1Encodable
{
  private ASN1Sequence content;

  private Controls(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public Controls(AttributeTypeAndValue paramAttributeTypeAndValue)
  {
    this.content = new DERSequence(paramAttributeTypeAndValue);
  }

  public Controls(AttributeTypeAndValue[] paramArrayOfAttributeTypeAndValue)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfAttributeTypeAndValue.length)
      {
        this.content = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfAttributeTypeAndValue[i]);
    }
  }

  public static Controls getInstance(Object paramObject)
  {
    if ((paramObject instanceof Controls))
      return (Controls)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Controls((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public AttributeTypeAndValue[] toAttributeTypeAndValueArray()
  {
    AttributeTypeAndValue[] arrayOfAttributeTypeAndValue = new AttributeTypeAndValue[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfAttributeTypeAndValue.length)
        return arrayOfAttributeTypeAndValue;
      arrayOfAttributeTypeAndValue[i] = AttributeTypeAndValue.getInstance(this.content.getObjectAt(i));
    }
  }
}