package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.KeyGenerationParameters;

public class DHKeyGenerationParameters extends KeyGenerationParameters
{
  private DHParameters params;

  public DHKeyGenerationParameters(SecureRandom paramSecureRandom, DHParameters paramDHParameters)
  {
    super(paramSecureRandom, getStrength(paramDHParameters));
    this.params = paramDHParameters;
  }

  static int getStrength(DHParameters paramDHParameters)
  {
    if (paramDHParameters.getL() != 0)
      return paramDHParameters.getL();
    return paramDHParameters.getP().bitLength();
  }

  public DHParameters getParameters()
  {
    return this.params;
  }
}