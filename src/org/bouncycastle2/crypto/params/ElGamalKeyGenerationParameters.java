package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.KeyGenerationParameters;

public class ElGamalKeyGenerationParameters extends KeyGenerationParameters
{
  private ElGamalParameters params;

  public ElGamalKeyGenerationParameters(SecureRandom paramSecureRandom, ElGamalParameters paramElGamalParameters)
  {
    super(paramSecureRandom, getStrength(paramElGamalParameters));
    this.params = paramElGamalParameters;
  }

  static int getStrength(ElGamalParameters paramElGamalParameters)
  {
    if (paramElGamalParameters.getL() != 0)
      return paramElGamalParameters.getL();
    return paramElGamalParameters.getP().bitLength();
  }

  public ElGamalParameters getParameters()
  {
    return this.params;
  }
}