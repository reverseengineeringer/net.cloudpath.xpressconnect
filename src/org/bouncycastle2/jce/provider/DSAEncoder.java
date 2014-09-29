package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.math.BigInteger;

public abstract interface DSAEncoder
{
  public abstract BigInteger[] decode(byte[] paramArrayOfByte)
    throws IOException;

  public abstract byte[] encode(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws IOException;
}