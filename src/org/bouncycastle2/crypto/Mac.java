package org.bouncycastle2.crypto;

public abstract interface Mac
{
  public abstract int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException;

  public abstract String getAlgorithmName();

  public abstract int getMacSize();

  public abstract void init(CipherParameters paramCipherParameters)
    throws IllegalArgumentException;

  public abstract void reset();

  public abstract void update(byte paramByte)
    throws IllegalStateException;

  public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataLengthException, IllegalStateException;
}