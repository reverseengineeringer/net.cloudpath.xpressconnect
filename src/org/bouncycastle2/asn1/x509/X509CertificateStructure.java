package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;

public class X509CertificateStructure extends ASN1Encodable
  implements X509ObjectIdentifiers, PKCSObjectIdentifiers
{
  ASN1Sequence seq;
  DERBitString sig;
  AlgorithmIdentifier sigAlgId;
  TBSCertificateStructure tbsCert;

  public X509CertificateStructure(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
    if (paramASN1Sequence.size() == 3)
    {
      this.tbsCert = TBSCertificateStructure.getInstance(paramASN1Sequence.getObjectAt(0));
      this.sigAlgId = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
      this.sig = DERBitString.getInstance(paramASN1Sequence.getObjectAt(2));
      return;
    }
    throw new IllegalArgumentException("sequence wrong size for a certificate");
  }

  public static X509CertificateStructure getInstance(Object paramObject)
  {
    if ((paramObject instanceof X509CertificateStructure))
      return (X509CertificateStructure)paramObject;
    if (paramObject != null)
      return new X509CertificateStructure(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static X509CertificateStructure getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public Time getEndDate()
  {
    return this.tbsCert.getEndDate();
  }

  public X509Name getIssuer()
  {
    return this.tbsCert.getIssuer();
  }

  public DERInteger getSerialNumber()
  {
    return this.tbsCert.getSerialNumber();
  }

  public DERBitString getSignature()
  {
    return this.sig;
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.sigAlgId;
  }

  public Time getStartDate()
  {
    return this.tbsCert.getStartDate();
  }

  public X509Name getSubject()
  {
    return this.tbsCert.getSubject();
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.tbsCert.getSubjectPublicKeyInfo();
  }

  public TBSCertificateStructure getTBSCertificate()
  {
    return this.tbsCert;
  }

  public int getVersion()
  {
    return this.tbsCert.getVersion();
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }
}