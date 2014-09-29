package org.bouncycastle2.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class OcspListID extends ASN1Encodable
{
  private ASN1Sequence ocspResponses;

  private OcspListID(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 1)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.ocspResponses = ((ASN1Sequence)paramASN1Sequence.getObjectAt(0));
    Enumeration localEnumeration = this.ocspResponses.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      OcspResponsesID.getInstance(localEnumeration.nextElement());
    }
  }

  public OcspListID(OcspResponsesID[] paramArrayOfOcspResponsesID)
  {
    this.ocspResponses = new DERSequence(paramArrayOfOcspResponsesID);
  }

  public static OcspListID getInstance(Object paramObject)
  {
    if ((paramObject instanceof OcspListID))
      return (OcspListID)paramObject;
    if (paramObject != null)
      return new OcspListID(ASN1Sequence.getInstance(paramObject));
    throw new IllegalArgumentException("null value in getInstance");
  }

  public OcspResponsesID[] getOcspResponses()
  {
    OcspResponsesID[] arrayOfOcspResponsesID = new OcspResponsesID[this.ocspResponses.size()];
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfOcspResponsesID.length)
        return arrayOfOcspResponsesID;
      arrayOfOcspResponsesID[i] = OcspResponsesID.getInstance(this.ocspResponses.getObjectAt(i));
    }
  }

  public DERObject toASN1Object()
  {
    return new DERSequence(this.ocspResponses);
  }
}