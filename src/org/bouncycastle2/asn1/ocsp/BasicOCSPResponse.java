package org.bouncycastle2.asn1.ocsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;

public class BasicOCSPResponse extends ASN1Encodable
{
  private ASN1Sequence certs;
  private DERBitString signature;
  private AlgorithmIdentifier signatureAlgorithm;
  private ResponseData tbsResponseData;

  public BasicOCSPResponse(ASN1Sequence paramASN1Sequence)
  {
    this.tbsResponseData = ResponseData.getInstance(paramASN1Sequence.getObjectAt(0));
    this.signatureAlgorithm = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
    this.signature = ((DERBitString)paramASN1Sequence.getObjectAt(2));
    if (paramASN1Sequence.size() > 3)
      this.certs = ASN1Sequence.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(3), true);
  }

  public BasicOCSPResponse(ResponseData paramResponseData, AlgorithmIdentifier paramAlgorithmIdentifier, DERBitString paramDERBitString, ASN1Sequence paramASN1Sequence)
  {
    this.tbsResponseData = paramResponseData;
    this.signatureAlgorithm = paramAlgorithmIdentifier;
    this.signature = paramDERBitString;
    this.certs = paramASN1Sequence;
  }

  public static BasicOCSPResponse getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof BasicOCSPResponse)))
      return (BasicOCSPResponse)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new BasicOCSPResponse((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static BasicOCSPResponse getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1Sequence getCerts()
  {
    return this.certs;
  }

  public DERBitString getSignature()
  {
    return this.signature;
  }

  public AlgorithmIdentifier getSignatureAlgorithm()
  {
    return this.signatureAlgorithm;
  }

  public ResponseData getTbsResponseData()
  {
    return this.tbsResponseData;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.tbsResponseData);
    localASN1EncodableVector.add(this.signatureAlgorithm);
    localASN1EncodableVector.add(this.signature);
    if (this.certs != null)
      localASN1EncodableVector.add(new DERTaggedObject(true, 0, this.certs));
    return new DERSequence(localASN1EncodableVector);
  }
}