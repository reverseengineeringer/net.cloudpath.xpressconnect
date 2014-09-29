package org.bouncycastle2.crypto.params;

import java.math.BigInteger;

public class ElGamalPublicKeyParameters extends ElGamalKeyParameters
{
  private BigInteger y;

  public ElGamalPublicKeyParameters(BigInteger paramBigInteger, ElGamalParameters paramElGamalParameters)
  {
    super(false, paramElGamalParameters);
    this.y = paramBigInteger;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ElGamalPublicKeyParameters));
    while ((!((ElGamalPublicKeyParameters)paramObject).getY().equals(this.y)) || (!super.equals(paramObject)))
      return false;
    return true;
  }

  public BigInteger getY()
  {
    return this.y;
  }

  public int hashCode()
  {
    return this.y.hashCode() ^ super.hashCode();
  }
}