package org.bouncycastle2.crypto.macs;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.ParametersWithIV;

class MacCFBBlockCipher
{
  private byte[] IV;
  private int blockSize;
  private byte[] cfbOutV;
  private byte[] cfbV;
  private BlockCipher cipher = null;

  public MacCFBBlockCipher(BlockCipher paramBlockCipher, int paramInt)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = (paramInt / 8);
    this.IV = new byte[paramBlockCipher.getBlockSize()];
    this.cfbV = new byte[paramBlockCipher.getBlockSize()];
    this.cfbOutV = new byte[paramBlockCipher.getBlockSize()];
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/CFB" + 8 * this.blockSize;
  }

  public int getBlockSize()
  {
    return this.blockSize;
  }

  void getMacBlock(byte[] paramArrayOfByte)
  {
    this.cipher.processBlock(this.cfbV, 0, paramArrayOfByte, 0);
  }

  public void init(CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    if ((paramCipherParameters instanceof ParametersWithIV))
    {
      ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
      byte[] arrayOfByte = localParametersWithIV.getIV();
      if (arrayOfByte.length < this.IV.length)
        System.arraycopy(arrayOfByte, 0, this.IV, this.IV.length - arrayOfByte.length, arrayOfByte.length);
      while (true)
      {
        reset();
        this.cipher.init(true, localParametersWithIV.getParameters());
        return;
        System.arraycopy(arrayOfByte, 0, this.IV, 0, this.IV.length);
      }
    }
    reset();
    this.cipher.init(true, paramCipherParameters);
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt1 + this.blockSize > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + this.blockSize > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    this.cipher.processBlock(this.cfbV, 0, this.cfbOutV, 0);
    for (int i = 0; ; i++)
    {
      if (i >= this.blockSize)
      {
        System.arraycopy(this.cfbV, this.blockSize, this.cfbV, 0, this.cfbV.length - this.blockSize);
        System.arraycopy(paramArrayOfByte2, paramInt2, this.cfbV, this.cfbV.length - this.blockSize, this.blockSize);
        return this.blockSize;
      }
      paramArrayOfByte2[(paramInt2 + i)] = ((byte)(this.cfbOutV[i] ^ paramArrayOfByte1[(paramInt1 + i)]));
    }
  }

  public void reset()
  {
    System.arraycopy(this.IV, 0, this.cfbV, 0, this.IV.length);
    this.cipher.reset();
  }
}