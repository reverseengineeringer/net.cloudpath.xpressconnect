package org.bouncycastle2.asn1.pkcs;

import java.math.BigInteger;
import java.util.Enumeration;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;

public class DHParameter extends ASN1Encodable
{
  DERInteger g;
  DERInteger l;
  DERInteger p;

  public DHParameter(BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt)
  {
    this.p = new DERInteger(paramBigInteger1);
    this.g = new DERInteger(paramBigInteger2);
    if (paramInt != 0)
    {
      this.l = new DERInteger(paramInt);
      return;
    }
    this.l = null;
  }

  public DHParameter(ASN1Sequence paramASN1Sequence)
  {
    Enumeration localEnumeration = paramASN1Sequence.getObjects();
    this.p = ((DERInteger)localEnumeration.nextElement());
    this.g = ((DERInteger)localEnumeration.nextElement());
    if (localEnumeration.hasMoreElements())
    {
      this.l = ((DERInteger)localEnumeration.nextElement());
      return;
    }
    this.l = null;
  }

  public BigInteger getG()
  {
    return this.g.getPositiveValue();
  }

  public BigInteger getL()
  {
    if (this.l == null)
      return null;
    return this.l.getPositiveValue();
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
    if (getL() != null)
      localASN1EncodableVector.add(this.l);
    return new DERSequence(localASN1EncodableVector);
  }
}