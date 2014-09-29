package org.bouncycastle2.asn1.x9;

import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECPoint;

public class X9ECPoint extends ASN1Encodable
{
  ECPoint p;

  public X9ECPoint(ECCurve paramECCurve, ASN1OctetString paramASN1OctetString)
  {
    this.p = paramECCurve.decodePoint(paramASN1OctetString.getOctets());
  }

  public X9ECPoint(ECPoint paramECPoint)
  {
    this.p = paramECPoint;
  }

  public ECPoint getPoint()
  {
    return this.p;
  }

  public DERObject toASN1Object()
  {
    return new DEROctetString(this.p.getEncoded());
  }
}