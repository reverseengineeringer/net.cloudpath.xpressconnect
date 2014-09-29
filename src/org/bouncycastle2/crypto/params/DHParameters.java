package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import org.bouncycastle2.crypto.CipherParameters;

public class DHParameters
  implements CipherParameters
{
  private static final int DEFAULT_MINIMUM_LENGTH = 160;
  private BigInteger g;
  private BigInteger j;
  private int l;
  private int m;
  private BigInteger p;
  private BigInteger q;
  private DHValidationParameters validation;

  public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this(paramBigInteger1, paramBigInteger2, null, 0);
  }

  public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this(paramBigInteger1, paramBigInteger2, paramBigInteger3, 0);
  }

  public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt)
  {
    this(paramBigInteger1, paramBigInteger2, paramBigInteger3, getDefaultMParam(paramInt), paramInt, null, null);
  }

  public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt1, int paramInt2)
  {
    this(paramBigInteger1, paramBigInteger2, paramBigInteger3, paramInt1, paramInt2, null, null);
  }

  public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, int paramInt1, int paramInt2, BigInteger paramBigInteger4, DHValidationParameters paramDHValidationParameters)
  {
    if (paramInt2 != 0)
    {
      if (paramInt2 >= paramBigInteger1.bitLength())
        throw new IllegalArgumentException("when l value specified, it must be less than bitlength(p)");
      if (paramInt2 < paramInt1)
        throw new IllegalArgumentException("when l value specified, it may not be less than m value");
    }
    this.g = paramBigInteger2;
    this.p = paramBigInteger1;
    this.q = paramBigInteger3;
    this.m = paramInt1;
    this.l = paramInt2;
    this.j = paramBigInteger4;
    this.validation = paramDHValidationParameters;
  }

  public DHParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4, DHValidationParameters paramDHValidationParameters)
  {
    this(paramBigInteger1, paramBigInteger2, paramBigInteger3, 160, 0, paramBigInteger4, paramDHValidationParameters);
  }

  private static int getDefaultMParam(int paramInt)
  {
    if (paramInt == 0)
      return 160;
    if (paramInt < 160);
    while (true)
    {
      return paramInt;
      paramInt = 160;
    }
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DHParameters));
    DHParameters localDHParameters;
    do
    {
      while ((!localDHParameters.getP().equals(this.p)) || (!localDHParameters.getG().equals(this.g)))
        do
        {
          return false;
          localDHParameters = (DHParameters)paramObject;
          if (getQ() == null)
            break;
        }
        while (!getQ().equals(localDHParameters.getQ()));
      return true;
    }
    while (localDHParameters.getQ() == null);
    return false;
  }

  public BigInteger getG()
  {
    return this.g;
  }

  public BigInteger getJ()
  {
    return this.j;
  }

  public int getL()
  {
    return this.l;
  }

  public int getM()
  {
    return this.m;
  }

  public BigInteger getP()
  {
    return this.p;
  }

  public BigInteger getQ()
  {
    return this.q;
  }

  public DHValidationParameters getValidationParameters()
  {
    return this.validation;
  }

  public int hashCode()
  {
    int i = getP().hashCode() ^ getG().hashCode();
    if (getQ() != null);
    for (int k = getQ().hashCode(); ; k = 0)
      return k ^ i;
  }
}