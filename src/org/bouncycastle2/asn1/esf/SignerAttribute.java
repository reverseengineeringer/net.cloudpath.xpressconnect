package org.bouncycastle2.asn1.esf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AttributeCertificate;

public class SignerAttribute extends ASN1Encodable
{
  private AttributeCertificate certifiedAttributes;
  private ASN1Sequence claimedAttributes;

  private SignerAttribute(Object paramObject)
  {
    DERTaggedObject localDERTaggedObject = (DERTaggedObject)((ASN1Sequence)paramObject).getObjectAt(0);
    if (localDERTaggedObject.getTagNo() == 0)
    {
      this.claimedAttributes = ASN1Sequence.getInstance(localDERTaggedObject, true);
      return;
    }
    if (localDERTaggedObject.getTagNo() == 1)
    {
      this.certifiedAttributes = AttributeCertificate.getInstance(localDERTaggedObject);
      return;
    }
    throw new IllegalArgumentException("illegal tag.");
  }

  public SignerAttribute(ASN1Sequence paramASN1Sequence)
  {
    this.claimedAttributes = paramASN1Sequence;
  }

  public SignerAttribute(AttributeCertificate paramAttributeCertificate)
  {
    this.certifiedAttributes = paramAttributeCertificate;
  }

  public static SignerAttribute getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SignerAttribute)))
      return (SignerAttribute)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SignerAttribute(paramObject);
    throw new IllegalArgumentException("unknown object in 'SignerAttribute' factory: " + paramObject.getClass().getName() + ".");
  }

  public AttributeCertificate getCertifiedAttributes()
  {
    return this.certifiedAttributes;
  }

  public ASN1Sequence getClaimedAttributes()
  {
    return this.claimedAttributes;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.claimedAttributes != null)
      localASN1EncodableVector.add(new DERTaggedObject(0, this.claimedAttributes));
    while (true)
    {
      return new DERSequence(localASN1EncodableVector);
      localASN1EncodableVector.add(new DERTaggedObject(1, this.certifiedAttributes));
    }
  }
}