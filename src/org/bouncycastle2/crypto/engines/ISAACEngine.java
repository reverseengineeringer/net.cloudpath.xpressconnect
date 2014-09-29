package org.bouncycastle2.crypto.engines;

import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.StreamCipher;
import org.bouncycastle2.crypto.params.KeyParameter;

public class ISAACEngine
  implements StreamCipher
{
  private int a = 0;
  private int b = 0;
  private int c = 0;
  private int[] engineState = null;
  private int index = 0;
  private boolean initialised = false;
  private byte[] keyStream = new byte[1024];
  private int[] results = null;
  private final int sizeL = 8;
  private final int stateArraySize = 256;
  private byte[] workingKey = null;

  private int byteToIntLittle(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    int j = 0xFF & paramArrayOfByte[paramInt];
    int k = i + 1;
    int m = j | (0xFF & paramArrayOfByte[i]) << 8;
    int n = k + 1;
    int i1 = m | (0xFF & paramArrayOfByte[k]) << 16;
    (n + 1);
    return i1 | paramArrayOfByte[n] << 24;
  }

  private byte[] intToByteLittle(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[3] = ((byte)paramInt);
    arrayOfByte[2] = ((byte)(paramInt >>> 8));
    arrayOfByte[1] = ((byte)(paramInt >>> 16));
    arrayOfByte[0] = ((byte)(paramInt >>> 24));
    return arrayOfByte;
  }

  private byte[] intToByteLittle(int[] paramArrayOfInt)
  {
    byte[] arrayOfByte = new byte[4 * paramArrayOfInt.length];
    int i = 0;
    for (int j = 0; ; j += 4)
    {
      if (i >= paramArrayOfInt.length)
        return arrayOfByte;
      System.arraycopy(intToByteLittle(paramArrayOfInt[i]), 0, arrayOfByte, j, 4);
      i++;
    }
  }

  private void isaac()
  {
    int i = this.b;
    int j = 1 + this.c;
    this.c = j;
    this.b = (i + j);
    int k = 0;
    if (k >= 256)
      return;
    int m = this.engineState[k];
    switch (k & 0x3)
    {
    default:
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      this.a += this.engineState[(0xFF & k + 128)];
      int[] arrayOfInt1 = this.engineState;
      int n = this.engineState[(0xFF & m >>> 2)] + this.a + this.b;
      arrayOfInt1[k] = n;
      int[] arrayOfInt2 = this.results;
      int i1 = m + this.engineState[(0xFF & n >>> 10)];
      this.b = i1;
      arrayOfInt2[k] = i1;
      k++;
      break;
      this.a ^= this.a << 13;
      continue;
      this.a ^= this.a >>> 6;
      continue;
      this.a ^= this.a << 2;
      continue;
      this.a ^= this.a >>> 16;
    }
  }

  private void mix(int[] paramArrayOfInt)
  {
    paramArrayOfInt[0] ^= paramArrayOfInt[1] << 11;
    paramArrayOfInt[3] += paramArrayOfInt[0];
    paramArrayOfInt[1] += paramArrayOfInt[2];
    paramArrayOfInt[1] ^= paramArrayOfInt[2] >>> 2;
    paramArrayOfInt[4] += paramArrayOfInt[1];
    paramArrayOfInt[2] += paramArrayOfInt[3];
    paramArrayOfInt[2] ^= paramArrayOfInt[3] << 8;
    paramArrayOfInt[5] += paramArrayOfInt[2];
    paramArrayOfInt[3] += paramArrayOfInt[4];
    paramArrayOfInt[3] ^= paramArrayOfInt[4] >>> 16;
    paramArrayOfInt[6] += paramArrayOfInt[3];
    paramArrayOfInt[4] += paramArrayOfInt[5];
    paramArrayOfInt[4] ^= paramArrayOfInt[5] << 10;
    paramArrayOfInt[7] += paramArrayOfInt[4];
    paramArrayOfInt[5] += paramArrayOfInt[6];
    paramArrayOfInt[5] ^= paramArrayOfInt[6] >>> 4;
    paramArrayOfInt[0] += paramArrayOfInt[5];
    paramArrayOfInt[6] += paramArrayOfInt[7];
    paramArrayOfInt[6] ^= paramArrayOfInt[7] << 8;
    paramArrayOfInt[1] += paramArrayOfInt[6];
    paramArrayOfInt[7] += paramArrayOfInt[0];
    paramArrayOfInt[7] ^= paramArrayOfInt[0] >>> 9;
    paramArrayOfInt[2] += paramArrayOfInt[7];
    paramArrayOfInt[0] += paramArrayOfInt[1];
  }

  private void setKey(byte[] paramArrayOfByte)
  {
    this.workingKey = paramArrayOfByte;
    if (this.engineState == null)
      this.engineState = new int[256];
    if (this.results == null)
      this.results = new int[256];
    int i = 0;
    byte[] arrayOfByte;
    int j;
    label90: int[] arrayOfInt2;
    int k;
    label107: int m;
    if (i >= 256)
    {
      this.c = 0;
      this.b = 0;
      this.a = 0;
      this.index = 0;
      arrayOfByte = new byte[paramArrayOfByte.length + (0x3 & paramArrayOfByte.length)];
      System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
      j = 0;
      if (j < arrayOfByte.length)
        break label164;
      arrayOfInt2 = new int[8];
      k = 0;
      if (k < 8)
        break label187;
      m = 0;
      label117: if (m < 4)
        break label200;
    }
    label164: label187: label200: int i1;
    for (int n = 0; ; n++)
    {
      if (n >= 2)
      {
        isaac();
        this.initialised = true;
        return;
        int[] arrayOfInt1 = this.engineState;
        this.results[i] = 0;
        arrayOfInt1[i] = 0;
        i++;
        break;
        this.results[(j >> 2)] = byteToIntLittle(arrayOfByte, j);
        j += 4;
        break label90;
        arrayOfInt2[k] = -1640531527;
        k++;
        break label107;
        mix(arrayOfInt2);
        m++;
        break label117;
      }
      i1 = 0;
      if (i1 < 256)
        break label229;
    }
    label229: int i2 = 0;
    if (i2 >= 8)
      mix(arrayOfInt2);
    for (int i5 = 0; ; i5++)
    {
      if (i5 >= 8)
      {
        i1 += 8;
        break;
        int i3 = arrayOfInt2[i2];
        if (n < 1);
        for (int i4 = this.results[(i1 + i2)]; ; i4 = this.engineState[(i1 + i2)])
        {
          arrayOfInt2[i2] = (i4 + i3);
          i2++;
          break;
        }
      }
      this.engineState[(i1 + i5)] = arrayOfInt2[i5];
    }
  }

  public String getAlgorithmName()
  {
    return "ISAAC";
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (!(paramCipherParameters instanceof KeyParameter))
      throw new IllegalArgumentException("invalid parameter passed to ISAAC init - " + paramCipherParameters.getClass().getName());
    setKey(((KeyParameter)paramCipherParameters).getKey());
  }

  public void processBytes(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3)
  {
    if (!this.initialised)
      throw new IllegalStateException(getAlgorithmName() + " not initialised");
    if (paramInt1 + paramInt2 > paramArrayOfByte1.length)
      throw new DataLengthException("input buffer too short");
    if (paramInt3 + paramInt2 > paramArrayOfByte2.length)
      throw new DataLengthException("output buffer too short");
    for (int i = 0; ; i++)
    {
      if (i >= paramInt2)
        return;
      if (this.index == 0)
      {
        isaac();
        this.keyStream = intToByteLittle(this.results);
      }
      paramArrayOfByte2[(i + paramInt3)] = ((byte)(this.keyStream[this.index] ^ paramArrayOfByte1[(i + paramInt1)]));
      this.index = (0x3FF & 1 + this.index);
    }
  }

  public void reset()
  {
    setKey(this.workingKey);
  }

  public byte returnByte(byte paramByte)
  {
    if (this.index == 0)
    {
      isaac();
      this.keyStream = intToByteLittle(this.results);
    }
    byte b1 = (byte)(paramByte ^ this.keyStream[this.index]);
    this.index = (0x3FF & 1 + this.index);
    return b1;
  }
}