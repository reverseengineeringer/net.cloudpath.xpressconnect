package org.bouncycastle2.asn1.crmf;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.Time;

public class OptionalValidity extends ASN1Encodable
{
  private Time notAfter;
  private Time notBefore;

  private OptionalValidity(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localEnumeration.nextElement();
      if (localASN1TaggedObject.getTagNo() == 0)
        this.notBefore = Time.getInstance(localASN1TaggedObject, true);
      else
        this.notAfter = Time.getInstance(localASN1TaggedObject, true);
    }
  }

  public static OptionalValidity getInstance(Object paramObject)
  {
    if ((paramObject instanceof OptionalValidity))
      return (OptionalValidity)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OptionalValidity((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.notBefore != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.notBefore));
    if (this.notAfter != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.notAfter));
    return new DERSequence(localASN1EncodableVector);
  }
}