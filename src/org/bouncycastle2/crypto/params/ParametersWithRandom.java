package org.bouncycastle2.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CipherParameters;

public class ParametersWithRandom
  implements CipherParameters
{
  private CipherParameters parameters;
  private SecureRandom random;

  public ParametersWithRandom(CipherParameters paramCipherParameters)
  {
    this(paramCipherParameters, new SecureRandom());
  }

  public ParametersWithRandom(CipherParameters paramCipherParameters, SecureRandom paramSecureRandom)
  {
    this.random = paramSecureRandom;
    this.parameters = paramCipherParameters;
  }

  public CipherParameters getParameters()
  {
    return this.parameters;
  }

  public SecureRandom getRandom()
  {
    return this.random;
  }
}