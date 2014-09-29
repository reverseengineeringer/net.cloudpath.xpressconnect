package org.bouncycastle2.crypto;

public abstract interface StreamCipher
{
  public abstract String getAlgorithmName();

  public abstract void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException;

  public abstract void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException;

  public abstract void reset();

  public abstract byte returnByte(byte paramByte);
}