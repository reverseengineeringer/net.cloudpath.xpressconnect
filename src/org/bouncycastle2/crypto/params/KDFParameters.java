package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.DerivationParameters;

public class KDFParameters
  implements DerivationParameters
{
  byte[] iv;
  byte[] shared;

  public KDFParameters(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.shared = paramArrayOfByte1;
    this.iv = paramArrayOfByte2;
  }

  public byte[] getIV()
  {
    return this.iv;
  }

  public byte[] getSharedSecret()
  {
    return this.shared;
  }
}