package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class PaddedBlockCipher extends BufferedBlockCipher
{
  public PaddedBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.buf = new byte[paramBlockCipher.getBlockSize()];
    this.bufOff = 0;
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException, InvalidCipherTextException
  {
    int i = this.cipher.getBlockSize();
    int i2;
    int m;
    if (this.forEncryption)
    {
      int n = this.bufOff;
      int i1 = 0;
      if (n == i)
      {
        if (paramInt + i * 2 > paramArrayOfByte.length)
          throw new DataLengthException("output buffer too short");
        i1 = this.cipher.processBlock(this.buf, 0, paramArrayOfByte, paramInt);
        this.bufOff = 0;
      }
      i2 = (byte)(i - this.bufOff);
      if (this.bufOff >= i)
        m = i1 + this.cipher.processBlock(this.buf, 0, paramArrayOfByte, paramInt + i1);
    }
    while (true)
    {
      reset();
      return m;
      this.buf[this.bufOff] = i2;
      this.bufOff = (1 + this.bufOff);
      break;
      int j;
      int k;
      if (this.bufOff == i)
      {
        j = this.cipher.processBlock(this.buf, 0, this.buf, 0);
        this.bufOff = 0;
        k = 0xFF & this.buf[(i - 1)];
        if ((k < 0) || (k > i))
          throw new InvalidCipherTextException("pad block corrupted");
      }
      else
      {
        throw new DataLengthException("last block incomplete in decryption");
      }
      m = j - k;
      System.arraycopy(this.buf, 0, paramArrayOfByte, paramInt, m);
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