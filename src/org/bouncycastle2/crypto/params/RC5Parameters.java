package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class RC5Parameters
  implements CipherParameters
{
  private byte[] key;
  private int rounds;

  public RC5Parameters(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramArrayOfByte.length > 255)
      throw new IllegalArgumentException("RC5 key length can be no greater than 255");
    this.key = new byte[paramArrayOfByte.length];
    this.rounds = paramInt;
    System.arraycopy(paramArrayOfByte, 0, this.key, 0, paramArrayOfByte.length);
  }

  public byte[] getKey()
  {
    return this.key;
  }

  public int getRounds()
  {
    return this.rounds;
  }
}