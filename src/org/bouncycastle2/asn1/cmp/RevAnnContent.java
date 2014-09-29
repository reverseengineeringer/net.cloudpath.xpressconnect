package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.crmf.CertId;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class RevAnnContent extends ASN1Encodable
{
  private DERGeneralizedTime badSinceDate;
  private CertId certId;
  private X509Extensions crlDetails;
  private PKIStatus status;
  private DERGeneralizedTime willBeRevokedAt;

  private RevAnnContent(ASN1Sequence paramASN1Sequence)
  {
    this.status = PKIStatus.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certId = CertId.getInstance(paramASN1Sequence.getObjectAt(1));
    this.willBeRevokedAt = DERGeneralizedTime.getInstance(paramASN1Sequence.getObjectAt(2));
    this.badSinceDate = DERGeneralizedTime.getInstance(paramASN1Sequence.getObjectAt(3));
    if (paramASN1Sequence.size() > 4)
      this.crlDetails = X509Extensions.getInstance(paramASN1Sequence.getObjectAt(4));
  }

  public static RevAnnContent getInstance(Object paramObject)
  {
    if ((paramObject instanceof RevAnnContent))
      return (RevAnnContent)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RevAnnContent((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERGeneralizedTime getBadSinceDate()
  {
    return this.badSinceDate;
  }

  public CertId getCertId()
  {
    return this.certId;
  }

  public X509Extensions getCrlDetails()
  {
    return this.crlDetails;
  }

  public PKIStatus getStatus()
  {
    return this.status;
  }

  public DERGeneralizedTime getWillBeRevokedAt()
  {
    return this.willBeRevokedAt;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.status);
    localASN1EncodableVector.add(this.certId);
    localASN1EncodableVector.add(this.willBeRevokedAt);
    localASN1EncodableVector.add(this.badSinceDate);
    if (this.crlDetails != null)
      localASN1EncodableVector.add(this.crlDetails);
    return new DERSequence(localASN1EncodableVector);
  }
}