package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AttributeCertificate;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public class CMPCertificate extends ASN1Encodable
  implements ASN1Choice
{
  private AttributeCertificate x509v2AttrCert;
  private X509CertificateStructure x509v3PKCert;

  public CMPCertificate(AttributeCertificate paramAttributeCertificate)
  {
    this.x509v2AttrCert = paramAttributeCertificate;
  }

  public CMPCertificate(X509CertificateStructure paramX509CertificateStructure)
  {
    if (paramX509CertificateStructure.getVersion() != 3)
      throw new IllegalArgumentException("only version 3 certificates allowed");
    this.x509v3PKCert = paramX509CertificateStructure;
  }

  public static CMPCertificate getInstance(Object paramObject)
  {
    if ((paramObject instanceof CMPCertificate))
      return (CMPCertificate)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CMPCertificate(X509CertificateStructure.getInstance(paramObject));
    if ((paramObject instanceof ASN1TaggedObject))
      return new CMPCertificate(AttributeCertificate.getInstance(((ASN1TaggedObject)paramObject).getObject()));
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public AttributeCertificate getX509v2AttrCert()
  {
    return this.x509v2AttrCert;
  }

  public X509CertificateStructure getX509v3PKCert()
  {
    return this.x509v3PKCert;
  }

  public boolean isX509v3PKCert()
  {
    return this.x509v3PKCert != null;
  }

  public DERObject toASN1Object()
  {
    if (this.x509v2AttrCert != null)
      return new DERTaggedObject(true, 1, this.x509v2AttrCert);
    return this.x509v3PKCert.toASN1Object();
  }
}