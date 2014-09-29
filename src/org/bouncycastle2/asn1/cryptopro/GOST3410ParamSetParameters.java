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

public class GOST3410ParamSetParameters extends ASN1Encodable
{
  DERInteger a;
  int keySize;
  DERInteger p;
  DERInteger q;

  public GOST3410ParamSetParameters(int paramInt, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this.keySize = paramInt;
    this.p = new DERInteger(paramBigInteger1);
    this.q = new DERInteger(paramBigInteger2);
    this.a = new DERInteger(paramBigInteger3);
  }

  public GOST3410ParamSetParameters(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.keySize = ((DERInteger)localEnumeration.nextElement()).getValue().intValue();
    this.p = ((DERInteger)localEnumeration.nextElement());
    this.q = ((DERInteger)localEnumeration.nextElement());
    this.a = ((DERInteger)localEnumeration.nextElement());
  }

  public static GOST3410ParamSetParameters getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof GOST3410ParamSetParameters)))
      return (GOST3410ParamSetParameters)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new GOST3410ParamSetParameters((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid GOST3410Parameter: " + paramObject.getClass().getName());
  }

  public static GOST3410ParamSetParameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public BigInteger getA()
  {
    return this.a.getPositiveValue();
  }

  public int getKeySize()
  {
    return this.keySize;
  }

  public int getLKeySize()
  {
    return this.keySize;
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
    localASN1EncodableVector.add(new DERInteger(this.keySize));
    localASN1EncodableVector.add(this.p);
    localASN1EncodableVector.add(this.q);
    localASN1EncodableVector.add(this.a);
    return new DERSequence(localASN1EncodableVector);
  }
}