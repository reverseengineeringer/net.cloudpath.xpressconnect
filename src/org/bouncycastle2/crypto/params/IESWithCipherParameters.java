package org.bouncycastle2.crypto.params;

public class IESWithCipherParameters extends IESParameters
{
  private int cipherKeySize;

  public IESWithCipherParameters(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
  {
    super(paramArrayOfByte1, paramArrayOfByte2, paramInt1);
    this.cipherKeySize = paramInt2;
  }

  public int getCipherKeySize()
  {
    return this.cipherKeySize;
  }
}