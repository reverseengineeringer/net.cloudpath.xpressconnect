package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.params.DHParameters;

public class DHParametersGenerator
{
  private static final BigInteger TWO = BigInteger.valueOf(2L);
  private int certainty;
  private SecureRandom random;
  private int size;

  public DHParameters generateParameters()
  {
    BigInteger[] arrayOfBigInteger = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
    BigInteger localBigInteger1 = arrayOfBigInteger[0];
    BigInteger localBigInteger2 = arrayOfBigInteger[1];
    return new DHParameters(localBigInteger1, DHParametersHelper.selectGenerator(localBigInteger1, localBigInteger2, this.random), localBigInteger2, TWO, null);
  }

  public void init(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    this.size = paramInt1;
    this.certainty = paramInt2;
    this.random = paramSecureRandom;
  }
}