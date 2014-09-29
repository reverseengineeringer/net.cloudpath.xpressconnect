package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class SICBlockCipher
  implements BlockCipher
{
  private byte[] IV;
  private final int blockSize;
  private final BlockCipher cipher;
  private byte[] counter;
  private byte[] counterOut;

  public SICBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = this.cipher.getBlockSize();
    this.IV = new byte[this.blockSize];
    this.counter = new byte[this.blockSize];
    this.counterOut = new byte[this.blockSize];
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/SIC";
  }

  public int getBlockSize()
  {
    return this.cipher.getBlockSize();
  }

  public BlockCipher getUnderlyingCipher()
  {
    return this.cipher;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    if ((paramCipherParameters instanceof ParametersWithIV))
    {
      ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
      System.arraycopy(localParametersWithIV.getIV(), 0, this.IV, 0, this.IV.length);
      reset();
      this.cipher.init(true, localParametersWithIV.getParameters());
      return;
    }
    throw new IllegalArgumentException("SIC mode requires ParametersWithIV");
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
    int k;
    for (int i = 0; ; i++)
    {
      if (i >= this.counterOut.length)
      {
        j = 1;
        k = -1 + this.counter.length;
        if (k >= 0)
          break;
        return this.counter.length;
      }
      paramArrayOfByte2[(paramInt2 + i)] = ((byte)(this.counterOut[i] ^ paramArrayOfByte1[(paramInt1 + i)]));
    }
    int m = j + (0xFF & this.counter[k]);
    if (m > 255);
    for (int j = 1; ; j = 0)
    {
      this.counter[k] = ((byte)m);
      k--;
      break;
    }
  }

  public void reset()
  {
    System.arraycopy(this.IV, 0, this.counter, 0, this.counter.length);
    this.cipher.reset();
  }
}