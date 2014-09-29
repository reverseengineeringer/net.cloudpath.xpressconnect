package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class OtherKeyAttribute extends ASN1Encodable
{
  private DEREncodable keyAttr;
  private DERObjectIdentifier keyAttrId;

  public OtherKeyAttribute(ASN1Sequence paramASN1Sequence)
  {
    this.keyAttrId = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.keyAttr = paramASN1Sequence.getObjectAt(1);
  }

  public OtherKeyAttribute(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.keyAttrId = paramDERObjectIdentifier;
    this.keyAttr = paramDEREncodable;
  }

  public static OtherKeyAttribute getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OtherKeyAttribute)))
      return (OtherKeyAttribute)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OtherKeyAttribute((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public DEREncodable getKeyAttr()
  {
    return this.keyAttr;
  }

  public DERObjectIdentifier getKeyAttrId()
  {
    return this.keyAttrId;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.keyAttrId);
    localASN1EncodableVector.add(this.keyAttr);
    return new DERSequence(localASN1EncodableVector);
  }
}