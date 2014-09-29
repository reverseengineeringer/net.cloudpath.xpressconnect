package org.bouncycastle2.asn1.crmf;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class CertTemplateBuilder
{
  private X509Extensions extensions;
  private X500Name issuer;
  private DERBitString issuerUID;
  private SubjectPublicKeyInfo publicKey;
  private DERInteger serialNumber;
  private AlgorithmIdentifier signingAlg;
  private X500Name subject;
  private DERBitString subjectUID;
  private OptionalValidity validity;
  private DERInteger version;

  private void addOptional(ASN1EncodableVector paramASN1EncodableVector, int paramInt, boolean paramBoolean, ASN1Encodable paramASN1Encodable)
  {
    if (paramASN1Encodable != null)
      paramASN1EncodableVector.add(new DERTaggedObject(paramBoolean, paramInt, paramASN1Encodable));
  }

  public CertTemplate build()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    addOptional(localASN1EncodableVector, 0, false, this.version);
    addOptional(localASN1EncodableVector, 1, false, this.serialNumber);
    addOptional(localASN1EncodableVector, 2, false, this.signingAlg);
    addOptional(localASN1EncodableVector, 3, true, this.issuer);
    addOptional(localASN1EncodableVector, 4, false, this.validity);
    addOptional(localASN1EncodableVector, 5, true, this.subject);
    addOptional(localASN1EncodableVector, 6, false, this.publicKey);
    addOptional(localASN1EncodableVector, 7, false, this.issuerUID);
    addOptional(localASN1EncodableVector, 8, false, this.subjectUID);
    addOptional(localASN1EncodableVector, 9, false, this.extensions);
    return CertTemplate.getInstance(new DERSequence(localASN1EncodableVector));
  }

  public CertTemplateBuilder setExtensions(X509Extensions paramX509Extensions)
  {
    this.extensions = paramX509Extensions;
    return this;
  }

  public CertTemplateBuilder setIssuer(X500Name paramX500Name)
  {
    this.issuer = paramX500Name;
    return this;
  }

  public CertTemplateBuilder setIssuerUID(DERBitString paramDERBitString)
  {
    this.issuerUID = paramDERBitString;
    return this;
  }

  public CertTemplateBuilder setPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.publicKey = paramSubjectPublicKeyInfo;
    return this;
  }

  public CertTemplateBuilder setSerialNumber(DERInteger paramDERInteger)
  {
    this.serialNumber = paramDERInteger;
    return this;
  }

  public CertTemplateBuilder setSigningAlg(AlgorithmIdentifier paramAlgorithmIdentifier)
  {
    this.signingAlg = paramAlgorithmIdentifier;
    return this;
  }

  public CertTemplateBuilder setSubject(X500Name paramX500Name)
  {
    this.subject = paramX500Name;
    return this;
  }

  public CertTemplateBuilder setSubjectUID(DERBitString paramDERBitString)
  {
    this.subjectUID = paramDERBitString;
    return this;
  }

  public CertTemplateBuilder setValidity(OptionalValidity paramOptionalValidity)
  {
    this.validity = paramOptionalValidity;
    return this;
  }

  public CertTemplateBuilder setVersion(int paramInt)
  {
    this.version = new DERInteger(paramInt);
    return this;
  }
}