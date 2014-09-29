package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class AttributeTypeAndValue extends ASN1Encodable
{
  private DERObjectIdentifier type;
  private ASN1Encodable value;

  public AttributeTypeAndValue(String paramString, ASN1Encodable paramASN1Encodable)
  {
    this(new DERObjectIdentifier(paramString), paramASN1Encodable);
  }

  private AttributeTypeAndValue(ASN1Sequence paramASN1Sequence)
  {
    this.type = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.value = ((ASN1Encodable)paramASN1Sequence.getObjectAt(1));
  }

  public AttributeTypeAndValue(DERObjectIdentifier paramDERObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    this.type = paramDERObjectIdentifier;
    this.value = paramASN1Encodable;
  }

  public static AttributeTypeAndValue getInstance(Object paramObject)
  {
    if ((paramObject instanceof AttributeTypeAndValue))
      return (AttributeTypeAndValue)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AttributeTypeAndValue((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObjectIdentifier getType()
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