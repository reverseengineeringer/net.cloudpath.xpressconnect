package org.bouncycastle2.crypto.params;

import java.math.BigInteger;

public class RSAKeyParameters extends AsymmetricKeyParameter
{
  private BigInteger exponent;
  private BigInteger modulus;

  public RSAKeyParameters(boolean paramBoolean, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramBoolean);
    this.modulus = paramBigInteger1;
    this.exponent = paramBigInteger2;
  }

  public BigInteger getExponent()
  {
    return this.exponent;
  }

  public BigInteger getModulus()
  {
    return this.modulus;
  }
}