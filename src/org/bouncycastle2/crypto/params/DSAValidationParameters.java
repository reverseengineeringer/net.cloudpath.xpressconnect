package org.bouncycastle2.crypto.params;

import org.bouncycastle2.util.Arrays;

public class DSAValidationParameters
{
  private int counter;
  private byte[] seed;

  public DSAValidationParameters(byte[] paramArrayOfByte, int paramInt)
  {
    this.seed = paramArrayOfByte;
    this.counter = paramInt;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DSAValidationParameters));
    DSAValidationParameters localDSAValidationParameters;
    do
    {
      return false;
      localDSAValidationParameters = (DSAValidationParameters)paramObject;
    }
    while (localDSAValidationParameters.counter != this.counter);
    return Arrays.areEqual(this.seed, localDSAValidationParameters.seed);
  }

  public int getCounter()
  {
    return this.counter;
  }

  public byte[] getSeed()
  {
    return this.seed;
  }

  public int hashCode()
  {
    return this.counter ^ Arrays.hashCode(this.seed);
  }
}