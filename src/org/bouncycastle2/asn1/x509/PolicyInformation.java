package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class PolicyInformation extends ASN1Encodable
{
  private DERObjectIdentifier policyIdentifier;
  private ASN1Sequence policyQualifiers;

  public PolicyInformation(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.policyIdentifier = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.policyQualifiers = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public PolicyInformation(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.policyIdentifier = paramDERObjectIdentifier;
  }

  public PolicyInformation(DERObjectIdentifier paramDERObjectIdentifier, ASN1Sequence paramASN1Sequence)
  {
    this.policyIdentifier = paramDERObjectIdentifier;
    this.policyQualifiers = paramASN1Sequence;
  }

  public static PolicyInformation getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof PolicyInformation)))
      return (PolicyInformation)paramObject;
    return new PolicyInformation(ASN1Sequence.getInstance(paramObject));
  }

  public DERObjectIdentifier getPolicyIdentifier()
  {
    return this.policyIdentifier;
  }

  public ASN1Sequence getPolicyQualifiers()
  {
    return this.policyQualifiers;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.policyIdentifier);
    if (this.policyQualifiers != null)
      localASN1EncodableVector.add(this.policyQualifiers);
    return new DERSequence(localASN1EncodableVector);
  }
}