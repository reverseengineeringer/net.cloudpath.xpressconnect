package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CertResponse extends ASN1Encodable
{
  private DERInteger certReqId;
  private CertifiedKeyPair certifiedKeyPair;
  private ASN1OctetString rspInfo;
  private PKIStatusInfo status;

  private CertResponse(ASN1Sequence paramASN1Sequence)
  {
    this.certReqId = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.status = PKIStatusInfo.getInstance(paramASN1Sequence.getObjectAt(1));
    DEREncodable localDEREncodable;
    if (paramASN1Sequence.size() >= 3)
    {
      if (paramASN1Sequence.size() != 3)
        break label75;
      localDEREncodable = paramASN1Sequence.getObjectAt(2);
      if ((localDEREncodable instanceof ASN1OctetString))
        this.rspInfo = ASN1OctetString.getInstance(localDEREncodable);
    }
    else
    {
      return;
    }
    this.certifiedKeyPair = CertifiedKeyPair.getInstance(localDEREncodable);
    return;
    label75: this.certifiedKeyPair = CertifiedKeyPair.getInstance(paramASN1Sequence.getObjectAt(2));
    this.rspInfo = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(3));
  }

  public CertResponse(DERInteger paramDERInteger, PKIStatusInfo paramPKIStatusInfo)
  {
    this(paramDERInteger, paramPKIStatusInfo, null, null);
  }

  public CertResponse(DERInteger paramDERInteger, PKIStatusInfo paramPKIStatusInfo, CertifiedKeyPair paramCertifiedKeyPair, ASN1OctetString paramASN1OctetString)
  {
    if (paramDERInteger == null)
      throw new IllegalArgumentException("'certReqId' cannot be null");
    if (paramPKIStatusInfo == null)
      throw new IllegalArgumentException("'status' cannot be null");
    this.certReqId = paramDERInteger;
    this.status = paramPKIStatusInfo;
    this.certifiedKeyPair = paramCertifiedKeyPair;
    this.rspInfo = paramASN1OctetString;
  }

  public static CertResponse getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertResponse))
      return (CertResponse)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertResponse((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERInteger getCertReqId()
  {
    return this.certReqId;
  }

  public CertifiedKeyPair getCertifiedKeyPair()
  {
    return this.certifiedKeyPair;
  }

  public PKIStatusInfo getStatus()
  {
    return this.status;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certReqId);
    localASN1EncodableVector.add(this.status);
    if (this.certifiedKeyPair != null)
      localASN1EncodableVector.add(this.certifiedKeyPair);
    if (this.rspInfo != null)
      localASN1EncodableVector.add(this.rspInfo);
    return new DERSequence(localASN1EncodableVector);
  }
}