package org.bouncycastle2.crypto.generators;

import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.DerivationFunction;
import org.bouncycastle2.crypto.DerivationParameters;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.params.MGFParameters;

public class MGF1BytesGenerator
  implements DerivationFunction
{
  private Digest digest;
  private int hLen;
  private byte[] seed;

  public MGF1BytesGenerator(Digest paramDigest)
  {
    this.digest = paramDigest;
    this.hLen = paramDigest.getDigestSize();
  }

  private void ItoOSP(int paramInt, byte[] paramArrayOfByte)
  {
    paramArrayOfByte[0] = ((byte)(paramInt >>> 24));
    paramArrayOfByte[1] = ((byte)(paramInt >>> 16));
    paramArrayOfByte[2] = ((byte)(paramInt >>> 8));
    paramArrayOfByte[3] = ((byte)(paramInt >>> 0));
  }

  public int generateBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws DataLengthException, IllegalArgumentException
  {
    if (paramArrayOfByte.length - paramInt2 < paramInt1)
      throw new DataLengthException("output buffer too small");
    byte[] arrayOfByte1 = new byte[this.hLen];
    byte[] arrayOfByte2 = new byte[4];
    this.digest.reset();
    int i = this.hLen;
    int j = 0;
    if (paramInt2 > i)
      do
      {
        ItoOSP(j, arrayOfByte2);
        this.digest.update(this.seed, 0, this.seed.length);
        this.digest.update(arrayOfByte2, 0, arrayOfByte2.length);
        this.digest.doFinal(arrayOfByte1, 0);
        System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, paramInt1 + j * this.hLen, this.hLen);
        j++;
      }
      while (j < paramInt2 / this.hLen);
    if (j * this.hLen < paramInt2)
    {
      ItoOSP(j, arrayOfByte2);
      this.digest.update(this.seed, 0, this.seed.length);
      this.digest.update(arrayOfByte2, 0, arrayOfByte2.length);
      this.digest.doFinal(arrayOfByte1, 0);
      System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, paramInt1 + j * this.hLen, paramInt2 - j * this.hLen);
    }
    return paramInt2;
  }

  public Digest getDigest()
  {
    return this.digest;
  }

  public void init(DerivationParameters paramDerivationParameters)
  {
    if (!(paramDerivationParameters instanceof MGFParameters))
      throw new IllegalArgumentException("MGF parameters required for MGF1Generator");
    this.seed = ((MGFParameters)paramDerivationParameters).getSeed();
  }
}