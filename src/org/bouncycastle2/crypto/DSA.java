package org.bouncycastle2.crypto;

import java.math.BigInteger;

public abstract interface DSA
{
  public abstract BigInteger[] generateSignature(byte[] paramArrayOfByte);

  public abstract void init(boolean paramBoolean, CipherParameters paramCipherParameters);

  public abstract boolean verifySignature(byte[] paramArrayOfByte, BigInteger paramBigInteger1, BigInteger paramBigInteger2);
}