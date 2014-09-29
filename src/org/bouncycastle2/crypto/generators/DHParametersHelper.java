package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.util.BigIntegers;

class DHParametersHelper
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private static final BigInteger TWO = BigInteger.valueOf(2L);

  static BigInteger[] generateSafePrimes(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    int i = paramInt1 - 1;
    BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    do
    {
      localBigInteger1 = new BigInteger(i, 2, paramSecureRandom);
      localBigInteger2 = localBigInteger1.shiftLeft(1).add(ONE);
    }
    while ((!localBigInteger2.isProbablePrime(paramInt2)) || ((paramInt2 > 2) && (!localBigInteger1.isProbablePrime(paramInt2))));
    return new BigInteger[] { localBigInteger2, localBigInteger1 };
  }

  static BigInteger selectGenerator(BigInteger paramBigInteger1, BigInteger paramBigInteger2, SecureRandom paramSecureRandom)
  {
    BigInteger localBigInteger1 = paramBigInteger1.subtract(TWO);
    BigInteger localBigInteger2;
    do
      localBigInteger2 = BigIntegers.createRandomInRange(TWO, localBigInteger1, paramSecureRandom).modPow(TWO, paramBigInteger1);
    while (localBigInteger2.equals(ONE));
    return localBigInteger2;
  }
}