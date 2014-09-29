package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.util.BigIntegers;

class DHKeyGeneratorHelper
{
  static final DHKeyGeneratorHelper INSTANCE = new DHKeyGeneratorHelper();
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private static final BigInteger TWO = BigInteger.valueOf(2L);

  BigInteger calculatePrivate(DHParameters paramDHParameters, SecureRandom paramSecureRandom)
  {
    BigInteger localBigInteger1 = paramDHParameters.getP();
    int i = paramDHParameters.getL();
    if (i != 0)
      return new BigInteger(i, paramSecureRandom).setBit(i - 1);
    BigInteger localBigInteger2 = TWO;
    int j = paramDHParameters.getM();
    if (j != 0)
      localBigInteger2 = ONE.shiftLeft(j - 1);
    BigInteger localBigInteger3 = localBigInteger1.subtract(TWO);
    BigInteger localBigInteger4 = paramDHParameters.getQ();
    if (localBigInteger4 != null)
      localBigInteger3 = localBigInteger4.subtract(TWO);
    return BigIntegers.createRandomInRange(localBigInteger2, localBigInteger3, paramSecureRandom);
  }

  BigInteger calculatePublic(DHParameters paramDHParameters, BigInteger paramBigInteger)
  {
    return paramDHParameters.getG().modPow(paramBigInteger, paramDHParameters.getP());
  }
}