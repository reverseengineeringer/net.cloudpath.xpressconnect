package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.DerivationParameters;

public class ISO18033KDFParameters
  implements DerivationParameters
{
  byte[] seed;

  public ISO18033KDFParameters(byte[] paramArrayOfByte)
  {
    this.seed = paramArrayOfByte;
  }

  public byte[] getSeed()
  {
    return this.seed;
  }
}