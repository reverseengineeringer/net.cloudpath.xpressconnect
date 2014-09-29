package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle2.util.BigIntegers;

public class DSAKeyPairGenerator
  implements AsymmetricCipherKeyPairGenerator
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private DSAKeyGenerationParameters param;

  private static BigInteger calculatePublicKey(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    return paramBigInteger2.modPow(paramBigInteger3, paramBigInteger1);
  }

  private static BigInteger generatePrivateKey(BigInteger paramBigInteger, SecureRandom paramSecureRandom)
  {
    return BigIntegers.createRandomInRange(ONE, paramBigInteger.subtract(ONE), paramSecureRandom);
  }

  public AsymmetricCipherKeyPair generateKeyPair()
  {
    DSAParameters localDSAParameters = this.param.getParameters();
    BigInteger localBigInteger = generatePrivateKey(localDSAParameters.getQ(), this.param.getRandom());
    return new AsymmetricCipherKeyPair(new DSAPublicKeyParameters(calculatePublicKey(localDSAParameters.getP(), localDSAParameters.getG(), localBigInteger), localDSAParameters), new DSAPrivateKeyParameters(localBigInteger, localDSAParameters));
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.param = ((DSAKeyGenerationParameters)paramKeyGenerationParameters);
  }
}