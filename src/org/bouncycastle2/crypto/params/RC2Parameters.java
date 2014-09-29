package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class RC2Parameters
  implements CipherParameters
{
  private int bits;
  private byte[] key;

  public RC2Parameters(byte[] paramArrayOfByte)
  {
  }

  public RC2Parameters(byte[] paramArrayOfByte, int paramInt)
  {
    this.key = new byte[paramArrayOfByte.length];
    this.bits = paramInt;
    System.arraycopy(paramArrayOfByte, 0, this.key, 0, paramArrayOfByte.length);
  }

  public int getEffectiveKeyBits()
  {
    return this.bits;
  }

  public byte[] getKey()
  {
    return this.key;
  }
}