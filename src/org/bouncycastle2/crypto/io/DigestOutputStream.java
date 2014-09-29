package org.bouncycastle2.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle2.crypto.Digest;

public class DigestOutputStream extends FilterOutputStream
{
  protected Digest digest;

  public DigestOutputStream(OutputStream paramOutputStream, Digest paramDigest)
  {
    super(paramOutputStream);
    this.digest = paramDigest;
  }

  public Digest getDigest()
  {
    return this.digest;
  }

  public void write(int paramInt)
    throws IOException
  {
    this.digest.update((byte)paramInt);
    this.out.write(paramInt);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
  }
}