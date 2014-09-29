package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class AttributeCertificate extends ASN1Encodable
{
  AttributeCertificateInfo acinfo;
  AlgorithmIdentifier signatureAlgorithm;
  DERBitString signatureValue;

  public AttributeCertificate(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.acinfo = AttributeCertificateInfo.getInstance(paramASN1Sequence.getObjectAt(0));
    this.signatureAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.signatureValue = DERBitString.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public AttributeCertificate(AttributeCertificateInfo paramAttributeCertificateInfo, AlgorithmIdentifier paramAlgorithmIdentifier, DERBitString paramDERBitString)
  {
    this.acinfo = paramAttributeCertificateInfo;
    this.signatureAlgorithm = paramAlgorithmIdentifier;
    this.signatureValue = paramDERBitString;
  }

  public static AttributeCertificate getInstance(Object paramObject)
  {
    if ((paramObject instanceof AttributeCertificate))
      return (AttributeCertificate)paramObject;
    if (paramObject != null)
      return new AttributeCertificate(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public AttributeCertificateInfo getAcinfo()
  {
    return this.acinfo;
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.signatureAlgorithm;
  }

  public DERBitString getSignatureValue()
  {
    return this.signatureValue;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.acinfo);
    localASN1EncodableVector.add(this.signatureAlgorithm);
    localASN1EncodableVector.add(this.signatureValue);
    return new DERSequence(localASN1EncodableVector);
  }
}