package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class PKIMessages extends ASN1Encodable
{
  private ASN1Sequence content;

  private PKIMessages(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public PKIMessages(PKIMessage paramPKIMessage)
  {
    this.content = new DERSequence(paramPKIMessage);
  }

  public PKIMessages(PKIMessage[] paramArrayOfPKIMessage)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfPKIMessage.length)
      {
        this.content = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfPKIMessage[i]);
    }
  }

  public static PKIMessages getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIMessages))
      return (PKIMessages)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKIMessages((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public PKIMessage[] toPKIMessageArray()
  {
    PKIMessage[] arrayOfPKIMessage = new PKIMessage[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfPKIMessage.length)
        return arrayOfPKIMessage;
      arrayOfPKIMessage[i] = PKIMessage.getInstance(this.content.getObjectAt(i));
    }
  }
}