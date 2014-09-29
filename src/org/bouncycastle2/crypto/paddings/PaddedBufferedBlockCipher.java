package org.bouncycastle2.crypto.paddings;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class PaddedBufferedBlockCipher extends BufferedBlockCipher
{
  BlockCipherPadding padding;

  public PaddedBufferedBlockCipher(BlockCipher paramBlockCipher)
  {
    this(paramBlockCipher, new PKCS7Padding());
  }

  public PaddedBufferedBlockCipher(BlockCipher paramBlockCipher, BlockCipherPadding paramBlockCipherPadding)
  {
    this.cipher = paramBlockCipher;
    this.padding = paramBlockCipherPadding;
    this.buf = new byte[paramBlockCipher.getBlockSize()];
    this.bufOff = 0;
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException, InvalidCipherTextException
  {
    int i = this.cipher.getBlockSize();
    if (this.forEncryption)
    {
      int m = this.bufOff;
      int n = 0;
      if (m == i)
      {
        if (paramInt + i * 2 > paramArrayOfByte.length)
        {
          reset();
          throw new DataLengthException("output buffer too short");
        }
        n = this.cipher.processBlock(this.buf, 0, paramArrayOfByte, paramInt);
        this.bufOff = 0;
      }
      this.padding.addPadding(this.buf, this.bufOff);
      int i1 = n + this.cipher.processBlock(this.buf, 0, paramArrayOfByte, paramInt + n);
      reset();
      return i1;
    }
    int j;
    if (this.bufOff == i)
    {
      j = this.cipher.processBlock(this.buf, 0, this.buf, 0);
      this.bufOff = 0;
    }
    try
    {
      int k = j - this.padding.padCount(this.buf);
      System.arraycopy(this.buf, 0, paramArrayOfByte, paramInt, k);
      reset();
      return k;
      reset();
      throw new DataLengthException("last block incomplete in decryption");
    }
    finally
    {
      reset();
    }
  }

  public int getOutputSize(int paramInt)
  {
    int i = paramInt + this.bufOff;
    int j = i % this.buf.length;
    if (j == 0)
    {
      if (this.forEncryption)
        i += this.buf.length;
      return i;
    }
    return i - j + this.buf.length;
  }

  public int getUpdateOutputSize(int paramInt)
  {
    int i = paramInt + this.bufOff;
    int j = i % this.buf.length;
    if (j == 0)
      return i - this.buf.length;
    return i - j;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.forEncryption = paramBoolean;
    reset();
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      ParametersWithRandom localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.padding.init(localParametersWithRandom.getRandom());
      this.cipher.init(paramBoolean, localParametersWithRandom.getParameters());
      return;
    }
    this.padding.init(null);
    this.cipher.init(paramBoolean, paramCipherParameters);
  }

  public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException
  {
    int i = this.bufOff;
    int j = this.buf.length;
    int k = 0;
    if (i == j)
    {
      k = this.cipher.processBlock(this.buf, 0, paramArrayOfByte, paramInt);
      this.bufOff = 0;
    }
    byte[] arrayOfByte = this.buf;
    int m = this.bufOff;
    this.bufOff = (m + 1);
    arrayOfByte[m] = paramByte;
    return k;
  }

  public int processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt2 < 0)
      throw new IllegalArgumentException("Can't have a negative input length!");
    int i = getBlockSize();
    int j = getUpdateOutputSize(paramInt2);
    if ((j > 0) && (paramInt3 + j > paramArrayOfByte2.length))
      throw new DataLengthException("output buffer too short");
    int k = this.buf.length - this.bufOff;
    int m = 0;
    if (paramInt2 > k)
    {
      System.arraycopy(paramArrayOfByte1, paramInt1, this.buf, this.bufOff, k);
      m = 0 + this.cipher.processBlock(this.buf, 0, paramArrayOfByte2, paramInt3);
      this.bufOff = 0;
      paramInt2 -= k;
      paramInt1 += k;
    }
    while (true)
    {
      if (paramInt2 <= this.buf.length)
      {
        System.arraycopy(paramArrayOfByte1, paramInt1, this.buf, this.bufOff, paramInt2);
        this.bufOff = (paramInt2 + this.bufOff);
        return m;
      }
      m += this.cipher.processBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt3 + m);
      paramInt2 -= i;
      paramInt1 += i;
    }
  }
}