package org.bouncycastle2.math.ec;

import java.math.BigInteger;
import org.bouncycastle2.util.Arrays;

class IntArray
{
  private int[] m_ints;

  public IntArray(int paramInt)
  {
    this.m_ints = new int[paramInt];
  }

  public IntArray(BigInteger paramBigInteger)
  {
    this(paramBigInteger, 0);
  }

  public IntArray(BigInteger paramBigInteger, int paramInt)
  {
    if (paramBigInteger.signum() == -1)
      throw new IllegalArgumentException("Only positive Integers allowed");
    if (paramBigInteger.equals(ECConstants.ZERO))
    {
      this.m_ints = new int[1];
      return;
    }
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    int i = arrayOfByte.length;
    int j = arrayOfByte[0];
    int k = 0;
    if (j == 0)
    {
      i--;
      k = 1;
    }
    int m = (i + 3) / 4;
    label89: int n;
    int i2;
    int i3;
    label118: int i4;
    label148: int i5;
    if (m < paramInt)
    {
      this.m_ints = new int[paramInt];
      n = m - 1;
      int i1 = k + i % 4;
      i2 = k;
      i3 = 0;
      if (k < i1)
      {
        if (i2 < i1)
          break label199;
        int[] arrayOfInt = this.m_ints;
        int i12 = n - 1;
        arrayOfInt[n] = i3;
        n = i12;
      }
      if (n >= 0)
      {
        i4 = 0;
        i5 = 0;
      }
    }
    label199: int i8;
    for (int i6 = i2; ; i6 = i8)
    {
      if (i5 >= 4)
      {
        this.m_ints[n] = i4;
        n--;
        i2 = i6;
        break label148;
        break;
        this.m_ints = new int[m];
        break label89;
        int i10 = i3 << 8;
        int i11 = arrayOfByte[i2];
        if (i11 < 0)
          i11 += 256;
        i3 = i10 | i11;
        i2++;
        break label118;
      }
      int i7 = i4 << 8;
      i8 = i6 + 1;
      int i9 = arrayOfByte[i6];
      if (i9 < 0)
        i9 += 256;
      i4 = i7 | i9;
      i5++;
    }
  }

  public IntArray(int[] paramArrayOfInt)
  {
    this.m_ints = paramArrayOfInt;
  }

  private int[] resizedInts(int paramInt)
  {
    int[] arrayOfInt = new int[paramInt];
    int i = this.m_ints.length;
    if (i < paramInt);
    for (int j = i; ; j = paramInt)
    {
      System.arraycopy(this.m_ints, 0, arrayOfInt, 0, j);
      return arrayOfInt;
    }
  }

  public void addShifted(IntArray paramIntArray, int paramInt)
  {
    int i = paramIntArray.getUsedLength();
    int j = i + paramInt;
    if (j > this.m_ints.length)
      this.m_ints = resizedInts(j);
    for (int k = 0; ; k++)
    {
      if (k >= i)
        return;
      int[] arrayOfInt = this.m_ints;
      int m = k + paramInt;
      arrayOfInt[m] ^= paramIntArray.m_ints[k];
    }
  }

  public int bitLength()
  {
    int i = getUsedLength();
    int m;
    if (i == 0)
      m = 0;
    while (true)
    {
      return m;
      int j = i - 1;
      int k = this.m_ints[j];
      m = 1 + (j << 5);
      if ((0xFFFF0000 & k) != 0)
        if ((0xFF000000 & k) != 0)
        {
          m += 24;
          k >>>= 24;
        }
      while (k != 1)
      {
        m++;
        k >>>= 1;
        continue;
        m += 16;
        k >>>= 16;
        continue;
        if (k > 255)
        {
          m += 8;
          k >>>= 8;
        }
      }
    }
  }

