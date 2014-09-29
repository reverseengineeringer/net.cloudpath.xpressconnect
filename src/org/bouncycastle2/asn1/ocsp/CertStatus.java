package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;

public class CertStatus extends ASN1Encodable
  implements ASN1Choice
{
  private int tagNo;
  private DEREncodable value;

  public CertStatus()
  {
    this.tagNo = 0;
    this.value = new DERNull();
  }

  public CertStatus(int paramInt, DEREncodable paramDEREncodable)
  {
    this.tagNo = paramInt;
    this.value = paramDEREncodable;
  }

  public CertStatus(ASN1TaggedObject paramASN1TaggedObject)
  {
    this.tagNo = paramASN1TaggedObject.getTagNo();
    switch (paramASN1TaggedObject.getTagNo())
    {
    default:
      return;
    case 0:
      this.value = new DERNull();
      return;
    case 1:
      this.value = RevokedInfo.getInstance(paramASN1TaggedObject, false);
      return;
    case 2:
    }
    this.value = new DERNull();
  }

  public CertStatus(RevokedInfo paramRevokedInfo)
  {
    this.tagNo = 1;
    this.value = paramRevokedInfo;
  }

  public static CertStatus getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof CertStatus)))
      return (CertStatus)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new CertStatus((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static CertStatus getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public DEREncodable getStatus()
  {
    return this.value;
  }

  public int getTagNo()
  {
    return this.tagNo;
  }

  public DERObject toASN1Object()
  {
    return new DERTaggedObject(false, this.tagNo, this.value);
  }
}