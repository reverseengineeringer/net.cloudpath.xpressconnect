package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class Request extends ASN1Encodable
{
  CertID reqCert;
  X509Extensions singleRequestExtensions;

  public Request(ASN1Sequence paramASN1Sequence)
  {
    this.reqCert = CertID.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
      this.singleRequestExtensions = X509Extensions.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), true);
  }

  public Request(CertID paramCertID, X509Extensions paramX509Extensions)
  {
    this.reqCert = paramCertID;
    this.singleRequestExtensions = paramX509Extensions;
  }

  public static Request getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof Request)))
      return (Request)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Request((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static Request getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public CertID getReqCert()
  {
    return this.reqCert;
  }

  public X509Extensions getSingleRequestExtensions()
  {
    return this.singleRequestExtensions;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.reqCert);
    if (this.singleRequestExtensions != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.singleRequestExtensions));
    return new DERSequence(localASN1EncodableVector);
  }
}