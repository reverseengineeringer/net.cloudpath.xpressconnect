package org.bouncycastle2.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.math.ec.ECFieldElement;
import org.bouncycastle2.math.ec.ECFieldElement.F2m;
import org.bouncycastle2.math.ec.ECFieldElement.Fp;

public class X9FieldElement extends ASN1Encodable
{
  private static X9IntegerConverter converter = new X9IntegerConverter();
  protected ECFieldElement f;

  public X9FieldElement(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ASN1OctetString paramASN1OctetString)
  {
    this(new ECFieldElement.F2m(paramInt1, paramInt2, paramInt3, paramInt4, new BigInteger(1, paramASN1OctetString.getOctets())));
  }

  public X9FieldElement(BigInteger paramBigInteger, ASN1OctetString paramASN1OctetString)
  {
    this(new ECFieldElement.Fp(paramBigInteger, new BigInteger(1, paramASN1OctetString.getOctets())));
  }

  public X9FieldElement(ECFieldElement paramECFieldElement)
  {
    this.f = paramECFieldElement;
  }

  public ECFieldElement getValue()
  {
    return this.f;
  }

  public DERObject toASN1Object()
  {
    int i = converter.getByteLength(this.f);
    return new DEROctetString(converter.integerToBytes(this.f.toBigInteger(), i));
  }
}