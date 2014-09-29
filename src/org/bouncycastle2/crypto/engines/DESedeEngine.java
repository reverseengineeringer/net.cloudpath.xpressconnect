package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.KeyParameter;

public class DESedeEngine extends DESEngine
{
  protected static final int BLOCK_SIZE = 8;
  private boolean forEncryption;
  private int[] workingKey1 = null;
  private int[] workingKey2 = null;
  private int[] workingKey3 = null;

  public String getAlgorithmName()
  {
    return "DESede";
  }

  public int getBlockSize()
  {
    return 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to DESede init - " + paramCipherParameters.getClass().getName());
    byte[] arrayOfByte1 = ((KeyParameter)paramCipherParameters).getKey();
    if (arrayOfByte1.length > 24)
      throw new IllegalArgumentException("key size greater than 24 bytes");
    this.forEncryption = paramBoolean;
    byte[] arrayOfByte2 = new byte[8];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
    this.workingKey1 = generateWorkingKey(paramBoolean, arrayOfByte2);
    byte[] arrayOfByte3 = new byte[8];
    System.arraycopy(arrayOfByte1, 8, arrayOfByte3, 0, arrayOfByte3.length);
    if (paramBoolean);
    for (boolean bool = false; ; bool = true)
    {
      this.workingKey2 = generateWorkingKey(bool, arrayOfByte3);
      if (arrayOfByte1.length != 24)
        break;
      byte[] arrayOfByte4 = new byte[8];
      System.arraycopy(arrayOfByte1, 16, arrayOfByte4, 0, arrayOfByte4.length);
      this.workingKey3 = generateWorkingKey(paramBoolean, arrayOfByte4);
      return;
    }
    this.workingKey3 = this.workingKey1;
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
  {
    if (this.workingKey1 == null)
      throw new IllegalStateException("DESede engine not initialised");
    if (paramInt1 + 8 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 8 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    byte[] arrayOfByte = new byte[8];
    if (this.forEncryption)
    {
      desFunc(this.workingKey1, paramArrayOfByte1, paramInt1, arrayOfByte, 0);
      desFunc(this.workingKey2, arrayOfByte, 0, arrayOfByte, 0);
      desFunc(this.workingKey3, arrayOfByte, 0, paramArrayOfByte2, paramInt2);
      return 8;
    }
    desFunc(this.workingKey3, paramArrayOfByte1, paramInt1, arrayOfByte, 0);
    desFunc(this.workingKey2, arrayOfByte, 0, arrayOfByte, 0);
    desFunc(this.workingKey1, arrayOfByte, 0, paramArrayOfByte2, paramInt2);
    return 8;
  }

  public void reset()
  {
  }
}