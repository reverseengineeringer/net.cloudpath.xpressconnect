package org.bouncycastle2.crypto;

public class BufferedAsymmetricBlockCipher
{
  protected byte[] buf;
  protected int bufOff;
  private final AsymmetricBlockCipher cipher;

  public BufferedAsymmetricBlockCipher(AsymmetricBlockCipher paramAsymmetricBlockCipher)
  {
    this.cipher = paramAsymmetricBlockCipher;
  }

  public byte[] doFinal()
    throws InvalidCipherTextException
  {
    byte[] arrayOfByte = this.cipher.processBlock(this.buf, 0, this.bufOff);
    reset();
    return arrayOfByte;
  }

  public int getBufferPosition()
  {
    return this.bufOff;
  }

  public int getInputBlockSize()
  {
    return this.cipher.getInputBlockSize();
  }

  public int getOutputBlockSize()
  {
    return this.cipher.getOutputBlockSize();
  }

  public AsymmetricBlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    reset();
    this.cipher.init(paramBoolean, paramCipherParameters);
    int i = this.cipher.getInputBlockSize();
    if (paramBoolean);
    for (int j = 1; ; j = 0)
    {
      this.buf = new byte[j + i];
      this.bufOff = 0;
      return;
    }
  }

  public void processByte(byte paramByte)
  {
    if (this.bufOff >= this.buf.length)
      throw new DataLengthException("attempt to process message too long for cipher");
    byte[] arrayOfByte = this.buf;
    int i = this.bufOff;
    this.bufOff = (i + 1);
    arrayOfByte[i] = paramByte;
  }

  public void processBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0)
      return;
    if (paramInt2 < 0)
      throw new IllegalArgumentException("Can't have a negative input length!");
    if (paramInt2 + this.bufOff > this.buf.length)
      throw new DataLengthException("attempt to process message too long for cipher");
    System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.bufOff, paramInt2);
    this.bufOff = (paramInt2 + this.bufOff);
  }

  public void reset()
  {
    if (this.buf != null);
    for (int i = 0; ; i++)
    {
      if (i >= this.buf.length)
      {
        this.bufOff = 0;
        return;
      }
      this.buf[i] = 0;
    }
  }
}