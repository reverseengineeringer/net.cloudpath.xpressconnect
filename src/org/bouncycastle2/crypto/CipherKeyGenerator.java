package org.bouncycastle2.crypto;

import java.security.SecureRandom;

public class CipherKeyGenerator
{
  protected SecureRandom random;
  protected int strength;

  public byte[] generateKey()
  {
    byte[] arrayOfByte = new byte[this.strength];
    this.random.nextBytes(arrayOfByte);
    return arrayOfByte;
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.random = paramKeyGenerationParameters.getRandom();
    this.strength = ((7 + paramKeyGenerationParameters.getStrength()) / 8);
  }
}