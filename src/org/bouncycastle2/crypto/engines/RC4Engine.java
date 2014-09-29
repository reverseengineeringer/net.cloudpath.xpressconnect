package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;

public class RC4Engine
  implements StreamCipher
{
  private static final int STATE_LENGTH = 256;
  private byte[] engineState = null;
  private byte[] workingKey = null;
  private int x = 0;
  private int y = 0;

  private void setKey(byte[] paramArrayOfByte)
  {
    this.workingKey = paramArrayOfByte;
    this.x = 0;
    this.y = 0;
    if (this.engineState == null)
      this.engineState = new byte[256];
    int i = 0;
    int j;
    int k;
    if (i >= 256)
    {
      j = 0;
      k = 0;
    }
    for (int m = 0; ; m++)
    {
      if (m >= 256)
      {
        return;
        this.engineState[i] = ((byte)i);
        i++;
        break;
      }
      k = 0xFF & k + ((0xFF & paramArrayOfByte[j]) + this.engineState[m]);
      int n = this.engineState[m];
      this.engineState[m] = this.engineState[k];
      this.engineState[k] = n;
      j = (j + 1) % paramArrayOfByte.length;
    }
  }

  public String getAlgorithmName()
  {
    return "RC4";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if ((paramCipherParameters instanceof KeyParameter))
    {
      this.workingKey = ((KeyParameter)paramCipherParameters).getKey();
      setKey(this.workingKey);
      return;
    }
    throw new IllegalArgumentException("invalid parameter passed to RC4 init - " + paramCipherParameters.getClass().getName());
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    for (int i = 0; ; i++)
    {
      if (i >= paramInt2)
        return;
      this.x = (0xFF & 1 + this.x);
      this.y = (0xFF & this.engineState[this.x] + this.y);
      int j = this.engineState[this.x];
      this.engineState[this.x] = this.engineState[this.y];
      this.engineState[this.y] = j;
      paramArrayOfByte2[(i + paramInt3)] = ((byte)(paramArrayOfByte1[(i + paramInt1)] ^ this.engineState[(0xFF & this.engineState[this.x] + this.engineState[this.y])]));
    }
  }

  public void reset()
  {
    setKey(this.workingKey);
  }

  public byte returnByte(byte paramByte)
  {
    this.x = (0xFF & 1 + this.x);
    this.y = (0xFF & this.engineState[this.x] + this.y);
    int i = this.engineState[this.x];
    this.engineState[this.x] = this.engineState[this.y];
    this.engineState[this.y] = i;
    return (byte)(paramByte ^ this.engineState[(0xFF & this.engineState[this.x] + this.engineState[this.y])]);
  }
}