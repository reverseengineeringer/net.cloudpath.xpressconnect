package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DigestInfo;
import org.bouncycastle2.asn1.x509.IssuerSerial;

public class OtherCertID extends ASN1Encodable
{
  private IssuerSerial issuerSerial;
  private ASN1Encodable otherCertHash;

  public OtherCertID(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 1) || (paramASN1Sequence.size() > 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    if ((paramASN1Sequence.getObjectAt(0).getDERObject() instanceof ASN1OctetString));
    for (this.otherCertHash = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0)); ; this.otherCertHash = DigestInfo.getInstance(paramASN1Sequence.getObjectAt(0)))
    {
      if (paramASN1Sequence.size() > 1)
        this.issuerSerial = new IssuerSerial(ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1)));
      return;
    }
  }

  public OtherCertID(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.otherCertHash = new DigestInfo(paramAlgorithmIdentifier, paramArrayOfByte);
  }

  public OtherCertID(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte, IssuerSerial paramIssuerSerial)
  {
    this.otherCertHash = new DigestInfo(paramAlgorithmIdentifier, paramArrayOfByte);
    this.issuerSerial = paramIssuerSerial;
  }

  public static OtherCertID getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OtherCertID)))
      return (OtherCertID)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OtherCertID((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'OtherCertID' factory : " + paramObject.getClass().getName() + ".");
  }

  public AlgorithmIdentifier getAlgorithmHash()
  {
    if ((this.otherCertHash.getDERObject() instanceof ASN1OctetString))
      return new AlgorithmIdentifier("1.3.14.3.2.26");
    return DigestInfo.getInstance(this.otherCertHash).getAlgorithmId();
  }

  public byte[] getCertHash()
  {
    if ((this.otherCertHash.getDERObject() instanceof ASN1OctetString))
      return ((ASN1OctetString)this.otherCertHash.getDERObject()).getOctets();
    return DigestInfo.getInstance(this.otherCertHash).getDigest();
  }

  public IssuerSerial getIssuerSerial()
  {
    return this.issuerSerial;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.otherCertHash);
    if (this.issuerSerial != null)
      localASN1EncodableVector.add(this.issuerSerial);
    return new DERSequence(localASN1EncodableVector);
  }
}