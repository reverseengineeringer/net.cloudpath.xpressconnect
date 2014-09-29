package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.x509.CertificateList;

public class CRLAnnContent extends ASN1Encodable
{
  private ASN1Sequence content;

  private CRLAnnContent(ASN1Sequence paramASN1Sequence)
  {
    this.content = paramASN1Sequence;
  }

  public static CRLAnnContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof CRLAnnContent))
      return (CRLAnnContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CRLAnnContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObject toASN1Object()
  {
    return this.content;
  }

  public CertificateList[] toCertificateListArray()
  {
    CertificateList[] arrayOfCertificateList = new CertificateList[this.content.size()];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfCertificateList.length)
        return arrayOfCertificateList;
      arrayOfCertificateList[i] = CertificateList.getInstance(this.content.getObjectAt(i));
    }
  }
}