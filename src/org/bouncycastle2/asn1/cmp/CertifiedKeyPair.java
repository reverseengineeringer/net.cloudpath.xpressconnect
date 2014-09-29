package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.crmf.EncryptedValue;
import org.bouncycastle2.asn1.crmf.PKIPublicationInfo;

public class CertifiedKeyPair extends ASN1Encodable
{
  private CertOrEncCert certOrEncCert;
  private EncryptedValue privateKey;
  private PKIPublicationInfo publicationInfo;

  private CertifiedKeyPair(ASN1Sequence paramASN1Sequence)
  {
    this.certOrEncCert = CertOrEncCert.getInstance(paramASN1Sequence.getObjectAt(0));
    ASN1TaggedObject localASN1TaggedObject;
    if (paramASN1Sequence.size() >= 2)
    {
      if (paramASN1Sequence.size() != 2)
        break label72;
      localASN1TaggedObject = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(1));
      if (localASN1TaggedObject.getTagNo() == 0)
        this.privateKey = EncryptedValue.getInstance(localASN1TaggedObject.getObject());
    }
    else
    {
      return;
    }
    this.publicationInfo = PKIPublicationInfo.getInstance(localASN1TaggedObject.getObject());
    return;
    label72: this.privateKey = EncryptedValue.getInstance(ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(1)));
    this.publicationInfo = PKIPublicationInfo.getInstance(ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(2)));
  }

  public CertifiedKeyPair(CertOrEncCert paramCertOrEncCert)
  {
    this(paramCertOrEncCert, null, null);
  }

  public CertifiedKeyPair(CertOrEncCert paramCertOrEncCert, EncryptedValue paramEncryptedValue, PKIPublicationInfo paramPKIPublicationInfo)
  {
    if (paramCertOrEncCert == null)
      throw new IllegalArgumentException("'certOrEncCert' cannot be null");
    this.certOrEncCert = paramCertOrEncCert;
    this.privateKey = paramEncryptedValue;
    this.publicationInfo = paramPKIPublicationInfo;
  }

  public static CertifiedKeyPair getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertifiedKeyPair))
      return (CertifiedKeyPair)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertifiedKeyPair((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CertOrEncCert getCertOrEncCert()
  {
    return this.certOrEncCert;
  }

  public EncryptedValue getPrivateKey()
  {
    return this.privateKey;
  }

  public PKIPublicationInfo getPublicationInfo()
  {
    return this.publicationInfo;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certOrEncCert);
    if (this.privateKey != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.privateKey));
    if (this.publicationInfo != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.publicationInfo));
    return new DERSequence(localASN1EncodableVector);
  }
}