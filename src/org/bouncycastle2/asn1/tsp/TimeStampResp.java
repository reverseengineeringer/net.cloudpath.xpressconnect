package org.bouncycastle2.asn1.tsp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.cmp.PKIStatusInfo;
import org.bouncycastle2.asn1.cms.ContentInfo;

public class TimeStampResp extends ASN1Encodable
{
  PKIStatusInfo pkiStatusInfo;
  ContentInfo timeStampToken;

  public TimeStampResp(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.pkiStatusInfo = PKIStatusInfo.getInstance(localEnumeration.nextElement());
    if (localEnumeration.hasMoreElements())
      this.timeStampToken = ContentInfo.getInstance(localEnumeration.nextElement());
  }

  public TimeStampResp(PKIStatusInfo paramPKIStatusInfo, ContentInfo paramContentInfo)
  {
    this.pkiStatusInfo = paramPKIStatusInfo;
    this.timeStampToken = paramContentInfo;
  }

  public static TimeStampResp getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof TimeStampResp)))
      return (TimeStampResp)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TimeStampResp((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'TimeStampResp' factory : " + paramObject.getClass().getName() + ".");
  }

  public PKIStatusInfo getStatus()
  {
    return this.pkiStatusInfo;
  }

  public ContentInfo getTimeStampToken()
  {
    return this.timeStampToken;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.pkiStatusInfo);
    if (this.timeStampToken != null)
      localASN1EncodableVector.add(this.timeStampToken);
    return new DERSequence(localASN1EncodableVector);
  }
}