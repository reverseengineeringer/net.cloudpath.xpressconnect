package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class RevReqContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private RevReqContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public RevReqContent(RevDetails paramRevDetails)
  {
    this.content = new DERSequence(paramRevDetails);
  }

  public RevReqContent(RevDetails[] paramArrayOfRevDetails)
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfRevDetails.length)
      {
        this.content = new DERSequence(localASN1EncodableVector);
        return;
      }
      localASN1EncodableVector.add(paramArrayOfRevDetails[i]);
    }
  }

  public static RevReqContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof RevReqContent))
      return (RevReqContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RevReqContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public RevDetails[] toRevDetailsArray()
  {
    RevDetails[] arrayOfRevDetails = new RevDetails[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfRevDetails.length)
        return arrayOfRevDetails;
      arrayOfRevDetails[i] = RevDetails.getInstance(this.content.getObjectAt(i));
    }
  }
}