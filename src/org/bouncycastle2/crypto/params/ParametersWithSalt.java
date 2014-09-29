package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class ParametersWithSalt
  implements CipherParameters
{
  private CipherParameters parameters;
  private byte[] salt;

  public ParametersWithSalt(CipherParameters paramCipherParameters, byte[] paramArrayOfByte)
  {
    this(paramCipherParameters, paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public ParametersWithSalt(CipherParameters paramCipherParameters, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.salt = new byte[paramInt2];
    this.parameters = paramCipherParameters;
    System.arraycopy(paramArrayOfByte, paramInt1, this.salt, 0, paramInt2);
  }

  public CipherParameters getParameters()
  {
    return this.parameters;
  }

  public byte[] getSalt()
  {
    return this.salt;
  }
}