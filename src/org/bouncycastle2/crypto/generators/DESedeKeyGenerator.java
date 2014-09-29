package org.bouncycastle2.crypto.generators;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.DESedeParameters;

public class DESedeKeyGenerator extends DESKeyGenerator
{
  public byte[] generateKey()
  {
    byte[] arrayOfByte = new byte[this.strength];
    do
    {
      this.random.nextBytes(arrayOfByte);
      DESedeParameters.setOddParity(arrayOfByte);
    }
    while (DESedeParameters.isWeakKey(arrayOfByte, 0, arrayOfByte.length));
    return arrayOfByte;
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.random = paramKeyGenerationParameters.getRandom();
    this.strength = ((7 + paramKeyGenerationParameters.getStrength()) / 8);
    if ((this.strength == 0) || (this.strength == 21))
      this.strength = 24;
    do
    {
      return;
      if (this.strength == 14)
      {
        this.strength = 16;
        return;
      }
    }
    while ((this.strength == 24) || (this.strength == 16));
    throw new IllegalArgumentException("DESede key must be 192 or 128 bits long.");
  }
}