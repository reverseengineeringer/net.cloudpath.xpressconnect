package org.bouncycastle2.asn1.cmp;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;

public class InfoTypeAndValue extends ASN1Encodable
{
  private DERObjectIdentifier infoType;
  private ASN1Encodable infoValue;

  private InfoTypeAndValue(ASN1Sequence paramASN1Sequence)
  {
    this.infoType = DERObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
    if (paramASN1Sequence.size() > 1)
      this.infoValue = ((ASN1Encodable)paramASN1Sequence.getObjectAt(1));
  }

  public InfoTypeAndValue(DERObjectIdentifier paramDERObjectIdentifier)
  {
    this.infoType = paramDERObjectIdentifier;
    this.infoValue = null;
  }

  public InfoTypeAndValue(DERObjectIdentifier paramDERObjectIdentifier, ASN1Encodable paramASN1Encodable)
  {
    this.infoType = paramDERObjectIdentifier;
    this.infoValue = paramASN1Encodable;
  }

  public static InfoTypeAndValue getInstance(Object paramObject)
  {
    if ((paramObject instanceof InfoTypeAndValue))
      return (InfoTypeAndValue)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new InfoTypeAndValue((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid object: " + paramObject.getClass().getName());
  }

  public DERObjectIdentifier getInfoType()
  {
    return this.infoType;
  }

  public ASN1Encodable getInfoValue()
  {
    return this.infoValue;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.infoType);
    if (this.infoValue != null)
      localASN1EncodableVector.add(this.infoValue);
    return new DERSequence(localASN1EncodableVector);
  }
}