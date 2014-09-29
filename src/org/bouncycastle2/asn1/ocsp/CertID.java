package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class CertID extends ASN1Encodable
{
  AlgorithmIdentifier hashAlgorithm;
  ASN1OctetString issuerKeyHash;
  ASN1OctetString issuerNameHash;
  DERInteger serialNumber;

  public CertID(ASN1Sequence paramASN1Sequence)
  {
    this.hashAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.issuerNameHash = ((ASN1OctetString)paramASN1Sequence.getObjectAt(1));
    this.issuerKeyHash = ((ASN1OctetString)paramASN1Sequence.getObjectAt(2));
    this.serialNumber = ((DERInteger)paramASN1Sequence.getObjectAt(3));
  }

  public CertID(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1OctetString paramASN1OctetString1, ASN1OctetString paramASN1OctetString2, DERInteger paramDERInteger)
  {
    this.hashAlgorithm = paramAlgorithmIdentifier;
    this.issuerNameHash = paramASN1OctetString1;
    this.issuerKeyHash = paramASN1OctetString2;
    this.serialNumber = paramDERInteger;
  }

  public static CertID getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof CertID)))
      return (CertID)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertID((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static CertID getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getHashAlgorithm()
  {
    return this.hashAlgorithm;
  }

  public ASN1OctetString getIssuerKeyHash()
  {
    return this.issuerKeyHash;
  }

  public ASN1OctetString getIssuerNameHash()
  {
    return this.issuerNameHash;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.hashAlgorithm);
    localASN1EncodableVector.add(this.issuerNameHash);
    localASN1EncodableVector.add(this.issuerKeyHash);
    localASN1EncodableVector.add(this.serialNumber);
    return new DERSequence(localASN1EncodableVector);
  }
}