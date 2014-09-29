package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREnumerated;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.CRLReason;

public class RevokedInfo extends ASN1Encodable
{
  private CRLReason revocationReason;
  private DERGeneralizedTime revocationTime;

  public RevokedInfo(ASN1Sequence paramASN1Sequence)
  {
    this.revocationTime = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.revocationReason = new CRLReason(DEREnumerated.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), true));
  }

  public RevokedInfo(DERGeneralizedTime paramDERGeneralizedTime, CRLReason paramCRLReason)
  {
    this.revocationTime = paramDERGeneralizedTime;
    this.revocationReason = paramCRLReason;
  }

  public static RevokedInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RevokedInfo)))
      return (RevokedInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RevokedInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static RevokedInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public CRLReason getRevocationReason()
  {
    return this.revocationReason;
  }

  public DERGeneralizedTime getRevocationTime()
  {
    return this.revocationTime;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.revocationTime);
    if (this.revocationReason != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.revocationReason));
    return new DERSequence(localASN1EncodableVector);
  }
}