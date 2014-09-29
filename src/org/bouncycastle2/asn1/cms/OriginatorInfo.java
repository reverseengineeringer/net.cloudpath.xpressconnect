package org.bouncycastle2.asn1.cms;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class OriginatorInfo extends ASN1Encodable
{
  private ASN1Set certs;
  private ASN1Set crls;

  public OriginatorInfo(ASN1Sequence paramASN1Sequence)
  {
    ASN1TaggedObject localASN1TaggedObject;
    switch (paramASN1Sequence.size())
    {
    default:
      throw new IllegalArgumentException("OriginatorInfo too big");
    case 1:
      localASN1TaggedObject = (ASN1TaggedObject)paramASN1Sequence.getObjectAt(0);
      switch (localASN1TaggedObject.getTagNo())
      {
      default:
        throw new IllegalArgumentException("Bad tag in OriginatorInfo: " + localASN1TaggedObject.getTagNo());
      case 0:
        this.certs = ASN1Set.getInstance(localASN1TaggedObject, false);
      case 1:
      }
    case 0:
      return;
      this.crls = ASN1Set.getInstance(localASN1TaggedObject, false);
      return;
    case 2:
    }
    this.certs = ASN1Set.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(0), false);
    this.crls = ASN1Set.getInstance((ASN1TaggedObject)paramASN1Sequence.getObjectAt(1), false);
  }

  public OriginatorInfo(ASN1Set paramASN1Set1, ASN1Set paramASN1Set2)
  {
    this.certs = paramASN1Set1;
    this.crls = paramASN1Set2;
  }

  public static OriginatorInfo getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof OriginatorInfo)))
      return (OriginatorInfo)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new OriginatorInfo((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid OriginatorInfo: " + paramObject.getClass().getName());
  }

  public static OriginatorInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public ASN1Set getCRLs()
  {
    return this.crls;
  }

  public ASN1Set getCertificates()
  {
    return this.certs;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.certs != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.certs));
    if (this.crls != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.crls));
    return new DERSequence(localASN1EncodableVector);
  }
}