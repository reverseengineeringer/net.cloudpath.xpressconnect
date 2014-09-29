package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class GeneralSubtree extends ASN1Encodable
{
  private static final BigInteger ZERO = BigInteger.valueOf(0L);
  private GeneralName base;
  private DERInteger maximum;
  private DERInteger minimum;

  public GeneralSubtree(ASN1Sequence paramASN1Sequence)
  {
    this.base = GeneralName.getInstance(paramASN1Sequence.getObjectAt(0));
    ASN1TaggedObject localASN1TaggedObject3;
    switch (paramASN1Sequence.size())
    {
    default:
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    case 2:
      localASN1TaggedObject3 = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(1));
      switch (localASN1TaggedObject3.getTagNo())
      {
      default:
        throw new IllegalArgumentException("Bad tag number: " + localASN1TaggedObject3.getTagNo());
      case 0:
        this.minimum = DERInteger.getInstance(localASN1TaggedObject3, false);
      case 1:
      }
    case 1:
      return;
      this.maximum = DERInteger.getInstance(localASN1TaggedObject3, false);
      return;
    case 3:
    }
    ASN1TaggedObject localASN1TaggedObject1 = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(1));
    if (localASN1TaggedObject1.getTagNo() != 0)
      throw new IllegalArgumentException("Bad tag number for 'minimum': " + localASN1TaggedObject1.getTagNo());
    this.minimum = DERInteger.getInstance(localASN1TaggedObject1, false);
    ASN1TaggedObject localASN1TaggedObject2 = ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(2));
    if (localASN1TaggedObject2.getTagNo() != 1)
      throw new IllegalArgumentException("Bad tag number for 'maximum': " + localASN1TaggedObject2.getTagNo());
    this.maximum = DERInteger.getInstance(localASN1TaggedObject2, false);
  }

  public GeneralSubtree(GeneralName paramGeneralName)
  {
    this(paramGeneralName, null, null);
  }

  public GeneralSubtree(GeneralName paramGeneralName, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this.base = paramGeneralName;
    if (paramBigInteger2 != null)
      this.maximum = new DERInteger(paramBigInteger2);
    if (paramBigInteger1 == null)
    {
      this.minimum = null;
      return;
    }
    this.minimum = new DERInteger(paramBigInteger1);
  }

  public static GeneralSubtree getInstance(Object paramObject)
  {
    if (paramObject == null)
      return null;
    if ((paramObject instanceof GeneralSubtree))
      return (GeneralSubtree)paramObject;
    return new GeneralSubtree(ASN1Sequence.getInstance(paramObject));
  }

  public static GeneralSubtree getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return new GeneralSubtree(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public GeneralName getBase()
  {
    return this.base;
  }

  public BigInteger getMaximum()
  {
    if (this.maximum == null)
      return null;
    return this.maximum.getValue();
  }

  public BigInteger getMinimum()
  {
    if (this.minimum == null)
      return ZERO;
    return this.minimum.getValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.base);
    if ((this.minimum != null) && (!this.minimum.getValue().equals(ZERO)))
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.minimum));
    if (this.maximum != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.maximum));
    return new DERSequence(localASN1EncodableVector);
  }
}