package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class ParametersWithSBox
  implements CipherParameters
{
  private CipherParameters parameters;
  private byte[] sBox;

  public ParametersWithSBox(CipherParameters paramCipherParameters, byte[] paramArrayOfByte)
  {
    this.parameters = paramCipherParameters;
    this.sBox = paramArrayOfByte;
  }

  public CipherParameters getParameters()
  {
    return this.parameters;
  }

  public byte[] getSBox()
  {
    return this.sBox;
  }
}