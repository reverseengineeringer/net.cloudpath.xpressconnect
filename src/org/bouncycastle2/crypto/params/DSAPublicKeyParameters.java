package org.bouncycastle2.crypto.params;

import java.math.BigInteger;

public class DSAPublicKeyParameters extends DSAKeyParameters
{
  private BigInteger y;

  public DSAPublicKeyParameters(BigInteger paramBigInteger, DSAParameters paramDSAParameters)
  {
    super(false, paramDSAParameters);
    this.y = paramBigInteger;
  }

  public BigInteger getY()
  {
    return this.y;
  }
}