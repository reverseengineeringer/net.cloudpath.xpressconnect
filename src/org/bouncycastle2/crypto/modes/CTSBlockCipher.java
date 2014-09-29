package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.BufferedBlockCipher;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;

public class CTSBlockCipher extends BufferedBlockCipher
{
  private int blockSize;

  public CTSBlockCipher(BlockCipher paramBlockCipher)
  {
    if (((paramBlockCipher instanceof OFBBlockCipher)) || ((paramBlockCipher instanceof CFBBlockCipher)))
      throw new IllegalArgumentException("CTSBlockCipher can only accept ECB, or CBC ciphers");
    this.cipher = paramBlockCipher;
    this.blockSize = paramBlockCipher.getBlockSize();
    this.buf = new byte[2 * this.blockSize];
    this.bufOff = 0;
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException, InvalidCipherTextException
  {
    if (paramInt + this.bufOff > paramArrayOfByte.length)
      throw new DataLengthException("output buffer to small in doFinal");
    int i = this.cipher.getBlockSize();
    int j = this.bufOff - i;
    byte[] arrayOfByte1 = new byte[i];
    if (this.forEncryption)
    {
      this.cipher.processBlock(this.buf, 0, arrayOfByte1, 0);
      if (this.bufOff < i)
        throw new DataLengthException("need at least one block of input for CTS");
      int n = this.bufOff;
      int i1;
      if (n == this.buf.length)
      {
        i1 = i;
        label106: if (i1 != this.bufOff)
          break label193;
        if (!(this.cipher instanceof CBCBlockCipher))
          break label224;
        ((CBCBlockCipher)this.cipher).getUnderlyingCipher().processBlock(this.buf, i, paramArrayOfByte, paramInt);
      }
      while (true)
      {
        System.arraycopy(arrayOfByte1, 0, paramArrayOfByte, paramInt + i, j);
        int m = this.bufOff;
        reset();
        return m;
        this.buf[n] = arrayOfByte1[(n - i)];
        n++;
        break;
        label193: byte[] arrayOfByte3 = this.buf;
        arrayOfByte3[i1] = ((byte)(arrayOfByte3[i1] ^ arrayOfByte1[(i1 - i)]));
        i1++;
        break label106;
        label224: this.cipher.processBlock(this.buf, i, paramArrayOfByte, paramInt);
      }
    }
    byte[] arrayOfByte2 = new byte[i];
    if ((this.cipher instanceof CBCBlockCipher))
      ((CBCBlockCipher)this.cipher).getUnderlyingCipher().processBlock(this.buf, 0, arrayOfByte1, 0);
    label283: for (int k = i; ; k++)
    {
      if (k == this.bufOff)
      {
        System.arraycopy(this.buf, i, arrayOfByte1, 0, j);
        this.cipher.processBlock(arrayOfByte1, 0, paramArrayOfByte, paramInt);
        System.arraycopy(arrayOfByte2, 0, paramArrayOfByte, paramInt + i, j);
        break;
        this.cipher.processBlock(this.buf, 0, arrayOfByte1, 0);
        break label283;
      }
      arrayOfByte2[(k - i)] = ((byte)(arrayOfByte1[(k - i)] ^ this.buf[k]));
    }
  }

  public int getOutputSize(int paramInt)
  {
    return paramInt + this.bufOff;
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
      System.arraycopy(this.buf, this.blockSize, this.buf, 0, this.blockSize);
      this.bufOff = this.blockSize;
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
      System.arraycopy(this.buf, i, this.buf, 0, i);
      this.bufOff = i;
      paramInt2 -= k;
      paramInt1 += k;
    }
    while (true)
    {
      if (paramInt2 <= i)
      {
        System.arraycopy(paramArrayOfByte1, paramInt1, this.buf, this.bufOff, paramInt2);
        this.bufOff = (paramInt2 + this.bufOff);
        return m;
      }
      System.arraycopy(paramArrayOfByte1, paramInt1, this.buf, this.bufOff, i);
      m += this.cipher.processBlock(this.buf, 0, paramArrayOfByte2, paramInt3 + m);
      System.arraycopy(this.buf, i, this.buf, 0, i);
      paramInt2 -= i;
      paramInt1 += i;
    }
  }
}