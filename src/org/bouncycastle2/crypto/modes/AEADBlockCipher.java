package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public abstract interface AEADBlockCipher
{
  public abstract int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws IllegalStateException, InvalidCipherTextException;

  public abstract String getAlgorithmName();

  public abstract byte[] getMac();

  public abstract int getOutputSize(int paramInt);

  public abstract BlockCipher getUnderlyingCipher();

  public abstract int getUpdateOutputSize(int paramInt);

  public abstract void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException;

  public abstract int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException;

  public abstract int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException;

  public abstract void reset();
}