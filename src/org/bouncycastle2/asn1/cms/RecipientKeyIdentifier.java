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

public class RecipientKeyIdentifier extends ASN1Encodable
{
  private DERGeneralizedTime date;
  private OtherKeyAttribute other;
  private ASN1OctetString subjectKeyIdentifier;

  public RecipientKeyIdentifier(ASN1OctetString paramASN1OctetString, DERGeneralizedTime paramDERGeneralizedTime, OtherKeyAttribute paramOtherKeyAttribute)
  {
    this.subjectKeyIdentifier = paramASN1OctetString;
    this.date = paramDERGeneralizedTime;
    this.other = paramOtherKeyAttribute;
  }

  public RecipientKeyIdentifier(ASN1Sequence paramASN1Sequence)
  {
    this.subjectKeyIdentifier = ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0));
    switch (paramASN1Sequence.size())
    {
    default:
      throw new IllegalArgumentException("Invalid RecipientKeyIdentifier");
    case 2:
      if ((paramASN1Sequence.getObjectAt(1) instanceof DERGeneralizedTime))
        this.date = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(1));
    case 1:
      return;
      this.other = OtherKeyAttribute.getInstance(paramASN1Sequence.getObjectAt(2));
      return;
    case 3:
    }
    this.date = ((DERGeneralizedTime)paramASN1Sequence.getObjectAt(1));
    this.other = OtherKeyAttribute.getInstance(paramASN1Sequence.getObjectAt(2));
  }

  public RecipientKeyIdentifier(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, null, null);
  }

  public RecipientKeyIdentifier(byte[] paramArrayOfByte, DERGeneralizedTime paramDERGeneralizedTime, OtherKeyAttribute paramOtherKeyAttribute)
  {
    this.subjectKeyIdentifier = new DEROctetString(paramArrayOfByte);
    this.date = paramDERGeneralizedTime;
    this.other = paramOtherKeyAttribute;
  }

  public static RecipientKeyIdentifier getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof RecipientKeyIdentifier)))
      return (RecipientKeyIdentifier)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RecipientKeyIdentifier((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid RecipientKeyIdentifier: " + paramObject.getClass().getName());
  }

  public static RecipientKeyIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public DERGeneralizedTime getDate()
  {
    return this.date;
  }

  public OtherKeyAttribute getOtherKeyAttribute()
  {
    return this.other;
  }

  public ASN1OctetString getSubjectKeyIdentifier()
  {
    return this.subjectKeyIdentifier;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.subjectKeyIdentifier);
    if (this.date != null)
      localASN1EncodableVector.add(this.date);
    if (this.other != null)
      localASN1EncodableVector.add(this.other);
    return new DERSequence(localASN1EncodableVector);
  }
}