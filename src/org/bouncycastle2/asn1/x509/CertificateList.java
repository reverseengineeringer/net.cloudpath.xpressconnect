package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class CertificateList extends ASN1Encodable
{
  DERBitString sig;
  AlgorithmIdentifier sigAlgId;
  TBSCertList tbsCertList;

  public CertificateList(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() == 3)
    {
      this.tbsCertList = TBSCertList.getInstance(paramASN1Sequence.getObjectAt(0));
      this.sigAlgId = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
      this.sig = DERBitString.getInstance(paramASN1Sequence.getObjectAt(2));
      return;
    }
    throw new IllegalArgumentException("sequence wrong size for CertificateList");
  }

  public static CertificateList getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertificateList))
      return (CertificateList)paramObject;
    if (paramObject != null)
      return new CertificateList(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public static CertificateList getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public X509Name getIssuer()
  {
    return this.tbsCertList.getIssuer();
  }

  public Time getNextUpdate()
  {
    return this.tbsCertList.getNextUpdate();
  }

  public Enumeration getRevokedCertificateEnumeration()
  {
    return this.tbsCertList.getRevokedCertificateEnumeration();
  }

  public TBSCertList.CRLEntry[] getRevokedCertificates()
  {
    return this.tbsCertList.getRevokedCertificates();
  }

  public DERBitString getSignature()
  {
    return this.sig;
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.sigAlgId;
  }

  public TBSCertList getTBSCertList()
  {
    return this.tbsCertList;
  }

  public Time getThisUpdate()
  {
    return this.tbsCertList.getThisUpdate();
  }

  public int getVersion()
  {
    return this.tbsCertList.getVersion();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.tbsCertList);
    localASN1EncodableVector.add(this.sigAlgId);
    localASN1EncodableVector.add(this.sig);
    return new DERSequence(localASN1EncodableVector);
  }
}