package org.bouncycastle2.asn1.tsp;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERTaggedObject;

public class Accuracy extends ASN1Encodable
{
  protected static final int MAX_MICROS = 999;
  protected static final int MAX_MILLIS = 999;
  protected static final int MIN_MICROS = 1;
  protected static final int MIN_MILLIS = 1;
  DERInteger micros;
  DERInteger millis;
  DERInteger seconds;

  protected Accuracy()
  {
  }

  public Accuracy(ASN1Sequence paramASN1Sequence)
  {
    this.seconds = null;
    this.millis = null;
    this.micros = null;
    int i = 0;
    if (i >= paramASN1Sequence.size())
      return;
    if ((paramASN1Sequence.getObjectAt(i) instanceof DERInteger))
      this.seconds = ((DERInteger)paramASN1Sequence.getObjectAt(i));
    do
    {
      DERTaggedObject localDERTaggedObject;
      do
      {
        do
        {
          i++;
          break;
        }
        while (!(paramASN1Sequence.getObjectAt(i) instanceof DERTaggedObject));
        localDERTaggedObject = (DERTaggedObject)paramASN1Sequence.getObjectAt(i);
        switch (localDERTaggedObject.getTagNo())
        {
        default:
          throw new IllegalArgumentException("Invalig tag number");
        case 0:
          this.millis = DERInteger.getInstance(localDERTaggedObject, false);
        case 1:
        }
      }
      while ((this.millis.getValue().intValue() >= 1) && (this.millis.getValue().intValue() <= 999));
      throw new IllegalArgumentException("Invalid millis field : not in (1..999).");
      this.micros = DERInteger.getInstance(localDERTaggedObject, false);
    }
    while ((this.micros.getValue().intValue() >= 1) && (this.micros.getValue().intValue() <= 999));
    throw new IllegalArgumentException("Invalid micros field : not in (1..999).");
  }

  public Accuracy(DERInteger paramDERInteger1, DERInteger paramDERInteger2, DERInteger paramDERInteger3)
  {
    this.seconds = paramDERInteger1;
    if ((paramDERInteger2 != null) && ((paramDERInteger2.getValue().intValue() < 1) || (paramDERInteger2.getValue().intValue() > 999)))
      throw new IllegalArgumentException("Invalid millis field : not in (1..999)");
    this.millis = paramDERInteger2;
    if ((paramDERInteger3 != null) && ((paramDERInteger3.getValue().intValue() < 1) || (paramDERInteger3.getValue().intValue() > 999)))
      throw new IllegalArgumentException("Invalid micros field : not in (1..999)");
    this.micros = paramDERInteger3;
  }

  public static Accuracy getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof Accuracy)))
      return (Accuracy)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new Accuracy((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Unknown object in 'Accuracy' factory : " + paramObject.getClass().getName() + ".");
  }

  public DERInteger getMicros()
  {
    return this.micros;
  }

  public DERInteger getMillis()
  {
    return this.millis;
  }

  public DERInteger getSeconds()
  {
    return this.seconds;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    if (this.seconds != null)
      localASN1EncodableVector.add(this.seconds);
    if (this.millis != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 0, this.millis));
    if (this.micros != null)
      localASN1EncodableVector.add(new DERTaggedObject(false, 1, this.micros));
    return new DERSequence(localASN1EncodableVector);
  }
}