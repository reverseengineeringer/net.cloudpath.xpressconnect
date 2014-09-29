package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.params.RSAPrivateCrtKeyParameters;

public class RSAKeyPairGenerator
  implements AsymmetricCipherKeyPairGenerator
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private RSAKeyGenerationParameters param;

  public AsymmetricCipherKeyPair generateKeyPair()
  {
    int i = this.param.getStrength();
    int j = (i + 1) / 2;
    int k = i - j;
    int m = i / 3;
    BigInteger localBigInteger1 = this.param.getPublicExponent();
    Object localObject1;
    do
      localObject1 = new BigInteger(j, 1, this.param.getRandom());
    while ((((BigInteger)localObject1).mod(localBigInteger1).equals(ONE)) || (!((BigInteger)localObject1).isProbablePrime(this.param.getCertainty())) || (!localBigInteger1.gcd(((BigInteger)localObject1).subtract(ONE)).equals(ONE)));
    while (true)
    {
      Object localObject2 = new BigInteger(k, 1, this.param.getRandom());
      if ((((BigInteger)localObject2).subtract((BigInteger)localObject1).abs().bitLength() >= m) && (!((BigInteger)localObject2).mod(localBigInteger1).equals(ONE)) && (((BigInteger)localObject2).isProbablePrime(this.param.getCertainty())) && (localBigInteger1.gcd(((BigInteger)localObject2).subtract(ONE)).equals(ONE)))
      {
        BigInteger localBigInteger2 = ((BigInteger)localObject1).multiply((BigInteger)localObject2);
        if (localBigInteger2.bitLength() == this.param.getStrength())
        {
          if (((BigInteger)localObject1).compareTo((BigInteger)localObject2) < 0)
          {
            Object localObject3 = localObject1;
            localObject1 = localObject2;
            localObject2 = localObject3;
          }
          BigInteger localBigInteger3 = ((BigInteger)localObject1).subtract(ONE);
          BigInteger localBigInteger4 = ((BigInteger)localObject2).subtract(ONE);
          BigInteger localBigInteger5 = localBigInteger1.modInverse(localBigInteger3.multiply(localBigInteger4));
          BigInteger localBigInteger6 = localBigInteger5.remainder(localBigInteger3);
          BigInteger localBigInteger7 = localBigInteger5.remainder(localBigInteger4);
          BigInteger localBigInteger8 = ((BigInteger)localObject2).modInverse((BigInteger)localObject1);
          RSAKeyParameters localRSAKeyParameters = new RSAKeyParameters(false, localBigInteger2, localBigInteger1);
          AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = new AsymmetricCipherKeyPair(localRSAKeyParameters, new RSAPrivateCrtKeyParameters(localBigInteger2, localBigInteger1, localBigInteger5, (BigInteger)localObject1, (BigInteger)localObject2, localBigInteger6, localBigInteger7, localBigInteger8));
          return localAsymmetricCipherKeyPair;
        }
        localObject1 = ((BigInteger)localObject1).max((BigInteger)localObject2);
      }
    }
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.param = ((RSAKeyGenerationParameters)paramKeyGenerationParameters);
  }
}