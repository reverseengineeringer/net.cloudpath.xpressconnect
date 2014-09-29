package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class PollRepContent extends ASN1Encodable
{
  private DERInteger certReqId;
  private DERInteger checkAfter;
  private PKIFreeText reason;

  private PollRepContent(ASN1Sequence paramASN1Sequence)
  {
    this.certReqId = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.checkAfter = DERInteger.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() > 2)
      this.reason = PKIFreeText.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public static PollRepContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof PollRepContent))
      return (PollRepContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PollRepContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERInteger getCertReqId()
  {
    return this.certReqId;
  }

  public DERInteger getCheckAfter()
  {
    return this.checkAfter;
  }

  public PKIFreeText getReason()
  {
    return this.reason;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certReqId);
    localASN1EncodableVector.add(this.checkAfter);
    if (this.reason != null)
      localASN1EncodableVector.add(this.reason);
    return new DERSequence(localASN1EncodableVector);
  }
}