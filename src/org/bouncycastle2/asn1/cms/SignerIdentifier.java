package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class SignerIdentifier extends ASN1Encodable
  implements ASN1Choice
{
  private DEREncodable id;

  public SignerIdentifier(ASN1OctetString paramASN1OctetString)
  {
    this.id = new DERTaggedObject(false, 0, paramASN1OctetString);
  }

  public SignerIdentifier(DERObject paramDERObject)
  {
    this.id = paramDERObject;
  }

  public SignerIdentifier(IssuerAndSerialNumber paramIssuerAndSerialNumber)
  {
    this.id = paramIssuerAndSerialNumber;
  }

  public static SignerIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SignerIdentifier)))
      return (SignerIdentifier)paramObject;
    if ((paramObject instanceof IssuerAndSerialNumber))
      return new SignerIdentifier((IssuerAndSerialNumber)paramObject);
    if ((paramObject instanceof ASN1OctetString))
      return new SignerIdentifier((ASN1OctetString)paramObject);
    if ((paramObject instanceof DERObject))
      return new SignerIdentifier((DERObject)paramObject);
    throw new IllegalArgumentException("Illegal object in SignerIdentifier: " + paramObject.getClass().getName());
  }

  public DEREncodable getId()
  {
    if ((this.id instanceof ASN1TaggedObject))
      return ASN1OctetString.getInstance((ASN1TaggedObject)this.id, false);
    return this.id;
  }

  public boolean isTagged()
  {
    return this.id instanceof ASN1TaggedObject;
  }

  public DERObject toASN1Object()
  {
    return this.id.getDERObject();
  }
}