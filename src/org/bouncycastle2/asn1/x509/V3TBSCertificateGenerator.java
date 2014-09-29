package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.DERUTCTime;
import org.bouncycastle2.asn1.x500.X500Name;

public class V3TBSCertificateGenerator
{
  private boolean altNamePresentAndCritical;
  Time endDate;
  X509Extensions extensions;
  X509Name issuer;
  private DERBitString issuerUniqueID;
  DERInteger serialNumber;
  AlgorithmIdentifier signature;
  Time startDate;
  X509Name subject;
  SubjectPublicKeyInfo subjectPublicKeyInfo;
  private DERBitString subjectUniqueID;
  DERTaggedObject version = new DERTaggedObject(0, new DERInteger(2));

  public TBSCertificateStructure generateTBSCertificate()
  {
    if ((this.serialNumber == null) || (this.signature == null) || (this.issuer == null) || (this.startDate == null) || (this.endDate == null) || ((this.subject == null) && (!this.altNamePresentAndCritical)) || (this.subjectPublicKeyInfo == null))
      throw new IllegalStateException("not all mandatory fields set in V3 TBScertificate generator");
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    localASN1EncodableVector1.add(this.version);
    localASN1EncodableVector1.add(this.serialNumber);
    localASN1EncodableVector1.add(this.signature);
    localASN1EncodableVector1.add(this.issuer);
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    localASN1EncodableVector2.add(this.startDate);
    localASN1EncodableVector2.add(this.endDate);
    localASN1EncodableVector1.add(new DERSequence(localASN1EncodableVector2));
    if (this.subject != null)
      localASN1EncodableVector1.add(this.subject);
    while (true)
    {
      localASN1EncodableVector1.add(this.subjectPublicKeyInfo);
      if (this.issuerUniqueID != null)
        localASN1EncodableVector1.add(new DERTaggedObject(false, 1, this.issuerUniqueID));
      if (this.subjectUniqueID != null)
        localASN1EncodableVector1.add(new DERTaggedObject(false, 2, this.subjectUniqueID));
      if (this.extensions != null)
        localASN1EncodableVector1.add(new DERTaggedObject(3, this.extensions));
      return new TBSCertificateStructure(new DERSequence(localASN1EncodableVector1));
      localASN1EncodableVector1.add(new DERSequence());
    }
  }

  public void setEndDate(DERUTCTime paramDERUTCTime)
  {
    this.endDate = new Time(paramDERUTCTime);
  }

  public void setEndDate(Time paramTime)
  {
    this.endDate = paramTime;
  }

  public void setExtensions(X509Extensions paramX509Extensions)
  {
    this.extensions = paramX509Extensions;
    if (paramX509Extensions != null)
    {
      X509Extension localX509Extension = paramX509Extensions.getExtension(X509Extensions.SubjectAlternativeName);
      if ((localX509Extension != null) && (localX509Extension.isCritical()))
        this.altNamePresentAndCritical = true;
    }
  }

  public void setIssuer(X500Name paramX500Name)
  {
    this.issuer = X509Name.getInstance(paramX500Name.getDERObject());
  }

  public void setIssuer(X509Name paramX509Name)
  {
    this.issuer = paramX509Name;
  }

  public void setIssuerUniqueID(DERBitString paramDERBitString)
  {
    this.issuerUniqueID = paramDERBitString;
  }

  public void setSerialNumber(DERInteger paramDERInteger)
  {
    this.serialNumber = paramDERInteger;
  }

  public void setSignature(AlgorithmIdentifier paramAlgorithmIdentifier)
  {
    this.signature = paramAlgorithmIdentifier;
  }

  public void setStartDate(DERUTCTime paramDERUTCTime)
  {
    this.startDate = new Time(paramDERUTCTime);
  }

  public void setStartDate(Time paramTime)
  {
    this.startDate = paramTime;
  }

  public void setSubject(X500Name paramX500Name)
  {
    this.subject = X509Name.getInstance(paramX500Name.getDERObject());
  }

  public void setSubject(X509Name paramX509Name)
  {
    this.subject = paramX509Name;
  }

  public void setSubjectPublicKeyInfo(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.subjectPublicKeyInfo = paramSubjectPublicKeyInfo;
  }

  public void setSubjectUniqueID(DERBitString paramDERBitString)
  {
    this.subjectUniqueID = paramDERBitString;
  }
}