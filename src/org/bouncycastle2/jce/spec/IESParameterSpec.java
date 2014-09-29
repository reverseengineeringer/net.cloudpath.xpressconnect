package org.bouncycastle2.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class IESParameterSpec
  implements AlgorithmParameterSpec
{
  private byte[] derivation;
  private byte[] encoding;
  private int macKeySize;

  public IESParameterSpec(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
  {
    this.derivation = new byte[paramArrayOfByte1.length];
    System.arraycopy(paramArrayOfByte1, 0, this.derivation, 0, paramArrayOfByte1.length);
    this.encoding = new byte[paramArrayOfByte2.length];
    System.arraycopy(paramArrayOfByte2, 0, this.encoding, 0, paramArrayOfByte2.length);
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