package org.bouncycastle2.crypto.modes.gcm;

import org.bouncycastle2.util.Arrays;

public class BasicGCMMultiplier
  implements GCMMultiplier
{
  private byte[] H;

  public void init(byte[] paramArrayOfByte)
  {
    this.H = Arrays.clone(paramArrayOfByte);
  }

  public void multiplyH(byte[] paramArrayOfByte)
  {
    GCMUtil.multiply(paramArrayOfByte, this.H);
  }
}