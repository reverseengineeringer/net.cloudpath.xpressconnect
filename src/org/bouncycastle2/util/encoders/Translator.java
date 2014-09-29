package org.bouncycastle2.util.encoders;

public abstract interface Translator
{
  public abstract int decode(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);

  public abstract int encode(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3);

  public abstract int getDecodedBlockSize();

  public abstract int getEncodedBlockSize();
}