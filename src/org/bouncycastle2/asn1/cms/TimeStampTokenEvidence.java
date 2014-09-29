package org.bouncycastle2.asn1.cms;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class TimeStampTokenEvidence extends ASN1Encodable
{
  private TimeStampAndCRL[] timeStampAndCRLs;

  private TimeStampTokenEvidence(ASN1Sequence paramASN1Sequence)
  {
    this.timeStampAndCRLs = new TimeStampAndCRL[paramASN1Sequence.size()];
    int i = 0;
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      TimeStampAndCRL[] arrayOfTimeStampAndCRL = this.timeStampAndCRLs;
      int j = i + 1;
      arrayOfTimeStampAndCRL[i] = TimeStampAndCRL.getInstance(localEnumeration.nextElement());
      i = j;
    }
  }

  public TimeStampTokenEvidence(TimeStampAndCRL paramTimeStampAndCRL)
  {
    this.timeStampAndCRLs = new TimeStampAndCRL[1];
    this.timeStampAndCRLs[0] = paramTimeStampAndCRL;
  }

  public TimeStampTokenEvidence(TimeStampAndCRL[] paramArrayOfTimeStampAndCRL)
  {
    this.timeStampAndCRLs = paramArrayOfTimeStampAndCRL;
  }

  public static TimeStampTokenEvidence getInstance(Object paramObject)
  {
    if ((paramObject instanceof TimeStampTokenEvidence))
      return (TimeStampTokenEvidence)paramObject;
    if (paramObject != null)
      return new TimeStampTokenEvidence(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static TimeStampTokenEvidence getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i == this.timeStampAndCRLs.length)
        return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(this.timeStampAndCRLs[i]);
    }
  }

  public TimeStampAndCRL[] toTimeStampAndCRLArray()
  {
    return this.timeStampAndCRLs;
  }
}