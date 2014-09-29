package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CRLDistPoint extends ASN1Encodable
{
  ASN1Sequence seq = null;

  public CRLDistPoint(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
  }

  public CRLDistPoint(DistributionPoint[] paramArrayOfDistributionPoint)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfDistributionPoint.length)
      {
        this.seq = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfDistributionPoint[i]);
    }
  }

  public static CRLDistPoint getInstance(Object paramObject)
  {
    if (((paramObject instanceof CRLDistPoint)) || (paramObject == null))
      return (CRLDistPoint)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CRLDistPoint((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static CRLDistPoint getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DistributionPoint[] getDistributionPoints()
  {
    DistributionPoint[] arrayOfDistributionPoint = new DistributionPoint[this.seq.size()];
    for (int i = 0; ; i++)
    {
      if (i == this.seq.size())
        return arrayOfDistributionPoint;
      arrayOfDistributionPoint[i] = DistributionPoint.getInstance(this.seq.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str = System.getProperty("line.separator");
    localStringBuffer.append("CRLDistPoint:");
    localStringBuffer.append(str);
    DistributionPoint[] arrayOfDistributionPoint = getDistributionPoints();
    for (int i = 0; ; i++)
    {
      if (i == arrayOfDistributionPoint.length)
        return localStringBuffer.toString();
      localStringBuffer.append("    ");
      localStringBuffer.append(arrayOfDistributionPoint[i]);
      localStringBuffer.append(str);
    }
  }
}