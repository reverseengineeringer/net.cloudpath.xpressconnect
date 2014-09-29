package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class IESParameters
  implements CipherParameters
{
  private byte[] derivation;
  private byte[] encoding;
  private int macKeySize;

  public IESParameters(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    this.derivation = paramArrayOfByte1;
    this.encoding = paramArrayOfByte2;
    this.macKeySize = paramInt;
  }

  public byte[] getDerivationV()
  {
    return this.derivation;
  }

  public byte[] getEncodingV()
  {
    return this.encoding;
  }

  public int getMacKeySize()
  {
    return this.macKeySize;
  }
}