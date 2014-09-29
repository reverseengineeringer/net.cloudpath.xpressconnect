package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;

public class POPODecKeyRespContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private POPODecKeyRespContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public static POPODecKeyRespContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof POPODecKeyRespContent))
      return (POPODecKeyRespContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new POPODecKeyRespContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public DERInteger[] toDERIntegerArray()
  {
    DERInteger[] arrayOfDERInteger = new DERInteger[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfDERInteger.length)
        return arrayOfDERInteger;
      arrayOfDERInteger[i] = DERInteger.getInstance(this.content.getObjectAt(i));
    }
  }
}