package org.bouncycastle2.jce.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class ElGamalParameterSpec
  implements AlgorithmParameterSpec
{
  private BigInteger g;
  private BigInteger p;

  public ElGamalParameterSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this.p = paramBigInteger1;
    this.g = paramBigInteger2;
  }

  public BigInteger getG()
  {
    return this.g;
  }

  public BigInteger getP()
  {
    return this.p;
  }
}