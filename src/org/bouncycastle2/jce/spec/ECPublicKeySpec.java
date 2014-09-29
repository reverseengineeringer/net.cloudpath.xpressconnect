package org.bouncycastle2.jce.spec;

import org.bouncycastle2.math.ec.ECPoint;

public class ECPublicKeySpec extends ECKeySpec
{
  private ECPoint q;

  public ECPublicKeySpec(ECPoint paramECPoint, ECParameterSpec paramECParameterSpec)
  {
    super(paramECParameterSpec);
    this.q = paramECPoint;
  }

  public ECPoint getQ()
  {
    return this.q;
  }
}