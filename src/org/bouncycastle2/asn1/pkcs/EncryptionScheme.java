package org.bouncycastle2.asn1.pkcs;

import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class EncryptionScheme extends AlgorithmIdentifier
{
  EncryptionScheme(ASN1Sequence paramASN1Sequence)
  {
    this((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0), paramASN1Sequence.getObjectAt(1));
  }

  public EncryptionScheme(DERObjectIdentifier paramDERObjectIdentifier, DEREncodable paramDEREncodable)
  {
    super(paramDERObjectIdentifier, paramDEREncodable);
  }

  public static final AlgorithmIdentifier getInstance(Object paramObject)
  {
    if ((paramObject instanceof EncryptionScheme))
      return (EncryptionScheme)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new EncryptionScheme((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public DERObject getDERObject()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(getObjectId());
    localASN1EncodableVector.add(getParameters());
    return new DERSequence(localASN1EncodableVector);
  }

  public DERObject getObject()
  {
    return (DERObject)getParameters();
  }
}