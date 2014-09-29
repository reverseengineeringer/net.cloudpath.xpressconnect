package org.bouncycastle2.crypto;

public abstract interface DerivationFunction
{
  public abstract int generateBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataLengthException, IllegalArgumentException;

  public abstract Digest getDigest();

  public abstract void init(DerivationParameters paramDerivationParameters);
}