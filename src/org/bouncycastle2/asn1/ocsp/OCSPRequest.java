package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class OCSPRequest extends ASN1Encodable
{
  Signature optionalSignature;
  TBSRequest tbsRequest;

  public OCSPRequest(ASN1Sequence paramASN1Sequence)
  {
    this.tbsRequest = TBSRequest.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() == 2)
      this.optionalSignature = Signature.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), true);
  }

  public OCSPRequest(TBSRequest paramTBSRequest, Signature paramSignature)
  {
    this.tbsRequest = paramTBSRequest;
    this.optionalSignature = paramSignature;
  }

  public static OCSPRequest getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OCSPRequest)))
      return (OCSPRequest)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OCSPRequest((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static OCSPRequest getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public Signature getOptionalSignature()
  {
    return this.optionalSignature;
  }

  public TBSRequest getTbsRequest()
  {
    return this.tbsRequest;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.tbsRequest);
    if (this.optionalSignature != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.optionalSignature));
    return new DERSequence(localASN1EncodableVector);
  }
}