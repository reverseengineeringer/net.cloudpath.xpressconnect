package org.bouncycastle2.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle2.crypto.Digest;

public class DigestInputStream extends FilterInputStream
{
  protected Digest digest;

  public DigestInputStream(InputStream paramInputStream, Digest paramDigest)
  {
    super(paramInputStream);
    this.digest = paramDigest;
  }

  public Digest getDigest()
  {
    return this.digest;
  }

  public int read()
    throws IOException
  {
    int i = this.in.read();
    if (i >= 0)
      this.digest.update((byte)i);
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i > 0)
      this.digest.update(paramArrayOfByte, paramInt1, i);
    return i;
  }
}