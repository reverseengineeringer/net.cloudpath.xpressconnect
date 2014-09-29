package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.ocsp.ResponderID;

public class OcspIdentifier extends ASN1Encodable
{
  private ResponderID ocspResponderID;
  private DERGeneralizedTime producedAt;

  private OcspIdentifier(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.ocspResponderID = ResponderID.getInstance(paramASN1Sequence.getObjectAt(0));
    this.producedAt = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(1));
  }

  public OcspIdentifier(ResponderID paramResponderID, DERGeneralizedTime paramDERGeneralizedTime)
  {
    this.ocspResponderID = paramResponderID;
    this.producedAt = paramDERGeneralizedTime;
  }

  public static OcspIdentifier getInstance(Object paramObject)
  {
    if ((paramObject instanceof OcspIdentifier))
      return (OcspIdentifier)paramObject;
    if (paramObject != null)
      return new OcspIdentifier(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public ResponderID getOcspResponderID()
  {
    return this.ocspResponderID;
  }

  public DERGeneralizedTime getProducedAt()
  {
    return this.producedAt;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.ocspResponderID);
    localASN1EncodableVector.add(this.producedAt);
    return new DERSequence(localASN1EncodableVector);
  }
}