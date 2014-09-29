package org.bouncycastle2.crypto;

public class BufferedBlockCipher
{
  protected byte[] buf;
  protected int bufOff;
  protected BlockCipher cipher;
  protected boolean forEncryption;
  protected boolean partialBlockOkay;
  protected boolean pgpCFB;

  protected BufferedBlockCipher()
  {
  }

  public BufferedBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.buf = new byte[paramBlockCipher.getBlockSize()];
    this.bufOff = 0;
    String str = paramBlockCipher.getAlgorithmName();
    int i = 1 + str.indexOf('/');
    if ((i > 0) && (str.startsWith("PGP", i)));
    for (boolean bool2 = bool1; ; bool2 = false)
    {
      this.pgpCFB = bool2;
      if (!this.pgpCFB)
        break;
      this.partialBlockOkay = bool1;
      return;
    }
    if ((i > 0) && ((str.startsWith("CFB", i)) || (str.startsWith("OFB", i)) || (str.startsWith("OpenPGP", i)) || (str.startsWith("SIC", i)) || (str.startsWith("GCTR", i))));
    while (true)
    {
      this.partialBlockOkay = bool1;
      return;
      bool1 = false;
    }
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException, InvalidCipherTextException
  {
    try
    {
      if (paramInt + this.bufOff > paramArrayOfByte.length)
        throw new DataLengthException("output buffer too short for doFinal()");
    }
    finally
    {
      reset();
    }
    int i = this.bufOff;
    int j = 0;
    if (i != 0)
    {
      if (!this.partialBlockOkay)
        throw new DataLengthException("data not block size aligned");
      this.cipher.processBlock(this.buf, 0, this.buf, 0);
      j = this.bufOff;
      this.bufOff = 0;
      System.arraycopy(this.buf, 0, paramArrayOfByte, paramInt, j);
    }
    reset();
    return j;
  }

  public int getBlockSize()
  {
    return this.cipher.getBlockSize();
  }

  public int getOutputSize(int paramInt)
  {
    return paramInt + this.bufOff;
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public int getUpdateOutputSize(int paramInt)
  {
    int i = paramInt + this.bufOff;
    if (this.pgpCFB);
    for (int j = i % this.buf.length - (2 + this.cipher.getBlockSize()); ; j = i % this.buf.length)
      return i - j;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.forEncryption = paramBoolean;
    reset();
    this.cipher.init(paramBoolean, paramCipherParameters);
  }

  public int processByte(byte paramByte, byte[] paramArrayOfByte, int paramInt)
    throws DataLengthException, IllegalStateException
  {
    byte[] arrayOfByte = this.buf;
    int i = this.bufOff;
    this.bufOff = (i + 1);
    arrayOfByte[i] = paramByte;
    int j = this.bufOff;
    int k = this.buf.length;
    int m = 0;
    if (j == k)
    {
      m = this.cipher.processBlock(this.buf, 0, paramArrayOfByte, paramInt);
      this.bufOff = 0;
    }
    return m;
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
        if (this.bufOff == this.buf.length)
        {
          m += this.cipher.processBlock(this.buf, 0, paramArrayOfByte2, paramInt3 + m);
          this.bufOff = 0;
        }
        return m;
      }
      m += this.cipher.processBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt3 + m);
      paramInt2 -= i;
      paramInt1 += i;
    }
  }

  public void reset()
  {
    for (int i = 0; ; i++)
    {
      if (i >= this.buf.length)
      {
        this.bufOff = 0;
        this.cipher.reset();
        return;
      }
      this.buf[i] = 0;
    }
  }
}