package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class SigPolicyQualifiers extends ASN1Encodable
{
  ASN1Sequence qualifiers;

  public SigPolicyQualifiers(ASN1Sequence paramASN1Sequence)
  {
    this.qualifiers = paramASN1Sequence;
  }

  public SigPolicyQualifiers(SigPolicyQualifierInfo[] paramArrayOfSigPolicyQualifierInfo)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfSigPolicyQualifierInfo.length)
      {
        this.qualifiers = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfSigPolicyQualifierInfo[i]);
    }
  }

  public static SigPolicyQualifiers getInstance(Object paramObject)
  {
    if ((paramObject instanceof SigPolicyQualifiers))
      return (SigPolicyQualifiers)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SigPolicyQualifiers((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in 'SigPolicyQualifiers' factory: " + paramObject.getClass().getName() + ".");
  }

  public SigPolicyQualifierInfo getStringAt(int paramInt)
  {
    return SigPolicyQualifierInfo.getInstance(this.qualifiers.getObjectAt(paramInt));
  }

  public int size()
  {
    return this.qualifiers.size();
  }

  public DERObject toASN1Object()
  {
    return this.qualifiers;
  }
}