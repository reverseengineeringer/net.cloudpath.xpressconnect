package org.bouncycastle2.crypto.tls;

import java.io.IOException;

public abstract interface TlsCipherFactory
{
  public abstract TlsCipher createCipher(TlsClientContext paramTlsClientContext, int paramInt1, int paramInt2)
    throws IOException;
}