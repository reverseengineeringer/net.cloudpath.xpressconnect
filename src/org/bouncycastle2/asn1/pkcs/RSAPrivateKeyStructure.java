package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class RSAPrivateKeyStructure extends ASN1Encodable
{
  private BigInteger coefficient;
  private BigInteger exponent1;
  private BigInteger exponent2;
  private BigInteger modulus;
  private ASN1Sequence otherPrimeInfos = null;
  private BigInteger prime1;
  private BigInteger prime2;
  private BigInteger privateExponent;
  private BigInteger publicExponent;
  private int version;

  public RSAPrivateKeyStructure(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, BigInteger paramBigInteger5, BigInteger paramBigInteger6, BigInteger paramBigInteger7, BigInteger paramBigInteger8)
  {
    this.version = 0;
    this.modulus = paramBigInteger1;
    this.publicExponent = paramBigInteger2;
    this.privateExponent = paramBigInteger3;
    this.prime1 = paramBigInteger4;
    this.prime2 = paramBigInteger5;
    this.exponent1 = paramBigInteger6;
    this.exponent2 = paramBigInteger7;
    this.coefficient = paramBigInteger8;
  }

  public RSAPrivateKeyStructure(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    BigInteger localBigInteger = ((DERInteger)localEnumeration.nextElement()).getValue();
    if ((localBigInteger.intValue() != 0) && (localBigInteger.intValue() != 1))
      throw new IllegalArgumentException("wrong version for RSA private key");
    this.version = localBigInteger.intValue();
    this.modulus = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.publicExponent = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.privateExponent = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.prime1 = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.prime2 = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.exponent1 = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.exponent2 = ((DERInteger)localEnumeration.nextElement()).getValue();
    this.coefficient = ((DERInteger)localEnumeration.nextElement()).getValue();
    if (localEnumeration.hasMoreElements())
      this.otherPrimeInfos = ((ASN1Sequence)localEnumeration.nextElement());
  }

  public static RSAPrivateKeyStructure getInstance(Object paramObject)
  {
    if ((paramObject instanceof RSAPrivateKeyStructure))
      return (RSAPrivateKeyStructure)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new RSAPrivateKeyStructure((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("unknown object in factory: " + paramObject.getClass().getName());
  }

  public static RSAPrivateKeyStructure getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public BigInteger getCoefficient()
  {
    return this.coefficient;
  }

  public BigInteger getExponent1()
  {
    return this.exponent1;
  }

  public BigInteger getExponent2()
  {
    return this.exponent2;
  }

  public BigInteger getModulus()
  {
    return this.modulus;
  }

  public BigInteger getPrime1()
  {
    return this.prime1;
  }

  public BigInteger getPrime2()
  {
    return this.prime2;
  }

  public BigInteger getPrivateExponent()
  {
    return this.privateExponent;
  }

  public BigInteger getPublicExponent()
  {
    return this.publicExponent;
  }

  public int getVersion()
  {
    return this.version;
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(new DERInteger(this.version));
    localASN1EncodableVector.add(new DERInteger(getModulus()));
    localASN1EncodableVector.add(new DERInteger(getPublicExponent()));
    localASN1EncodableVector.add(new DERInteger(getPrivateExponent()));
    localASN1EncodableVector.add(new DERInteger(getPrime1()));
    localASN1EncodableVector.add(new DERInteger(getPrime2()));
    localASN1EncodableVector.add(new DERInteger(getExponent1()));
    localASN1EncodableVector.add(new DERInteger(getExponent2()));
    localASN1EncodableVector.add(new DERInteger(getCoefficient()));
    if (this.otherPrimeInfos != null)
      localASN1EncodableVector.add(this.otherPrimeInfos);
    return new DERSequence(localASN1EncodableVector);
  }
}