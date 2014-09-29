package org.bouncycastle2.jce.spec;

import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.crypto.engines.GOST28147Engine;

public class GOST28147ParameterSpec
  implements AlgorithmParameterSpec
{
  private byte[] iv = null;
  private byte[] sBox = null;

  public GOST28147ParameterSpec(String paramString)
  {
    this.sBox = GOST28147Engine.getSBox(paramString);
  }

  public GOST28147ParameterSpec(String paramString, byte[] paramArrayOfByte)
  {
    this(paramString);
    this.iv = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, this.iv, 0, paramArrayOfByte.length);
  }

  public GOST28147ParameterSpec(byte[] paramArrayOfByte)
  {
    this.sBox = new byte[paramArrayOfByte.length];
    System.arraycopy(paramArrayOfByte, 0, this.sBox, 0, paramArrayOfByte.length);
  }

  public GOST28147ParameterSpec(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this(paramArrayOfByte1);
    this.iv = new byte[paramArrayOfByte2.length];
    System.arraycopy(paramArrayOfByte2, 0, this.iv, 0, paramArrayOfByte2.length);
  }

  public byte[] getIV()
  {
    if (this.iv == null)
      return null;
    byte[] arrayOfByte = new byte[this.iv.length];
    System.arraycopy(this.iv, 0, arrayOfByte, 0, arrayOfByte.length);
    return arrayOfByte;
  }

  public byte[] getSbox()
  {
    return this.sBox;
  }
}