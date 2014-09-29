package org.bouncycastle2.crypto;

public abstract interface Signer
{
  public abstract byte[] generateSignature()
    throws CryptoException, DataLengthException;

  public abstract void init(boolean paramBoolean, CipherParameters paramCipherParameters);

  public abstract void reset();

  public abstract void update(byte paramByte);

  public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract boolean verifySignature(byte[] paramArrayOfByte);
}