package org.bouncycastle2.crypto.paddings;

import java.security.SecureRandom;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public abstract interface BlockCipherPadding
{
  public abstract int addPadding(byte[] paramArrayOfByte, int paramInt);

  public abstract String getPaddingName();

  public abstract void init(SecureRandom paramSecureRandom)
    throws IllegalArgumentException;

  public abstract int padCount(byte[] paramArrayOfByte)
    throws InvalidCipherTextException;
}