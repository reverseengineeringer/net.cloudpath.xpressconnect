package org.bouncycastle2.asn1.pkcs;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509Name;

public class CertificationRequestInfo extends ASN1Encodable
{
  ASN1Set attributes = null;
  X509Name subject;
  SubjectPublicKeyInfo subjectPKInfo;
  DERInteger version = new DERInteger(0);

  public CertificationRequestInfo(ASN1Sequence paramASN1Sequence)
  {
    this.version = ((DERInteger)paramASN1Sequence.getObjectAt(0));
    this.subject = X509Name.getInstance(paramASN1Sequence.getObjectAt(1));
    this.subjectPKInfo = SubjectPublicKeyInfo.getInstance(paramASN1Sequence.getObjectAt(2));
    if (paramASN1Sequence.size() > 3)
      this.attributes = ASN1Set.getInstance((DERTaggedObject)paramASN1Sequence.getObjectAt(3), false);
    if ((this.subject == null) || (this.version == null) || (this.subjectPKInfo == null))
      throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
  }

  public CertificationRequestInfo(X500Name paramX500Name, SubjectPublicKeyInfo paramSubjectPublicKeyInfo, ASN1Set paramASN1Set)
  {
    this.subject = X509Name.getInstance(paramX500Name.getDERObject());
    this.subjectPKInfo = paramSubjectPublicKeyInfo;
    this.attributes = paramASN1Set;
    if ((paramX500Name == null) || (this.version == null) || (this.subjectPKInfo == null))
      throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
  }

  public CertificationRequestInfo(X509Name paramX509Name, SubjectPublicKeyInfo paramSubjectPublicKeyInfo, ASN1Set paramASN1Set)
  {
    this.subject = paramX509Name;
    this.subjectPKInfo = paramSubjectPublicKeyInfo;
    this.attributes = paramASN1Set;
    if ((paramX509Name == null) || (this.version == null) || (this.subjectPKInfo == null))
      throw new IllegalArgumentException("Not all mandatory fields set in CertificationRequestInfo generator.");
  }

  public static CertificationRequestInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof CertificationRequestInfo))
      return (CertificationRequestInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new CertificationRequestInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public ASN1Set getAttributes()
  {
    return this.attributes;
  }

  public X509Name getSubject()
  {
    return this.subject;
  }

  public SubjectPublicKeyInfo getSubjectPublicKeyInfo()
  {
    return this.subjectPKInfo;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.subject);
    localASN1EncodableVector.add(this.subjectPKInfo);
    if (this.attributes != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.attributes));
    return new DERSequence(localASN1EncodableVector);
  }
}