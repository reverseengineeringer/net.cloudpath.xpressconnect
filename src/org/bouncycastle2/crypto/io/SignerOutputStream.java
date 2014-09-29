package org.bouncycastle2.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle2.crypto.Signer;

public class SignerOutputStream extends FilterOutputStream
{
  protected Signer signer;

  public SignerOutputStream(OutputStream paramOutputStream, Signer paramSigner)
  {
    super(paramOutputStream);
    this.signer = paramSigner;
  }

  public Signer getSigner()
  {
    return this.signer;
  }

  public void write(int paramInt)
    throws IOException
  {
    this.signer.update((byte)paramInt);
    this.out.write(paramInt);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.signer.update(paramArrayOfByte, paramInt1, paramInt2);
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
  }
}