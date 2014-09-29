package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.KeyGenerationParameters;

public class DSAKeyGenerationParameters extends KeyGenerationParameters
{
  private DSAParameters params;

  public DSAKeyGenerationParameters(SecureRandom paramSecureRandom, DSAParameters paramDSAParameters)
  {
    super(paramSecureRandom, -1 + paramDSAParameters.getP().bitLength());
    this.params = paramDSAParameters;
  }

  public DSAParameters getParameters()
  {
    return this.params;
  }
}