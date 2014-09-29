package org.bouncycastle2.jce.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class GOST3410PrivateKeySpec
  implements KeySpec
{
  private BigInteger a;
  private BigInteger p;
  private BigInteger q;
  private BigInteger x;

  public GOST3410PrivateKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
  {
    this.x = paramBigInteger1;
    this.p = paramBigInteger2;
    this.q = paramBigInteger3;
    this.a = paramBigInteger4;
  }

  public BigInteger getA()
  {
    return this.a;
  }

  public BigInteger getP()
  {
    return this.p;
  }

  public BigInteger getQ()
  {
    return this.q;
  }

  public BigInteger getX()
  {
    return this.x;
  }
}