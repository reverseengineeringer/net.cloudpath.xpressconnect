package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class PollReqContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private PollReqContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public static PollReqContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof PollReqContent))
      return (PollReqContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PollReqContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  private DERInteger[] seqenceToDERIntegerArray(ASN1Sequence paramASN1Sequence)
  {
    DERInteger[] arrayOfDERInteger = new DERInteger[paramASN1Sequence.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfDERInteger.length)
        return arrayOfDERInteger;
      arrayOfDERInteger[i] = DERInteger.getInstance(paramASN1Sequence.getObjectAt(i));
    }
  }

  public DERInteger[][] getCertReqIds()
  {
    DERInteger[][] arrayOfDERInteger; = new DERInteger[this.content.size()][];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfDERInteger;.length)
        return arrayOfDERInteger;;
      arrayOfDERInteger;[i] = seqenceToDERIntegerArray((ASN1Sequence)this.content.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }
}