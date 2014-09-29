package org.bouncycastle2.asn1.crmf;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class CertTemplate extends ASN1Encodable
{
  private X509Extensions extensions;
  private X500Name issuer;
  private DERBitString issuerUID;
  private SubjectPublicKeyInfo publicKey;
  private ASN1Sequence seq;
  private DERInteger serialNumber;
  private AlgorithmIdentifier signingAlg;
  private X500Name subject;
  private DERBitString subjectUID;
  private OptionalValidity validity;
  private DERInteger version;

  private CertTemplate(ASN1Sequence paramASN1Sequence)
  {
    this.seq = paramASN1Sequence;
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)localEnumeration.nextElement();
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("unknown tag: " + localASN1TaggedObject.getTagNo());
      case 0:
        this.version = DERInteger.getInstance(localASN1TaggedObject, false);
        break;
      case 1:
        this.serialNumber = DERInteger.getInstance(localASN1TaggedObject, false);
        break;
      case 2:
        this.signingAlg = AlgorithmIdentifier.getInstance(localASN1TaggedObject, false);
        break;
      case 3:
        this.issuer = X500Name.getInstance(localASN1TaggedObject, true);
        break;
      case 4:
        this.validity = OptionalValidity.getInstance(ASN1Sequence.getInstance(localASN1TaggedObject, false));
        break;
      case 5:
        this.subject = X500Name.getInstance(localASN1TaggedObject, true);
        break;
      case 6:
        this.publicKey = SubjectPublicKeyInfo.getInstance(localASN1TaggedObject, false);
        break;
      case 7:
        this.issuerUID = DERBitString.getInstance(localASN1TaggedObject, false);
        break;
      case 8:
        this.subjectUID = DERBitString.getInstance(localASN1TaggedObject, false);
        break;
      case 9:
        this.extensions = X509Extensions.getInstance(localASN1TaggedObject, false);
      }
    }
  }

  public static CertTemplate getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertTemplate))
      return (CertTemplate)paramObject;
    if (paramObject != null)
      return new CertTemplate(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public X509Extensions getExtensions()
  {
    return this.extensions;
  }

  public X500Name getIssuer()
  {
    return this.issuer;
  }

  public DERBitString getIssuerUID()
  {
    return this.issuerUID;
  }

  public SubjectPublicKeyInfo getPublicKey()
  {
    return this.publicKey;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public AlgorithmIdentifier getSigningAlg()
  {
    return this.signingAlg;
  }

  public X500Name getSubject()
  {
    return this.subject;
  }

  public DERBitString getSubjectUID()
  {
    return this.subjectUID;
  }

  public OptionalValidity getValidity()
  {
    return this.validity;
  }

  public int getVersion()
  {
    return this.version.getValue().intValue();
  }

  public void setPublicKey(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    this.publicKey = paramSubjectPublicKeyInfo;
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }
}