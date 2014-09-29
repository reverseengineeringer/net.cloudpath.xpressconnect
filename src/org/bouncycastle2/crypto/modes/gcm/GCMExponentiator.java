package org.bouncycastle2.crypto.modes.gcm;

public abstract interface GCMExponentiator
{
  public abstract void exponentiateX(long paramLong, byte[] paramArrayOfByte);

  public abstract void init(byte[] paramArrayOfByte);
}