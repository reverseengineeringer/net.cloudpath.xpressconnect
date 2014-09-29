package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class EncryptedValue extends ASN1Encodable
{
  private DERBitString encSymmKey;
  private DERBitString encValue;
  private AlgorithmIdentifier intendedAlg;
  private AlgorithmIdentifier keyAlg;
  private AlgorithmIdentifier symmAlg;
  private ASN1OctetString valueHint;

  private EncryptedValue(ASN1Sequence paramASN1Sequence)
  {
    int i = 0;
    if (!(paramASN1Sequence.getObjectAt(i) instanceof ASN1TaggedObject))
    {
      this.encValue = DERBitString.getInstance(paramASN1Sequence.getObjectAt(i));
      return;
    }
    ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(i);
    switch (localASN1TaggedObject.getTagNo())
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      i++;
      break;
      this.intendedAlg = AlgorithmIdentifier.getInstance(localASN1TaggedObject, false);
      continue;
      this.symmAlg = AlgorithmIdentifier.getInstance(localASN1TaggedObject, false);
      continue;
      this.encSymmKey = DERBitString.getInstance(localASN1TaggedObject, false);
      continue;
      this.keyAlg = AlgorithmIdentifier.getInstance(localASN1TaggedObject, false);
      continue;
      this.valueHint = ASN1OctetString.getInstance(localASN1TaggedObject, false);
    }
  }

  public EncryptedValue(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2, DERBitString paramDERBitString1, AlgorithmIdentifier paramAlgorithmIdentifier3, ASN1OctetString paramASN1OctetString, DERBitString paramDERBitString2)
  {
    if (paramDERBitString2 == null)
      throw new IllegalArgumentException("'encValue' cannot be null");
    this.intendedAlg = paramAlgorithmIdentifier1;
    this.symmAlg = paramAlgorithmIdentifier2;
    this.encSymmKey = paramDERBitString1;
    this.keyAlg = paramAlgorithmIdentifier3;
    this.valueHint = paramASN1OctetString;
    this.encValue = paramDERBitString2;
  }

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(false, paramInt, paramASN1Encodable));
  }

  public static EncryptedValue getInstance(Object paramObject)
  {
    if ((paramObject instanceof EncryptedValue))
      return (EncryptedValue)paramObject;
    if (paramObject != null)
      return new EncryptedValue(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public DERBitString getEncSymmKey()
  {
    return this.encSymmKey;
  }

  public DERBitString getEncValue()
  {
    return this.encValue;
  }

  public AlgorithmIdentifier getIntendedAlg()
  {
    return this.intendedAlg;
  }

  public AlgorithmIdentifier getKeyAlg()
  {
    return this.keyAlg;
  }

  public AlgorithmIdentifier getSymmAlg()
  {
    return this.symmAlg;
  }

  public ASN1OctetString getValueHint()
  {
    return this.valueHint;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    addOptional(localASN1EncodableVector, 0, this.intendedAlg);
    addOptional(localASN1EncodableVector, 1, this.symmAlg);
    addOptional(localASN1EncodableVector, 2, this.encSymmKey);
    addOptional(localASN1EncodableVector, 3, this.keyAlg);
    addOptional(localASN1EncodableVector, 4, this.valueHint);
    localASN1EncodableVector.add(this.encValue);
    return new DERSequence(localASN1EncodableVector);
  }
}