package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.CertificateList;

public class TimeStampAndCRL extends ASN1Encodable
{
  private CertificateList crl;
  private ContentInfo timeStamp;

  private TimeStampAndCRL(ASN1Sequence paramASN1Sequence)
  {
    this.timeStamp = ContentInfo.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
      this.crl = CertificateList.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public TimeStampAndCRL(ContentInfo paramContentInfo)
  {
    this.timeStamp = paramContentInfo;
  }

  public static TimeStampAndCRL getInstance(Object paramObject)
  {
    if ((paramObject instanceof TimeStampAndCRL))
      return (TimeStampAndCRL)paramObject;
    if (paramObject != null)
      return new TimeStampAndCRL(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public CertificateList getCertificateList()
  {
    return this.crl;
  }

  public ContentInfo getTimeStampToken()
  {
    return this.timeStamp;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.timeStamp);
    if (this.crl != null)
      localASN1EncodableVector.add(this.crl);
    return new DERSequence(localASN1EncodableVector);
  }
}