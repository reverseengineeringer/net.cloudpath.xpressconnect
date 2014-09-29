package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;

public class KEKIdentifier extends ASN1Encodable
{
  private DERGeneralizedTime date;
  private ASN1OctetString keyIdentifier;
  private OtherKeyAttribute other;

  public KEKIdentifier(ASN1Sequence paramASN1Sequence)
  {
    this.keyIdentifier = ((ASN1OctetString)paramASN1Sequence.getObjectAt(0));
    switch (paramASN1Sequence.size())
    {
    default:
      throw new IllegalArgumentException("Invalid KEKIdentifier");
    case 2:
      if ((paramASN1Sequence.getObjectAt(1) instanceof DERGeneralizedTime))
        this.date = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(1));
    case 1:
      return;
      this.other = OtherKeyAttribute.getInstance(paramASN1Sequence.getObjectAt(1));
      return;
    case 3:
    }
    this.date = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(1));
    this.other = OtherKeyAttribute.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public KEKIdentifier(byte[] paramArrayOfByte, DERGeneralizedTime paramDERGeneralizedTime, OtherKeyAttribute paramOtherKeyAttribute)
  {
    this.keyIdentifier = new DEROctetString(paramArrayOfByte);
    this.date = paramDERGeneralizedTime;
    this.other = paramOtherKeyAttribute;
  }

  public static KEKIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof KEKIdentifier)))
      return (KEKIdentifier)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new KEKIdentifier((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid KEKIdentifier: " + paramObject.getClass().getName());
  }

  public static KEKIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERGeneralizedTime getDate()
  {
    return this.date;
  }

  public ASN1OctetString getKeyIdentifier()
  {
    return this.keyIdentifier;
  }

  public OtherKeyAttribute getOther()
  {
    return this.other;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.keyIdentifier);
    if (this.date != null)
      localASN1EncodableVector.add(this.date);
    if (this.other != null)
      localASN1EncodableVector.add(this.other);
    return new DERSequence(localASN1EncodableVector);
  }
}