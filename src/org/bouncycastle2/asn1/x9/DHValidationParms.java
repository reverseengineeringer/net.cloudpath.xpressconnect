package org.bouncycastle2.asn1.x9;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class DHValidationParms extends ASN1Encodable
{
  private DERInteger pgenCounter;
  private DERBitString seed;

  private DHValidationParms(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.seed = DERBitString.getInstance(paramASN1Sequence.getObjectAt(0));
    this.pgenCounter = DERInteger.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public DHValidationParms(DERBitString paramDERBitString, DERInteger paramDERInteger)
  {
    if (paramDERBitString == null)
      throw new IllegalArgumentException("'seed' cannot be null");
    if (paramDERInteger == null)
      throw new IllegalArgumentException("'pgenCounter' cannot be null");
    this.seed = paramDERBitString;
    this.pgenCounter = paramDERInteger;
  }

  public static DHValidationParms getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DHDomainParameters)))
      return (DHValidationParms)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new DHValidationParms((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid DHValidationParms: " + paramObject.getClass().getName());
  }

  public static DHValidationParms getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERInteger getPgenCounter()
  {
    return this.pgenCounter;
  }

  public DERBitString getSeed()
  {
    return this.seed;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.seed);
    localASN1EncodableVector.add(this.pgenCounter);
    return new DERSequence(localASN1EncodableVector);
  }
}