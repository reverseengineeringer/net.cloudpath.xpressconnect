package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class SingleResponse extends ASN1Encodable
{
  private CertID certID;
  private CertStatus certStatus;
  private DERGeneralizedTime nextUpdate;
  private X509Extensions singleExtensions;
  private DERGeneralizedTime thisUpdate;

  public SingleResponse(ASN1Sequence paramASN1Sequence)
  {
    this.certID = CertID.getInstance(paramASN1Sequence.getObjectAt(0));
    this.certStatus = CertStatus.getInstance(paramASN1Sequence.getObjectAt(1));
    this.thisUpdate = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(2));
    if (paramASN1Sequence.size() > 4)
    {
      this.nextUpdate = DERGeneralizedTime.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(3), true);
      this.singleExtensions = X509Extensions.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(4), true);
    }
    while (paramASN1Sequence.size() <= 3)
      return;
    ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(3);
    if (localASN1TaggedObject.getTagNo() == 0)
    {
      this.nextUpdate = DERGeneralizedTime.getInstance(localASN1TaggedObject, true);
      return;
    }
    this.singleExtensions = X509Extensions.getInstance(localASN1TaggedObject, true);
  }

  public SingleResponse(CertID paramCertID, CertStatus paramCertStatus, DERGeneralizedTime paramDERGeneralizedTime1, DERGeneralizedTime paramDERGeneralizedTime2, X509Extensions paramX509Extensions)
  {
    this.certID = paramCertID;
    this.certStatus = paramCertStatus;
    this.thisUpdate = paramDERGeneralizedTime1;
    this.nextUpdate = paramDERGeneralizedTime2;
    this.singleExtensions = paramX509Extensions;
  }

  public static SingleResponse getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof SingleResponse)))
      return (SingleResponse)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new SingleResponse((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static SingleResponse getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public CertID getCertID()
  {
    return this.certID;
  }

  public CertStatus getCertStatus()
  {
    return this.certStatus;
  }

  public DERGeneralizedTime getNextUpdate()
  {
    return this.nextUpdate;
  }

  public X509Extensions getSingleExtensions()
  {
    return this.singleExtensions;
  }

  public DERGeneralizedTime getThisUpdate()
  {
    return this.thisUpdate;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.certID);
    localASN1EncodableVector.add(this.certStatus);
    localASN1EncodableVector.add(this.thisUpdate);
    if (this.nextUpdate != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.nextUpdate));
    if (this.singleExtensions != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.singleExtensions));
    return new DERSequence(localASN1EncodableVector);
  }
}