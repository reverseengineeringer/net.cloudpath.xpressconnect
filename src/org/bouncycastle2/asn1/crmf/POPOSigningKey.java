package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class POPOSigningKey extends ASN1Encodable
{
  private AlgorithmIdentifier algorithmIdentifier;
  private POPOSigningKeyInput poposkInput;
  private DERBitString signature;

  private POPOSigningKey(ASN1Sequence paramASN1Sequence)
  {
    boolean bool = paramASN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject;
    int i = 0;
    if (bool)
    {
      int k = 0 + 1;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(0);
      if (localASN1TaggedObject.getTagNo() != 0)
        throw new IllegalArgumentException("Unknown POPOSigningKeyInput tag: " + localASN1TaggedObject.getTagNo());
      this.poposkInput = POPOSigningKeyInput.getInstance(localASN1TaggedObject.getObject());
      i = k;
    }
    int j = i + 1;
    this.algorithmIdentifier = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(i));
    this.signature = DERBitString.getInstance(paramASN1Sequence.getObjectAt(j));
  }

  public POPOSigningKey(POPOSigningKeyInput paramPOPOSigningKeyInput, AlgorithmIdentifier paramAlgorithmIdentifier, DERBitString paramDERBitString)
  {
    this.poposkInput = paramPOPOSigningKeyInput;
    this.algorithmIdentifier = paramAlgorithmIdentifier;
    this.signature = paramDERBitString;
  }

  public static POPOSigningKey getInstance(Object paramObject)
  {
    if ((paramObject instanceof POPOSigningKey))
      return (POPOSigningKey)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new POPOSigningKey((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public static POPOSigningKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AlgorithmIdentifier getAlgorithmIdentifier()
  {
    return this.algorithmIdentifier;
  }

  public POPOSigningKeyInput getPoposkInput()
  {
    return this.poposkInput;
  }

  public DERBitString getSignature()
  {
    return this.signature;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.poposkInput != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.poposkInput));
    localASN1EncodableVector.add(this.algorithmIdentifier);
    localASN1EncodableVector.add(this.signature);
    return new DERSequence(localASN1EncodableVector);
  }
}