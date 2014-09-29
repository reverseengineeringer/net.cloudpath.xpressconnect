package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;

public class V2AttributeCertificateInfoGenerator
{
  private ASN1EncodableVector attributes = new ASN1EncodableVector();
  private DERGeneralizedTime endDate;
  private X509Extensions extensions;
  private Holder holder;
  private AttCertIssuer issuer;
  private DERBitString issuerUniqueID;
  private DERInteger serialNumber;
  private AlgorithmIdentifier signature;
  private DERGeneralizedTime startDate;
  private DERInteger version = new DERInteger(1);

  public void addAttribute(String paramString, ASN1Encodable paramASN1Encodable)
  {
    this.attributes.add(new Attribute(new DERObjectIdentifier(paramString), new DERSet(paramASN1Encodable)));
  }

  public void addAttribute(Attribute paramAttribute)
  {
    this.attributes.add(paramAttribute);
  }

  public AttributeCertificateInfo generateAttributeCertificateInfo()
  {
    if ((this.serialNumber == null) || (this.signature == null) || (this.issuer == null) || (this.startDate == null) || (this.endDate == null) || (this.holder == null) || (this.attributes == null))
      throw new IllegalStateException("not all mandatory fields set in V2 AttributeCertificateInfo generator");
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.holder);
    localASN1EncodableVector.add(this.issuer);
    localASN1EncodableVector.add(this.signature);
    localASN1EncodableVector.add(this.serialNumber);
    localASN1EncodableVector.add(new AttCertValidityPeriod(this.startDate, this.endDate));
    localASN1EncodableVector.add(new DERSequence(this.attributes));
    if (this.issuerUniqueID != null)
      localASN1EncodableVector.add(this.issuerUniqueID);
    if (this.extensions != null)
      localASN1EncodableVector.add(this.extensions);
    return new AttributeCertificateInfo(new DERSequence(localASN1EncodableVector));
  }

  public void setEndDate(DERGeneralizedTime paramDERGeneralizedTime)
  {
    this.endDate = paramDERGeneralizedTime;
  }

  public void setExtensions(X509Extensions paramX509Extensions)
  {
    this.extensions = paramX509Extensions;
  }

  public void setHolder(Holder paramHolder)
  {
    this.holder = paramHolder;
  }

  public void setIssuer(AttCertIssuer paramAttCertIssuer)
  {
    this.issuer = paramAttCertIssuer;
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

  public void setStartDate(DERGeneralizedTime paramDERGeneralizedTime)
  {
    this.startDate = paramDERGeneralizedTime;
  }
}