package org.bouncycastle2.crypto.params;

public class DESedeParameters extends DESParameters
{
  public static final int DES_EDE_KEY_LENGTH = 24;

  public DESedeParameters(byte[] paramArrayOfByte)
  {
    super(paramArrayOfByte);
    if (isWeakKey(paramArrayOfByte, 0, paramArrayOfByte.length))
      throw new IllegalArgumentException("attempt to create weak DESede key");
  }

  public static boolean isWeakKey(byte[] paramArrayOfByte, int paramInt)
  {
    return isWeakKey(paramArrayOfByte, paramInt, paramArrayOfByte.length - paramInt);
  }

  public static boolean isWeakKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    for (int i = paramInt1; ; i += 8)
    {
      if (i >= paramInt2)
        return false;
      if (DESParameters.isWeakKey(paramArrayOfByte, i))
        return true;
    }
  }
}