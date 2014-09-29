package org.bouncycastle2.crypto;

public abstract interface SignerWithRecovery extends Signer
{
  public abstract byte[] getRecoveredMessage();

  public abstract boolean hasFullMessage();

  public abstract void updateWithRecoveredMessage(byte[] paramArrayOfByte)
    throws InvalidCipherTextException;
}