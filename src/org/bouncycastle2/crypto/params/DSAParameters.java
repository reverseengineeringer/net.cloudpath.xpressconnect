package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import org.bouncycastle2.crypto.CipherParameters;

public class DSAParameters
  implements CipherParameters
{
  private BigInteger g;
  private BigInteger p;
  private BigInteger q;
  private DSAValidationParameters validation;

  public DSAParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    this.g = paramBigInteger3;
    this.p = paramBigInteger1;
    this.q = paramBigInteger2;
  }

  public DSAParameters(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, DSAValidationParameters paramDSAValidationParameters)
  {
    this.g = paramBigInteger3;
    this.p = paramBigInteger1;
    this.q = paramBigInteger2;
    this.validation = paramDSAValidationParameters;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DSAParameters));
    DSAParameters localDSAParameters;
    do
    {
      return false;
      localDSAParameters = (DSAParameters)paramObject;
    }
    while ((!localDSAParameters.getP().equals(this.p)) || (!localDSAParameters.getQ().equals(this.q)) || (!localDSAParameters.getG().equals(this.g)));
    return true;
  }

  public BigInteger getG()
  {
    return this.g;
  }

  public BigInteger getP()
  {
    return this.p;
  }

  public BigInteger getQ()
  {
    return this.q;
  }

  public DSAValidationParameters getValidationParameters()
  {
    return this.validation;
  }

  public int hashCode()
  {
    return getP().hashCode() ^ getQ().hashCode() ^ getG().hashCode();
  }
}