package org.bouncycastle2.crypto.params;

import java.math.BigInteger;

public class DHPublicKeyParameters extends DHKeyParameters
{
  private BigInteger y;

  public DHPublicKeyParameters(BigInteger paramBigInteger, DHParameters paramDHParameters)
  {
    super(false, paramDHParameters);
    this.y = paramBigInteger;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DHPublicKeyParameters));
    while ((!((DHPublicKeyParameters)paramObject).getY().equals(this.y)) || (!super.equals(paramObject)))
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