package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class Evidence extends ASN1Encodable
  implements ASN1Choice
{
  private TimeStampTokenEvidence tstEvidence;

  private Evidence(ASN1TaggedObject paramASN1TaggedObject)
  {
    if (paramASN1TaggedObject.getTagNo() == 0)
      this.tstEvidence = TimeStampTokenEvidence.getInstance(paramASN1TaggedObject, false);
  }

  public Evidence(TimeStampTokenEvidence paramTimeStampTokenEvidence)
  {
    this.tstEvidence = paramTimeStampTokenEvidence;
  }

  public static Evidence getInstance(Object paramObject)
  {
    if ((paramObject instanceof Evidence))
      return (Evidence)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new Evidence(ASN1TaggedObject.getInstance(paramObject));
    throw new IllegalArgumentException("unknown object in getInstance");
  }

  public TimeStampTokenEvidence getTstEvidence()
  {
    return this.tstEvidence;
  }

  public DERObject toASN1Object()
  {
    if (this.tstEvidence != null)
      return new DERTaggedObject(false, 0, this.tstEvidence);
    return null;
  }
}