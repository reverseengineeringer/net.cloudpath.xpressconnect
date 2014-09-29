package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.Wrapper;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.util.Arrays;

public class RFC3394WrapEngine
  implements Wrapper
{
  private BlockCipher engine;
  private boolean forWrapping;
  private byte[] iv = { -90, -90, -90, -90, -90, -90, -90, -90 };
  private KeyParameter param;

  public RFC3394WrapEngine(BlockCipher paramBlockCipher)
  {
    this.engine = paramBlockCipher;
  }

  public String getAlgorithmName()
  {
    return this.engine.getAlgorithmName();
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forWrapping = paramBoolean;
    if ((paramCipherParameters instanceof ParametersWithRandom))
      paramCipherParameters = ((ParametersWithRandom)paramCipherParameters).getParameters();
    if ((paramCipherParameters instanceof KeyParameter))
      this.param = ((KeyParameter)paramCipherParameters);
    do
    {
      do
        return;
      while (!(paramCipherParameters instanceof ParametersWithIV));
      this.iv = ((ParametersWithIV)paramCipherParameters).getIV();
      this.param = ((KeyParameter)((ParametersWithIV)paramCipherParameters).getParameters());
    }
    while (this.iv.length == 8);
    throw new IllegalArgumentException("IV not equal to 8");
  }

  public byte[] unwrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.forWrapping)
      throw new IllegalStateException("not set for unwrapping");
    int i = paramInt2 / 8;
    if (i * 8 != paramInt2)
      throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
    byte[] arrayOfByte1 = new byte[paramInt2 - this.iv.length];
    byte[] arrayOfByte2 = new byte[this.iv.length];
    byte[] arrayOfByte3 = new byte[8 + this.iv.length];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte2, 0, this.iv.length);
    System.arraycopy(paramArrayOfByte, this.iv.length, arrayOfByte1, 0, paramInt2 - this.iv.length);
    this.engine.init(false, this.param);
    int j = i - 1;
    int m;
    for (int k = 5; ; k--)
    {
      if (k < 0)
      {
        if (Arrays.constantTimeAreEqual(arrayOfByte2, this.iv))
          break label318;
        throw new InvalidCipherTextException("checksum failed");
      }
      m = j;
      if (m >= 1)
        break;
    }
    System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, this.iv.length);
    System.arraycopy(arrayOfByte1, 8 * (m - 1), arrayOfByte3, this.iv.length, 8);
    int n = m + j * k;
    for (int i1 = 1; ; i1++)
    {
      if (n == 0)
      {
        this.engine.processBlock(arrayOfByte3, 0, arrayOfByte3, 0);
        System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 0, 8);
        System.arraycopy(arrayOfByte3, 8, arrayOfByte1, 8 * (m - 1), 8);
        m--;
        break;
      }
      int i2 = (byte)n;
      int i3 = this.iv.length - i1;
      arrayOfByte3[i3] = ((byte)(i2 ^ arrayOfByte3[i3]));
      n >>>= 8;
    }
    label318: return arrayOfByte1;
  }

  public byte[] wrap(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (!this.forWrapping)
      throw new IllegalStateException("not set for wrapping");
    int i = paramInt2 / 8;
    if (i * 8 != paramInt2)
      throw new DataLengthException("wrap data must be a multiple of 8 bytes");
    byte[] arrayOfByte1 = new byte[paramInt2 + this.iv.length];
    byte[] arrayOfByte2 = new byte[8 + this.iv.length];
    System.arraycopy(this.iv, 0, arrayOfByte1, 0, this.iv.length);
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, this.iv.length, paramInt2);
    this.engine.init(true, this.param);
    int k;
    for (int j = 0; ; j++)
    {
      if (j == 6)
        return arrayOfByte1;
      k = 1;
      if (k <= i)
        break;
    }
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, this.iv.length);
    System.arraycopy(arrayOfByte1, k * 8, arrayOfByte2, this.iv.length, 8);
    this.engine.processBlock(arrayOfByte2, 0, arrayOfByte2, 0);
    int m = k + i * j;
    for (int n = 1; ; n++)
    {
      if (m == 0)
      {
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 8);
        System.arraycopy(arrayOfByte2, 8, arrayOfByte1, k * 8, 8);
        k++;
        break;
      }
      int i1 = (byte)m;
      int i2 = this.iv.length - n;
      arrayOfByte2[i2] = ((byte)(i1 ^ arrayOfByte2[i2]));
      m >>>= 8;
    }
  }
}