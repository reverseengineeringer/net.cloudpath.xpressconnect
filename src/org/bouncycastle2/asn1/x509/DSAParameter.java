package org.bouncycastle2.asn1.x509;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1TaggedObject;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class DSAParameter extends ASN1Encodable
{
  DERInteger g;
  DERInteger p;
  DERInteger q;

  public DSAParameter(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this.p = new DERInteger(paramBigInteger1);
    this.q = new DERInteger(paramBigInteger2);
    this.g = new DERInteger(paramBigInteger3);
  }

  public DSAParameter(ASN1Sequence paramASN1Sequence)
  {
    if (paramASN1Sequence.size() != 3)
      throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence.size());
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.p = DERInteger.getInstance(localEnumeration.nextElement());
    this.q = DERInteger.getInstance(localEnumeration.nextElement());
    this.g = DERInteger.getInstance(localEnumeration.nextElement());
  }

  public static DSAParameter getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DSAParameter)))
      return (DSAParameter)paramObject;
    if ((paramObject instanceof ASN1Sequence))
      return new DSAParameter((ASN1Sequence)paramObject);
    throw new IllegalArgumentException("Invalid DSAParameter: " + paramObject.getClass().getName());
  }

  public static DSAParameter getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
  }

  public BigInteger getG()
  {
    return this.g.getPositiveValue();
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
    localASN1EncodableVector.add(this.p);
    localASN1EncodableVector.add(this.q);
    localASN1EncodableVector.add(this.g);
    return new DERSequence(localASN1EncodableVector);
  }
}