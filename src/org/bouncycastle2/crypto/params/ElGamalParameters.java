package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import org.bouncycastle2.crypto.CipherParameters;

public class ElGamalParameters
  implements CipherParameters
{
  private BigInteger g;
  private int l;
  private BigInteger p;

  public ElGamalParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this(paramBigInteger1, paramBigInteger2, 0);
  }

  public ElGamalParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, int paramInt)
  {
    this.g = paramBigInteger2;
    this.p = paramBigInteger1;
    this.l = paramInt;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ElGamalParameters));
    ElGamalParameters localElGamalParameters;
    do
    {
      return false;
      localElGamalParameters = (ElGamalParameters)paramObject;
    }
    while ((!localElGamalParameters.getP().equals(this.p)) || (!localElGamalParameters.getG().equals(this.g)) || (localElGamalParameters.getL() != this.l));
    return true;
  }

  public BigInteger getG()
  {
    return this.g;
  }

  public int getL()
  {
    return this.l;
  }

  public BigInteger getP()
  {
    return this.p;
  }

  public int hashCode()
  {
    return (getP().hashCode() ^ getG().hashCode()) + this.l;
  }
}