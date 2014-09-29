package org.bouncycastle2.crypto.params;

import org.bouncycastle2.math.ec.ECPoint;

public class ECPublicKeyParameters extends ECKeyParameters
{
  ECPoint Q;

  public ECPublicKeyParameters(ECPoint paramECPoint, ECDomainParameters paramECDomainParameters)
  {
    super(false, paramECDomainParameters);
    this.Q = paramECPoint;
  }

  public ECPoint getQ()
  {
    return this.Q;
  }
}