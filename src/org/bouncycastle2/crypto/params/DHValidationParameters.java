package org.bouncycastle2.crypto.params;

import org.bouncycastle2.util.Arrays;

public class DHValidationParameters
{
  private int counter;
  private byte[] seed;

  public DHValidationParameters(byte[] paramArrayOfByte, int paramInt)
  {
    this.seed = paramArrayOfByte;
    this.counter = paramInt;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof DHValidationParameters));
    DHValidationParameters localDHValidationParameters;
    do
    {
      return false;
      localDHValidationParameters = (DHValidationParameters)paramObject;
    }
    while (localDHValidationParameters.counter != this.counter);
    return Arrays.areEqual(this.seed, localDHValidationParameters.seed);
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