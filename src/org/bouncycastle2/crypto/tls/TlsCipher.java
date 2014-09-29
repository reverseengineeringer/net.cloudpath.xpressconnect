package org.bouncycastle2.crypto.tls;

import java.io.IOException;

public abstract interface TlsCipher
{
  public abstract byte[] decodeCiphertext(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract byte[] encodePlaintext(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
}