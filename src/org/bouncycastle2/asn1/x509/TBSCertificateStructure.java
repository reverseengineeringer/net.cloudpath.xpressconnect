package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;

public class TBSCertificateStructure extends ASN1Encodable
  implements X509ObjectIdentifiers, PKCSObjectIdentifiers
{
  Time endDate;
  X509Extensions extensions;
  X509Name issuer;
  DERBitString issuerUniqueId;
  ASN1Sequence seq;
  DERInteger serialNumber;
  AlgorithmIdentifier signature;
  Time startDate;
  X509Name subject;
  SubjectPublicKeyInfo subjectPublicKeyInfo;
  DERBitString subjectUniqueId;
  DERInteger version;

  public TBSCertificateStructure(ASN1Sequence paramASN1Sequence)
  {
    int i = 0;
    this.seq = paramASN1Sequence;
    if ((paramASN1Sequence.getObjectAt(0) instanceof DERTaggedObject));
    int j;
    for (this.version = DERInteger.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0), true); ; this.version = new DERInteger(0))
    {
      this.serialNumber = DERInteger.getInstance(paramASN1Sequence.getObjectAt(i + 1));
      this.signature = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(i + 2));
      this.issuer = X509Name.getInstance(paramASN1Sequence.getObjectAt(i + 3));
      ASN1Sequence localASN1Sequence = (ASN1Sequence)paramASN1Sequence.getObjectAt(i + 4);
      this.startDate = Time.getInstance(localASN1Sequence.getObjectAt(0));
      this.endDate = Time.getInstance(localASN1Sequence.getObjectAt(1));
      this.subject = X509Name.getInstance(paramASN1Sequence.getObjectAt(i + 5));
      this.subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(paramASN1Sequence.getObjectAt(i + 6));
      j = -1 + (paramASN1Sequence.size() - (i + 6));
      if (j > 0)
        break;
      return;
      i = -1;
    }
    DERTaggedObject localDERTaggedObject = (DERTaggedObject)paramASN1Sequence.getObjectAt(j + (i + 6));
    switch (localDERTaggedObject.getTagNo())
    {
    default:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      j--;
      break;
      this.issuerUniqueId = DERBitString.getInstance(localDERTaggedObject, false);
      continue;
      this.subjectUniqueId = DERBitString.getInstance(localDERTaggedObject, false);
      continue;
      this.extensions = X509Extensions.getInstance(localDERTaggedObject);
    }
  }

  public static TBSCertificateStructure getInstance(Object paramObject)
  {
    if ((paramObject instanceof TBSCertificateStructure))
      return (TBSCertificateStructure)paramObject;
    if (paramObject != null)
      return new TBSCertificateStructure(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static TBSCertificateStructure getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public Time getEndDate()
  {
    return this.endDate;
  }

  public X509Extensions getExtensions()
  {
    return this.extensions;
  }

  public X509Name getIssuer()
  {
    return this.issuer;
  }

  public DERBitString getIssuerUniqueId()
  {
    return this.issuerUniqueId;
  }

  public DERInteger getSerialNumber()
  {
    return this.serialNumber;
  }

  public AlgorithmIdentifier getSignature()
  {
    return this.signature;
  }

  public Time getStartDate()
  {
    return this.startDate;
  }

  public X509Name getSubject()
  {
    return this.subject;
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.subjectPublicKeyInfo;
  }

  public DERBitString getSubjectUniqueId()
  {
    return this.subjectUniqueId;
  }

  public int getVersion()
  {
    return 1 + this.version.getValue().intValue();
  }

  public DERInteger getVersionNumber()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    return this.seq;
  }
}