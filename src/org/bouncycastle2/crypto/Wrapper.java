package org.bouncycastle2.crypto;

public abstract interface Wrapper
{
  public abstract String getAlgorithmName();

  public abstract void init(boolean paramBoolean, CipherParameters paramCipherParameters);

  public abstract byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException;

  public abstract byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}