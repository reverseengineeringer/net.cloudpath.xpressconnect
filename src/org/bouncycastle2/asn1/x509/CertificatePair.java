package org.bouncycastle2.asn1.x509;

import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class CertificatePair extends ASN1Encodable
{
  private X509CertificateStructure forward;
  private X509CertificateStructure reverse;

  private CertificatePair(ASN1Sequence paramASN1Sequence)
  {
    if ((paramASN1Sequence.size() != 1) && (paramASN1Sequence.size() != 2))
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    ASN1TaggedObject localASN1TaggedObject;
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return;
      localASN1TaggedObject = ASN1TaggedObject.getInstance(localEnumeration.nextElement());
      if (localASN1TaggedObject.getTagNo() == 0)
      {
        this.forward = X509CertificateStructure.getInstance(localASN1TaggedObject, true);
      }
      else
      {
        if (localASN1TaggedObject.getTagNo() != 1)
          break;
        this.reverse = X509CertificateStructure.getInstance(localASN1TaggedObject, true);
      }
    }
    throw new IllegalArgumentException("Bad tag number: " + localASN1TaggedObject.getTagNo());
  }

  public CertificatePair(X509CertificateStructure paramX509CertificateStructure1, X509CertificateStructure paramX509CertificateStructure2)
  {
    this.forward = paramX509CertificateStructure1;
    this.reverse = paramX509CertificateStructure2;
  }

  public static CertificatePair getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof CertificatePair)))
      return (CertificatePair)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertificatePair((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public X509CertificateStructure getForward()
  {
    return this.forward;
  }

  public X509CertificateStructure getReverse()
  {
    return this.reverse;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.forward != null)
      localASN1EncodableVector.add(new DERTaggedObject(0, this.forward));
    if (this.reverse != null)
      localASN1EncodableVector.add(new DERTaggedObject(1, this.reverse));
    return new DERSequence(localASN1EncodableVector);
  }
}