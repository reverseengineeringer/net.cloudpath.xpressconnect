package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.crmf.EncryptedValue;

public class CertOrEncCert extends ASN1Encodable
  implements ASN1Choice
{
  private CMPCertificate certificate;
  private EncryptedValue encryptedCert;

  private CertOrEncCert(ASN1TaggedObject paramASN1TaggedObject)
  {
    if (paramASN1TaggedObject.getTagNo() == 0)
    {
      this.certificate = CMPCertificate.getInstance(paramASN1TaggedObject.getObject());
      return;
    }
    if (paramASN1TaggedObject.getTagNo() == 1)
    {
      this.encryptedCert = EncryptedValue.getInstance(paramASN1TaggedObject.getObject());
      return;
    }
    throw new IllegalArgumentException("unknown tag: " + paramASN1TaggedObject.getTagNo());
  }

  public CertOrEncCert(CMPCertificate paramCMPCertificate)
  {
    if (paramCMPCertificate == null)
      throw new IllegalArgumentException("'certificate' cannot be null");
    this.certificate = paramCMPCertificate;
  }

  public CertOrEncCert(EncryptedValue paramEncryptedValue)
  {
    if (paramEncryptedValue == null)
      throw new IllegalArgumentException("'encryptedCert' cannot be null");
    this.encryptedCert = paramEncryptedValue;
  }

  public static CertOrEncCert getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertOrEncCert))
      return (CertOrEncCert)paramObject;
    if ((paramObject instanceof ASN1TaggedObject))
      return new CertOrEncCert((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CMPCertificate getCertificate()
  {
    return this.certificate;
  }

  public EncryptedValue getEncryptedCert()
  {
    return this.encryptedCert;
  }

  public DERObject toASN1Object()
  {
    if (this.certificate != null)
      return new DERTaggedObject(true, 0, this.certificate);
    return new DERTaggedObject(true, 1, this.encryptedCert);
  }
}