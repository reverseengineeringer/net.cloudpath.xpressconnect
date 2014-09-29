package org.bouncycastle2.asn1.ocsp;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERIA5String;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class CrlID extends ASN1Encodable
{
  DERInteger crlNum;
  DERGeneralizedTime crlTime;
  DERIA5String crlUrl;

  public CrlID(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localEnumeration.nextElement();
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("unknown tag number: " + localASN1TaggedObject.getTagNo());
      case 0:
        this.crlUrl = DERIA5String.getInstance(localASN1TaggedObject, true);
        break;
      case 1:
        this.crlNum = DERInteger.getInstance(localASN1TaggedObject, true);
        break;
      case 2:
        this.crlTime = DERGeneralizedTime.getInstance(localASN1TaggedObject, true);
      }
    }
  }

  public DERInteger getCrlNum()
  {
    return this.crlNum;
  }

  public DERGeneralizedTime getCrlTime()
  {
    return this.crlTime;
  }

  public DERIA5String getCrlUrl()
  {
    return this.crlUrl;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.crlUrl != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.crlUrl));
    if (this.crlNum != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.crlNum));
    if (this.crlTime != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 2, this.crlTime));
    return new DERSequence(localASN1EncodableVector);
  }
}