package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.params.RSAPrivateCrtKeyParameters;

public class RSABlindingFactorGenerator
{
  private static BigInteger ONE = BigInteger.valueOf(1L);
  private static BigInteger ZERO = BigInteger.valueOf(0L);
  private RSAKeyParameters key;
  private SecureRandom random;

  public BigInteger generateBlindingFactor()
  {
    if (this.key == null)
      throw new IllegalStateException("generator not initialised");
    BigInteger localBigInteger1 = this.key.getModulus();
    int i = -1 + localBigInteger1.bitLength();
    BigInteger localBigInteger2;
    BigInteger localBigInteger3;
    do
    {
      localBigInteger2 = new BigInteger(i, this.random);
      localBigInteger3 = localBigInteger2.gcd(localBigInteger1);
    }
    while ((localBigInteger2.equals(ZERO)) || (localBigInteger2.equals(ONE)) || (!localBigInteger3.equals(ONE)));
    return localBigInteger2;
  }

  public void init(CipherParameters paramCipherParameters)
  {
    ParametersWithRandom localParametersWithRandom;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.key = ((RSAKeyParameters)localParametersWithRandom.getParameters());
    }
    for (this.random = localParametersWithRandom.getRandom(); (this.key instanceof RSAPrivateCrtKeyParameters); this.random = new SecureRandom())
    {
      throw new IllegalArgumentException("generator requires RSA public key");
      this.key = ((RSAKeyParameters)paramCipherParameters);
    }
  }
}