package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;

public class CertConfirmContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private CertConfirmContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public static CertConfirmContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertConfirmContent))
      return (CertConfirmContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertConfirmContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public CertStatus[] toCertStatusArray()
  {
    CertStatus[] arrayOfCertStatus = new CertStatus[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfCertStatus.length)
        return arrayOfCertStatus;
      arrayOfCertStatus[i] = CertStatus.getInstance(this.content.getObjectAt(i));
    }
  }
}