package org.bouncycastle2.asn1.cmp;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class CertStatus extends ASN1Encodable
{
  private ASN1OctetString certHash;
  private DERInteger certReqId;
  private PKIStatusInfo statusInfo;

  private CertStatus(ASN1Sequence paramASN1Sequence)
  {
    this.certHash = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certReqId = DERInteger.getInstance(paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() > 2)
      this.statusInfo = PKIStatusInfo.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public CertStatus(byte[] paramArrayOfByte, BigInteger paramBigInteger)
  {
    this.certHash = new DEROctetString(paramArrayOfByte);
    this.certReqId = new DERInteger(paramBigInteger);
  }

  public CertStatus(byte[] paramArrayOfByte, BigInteger paramBigInteger, PKIStatusInfo paramPKIStatusInfo)
  {
    this.certHash = new DEROctetString(paramArrayOfByte);
    this.certReqId = new DERInteger(paramBigInteger);
    this.statusInfo = paramPKIStatusInfo;
  }

  public static CertStatus getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertStatus))
      return (CertStatus)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertStatus((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public ASN1OctetString getCertHash()
  {
    return this.certHash;
  }

  public DERInteger getCertReqId()
  {
    return this.certReqId;
  }

  public PKIStatusInfo getStatusInfo()
  {
    return this.statusInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certHash);
    localASN1EncodableVector.add(this.certReqId);
    if (this.statusInfo != null)
      localASN1EncodableVector.add(this.statusInfo);
    return new DERSequence(localASN1EncodableVector);
  }
}