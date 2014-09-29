package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class ResponseBytes extends ASN1Encodable
{
  ASN1OctetString response;
  DERObjectIdentifier responseType;

  public ResponseBytes(ASN1Sequence paramASN1Sequence)
  {
    this.responseType = ((DERObjectIdentifier)paramASN1Sequence.getObjectAt(0));
    this.response = ((ASN1OctetString)paramASN1Sequence.getObjectAt(1));
  }

  public ResponseBytes(DERObjectIdentifier paramDERObjectIdentifier, ASN1OctetString paramASN1OctetString)
  {
    this.responseType = paramDERObjectIdentifier;
    this.response = paramASN1OctetString;
  }

  public static ResponseBytes getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ResponseBytes)))
      return (ResponseBytes)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ResponseBytes((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static ResponseBytes getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1OctetString getResponse()
  {
    return this.response;
  }

  public DERObjectIdentifier getResponseType()
  {
    return this.responseType;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.responseType);
    localASN1EncodableVector.add(this.response);
    return new DERSequence(localASN1EncodableVector);
  }
}