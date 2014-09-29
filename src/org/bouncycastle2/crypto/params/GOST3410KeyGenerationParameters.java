package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.KeyGenerationParameters;

public class GOST3410KeyGenerationParameters extends KeyGenerationParameters
{
  private GOST3410Parameters params;

  public GOST3410KeyGenerationParameters(SecureRandom paramSecureRandom, GOST3410Parameters paramGOST3410Parameters)
  {
    super(paramSecureRandom, -1 + paramGOST3410Parameters.getP().bitLength());
    this.params = paramGOST3410Parameters;
  }

  public GOST3410Parameters getParameters()
  {
    return this.params;
  }
}