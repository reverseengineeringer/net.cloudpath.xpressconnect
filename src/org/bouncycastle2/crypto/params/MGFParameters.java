package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.DerivationParameters;

public class MGFParameters
  implements DerivationParameters
{
  byte[] seed;

  public MGFParameters(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public MGFParameters(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.seed = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, this.seed, 0, paramInt2);
  }

  public byte[] getSeed()
  {
    return this.seed;
  }
}