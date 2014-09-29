package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class AttCertValidityPeriod extends ASN1Encodable
{
  DERGeneralizedTime notAfterTime;
  DERGeneralizedTime notBeforeTime;

  public AttCertValidityPeriod(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.notBeforeTime = DERGeneralizedTime.getInstance(paramASN1Sequence.getObjectAt(0));
    this.notAfterTime = DERGeneralizedTime.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public AttCertValidityPeriod(DERGeneralizedTime paramDERGeneralizedTime1, DERGeneralizedTime paramDERGeneralizedTime2)
  {
    this.notBeforeTime = paramDERGeneralizedTime1;
    this.notAfterTime = paramDERGeneralizedTime2;
  }

  public static AttCertValidityPeriod getInstance(Object paramObject)
  {
    if ((paramObject instanceof AttCertValidityPeriod))
      return (AttCertValidityPeriod)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AttCertValidityPeriod((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public DERGeneralizedTime getNotAfterTime()
  {
    return this.notAfterTime;
  }

  public DERGeneralizedTime getNotBeforeTime()
  {
    return this.notBeforeTime;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.notBeforeTime);
    localASN1EncodableVector.add(this.notAfterTime);
    return new DERSequence(localASN1EncodableVector);
  }
}