package org.bouncycastle2.asn1.cmp;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class PKIStatusInfo extends ASN1Encodable
{
  DERBitString failInfo;
  DERInteger status;
  PKIFreeText statusString;

  public PKIStatusInfo(int paramInt)
  {
    this.status = new DERInteger(paramInt);
  }

  public PKIStatusInfo(int paramInt, PKIFreeText paramPKIFreeText)
  {
    this.status = new DERInteger(paramInt);
    this.statusString = paramPKIFreeText;
  }

  public PKIStatusInfo(int paramInt, PKIFreeText paramPKIFreeText, PKIFailureInfo paramPKIFailureInfo)
  {
    this.status = new DERInteger(paramInt);
    this.statusString = paramPKIFreeText;
    this.failInfo = paramPKIFailureInfo;
  }

  public PKIStatusInfo(ASN1Sequence paramASN1Sequence)
  {
    this.status = DERInteger.getInstance(paramASN1Sequence.getObjectAt(0));
    this.statusString = null;
    this.failInfo = null;
    if (paramASN1Sequence.size() > 2)
    {
      this.statusString = PKIFreeText.getInstance(paramASN1Sequence.getObjectAt(1));
      this.failInfo = DERBitString.getInstance(paramASN1Sequence.getObjectAt(2));
    }
    while (paramASN1Sequence.size() <= 1)
      return;
    DEREncodable localDEREncodable = paramASN1Sequence.getObjectAt(1);
    if ((localDEREncodable instanceof DERBitString))
    {
      this.failInfo = DERBitString.getInstance(localDEREncodable);
      return;
    }
    this.statusString = PKIFreeText.getInstance(localDEREncodable);
  }

  public PKIStatusInfo(PKIStatus paramPKIStatus)
  {
    this.status = DERInteger.getInstance(paramPKIStatus.toASN1Object());
  }

  public PKIStatusInfo(PKIStatus paramPKIStatus, PKIFreeText paramPKIFreeText)
  {
    this.status = DERInteger.getInstance(paramPKIStatus.toASN1Object());
    this.statusString = paramPKIFreeText;
  }

  public static PKIStatusInfo getInstance(Object paramObject)
  {
    if ((paramObject instanceof PKIStatusInfo))
      return (PKIStatusInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new PKIStatusInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static PKIStatusInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERBitString getFailInfo()
  {
    return this.failInfo;
  }

  public BigInteger getStatus()
  {
    return this.status.getValue();
  }

  public PKIFreeText getStatusString()
  {
    return this.statusString;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.status);
    if (this.statusString != null)
      localASN1EncodableVector.add(this.statusString);
    if (this.failInfo != null)
      localASN1EncodableVector.add(this.failInfo);
    return new DERSequence(localASN1EncodableVector);
  }
}