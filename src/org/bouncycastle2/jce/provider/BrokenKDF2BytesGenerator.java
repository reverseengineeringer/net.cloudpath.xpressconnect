package org.bouncycastle2.jce.provider;

import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.DerivationFunction;
import org.bouncycastle2.crypto.DerivationParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.params.KDFParameters;

public class BrokenKDF2BytesGenerator
  implements DerivationFunction
{
  private Digest digest;
  private byte[] iv;
  private byte[] shared;

  public BrokenKDF2BytesGenerator(Digest paramDigest)
  {
    this.digest = paramDigest;
  }

  public int generateBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataLengthException, IllegalArgumentException
  {
    if (paramArrayOfByte.length - paramInt2 < paramInt1)
      throw new DataLengthException("output buffer too small");
    long l = paramInt2 * 8;
    if (l > 29L * (8 * this.digest.getDigestSize()))
      new IllegalArgumentException("Output length to large");
    int i = (int)(l / this.digest.getDigestSize());
    ((byte[])null);
    byte[] arrayOfByte = new byte[this.digest.getDigestSize()];
    int j = 1;
    if (j > i)
    {
      this.digest.reset();
      return paramInt2;
    }
    this.digest.update(this.shared, 0, this.shared.length);
    this.digest.update((byte)(j & 0xFF));
    this.digest.update((byte)(0xFF & j >> 8));
    this.digest.update((byte)(0xFF & j >> 16));
    this.digest.update((byte)(0xFF & j >> 24));
    this.digest.update(this.iv, 0, this.iv.length);
    this.digest.doFinal(arrayOfByte, 0);
    if (paramInt2 - paramInt1 > arrayOfByte.length)
    {
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, arrayOfByte.length);
      paramInt1 += arrayOfByte.length;
    }
    while (true)
    {
      j++;
      break;
      System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, paramInt2 - paramInt1);
    }
  }

  public Digest getDigest()
  {
    return this.digest;
  }

  public void init(DerivationParameters paramDerivationParameters)
  {
    if (!(paramDerivationParameters instanceof KDFParameters))
      throw new IllegalArgumentException("KDF parameters required for KDF2Generator");
    KDFParameters localKDFParameters = (KDFParameters)paramDerivationParameters;
    this.shared = localKDFParameters.getSharedSecret();
    this.iv = localKDFParameters.getIV();
  }
}