  public Object clone()
  {
    return new IntArray(Arrays.clone(this.m_ints));
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof IntArray));
    IntArray localIntArray;
    int i;
    do
    {
      return false;
      localIntArray = (IntArray)paramObject;
      i = getUsedLength();
    }
    while (localIntArray.getUsedLength() != i);
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return true;
      if (this.m_ints[j] != localIntArray.m_ints[j])
        break;
    }
  }

  public void flipBit(int paramInt)
  {
    int i = paramInt >> 5;
    int j = 1 << (paramInt & 0x1F);
    int[] arrayOfInt = this.m_ints;
    arrayOfInt[i] = (j ^ arrayOfInt[i]);
  }

  public int getLength()
  {
    return this.m_ints.length;
  }

  public int getUsedLength()
  {
    int i = this.m_ints.length;
    if (i < 1)
      return 0;
    if (this.m_ints[0] != 0)
    {
      int[] arrayOfInt2;
      do
      {
        arrayOfInt2 = this.m_ints;
        i--;
      }
      while (arrayOfInt2[i] == 0);
      return i + 1;
    }
    do
    {
      int[] arrayOfInt1 = this.m_ints;
      i--;
      if (arrayOfInt1[i] != 0)
        return i + 1;
    }
    while (i > 0);
    return 0;
  }

  public int hashCode()
  {
    int i = getUsedLength();
    int j = 1;
    for (int k = 0; ; k++)
    {
      if (k >= i)
        return j;
      j = j * 31 + this.m_ints[k];
    }
  }

  public boolean isZero()
  {
    return (this.m_ints.length == 0) || ((this.m_ints[0] == 0) && (getUsedLength() == 0));
  }

  public IntArray multiply(IntArray paramIntArray, int paramInt)
  {
    int i = paramInt + 31 >> 5;
    if (this.m_ints.length < i)
      this.m_ints = resizedInts(i);
    IntArray localIntArray1 = new IntArray(paramIntArray.resizedInts(1 + paramIntArray.getLength()));
    IntArray localIntArray2 = new IntArray(31 + (paramInt + paramInt) >> 5);
    int j = 1;
    int k = 0;
    if (k >= 32)
      return localIntArray2;
    for (int m = 0; ; m++)
    {
      if (m >= i)
      {
        j <<= 1;
        localIntArray1.shiftLeft();
        k++;
        break;
      }
      if ((j & this.m_ints[m]) != 0)
        localIntArray2.addShifted(localIntArray1, m);
    }
  }

  public void reduce(int paramInt, int[] paramArrayOfInt)
  {
    int i = -2 + (paramInt + paramInt);
    if (i < paramInt)
    {
      this.m_ints = resizedInts(paramInt + 31 >> 5);
      return;
    }
    int j;
    int k;
    if (testBit(i))
    {
      j = i - paramInt;
      flipBit(j);
      flipBit(i);
      k = paramArrayOfInt.length;
    }
    while (true)
    {
      k--;
      if (k < 0)
      {
        i--;
        break;
      }
      flipBit(j + paramArrayOfInt[k]);
    }
  }

  public void setBit(int paramInt)
  {
    int i = paramInt >> 5;
    int j = 1 << (paramInt & 0x1F);
    int[] arrayOfInt = this.m_ints;
    arrayOfInt[i] = (j | arrayOfInt[i]);
  }

  public IntArray shiftLeft(int paramInt)
  {
    int i = getUsedLength();
    if (i == 0);
    while (paramInt == 0)
      return this;
    if (paramInt > 31)
      throw new IllegalArgumentException("shiftLeft() for max 31 bits , " + paramInt + "bit shift is not possible");
    int[] arrayOfInt = new int[i + 1];
    int j = 32 - paramInt;
    arrayOfInt[0] = (this.m_ints[0] << paramInt);
    for (int k = 1; ; k++)
    {
      if (k >= i)
      {
        arrayOfInt[i] = (this.m_ints[(i - 1)] >>> j);
        return new IntArray(arrayOfInt);
      }
      arrayOfInt[k] = (this.m_ints[k] << paramInt | this.m_ints[(k - 1)] >>> j);
    }
  }

  public void shiftLeft()
  {
    int i = getUsedLength();
    if (i == 0)
      return;
    if (this.m_ints[(i - 1)] < 0)
    {
      i++;
      if (i > this.m_ints.length)
        this.m_ints = resizedInts(1 + this.m_ints.length);
    }
    int j = 0;
    int k = 0;
    label52: if (k < i)
      if (this.m_ints[k] >= 0)
        break label114;
    label114: for (int m = 1; ; m = 0)
    {
      int[] arrayOfInt1 = this.m_ints;
      arrayOfInt1[k] <<= 1;
      if (j != 0)
      {
        int[] arrayOfInt2 = this.m_ints;
        arrayOfInt2[k] = (0x1 | arrayOfInt2[k]);
      }
      j = m;
      k++;
      break label52;
      break;
    }
  }

  public IntArray square(int paramInt)
  {
    int[] arrayOfInt = new int[16];
    arrayOfInt[1] = 1;
    arrayOfInt[2] = 4;
    arrayOfInt[3] = 5;
    arrayOfInt[4] = 16;
    arrayOfInt[5] = 17;
    arrayOfInt[6] = 20;
    arrayOfInt[7] = 21;
    arrayOfInt[8] = 64;
    arrayOfInt[9] = 65;
    arrayOfInt[10] = 68;
    arrayOfInt[11] = 69;
    arrayOfInt[12] = 80;
    arrayOfInt[13] = 81;
    arrayOfInt[14] = 84;
    arrayOfInt[15] = 85;
    int i = paramInt + 31 >> 5;
    if (this.m_ints.length < i)
      this.m_ints = resizedInts(i);
    IntArray localIntArray = new IntArray(i + i);
    int j = 0;
    if (j >= i)
      return localIntArray;
    int k = 0;
    int m = 0;
    label142: int n;
    int i1;
    if (m >= 4)
    {
      localIntArray.m_ints[(j + j)] = k;
      n = 0;
      i1 = this.m_ints[j] >>> 16;
    }
    for (int i2 = 0; ; i2++)
    {
      if (i2 >= 4)
      {
        localIntArray.m_ints[(1 + (j + j))] = n;
        j++;
        break;
        k = k >>> 8 | arrayOfInt[(0xF & this.m_ints[j] >>> m * 4)] << 24;
        m++;
        break label142;
      }
      n = n >>> 8 | arrayOfInt[(0xF & i1 >>> i2 * 4)] << 24;
    }
  }

  public boolean testBit(int paramInt)
  {
    int i = paramInt >> 5;
    return (1 << (paramInt & 0x1F) & this.m_ints[i]) != 0;
  }

  public BigInteger toBigInteger()
  {
    int i = getUsedLength();
    if (i == 0)
      return ECConstants.ZERO;
    int j = this.m_ints[(i - 1)];
    byte[] arrayOfByte1 = new byte[4];
    int k = 0;
    int m = 3;
    int n = 0;
    byte[] arrayOfByte2;
    int i3;
    label55: int i4;
    int i5;
    int i2;
    if (m < 0)
    {
      arrayOfByte2 = new byte[n + 4 * (i - 1)];
      i3 = 0;
      if (i3 >= n)
      {
        i4 = i - 2;
        i5 = n;
        if (i4 >= 0)
          break label147;
        return new BigInteger(1, arrayOfByte2);
      }
    }
    else
    {
      int i1 = (byte)(j >>> m * 8);
      if ((k == 0) && (i1 == 0))
        break label204;
      k = 1;
      i2 = n + 1;
      arrayOfByte1[n] = i1;
    }
    while (true)
    {
      m--;
      n = i2;
      break;
      arrayOfByte2[i3] = arrayOfByte1[i3];
      i3++;
      break label55;
      label147: int i6 = 3;
      int i8;
      for (int i7 = i5; ; i7 = i8)
      {
        if (i6 < 0)
        {
          i4--;
          i5 = i7;
          break;
        }
        i8 = i7 + 1;
        arrayOfByte2[i7] = ((byte)(this.m_ints[i4] >>> i6 * 8));
        i6--;
      }
      label204: i2 = n;
    }
  }

  public String toString()
  {
    int i = getUsedLength();
    if (i == 0)
      return "0";
    StringBuffer localStringBuffer = new StringBuffer(Integer.toBinaryString(this.m_ints[(i - 1)]));
    int j = i - 2;
    if (j < 0)
      return localStringBuffer.toString();
    String str = Integer.toBinaryString(this.m_ints[j]);
    for (int k = str.length(); ; k++)
    {
      if (k >= 8)
      {
        localStringBuffer.append(str);
        j--;
        break;
      }
      str = "0" + str;
    }
  }
}