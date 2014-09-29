package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.ECDomainParameters;
import org.bouncycastle2.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle2.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.math.ec.ECConstants;
import org.bouncycastle2.math.ec.ECPoint;

public class ECKeyPairGenerator
  implements AsymmetricCipherKeyPairGenerator, ECConstants
{
  ECDomainParameters params;
  SecureRandom random;

  public AsymmetricCipherKeyPair generateKeyPair()
  {
    BigInteger localBigInteger1 = this.params.getN();
    int i = localBigInteger1.bitLength();
    BigInteger localBigInteger2;
    do
      localBigInteger2 = new BigInteger(i, this.random);
    while ((localBigInteger2.equals(ZERO)) || (localBigInteger2.compareTo(localBigInteger1) >= 0));
    return new AsymmetricCipherKeyPair(new ECPublicKeyParameters(this.params.getG().multiply(localBigInteger2), this.params), new ECPrivateKeyParameters(localBigInteger2, this.params));
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    ECKeyGenerationParameters localECKeyGenerationParameters = (ECKeyGenerationParameters)paramKeyGenerationParameters;
    this.random = localECKeyGenerationParameters.getRandom();
    this.params = localECKeyGenerationParameters.getDomainParameters();
  }
}