package org.bouncycastle2.crypto.modes;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.ParametersWithIV;
import org.bouncycastle2.util.Arrays;

public class CBCBlockCipher
  implements BlockCipher
{
  private byte[] IV;
  private int blockSize;
  private byte[] cbcNextV;
  private byte[] cbcV;
  private BlockCipher cipher = null;
  private boolean encrypting;

  public CBCBlockCipher(BlockCipher paramBlockCipher)
  {
    this.cipher = paramBlockCipher;
    this.blockSize = paramBlockCipher.getBlockSize();
    this.IV = new byte[this.blockSize];
    this.cbcV = new byte[this.blockSize];
    this.cbcNextV = new byte[this.blockSize];
  }

  private int decryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt1 + this.blockSize > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    System.arraycopy(paramArrayOfByte1, paramInt1, this.cbcNextV, 0, this.blockSize);
    int i = this.cipher.processBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    for (int j = 0; ; j++)
    {
      if (j >= this.blockSize)
      {
        byte[] arrayOfByte = this.cbcV;
        this.cbcV = this.cbcNextV;
        this.cbcNextV = arrayOfByte;
        return i;
      }
      int k = paramInt2 + j;
      paramArrayOfByte2[k] = ((byte)(paramArrayOfByte2[k] ^ this.cbcV[j]));
    }
  }

  private int encryptBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (paramInt1 + this.blockSize > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    for (int i = 0; ; i++)
    {
      if (i >= this.blockSize)
      {
        int j = this.cipher.processBlock(this.cbcV, 0, paramArrayOfByte2, paramInt2);
        System.arraycopy(paramArrayOfByte2, paramInt2, this.cbcV, 0, this.cbcV.length);
        return j;
      }
      byte[] arrayOfByte = this.cbcV;
      arrayOfByte[i] = ((byte)(arrayOfByte[i] ^ paramArrayOfByte1[(paramInt1 + i)]));
    }
  }

  public String getAlgorithmName()
  {
    return this.cipher.getAlgorithmName() + "/CBC";
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
    this.encrypting = paramBoolean;
    if ((paramCipherParameters instanceof ParametersWithIV))
    {
      ParametersWithIV localParametersWithIV = (ParametersWithIV)paramCipherParameters;
      byte[] arrayOfByte = localParametersWithIV.getIV();
      if (arrayOfByte.length != this.blockSize)
        throw new IllegalArgumentException("initialisation vector must be the same length as block size");
      System.arraycopy(arrayOfByte, 0, this.IV, 0, arrayOfByte.length);
      reset();
      this.cipher.init(paramBoolean, localParametersWithIV.getParameters());
      return;
    }
    reset();
    this.cipher.init(paramBoolean, paramCipherParameters);
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (this.encrypting)
      return encryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
    return decryptBlock(paramArrayOfByte1, paramInt1, paramArrayOfByte2, paramInt2);
  }

  public void reset()
  {
    System.arraycopy(this.IV, 0, this.cbcV, 0, this.IV.length);
    Arrays.fill(this.cbcNextV, (byte)0);
    this.cipher.reset();
  }
}