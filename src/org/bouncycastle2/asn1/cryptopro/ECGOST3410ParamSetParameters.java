package org.bouncycastle2.asn1.cryptopro;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class ECGOST3410ParamSetParameters extends ASN1Encodable
{
  DERInteger a;
  DERInteger b;
  DERInteger p;
  DERInteger q;
  DERInteger x;
  DERInteger y;

  public ECGOST3410ParamSetParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, int paramInt, BigInteger paramBigInteger5)
  {
    this.a = new DERInteger(paramBigInteger1);
    this.b = new DERInteger(paramBigInteger2);
    this.p = new DERInteger(paramBigInteger3);
    this.q = new DERInteger(paramBigInteger4);
    this.x = new DERInteger(paramInt);
    this.y = new DERInteger(paramBigInteger5);
  }

  public ECGOST3410ParamSetParameters(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.a = ((DERInteger)localEnumeration.nextElement());
    this.b = ((DERInteger)localEnumeration.nextElement());
    this.p = ((DERInteger)localEnumeration.nextElement());
    this.q = ((DERInteger)localEnumeration.nextElement());
    this.x = ((DERInteger)localEnumeration.nextElement());
    this.y = ((DERInteger)localEnumeration.nextElement());
  }

  public static ECGOST3410ParamSetParameters getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof ECGOST3410ParamSetParameters)))
      return (ECGOST3410ParamSetParameters)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new ECGOST3410ParamSetParameters((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid GOST3410Parameter: " + paramObject.getClass().getName());
  }

  public static ECGOST3410ParamSetParameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public BigInteger getA()
  {
    return this.a.getPositiveValue();
  }

  public BigInteger getP()
  {
    return this.p.getPositiveValue();
  }

  public BigInteger getQ()
  {
    return this.q.getPositiveValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.a);
    localASN1EncodableVector.add(this.b);
    localASN1EncodableVector.add(this.p);
    localASN1EncodableVector.add(this.q);
    localASN1EncodableVector.add(this.x);
    localASN1EncodableVector.add(this.y);
    return new DERSequence(localASN1EncodableVector);
  }
}