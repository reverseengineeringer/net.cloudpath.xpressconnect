package org.bouncycastle2.asn1.x500;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class AttributeTypeAndValue extends ASN1Encodable
{
  private ASN1ObjectIdentifier type;
  private ASN1Encodable value;

  public AttributeTypeAndValue(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    this.type = paramASN1ObjectIdentifier;
    this.value = paramASN1Encodable;
  }

  private AttributeTypeAndValue(ASN1Sequence paramASN1Sequence)
  {
    this.type = ((ASN1ObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.value = ((ASN1Encodable)paramASN1Sequence.getObjectAt(1));
  }

  public static AttributeTypeAndValue getInstance(Object paramObject)
  {
    if ((paramObject instanceof AttributeTypeAndValue))
      return (AttributeTypeAndValue)paramObject;
    if (paramObject != null)
      return new AttributeTypeAndValue(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance()");
  }

  public ASN1ObjectIdentifier getType()
  {
    return this.type;
  }

  public ASN1Encodable getValue()
  {
    return this.value;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.type);
    localASN1EncodableVector.add(this.value);
    return new DERSequence(localASN1EncodableVector);
  }
}