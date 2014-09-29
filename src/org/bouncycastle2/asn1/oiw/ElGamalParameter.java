package org.bouncycastle2.asn1.oiw;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class ElGamalParameter extends ASN1Encodable
{
  DERInteger g;
  DERInteger p;

  public ElGamalParameter(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this.p = new DERInteger(paramBigInteger1);
    this.g = new DERInteger(paramBigInteger2);
  }

  public ElGamalParameter(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.p = ((DERInteger)localEnumeration.nextElement());
    this.g = ((DERInteger)localEnumeration.nextElement());
  }

  public BigInteger getG()
  {
    return this.g.getPositiveValue();
  }

  public BigInteger getP()
  {
    return this.p.getPositiveValue();
  }

  public DERObject toASN1Object()
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(this.p);
    localASN1EncodableVector.add(this.g);
    return new DERSequence(localASN1EncodableVector);
  }
}