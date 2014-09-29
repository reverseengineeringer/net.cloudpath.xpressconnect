package org.bouncycastle2.crypto.generators;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.CipherKeyGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.DESParameters;

public class DESKeyGenerator extends CipherKeyGenerator
{
  public byte[] generateKey()
  {
    byte[] arrayOfByte = new byte[8];
    do
    {
      this.random.nextBytes(arrayOfByte);
      DESParameters.setOddParity(arrayOfByte);
    }
    while (DESParameters.isWeakKey(arrayOfByte, 0));
    return arrayOfByte;
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    super.init(paramKeyGenerationParameters);
    if ((this.strength == 0) || (this.strength == 7))
      this.strength = 8;
    while (this.strength == 8)
      return;
    throw new IllegalArgumentException("DES key must be 64 bits long.");
  }
}