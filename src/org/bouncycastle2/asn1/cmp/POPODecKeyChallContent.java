package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;

public class POPODecKeyChallContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private POPODecKeyChallContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public static POPODecKeyChallContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof POPODecKeyChallContent))
      return (POPODecKeyChallContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new POPODecKeyChallContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public Challenge[] toChallengeArray()
  {
    Challenge[] arrayOfChallenge = new Challenge[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChallenge.length)
        return arrayOfChallenge;
      arrayOfChallenge[i] = Challenge.getInstance(this.content.getObjectAt(i));
    }
  }
}