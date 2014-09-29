package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.crmf.CertTemplate;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class RevDetails extends ASN1Encodable
{
  private CertTemplate certDetails;
  private X509Extensions crlEntryDetails;

  private RevDetails(ASN1Sequence paramASN1Sequence)
  {
    this.certDetails = CertTemplate.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.crlEntryDetails = X509Extensions.getInstance(paramASN1Sequence.getObjectAt(1));
  }

  public RevDetails(CertTemplate paramCertTemplate)
  {
    this.certDetails = paramCertTemplate;
  }

  public RevDetails(CertTemplate paramCertTemplate, X509Extensions paramX509Extensions)
  {
    this.crlEntryDetails = paramX509Extensions;
  }

  public static RevDetails getInstance(Object paramObject)
  {
    if ((paramObject instanceof RevDetails))
      return (RevDetails)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RevDetails((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public CertTemplate getCertDetails()
  {
    return this.certDetails;
  }

  public X509Extensions getCrlEntryDetails()
  {
    return this.crlEntryDetails;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certDetails);
    if (this.crlEntryDetails != null)
      localASN1EncodableVector.add(this.crlEntryDetails);
    return new DERSequence(localASN1EncodableVector);
  }
}