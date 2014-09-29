package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class RecipientIdentifier extends ASN1Encodable
  implements ASN1Choice
{
  private DEREncodable id;

  public RecipientIdentifier(ASN1OctetString paramASN1OctetString)
  {
    this.id = new DERTaggedObject(false, 0, paramASN1OctetString);
  }

  public RecipientIdentifier(DERObject paramDERObject)
  {
    this.id = paramDERObject;
  }

  public RecipientIdentifier(IssuerAndSerialNumber paramIssuerAndSerialNumber)
  {
    this.id = paramIssuerAndSerialNumber;
  }

  public static RecipientIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RecipientIdentifier)))
      return (RecipientIdentifier)paramObject;
    if ((paramObject instanceof IssuerAndSerialNumber))
      return new RecipientIdentifier((IssuerAndSerialNumber)paramObject);
    if ((paramObject instanceof ASN1OctetString))
      return new RecipientIdentifier((ASN1OctetString)paramObject);
    if ((paramObject instanceof DERObject))
      return new RecipientIdentifier((DERObject)paramObject);
    throw new IllegalArgumentException("Illegal object in RecipientIdentifier: " + paramObject.getClass().getName());
  }

  public DEREncodable getId()
  {
    if ((this.id instanceof ASN1TaggedObject))
      return ASN1OctetString.getInstance((ASN1TaggedObject)this.id, false);
    return IssuerAndSerialNumber.getInstance(this.id);
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