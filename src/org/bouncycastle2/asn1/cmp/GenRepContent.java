package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class GenRepContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private GenRepContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public GenRepContent(InfoTypeAndValue paramInfoTypeAndValue)
  {
    this.content = new DERSequence(paramInfoTypeAndValue);
  }

  public GenRepContent(InfoTypeAndValue[] paramArrayOfInfoTypeAndValue)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfInfoTypeAndValue.length)
      {
        this.content = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfInfoTypeAndValue[i]);
    }
  }

  public static GenRepContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof GenRepContent))
      return (GenRepContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new GenRepContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public InfoTypeAndValue[] toInfoTypeAndValueArray()
  {
    InfoTypeAndValue[] arrayOfInfoTypeAndValue = new InfoTypeAndValue[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfInfoTypeAndValue.length)
        return arrayOfInfoTypeAndValue;
      arrayOfInfoTypeAndValue[i] = InfoTypeAndValue.getInstance(this.content.getObjectAt(i));
    }
  }
}