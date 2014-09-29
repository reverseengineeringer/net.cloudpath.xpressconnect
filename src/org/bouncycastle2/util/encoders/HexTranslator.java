package org.bouncycastle2.util.encoders;

public class HexTranslator
  implements Translator
{
  private static final byte[] hexTable = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };

  public int decode(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    int i = paramInt2 / 2;
    int j = 0;
    if (j >= i)
      return i;
    int k = paramArrayOfByte1[(paramInt1 + j * 2)];
    int m = paramArrayOfByte1[(1 + (paramInt1 + j * 2))];
    if (k < 97)
    {
      paramArrayOfByte2[paramInt3] = ((byte)(k - 48 << 4));
      label60: if (m >= 97)
        break label113;
      paramArrayOfByte2[paramInt3] = ((byte)(paramArrayOfByte2[paramInt3] + (byte)(m - 48)));
    }
    while (true)
    {
      paramInt3++;
      j++;
      break;
      paramArrayOfByte2[paramInt3] = ((byte)(10 + (k - 97) << 4));
      break label60;
      label113: paramArrayOfByte2[paramInt3] = ((byte)(paramArrayOfByte2[paramInt3] + (byte)(10 + (m - 97))));
    }
  }

  public int encode(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    int i = 0;
    for (int j = 0; ; j += 2)
    {
      if (i >= paramInt2)
        return paramInt2 * 2;
      paramArrayOfByte2[(paramInt3 + j)] = hexTable[(0xF & paramArrayOfByte1[paramInt1] >> 4)];
      paramArrayOfByte2[(1 + (paramInt3 + j))] = hexTable[(0xF & paramArrayOfByte1[paramInt1])];
      paramInt1++;
      i++;
    }
  }

  public int getDecodedBlockSize()
  {
    return 1;
  }

  public int getEncodedBlockSize()
  {
    return 2;
  }
}