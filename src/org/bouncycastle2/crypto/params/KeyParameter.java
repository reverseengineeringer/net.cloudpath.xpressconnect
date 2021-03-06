package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class KeyParameter
  implements CipherParameters
{
  private byte[] key;

  public KeyParameter(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public KeyParameter(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.key = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, this.key, 0, paramInt2);
  }

  public byte[] getKey()
  {
    return this.key;
  }
}