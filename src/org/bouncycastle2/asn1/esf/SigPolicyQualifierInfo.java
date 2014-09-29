package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class SigPolicyQualifierInfo extends ASN1Encodable
{
  private DERObjectIdentifier sigPolicyQualifierId;
  private DEREncodable sigQualifier;

  public SigPolicyQualifierInfo(ASN1Sequence paramASN1Sequence)
  {
    this.sigPolicyQualifierId = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.sigQualifier = paramASN1Sequence.getObjectAt(1);
  }

  public SigPolicyQualifierInfo(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.sigPolicyQualifierId = paramDERObjectIdentifier;
    this.sigQualifier = paramDEREncodable;
  }

  public static SigPolicyQualifierInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SigPolicyQualifierInfo)))
      return (SigPolicyQualifierInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SigPolicyQualifierInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'SigPolicyQualifierInfo' factory: " + paramObject.getClass().getName() + ".");
  }

  public ASN1ObjectIdentifier getSigPolicyQualifierId()
  {
    return new ASN1ObjectIdentifier(this.sigPolicyQualifierId.getId());
  }

  public DEREncodable getSigQualifier()
  {
    return this.sigQualifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.sigPolicyQualifierId);
    localASN1EncodableVector.add(this.sigQualifier);
    return new DERSequence(localASN1EncodableVector);
  }
}