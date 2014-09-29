package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class ResponseData extends ASN1Encodable
{
  private static final DERInteger V1 = new DERInteger(0);
  private DERGeneralizedTime producedAt;
  private ResponderID responderID;
  private X509Extensions responseExtensions;
  private ASN1Sequence responses;
  private DERInteger version;
  private boolean versionPresent;

  public ResponseData(ASN1Sequence paramASN1Sequence)
  {
    int i;
    if ((paramASN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject))
      if (((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0)).getTagNo() == 0)
      {
        this.versionPresent = true;
        this.version = DERInteger.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0), true);
        i = 0 + 1;
      }
    while (true)
    {
      int j = i + 1;
      this.responderID = ResponderID.getInstance(paramASN1Sequence.getObjectAt(i));
      int k = j + 1;
      this.producedAt = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(j));
      int m = k + 1;
      this.responses = ((ASN1Sequence)paramASN1Sequence.getObjectAt(k));
      if (paramASN1Sequence.size() > m)
        this.responseExtensions = X509Extensions.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(m), true);
      return;
      this.version = V1;
      i = 0;
      continue;
      this.version = V1;
      i = 0;
    }
  }

  public ResponseData(DERInteger paramDERInteger, ResponderID paramResponderID, DERGeneralizedTime paramDERGeneralizedTime, ASN1Sequence paramASN1Sequence, X509Extensions paramX509Extensions)
  {
    this.version = paramDERInteger;
    this.responderID = paramResponderID;
    this.producedAt = paramDERGeneralizedTime;
    this.responses = paramASN1Sequence;
    this.responseExtensions = paramX509Extensions;
  }

  public ResponseData(ResponderID paramResponderID, DERGeneralizedTime paramDERGeneralizedTime, ASN1Sequence paramASN1Sequence, X509Extensions paramX509Extensions)
  {
    this(V1, paramResponderID, paramDERGeneralizedTime, paramASN1Sequence, paramX509Extensions);
  }

  public static ResponseData getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ResponseData)))
      return (ResponseData)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ResponseData((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static ResponseData getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERGeneralizedTime getProducedAt()
  {
    return this.producedAt;
  }

  public ResponderID getResponderID()
  {
    return this.responderID;
  }

  public X509Extensions getResponseExtensions()
  {
    return this.responseExtensions;
  }

  public ASN1Sequence getResponses()
  {
    return this.responses;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if ((this.versionPresent) || (!this.version.equals(V1)))
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
    localASN1EncodableVector.add(this.responderID);
    localASN1EncodableVector.add(this.producedAt);
    localASN1EncodableVector.add(this.responses);
    if (this.responseExtensions != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 1, this.responseExtensions));
    return new DERSequence(localASN1EncodableVector);
  }
}