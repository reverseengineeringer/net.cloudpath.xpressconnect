package org.bouncycastle2.crypto.params;

import java.math.BigInteger;

public class ElGamalPrivateKeyParameters extends ElGamalKeyParameters
{
  private BigInteger x;

  public ElGamalPrivateKeyParameters(BigInteger paramBigInteger, ElGamalParameters paramElGamalParameters)
  {
    super(true, paramElGamalParameters);
    this.x = paramBigInteger;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ElGamalPrivateKeyParameters));
    while (!((ElGamalPrivateKeyParameters)paramObject).getX().equals(this.x))
      return false;
    return super.equals(paramObject);
  }

  public BigInteger getX()
  {
    return this.x;
  }

  public int hashCode()
  {
    return getX().hashCode();
  }
}