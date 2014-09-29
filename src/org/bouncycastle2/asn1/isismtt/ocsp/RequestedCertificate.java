package org.bouncycastle2.asn1.isismtt.ocsp;

import java.io.IOException;
import org.bouncycastle2.asn1.ASN1Choice;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public class RequestedCertificate extends ASN1Encodable
  implements ASN1Choice
{
  public static final int attributeCertificate = 1;
  public static final int certificate = -1;
  public static final int publicKeyCertificate;
  private byte[] attributeCert;
  private X509CertificateStructure cert;
  private byte[] publicKeyCert;

  public RequestedCertificate(int paramInt, byte[] paramArrayOfByte)
  {
    this(new DERTaggedObject(paramInt, new DEROctetString(paramArrayOfByte)));
  }

  private RequestedCertificate(ASN1TaggedObject paramASN1TaggedObject)
  {
    if (paramASN1TaggedObject.getTagNo() == 0)
    {
      this.publicKeyCert = ASN1OctetString.getInstance(paramASN1TaggedObject, true).getOctets();
      return;
    }
    if (paramASN1TaggedObject.getTagNo() == 1)
    {
      this.attributeCert = ASN1OctetString.getInstance(paramASN1TaggedObject, true).getOctets();
      return;
    }
    throw new IllegalArgumentException("unknown tag number: " + paramASN1TaggedObject.getTagNo());
  }

  public RequestedCertificate(X509CertificateStructure paramX509CertificateStructure)
  {
    this.cert = paramX509CertificateStructure;
  }

  public static RequestedCertificate getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RequestedCertificate)))
      return (RequestedCertificate)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RequestedCertificate(X509CertificateStructure.getInstance(paramObject));
    if ((paramObject instanceof ASN1TaggedObject))
      return new RequestedCertificate((ASN1TaggedObject)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static RequestedCertificate getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    if (!paramBoolean)
      throw new IllegalArgumentException("choice item must be explicitly tagged");
    return getInstance(paramASN1TaggedObject.getObject());
  }

  public byte[] getCertificateBytes()
  {
    if (this.cert != null)
      try
      {
        byte[] arrayOfByte = this.cert.getEncoded();
        return arrayOfByte;
      }
      catch (IOException localIOException)
      {
        throw new IllegalStateException("can't decode certificate: " + localIOException);
      }
    if (this.publicKeyCert != null)
      return this.publicKeyCert;
    return this.attributeCert;
  }

  public int getType()
  {
    if (this.cert != null)
      return -1;
    if (this.publicKeyCert != null)
      return 0;
    return 1;
  }

  public DERObject toASN1Object()
  {
    if (this.publicKeyCert != null)
      return new DERTaggedObject(0, new DEROctetString(this.publicKeyCert));
    if (this.attributeCert != null)
      return new DERTaggedObject(1, new DEROctetString(this.attributeCert));
    return this.cert.getDERObject();
  }
}