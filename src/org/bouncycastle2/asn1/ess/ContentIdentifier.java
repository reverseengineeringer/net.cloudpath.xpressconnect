package org.bouncycastle2.asn1.ess;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;

public class ContentIdentifier extends ASN1Encodable
{
  ASN1OctetString value;

  public ContentIdentifier(ASN1OctetString paramASN1OctetString)
  {
    this.value = paramASN1OctetString;
  }

  public ContentIdentifier(byte[] paramArrayOfByte)
  {
    this(new DEROctetString(paramArrayOfByte));
  }

  public static ContentIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ContentIdentifier)))
      return (ContentIdentifier)paramObject;
    if ((paramObject instanceof ASN1OctetString))
      return new ContentIdentifier((ASN1OctetString)paramObject);
    throw new IllegalArgumentException("unknown object in 'ContentIdentifier' factory : " + paramObject.getClass().getName() + ".");
  }

  public ASN1OctetString getValue()
  {
    return this.value;
  }

  public DERObject toASN1Object()
  {
    return this.value;
  }
}