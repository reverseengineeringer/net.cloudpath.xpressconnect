package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;

public class DHKeyPairGenerator
  implements AsymmetricCipherKeyPairGenerator
{
  private DHKeyGenerationParameters param;

  public AsymmetricCipherKeyPair generateKeyPair()
  {
    DHKeyGeneratorHelper localDHKeyGeneratorHelper = DHKeyGeneratorHelper.INSTANCE;
    DHParameters localDHParameters = this.param.getParameters();
    BigInteger localBigInteger = localDHKeyGeneratorHelper.calculatePrivate(localDHParameters, this.param.getRandom());
    return new AsymmetricCipherKeyPair(new DHPublicKeyParameters(localDHKeyGeneratorHelper.calculatePublic(localDHParameters, localBigInteger), localDHParameters), new DHPrivateKeyParameters(localBigInteger, localDHParameters));
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.param = ((DHKeyGenerationParameters)paramKeyGenerationParameters);
  }
}