package org.bouncycastle2.crypto.tls;

import java.io.OutputStream;

public abstract interface TlsCompression
{
  public abstract OutputStream compress(OutputStream paramOutputStream);

  public abstract OutputStream decompress(OutputStream paramOutputStream);
}