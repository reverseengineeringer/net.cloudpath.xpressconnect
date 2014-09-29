package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalPublicKeyParameters;

public class ElGamalKeyPairGenerator
  implements AsymmetricCipherKeyPairGenerator
{
  private ElGamalKeyGenerationParameters param;

  public AsymmetricCipherKeyPair generateKeyPair()
  {
    DHKeyGeneratorHelper localDHKeyGeneratorHelper = DHKeyGeneratorHelper.INSTANCE;
    ElGamalParameters localElGamalParameters = this.param.getParameters();
    DHParameters localDHParameters = new DHParameters(localElGamalParameters.getP(), localElGamalParameters.getG(), null, localElGamalParameters.getL());
    BigInteger localBigInteger = localDHKeyGeneratorHelper.calculatePrivate(localDHParameters, this.param.getRandom());
    return new AsymmetricCipherKeyPair(new ElGamalPublicKeyParameters(localDHKeyGeneratorHelper.calculatePublic(localDHParameters, localBigInteger), localElGamalParameters), new ElGamalPrivateKeyParameters(localBigInteger, localElGamalParameters));
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.param = ((ElGamalKeyGenerationParameters)paramKeyGenerationParameters);
  }
}