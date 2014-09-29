package org.bouncycastle2.asn1.tsp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBoolean;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class TimeStampReq extends ASN1Encodable
{
  DERBoolean certReq;
  X509Extensions extensions;
  MessageImprint messageImprint;
  DERInteger nonce;
  DERObjectIdentifier tsaPolicy;
  DERInteger version;

  public TimeStampReq(ASN1Sequence paramASN1Sequence)
  {
    int i = paramASN1Sequence.size();
    this.version = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    int j = 0 + 1;
    this.messageImprint = MessageImprint.getInstance(paramASN1Sequence.getObjectAt(j));
    int k = j + 1;
    if (k >= i)
      return;
    if ((paramASN1Sequence.getObjectAt(k) instanceof DERObjectIdentifier))
      this.tsaPolicy = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(k));
    while (true)
    {
      k++;
      break;
      if ((paramASN1Sequence.getObjectAt(k) instanceof DERInteger))
      {
        this.nonce = DERInteger.getInstance(paramASN1Sequence.getObjectAt(k));
      }
      else if ((paramASN1Sequence.getObjectAt(k) instanceof DERBoolean))
      {
        this.certReq = DERBoolean.getInstance(paramASN1Sequence.getObjectAt(k));
      }
      else if ((paramASN1Sequence.getObjectAt(k) instanceof ASN1TaggedObject))
      {
        ASN1TaggedObject localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(k);
        if (localASN1TaggedObject.getTagNo() == 0)
          this.extensions = X509Extensions.getInstance(localASN1TaggedObject, false);
      }
    }
  }

  public TimeStampReq(MessageImprint paramMessageImprint, DERObjectIdentifier paramDERObjectIdentifier, DERInteger paramDERInteger, DERBoolean paramDERBoolean, X509Extensions paramX509Extensions)
  {
    this.version = new DERInteger(1);
    this.messageImprint = paramMessageImprint;
    this.tsaPolicy = paramDERObjectIdentifier;
    this.nonce = paramDERInteger;
    this.certReq = paramDERBoolean;
    this.extensions = paramX509Extensions;
  }

  public static TimeStampReq getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof TimeStampReq)))
      return (TimeStampReq)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new TimeStampReq((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Unknown object in 'TimeStampReq' factory : " + paramObject.getClass().getName() + ".");
  }

  public DERBoolean getCertReq()
  {
    return this.certReq;
  }

  public X509Extensions getExtensions()
  {
    return this.extensions;
  }

  public MessageImprint getMessageImprint()
  {
    return this.messageImprint;
  }

  public DERInteger getNonce()
  {
    return this.nonce;
  }

  public DERObjectIdentifier getReqPolicy()
  {
    return this.tsaPolicy;
  }

  public DERInteger getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.version);
    localASN1EncodableVector.add(this.messageImprint);
    if (this.tsaPolicy != null)
      localASN1EncodableVector.add(this.tsaPolicy);
    if (this.nonce != null)
      localASN1EncodableVector.add(this.nonce);
    if ((this.certReq != null) && (this.certReq.isTrue()))
      localASN1EncodableVector.add(this.certReq);
    if (this.extensions != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.extensions));
    return new DERSequence(localASN1EncodableVector);
  }
}