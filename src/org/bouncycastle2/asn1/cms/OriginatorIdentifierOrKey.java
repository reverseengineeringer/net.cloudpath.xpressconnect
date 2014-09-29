package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.SubjectKeyIdentifier;

public class OriginatorIdentifierOrKey extends ASN1Encodable
  implements ASN1Choice
{
  private DEREncodable id;

  public OriginatorIdentifierOrKey(ASN1OctetString paramASN1OctetString)
  {
    this(new SubjectKeyIdentifier(paramASN1OctetString));
  }

  public OriginatorIdentifierOrKey(DERObject paramDERObject)
  {
    this.id = paramDERObject;
  }

  public OriginatorIdentifierOrKey(IssuerAndSerialNumber paramIssuerAndSerialNumber)
  {
    this.id = paramIssuerAndSerialNumber;
  }

  public OriginatorIdentifierOrKey(OriginatorPublicKey paramOriginatorPublicKey)
  {
    this.id = new DERTaggedObject(false, 1, paramOriginatorPublicKey);
  }

  public OriginatorIdentifierOrKey(SubjectKeyIdentifier paramSubjectKeyIdentifier)
  {
    this.id = new DERTaggedObject(false, 0, paramSubjectKeyIdentifier);
  }

  public static OriginatorIdentifierOrKey getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OriginatorIdentifierOrKey)))
      return (OriginatorIdentifierOrKey)paramObject;
    if ((paramObject instanceof IssuerAndSerialNumber))
      return new OriginatorIdentifierOrKey((IssuerAndSerialNumber)paramObject);
    if ((paramObject instanceof SubjectKeyIdentifier))
      return new OriginatorIdentifierOrKey((SubjectKeyIdentifier)paramObject);
    if ((paramObject instanceof OriginatorPublicKey))
      return new OriginatorIdentifierOrKey((OriginatorPublicKey)paramObject);
    if ((paramObject instanceof ASN1TaggedObject))
      return new OriginatorIdentifierOrKey((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("Invalid OriginatorIdentifierOrKey: " + paramObject.getClass().getName());
  }

  public static OriginatorIdentifierOrKey getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (!paramBoolean)
      throw new IllegalArgumentException("Can't implicitly tag OriginatorIdentifierOrKey");
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DEREncodable getId()
  {
    return this.id;
  }

  public IssuerAndSerialNumber getIssuerAndSerialNumber()
  {
    if ((this.id instanceof IssuerAndSerialNumber))
      return (IssuerAndSerialNumber)this.id;
    return null;
  }

  public OriginatorPublicKey getOriginatorKey()
  {
    if (((this.id instanceof ASN1TaggedObject)) && (((ASN1TaggedObject)this.id).getTagNo() == 1))
      return OriginatorPublicKey.getInstance((ASN1TaggedObject)this.id, false);
    return null;
  }

  public SubjectKeyIdentifier getSubjectKeyIdentifier()
  {
    if (((this.id instanceof ASN1TaggedObject)) && (((ASN1TaggedObject)this.id).getTagNo() == 0))
      return SubjectKeyIdentifier.getInstance((ASN1TaggedObject)this.id, false);
    return null;
  }

  public DERObject toASN1Object()
  {
    return this.id.getDERObject();
  }
}