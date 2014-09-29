package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x500.X500Name;

public class ResponderID extends ASN1Encodable
  implements ASN1Choice
{
  private DEREncodable value;

  public ResponderID(ASN1OctetString paramASN1OctetString)
  {
    this.value = paramASN1OctetString;
  }

  public ResponderID(X500Name paramX500Name)
  {
    this.value = paramX500Name;
  }

  public static ResponderID getInstance(Object paramObject)
  {
    if ((paramObject instanceof ResponderID))
      return (ResponderID)paramObject;
    if ((paramObject instanceof DEROctetString))
      return new ResponderID((DEROctetString)paramObject);
    if ((paramObject instanceof ASN1TaggedObject))
    {
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramObject;
      if (localASN1TaggedObject.getTagNo() == 1)
        return new ResponderID(X500Name.getInstance(localASN1TaggedObject, true));
      return new ResponderID(ASN1OctetString.getInstance(localASN1TaggedObject, true));
    }
    return new ResponderID(X500Name.getInstance(paramObject));
  }

  public static ResponderID getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public byte[] getKeyHash()
  {
    if ((this.value instanceof ASN1OctetString))
      return ((ASN1OctetString)this.value).getOctets();
    return null;
  }

  public X500Name getName()
  {
    if ((this.value instanceof ASN1OctetString))
      return null;
    return X500Name.getInstance(this.value);
  }

  public DERObject toASN1Object()
  {
    if ((this.value instanceof ASN1OctetString))
      return new DERTaggedObject(true, 2, this.value);
    return new DERTaggedObject(true, 1, this.value);
  }
}