package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DEREnumerated;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class OCSPResponse extends ASN1Encodable
{
  ResponseBytes responseBytes;
  OCSPResponseStatus responseStatus;

  public OCSPResponse(ASN1Sequence paramASN1Sequence)
  {
    this.responseStatus = new OCSPResponseStatus(DEREnumerated.getInstance(paramASN1Sequence.getObjectAt(0)));
    if (paramASN1Sequence.size() == 2)
      this.responseBytes = ResponseBytes.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), true);
  }

  public OCSPResponse(OCSPResponseStatus paramOCSPResponseStatus, ResponseBytes paramResponseBytes)
  {
    this.responseStatus = paramOCSPResponseStatus;
    this.responseBytes = paramResponseBytes;
  }

  public static OCSPResponse getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OCSPResponse)))
      return (OCSPResponse)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OCSPResponse((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static OCSPResponse getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ResponseBytes getResponseBytes()
  {
    return this.responseBytes;
  }

  public OCSPResponseStatus getResponseStatus()
  {
    return this.responseStatus;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.responseStatus);
    if (this.responseBytes != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.responseBytes));
    return new DERSequence(localASN1EncodableVector);
  }
}