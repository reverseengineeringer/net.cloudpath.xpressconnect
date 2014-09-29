package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Null;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;

public class PKIConfirmContent extends ASN1Encodable
{
  private ASN1Null val;

  public PKIConfirmContent()
  {
    this.val = DERNull.INSTANCE;
  }

  private PKIConfirmContent(ASN1Null paramASN1Null)
  {
    this.val = paramASN1Null;
  }

  public static PKIConfirmContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIConfirmContent))
      return (PKIConfirmContent)paramObject;
    if ((paramObject instanceof ASN1Null))
      return new PKIConfirmContent((ASN1Null)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.val;
  }
}