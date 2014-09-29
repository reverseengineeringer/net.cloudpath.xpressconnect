package org.bouncycastle2.asn1.x509;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class AttributeCertificateInfo extends ASN1Encodable
{
  private AttCertValidityPeriod attrCertValidityPeriod;
  private ASN1Sequence attributes;
  private X509Extensions extensions;
  private Holder holder;
  private AttCertIssuer issuer;
  private DERBitString issuerUniqueID;
  private DERInteger serialNumber;
  private AlgorithmIdentifier signature;
  private DERInteger version;

  public AttributeCertificateInfo(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() < 7) || (paramASN1Sequence.size() > 9))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    this.version = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.holder = Holder.getInstance(paramASN1Sequence.getObjectAt(1));
    this.issuer = AttCertIssuer.getInstance(paramASN1Sequence.getObjectAt(2));
    this.signature = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(3));
    this.serialNumber = DERInteger.getInstance(paramASN1Sequence.getObjectAt(4));
    this.attrCertValidityPeriod = AttCertValidityPeriod.getInstance(paramASN1Sequence.getObjectAt(5));
    this.attributes = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(6));
    int i = 7;
    if (i >= paramASN1Sequence.size())
      return;
    ASN1Encodable localASN1Encodable = (ASN1Encodable)paramASN1Sequence.getObjectAt(i);
    if ((localASN1Encodable instanceof DERBitString))
      this.issuerUniqueID = DERBitString.getInstance(paramASN1Sequence.getObjectAt(i));
    while (true)
    {
      i++;
      break;
      if (((localASN1Encodable instanceof ASN1Sequence)) || ((localASN1Encodable instanceof X509Extensions)))
        this.extensions = X509Extensions.getInstance(paramASN1Sequence.getObjectAt(i));
    }
  }

  public static AttributeCertificateInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof AttributeCertificateInfo))
      return (AttributeCertificateInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new AttributeCertificateInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static AttributeCertificateInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public AttCertValidityPeriod getAttrCertValidityPeriod()
  {
    return this.attrCertValidityPeriod;
  }

  public ASN1Sequence getAttributes()
  {
    return this.attributes;
  }

  public X509Extensions getExtensions()
  {
    return this.extensions;
  }

  public Holder getHolder()
  {
    return this.holder;
  }

  public AttCertIssuer getIssuer()
  {
    return this.issuer;
  }

  public DERBitString getIssuerUniqueID()
  {
    return this.issuerUniqueID;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public AlgorithmIdentifier getSignature()
  {
    return this.signature;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.holder);
    localASN1EncodableVector.add(this.issuer);
    localASN1EncodableVector.add(this.signature);
    localASN1EncodableVector.add(this.serialNumber);
    localASN1EncodableVector.add(this.attrCertValidityPeriod);
    localASN1EncodableVector.add(this.attributes);
    if (this.issuerUniqueID != null)
      localASN1EncodableVector.add(this.issuerUniqueID);
    if (this.extensions != null)
      localASN1EncodableVector.add(this.extensions);
    return new DERSequence(localASN1EncodableVector);
  }
}