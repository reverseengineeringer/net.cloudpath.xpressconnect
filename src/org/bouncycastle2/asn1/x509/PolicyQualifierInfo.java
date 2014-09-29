package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class PolicyQualifierInfo extends ASN1Encodable
{
  private DERObjectIdentifier policyQualifierId;
  private DEREncodable qualifier;

  public PolicyQualifierInfo(String paramString)
  {
    this.policyQualifierId = PolicyQualifierId.id_qt_cps;
    this.qualifier = new DERIA5String(paramString);
  }

  public PolicyQualifierInfo(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.policyQualifierId = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.qualifier = paramASN1Sequence.getObjectAt(1);
  }

  public PolicyQualifierInfo(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    this.policyQualifierId = paramDERObjectIdentifier;
    this.qualifier = paramDEREncodable;
  }

  public static PolicyQualifierInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof PolicyQualifierInfo))
      return (PolicyQualifierInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PolicyQualifierInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in getInstance.");
  }

  public DERObjectIdentifier getPolicyQualifierId()
  {
    return this.policyQualifierId;
  }

  public DEREncodable getQualifier()
  {
    return this.qualifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.policyQualifierId);
    localASN1EncodableVector.add(this.qualifier);
    return new DERSequence(localASN1EncodableVector);
  }
}