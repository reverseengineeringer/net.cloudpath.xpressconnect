package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class OtherRecipientInfo extends ASN1Encodable
{
  private DERObjectIdentifier oriType;
  private DEREncodable oriValue;

  public OtherRecipientInfo(ASN1Sequence paramASN1Sequence)
  {
    this.oriType = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.oriValue = paramASN1Sequence.getObjectAt(1);
  }

  public OtherRecipientInfo(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.oriType = paramDERObjectIdentifier;
    this.oriValue = paramDEREncodable;
  }

  public static OtherRecipientInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OtherRecipientInfo)))
      return (OtherRecipientInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OtherRecipientInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid OtherRecipientInfo: " + paramObject.getClass().getName());
  }

  public static OtherRecipientInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERObjectIdentifier getType()
  {
    return this.oriType;
  }

  public DEREncodable getValue()
  {
    return this.oriValue;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.oriType);
    localASN1EncodableVector.add(this.oriValue);
    return new DERSequence(localASN1EncodableVector);
  }
}