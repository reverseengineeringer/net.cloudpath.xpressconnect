package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.ParametersWithIV;

public class OFBBlockCipher
  implements BlockCipher
{
  private byte[] IV;
  private final int blockSize;
  private final BlockCipher cipher;
  private byte[] ofbOutV;
  private byte[] ofbV;

  public OFBBlockCipher(BlockCipher paramBlockCipher, int paramInt)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = (paramInt / 8);
    this.IV = new byte[paramBlockCipher.getBlockSize()];
    this.ofbV = new byte[paramBlockCipher.getBlockSize()];
    this.ofbOutV = new byte[paramBlockCipher.getBlockSize()];
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/OFB" + 8 * this.blockSize;
  }

  public int getBlockSize()
  {
    return this.blockSize;
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
      byte[] arrayOfByte = localParametersWithIV.getIV();
      int i;
      if (arrayOfByte.length < this.IV.length)
      {
        System.arraycopy(arrayOfByte, 0, this.IV, this.IV.length - arrayOfByte.length, arrayOfByte.length);
        i = 0;
        if (i < this.IV.length - arrayOfByte.length);
      }
      while (true)
      {
        reset();
        this.cipher.init(true, localParametersWithIV.getParameters());
        return;
        this.IV[i] = 0;
        i++;
        break;
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
    this.cipher.processBlock(this.ofbV, 0, this.ofbOutV, 0);
    for (int i = 0; ; i++)
    {
      if (i >= this.blockSize)
      {
        System.arraycopy(this.ofbV, this.blockSize, this.ofbV, 0, this.ofbV.length - this.blockSize);
        System.arraycopy(this.ofbOutV, 0, this.ofbV, this.ofbV.length - this.blockSize, this.blockSize);
        return this.blockSize;
      }
      paramArrayOfByte2[(paramInt2 + i)] = ((byte)(this.ofbOutV[i] ^ paramArrayOfByte1[(paramInt1 + i)]));
    }
  }

  public void reset()
  {
    System.arraycopy(this.IV, 0, this.ofbV, 0, this.IV.length);
    this.cipher.reset();
  }
}