package org.bouncycastle2.asn1.icao;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;

public class CscaMasterList extends ASN1Encodable
{
  private X509CertificateStructure[] certList;
  private DERInteger version = new DERInteger(0);

  private CscaMasterList(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence == null) || (paramASN1Sequence.size() == 0))
      throw new IllegalArgumentException("null or empty sequence passed.");
    if (paramASN1Sequence.size() != 2)
      throw new IllegalArgumentException("Incorrect sequence size: " + paramASN1Sequence.size());
    this.version = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    ASN1Set localASN1Set = ASN1Set.getInstance(paramASN1Sequence.getObjectAt(1));
    this.certList = new X509CertificateStructure[localASN1Set.size()];
    for (int i = 0; ; i++)
    {
      if (i >= this.certList.length)
        return;
      this.certList[i] = X509CertificateStructure.getInstance(localASN1Set.getObjectAt(i));
    }
  }

  public CscaMasterList(X509CertificateStructure[] paramArrayOfX509CertificateStructure)
  {
    this.certList = copyCertList(paramArrayOfX509CertificateStructure);
  }

  private X509CertificateStructure[] copyCertList(X509CertificateStructure[] paramArrayOfX509CertificateStructure)
  {
    X509CertificateStructure[] arrayOfX509CertificateStructure = new X509CertificateStructure[paramArrayOfX509CertificateStructure.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfX509CertificateStructure.length)
        return arrayOfX509CertificateStructure;
      arrayOfX509CertificateStructure[i] = paramArrayOfX509CertificateStructure[i];
    }
  }

  public static CscaMasterList getInstance(Object paramObject)
  {
    if ((paramObject instanceof CscaMasterList))
      return (CscaMasterList)paramObject;
    if (paramObject != null)
      return new CscaMasterList(ASN1Sequence.getInstance(paramObject));
    return null;
  }

  public X509CertificateStructure[] getCertStructs()
  {
    return copyCertList(this.certList);
  }

  public int getVersion()
  {
    return this.version.getValue().intValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    localASN1EncodableVector1.add(this.version);
    ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
    for (int i = 0; ; i++)
    {
      if (i >= this.certList.length)
      {
        localASN1EncodableVector1.add(new DERSet(localASN1EncodableVector2));
        return new DERSequence(localASN1EncodableVector1);
      }
      localASN1EncodableVector2.add(this.certList[i]);
    }
  }
}