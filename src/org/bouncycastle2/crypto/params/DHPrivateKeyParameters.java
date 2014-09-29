package org.bouncycastle2.crypto.params;

import java.math.BigInteger;

public class DHPrivateKeyParameters extends DHKeyParameters
{
  private BigInteger x;

  public DHPrivateKeyParameters(BigInteger paramBigInteger, DHParameters paramDHParameters)
  {
    super(true, paramDHParameters);
    this.x = paramBigInteger;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DHPrivateKeyParameters));
    while ((!((DHPrivateKeyParameters)paramObject).getX().equals(this.x)) || (!super.equals(paramObject)))
      return false;
    return true;
  }

  public BigInteger getX()
  {
    return this.x;
  }

  public int hashCode()
  {
    return this.x.hashCode() ^ super.hashCode();
  }
}