package org.bouncycastle2.crypto.agreement;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.generators.DHKeyPairGenerator;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class DHAgreement
{
  private DHParameters dhParams;
  private DHPrivateKeyParameters key;
  private BigInteger privateValue;
  private SecureRandom random;

  public BigInteger calculateAgreement(DHPublicKeyParameters paramDHPublicKeyParameters, BigInteger paramBigInteger)
  {
    if (!paramDHPublicKeyParameters.getParameters().equals(this.dhParams))
      throw new IllegalArgumentException("Diffie-Hellman public key has wrong parameters.");
    BigInteger localBigInteger = this.dhParams.getP();
    return paramBigInteger.modPow(this.key.getX(), localBigInteger).multiply(paramDHPublicKeyParameters.getY().modPow(this.privateValue, localBigInteger)).mod(localBigInteger);
  }

  public BigInteger calculateMessage()
  {
    DHKeyPairGenerator localDHKeyPairGenerator = new DHKeyPairGenerator();
    localDHKeyPairGenerator.init(new DHKeyGenerationParameters(this.random, this.dhParams));
    AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = localDHKeyPairGenerator.generateKeyPair();
    this.privateValue = ((DHPrivateKeyParameters)localAsymmetricCipherKeyPair.getPrivate()).getX();
    return ((DHPublicKeyParameters)localAsymmetricCipherKeyPair.getPublic()).getY();
  }

  public void init(CipherParameters paramCipherParameters)
  {
    ParametersWithRandom localParametersWithRandom;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.random = localParametersWithRandom.getRandom();
    }
    for (AsymmetricKeyParameter localAsymmetricKeyParameter = (AsymmetricKeyParameter)localParametersWithRandom.getParameters(); !(localAsymmetricKeyParameter instanceof DHPrivateKeyParameters); localAsymmetricKeyParameter = (AsymmetricKeyParameter)paramCipherParameters)
    {
      throw new IllegalArgumentException("DHEngine expects DHPrivateKeyParameters");
      this.random = new SecureRandom();
    }
    this.key = ((DHPrivateKeyParameters)localAsymmetricKeyParameter);
    this.dhParams = this.key.getParameters();
  }
}