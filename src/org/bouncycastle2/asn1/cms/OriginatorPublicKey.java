package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class OriginatorPublicKey extends ASN1Encodable
{
  private AlgorithmIdentifier algorithm;
  private DERBitString publicKey;

  public OriginatorPublicKey(ASN1Sequence paramASN1Sequence)
  {
    this.algorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    this.publicKey = ((DERBitString)paramASN1Sequence.getObjectAt(1));
  }

  public OriginatorPublicKey(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte)
  {
    this.algorithm = paramAlgorithmIdentifier;
    this.publicKey = new DERBitString(paramArrayOfByte);
  }

  public static OriginatorPublicKey getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OriginatorPublicKey)))
      return (OriginatorPublicKey)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OriginatorPublicKey((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid OriginatorPublicKey: " + paramObject.getClass().getName());
  }

  public static OriginatorPublicKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getAlgorithm()
  {
    return this.algorithm;
  }

  public DERBitString getPublicKey()
  {
    return this.publicKey;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.algorithm);
    localASN1EncodableVector.add(this.publicKey);
    return new DERSequence(localASN1EncodableVector);
  }
}