package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class Signature extends ASN1Encodable
{
  ASN1Sequence certs;
  DERBitString signature;
  AlgorithmIdentifier signatureAlgorithm;

  public Signature(ASN1Sequence paramASN1Sequence)
  {
    this.signatureAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.signature = ((DERBitString)paramASN1Sequence.getObjectAt(1));
    if (paramASN1Sequence.size() == 3)
      this.certs = ASN1Sequence.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(2), true);
  }

  public Signature(AlgorithmIdentifier paramAlgorithmIdentifier, DERBitString paramDERBitString)
  {
    this.signatureAlgorithm = paramAlgorithmIdentifier;
    this.signature = paramDERBitString;
  }

  public Signature(AlgorithmIdentifier paramAlgorithmIdentifier, DERBitString paramDERBitString, ASN1Sequence paramASN1Sequence)
  {
    this.signatureAlgorithm = paramAlgorithmIdentifier;
    this.signature = paramDERBitString;
    this.certs = paramASN1Sequence;
  }

  public static Signature getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof Signature)))
      return (Signature)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Signature((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static Signature getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1Sequence getCerts()
  {
    return this.certs;
  }

  public DERBitString getSignature()
  {
    return this.signature;
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.signatureAlgorithm;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.signatureAlgorithm);
    localASN1EncodableVector.add(this.signature);
    if (this.certs != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.certs));
    return new DERSequence(localASN1EncodableVector);
  }
}