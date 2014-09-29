package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.BlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;

public class NullEngine
  implements BlockCipher
{
  protected static final int BLOCK_SIZE = 1;
  private boolean initialised;

  public String getAlgorithmName()
  {
    return "Null";
  }

  public int getBlockSize()
  {
    return 1;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
    throws IllegalArgumentException
  {
    this.initialised = true;
  }

  public int processBlock(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
    throws DataLengthException, IllegalStateException
  {
    if (!this.initialised)
      throw new IllegalStateException("Null engine not initialised");
    if (paramInt1 + 1 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt2 + 1 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    for (int i = 0; ; i++)
    {
      if (i >= 1)
        return 1;
      paramArrayOfByte2[(paramInt2 + i)] = paramArrayOfByte1[(paramInt1 + i)];
    }
  }

  public void reset()
  {
  }
}