package jcifs.util;

import java.security.MessageDigest;

public class MD4 extends MessageDigest
  implements Cloneable
{
  private static final int BLOCK_LENGTH = 64;
  private int[] X = new int[16];
  private byte[] buffer = new byte[64];
  private int[] context = new int[4];
  private long count;

  public MD4()
  {
    super("MD4");
    engineReset();
  }

  private MD4(MD4 paramMD4)
  {
    this();
    this.context = ((int[])paramMD4.context.clone());
    this.buffer = ((byte[])paramMD4.buffer.clone());
    this.count = paramMD4.count;
  }

  private int FF(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = paramInt5 + (paramInt1 + (paramInt2 & paramInt3 | paramInt4 & (paramInt2 ^ 0xFFFFFFFF)));
    return i << paramInt6 | i >>> 32 - paramInt6;
  }

  private int GG(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = 1518500249 + (paramInt5 + (paramInt1 + (paramInt2 & (paramInt3 | paramInt4) | paramInt3 & paramInt4)));
    return i << paramInt6 | i >>> 32 - paramInt6;
  }

  private int HH(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    int i = 1859775393 + (paramInt5 + (paramInt1 + (paramInt4 ^ (paramInt2 ^ paramInt3))));
    return i << paramInt6 | i >>> 32 - paramInt6;
  }

  private void transform(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    int j = paramInt;
    while (i < 16)
    {
      int[] arrayOfInt5 = this.X;
      int i50 = j + 1;
      int i51 = 0xFF & paramArrayOfByte[j];
      int i52 = i50 + 1;
      int i53 = i51 | (0xFF & paramArrayOfByte[i50]) << 8;
      int i54 = i52 + 1;
      int i55 = i53 | (0xFF & paramArrayOfByte[i52]) << 16;
      j = i54 + 1;
      arrayOfInt5[i] = (i55 | (0xFF & paramArrayOfByte[i54]) << 24);
      i++;
    }
    int k = this.context[0];
    int m = this.context[1];
    int n = this.context[2];
    int i1 = this.context[3];
    int i2 = FF(k, m, n, i1, this.X[0], 3);
    int i3 = FF(i1, i2, m, n, this.X[1], 7);
    int i4 = FF(n, i3, i2, m, this.X[2], 11);
    int i5 = FF(m, i4, i3, i2, this.X[3], 19);
    int i6 = FF(i2, i5, i4, i3, this.X[4], 3);
    int i7 = FF(i3, i6, i5, i4, this.X[5], 7);
    int i8 = FF(i4, i7, i6, i5, this.X[6], 11);
    int i9 = FF(i5, i8, i7, i6, this.X[7], 19);
    int i10 = FF(i6, i9, i8, i7, this.X[8], 3);
    int i11 = FF(i7, i10, i9, i8, this.X[9], 7);
    int i12 = FF(i8, i11, i10, i9, this.X[10], 11);
    int i13 = FF(i9, i12, i11, i10, this.X[11], 19);
    int i14 = FF(i10, i13, i12, i11, this.X[12], 3);
    int i15 = FF(i11, i14, i13, i12, this.X[13], 7);
    int i16 = FF(i12, i15, i14, i13, this.X[14], 11);
    int i17 = FF(i13, i16, i15, i14, this.X[15], 19);
    int i18 = GG(i14, i17, i16, i15, this.X[0], 3);
    int i19 = GG(i15, i18, i17, i16, this.X[4], 5);
    int i20 = GG(i16, i19, i18, i17, this.X[8], 9);
    int i21 = GG(i17, i20, i19, i18, this.X[12], 13);
    int i22 = GG(i18, i21, i20, i19, this.X[1], 3);
    int i23 = GG(i19, i22, i21, i20, this.X[5], 5);
    int i24 = GG(i20, i23, i22, i21, this.X[9], 9);
    int i25 = GG(i21, i24, i23, i22, this.X[13], 13);
    int i26 = GG(i22, i25, i24, i23, this.X[2], 3);
    int i27 = GG(i23, i26, i25, i24, this.X[6], 5);
    int i28 = GG(i24, i27, i26, i25, this.X[10], 9);
    int i29 = GG(i25, i28, i27, i26, this.X[14], 13);
    int i30 = GG(i26, i29, i28, i27, this.X[3], 3);
    int i31 = GG(i27, i30, i29, i28, this.X[7], 5);
    int i32 = GG(i28, i31, i30, i29, this.X[11], 9);
    int i33 = GG(i29, i32, i31, i30, this.X[15], 13);
    int i34 = HH(i30, i33, i32, i31, this.X[0], 3);
    int i35 = HH(i31, i34, i33, i32, this.X[8], 9);
    int i36 = HH(i32, i35, i34, i33, this.X[4], 11);
    int i37 = HH(i33, i36, i35, i34, this.X[12], 15);
    int i38 = HH(i34, i37, i36, i35, this.X[2], 3);
    int i39 = HH(i35, i38, i37, i36, this.X[10], 9);
    int i40 = HH(i36, i39, i38, i37, this.X[6], 11);
    int i41 = HH(i37, i40, i39, i38, this.X[14], 15);
    int i42 = HH(i38, i41, i40, i39, this.X[1], 3);
    int i43 = HH(i39, i42, i41, i40, this.X[9], 9);
    int i44 = HH(i40, i43, i42, i41, this.X[5], 11);
    int i45 = HH(i41, i44, i43, i42, this.X[13], 15);
    int i46 = HH(i42, i45, i44, i43, this.X[3], 3);
    int i47 = HH(i43, i46, i45, i44, this.X[11], 9);
    int i48 = HH(i44, i47, i46, i45, this.X[7], 11);
    int i49 = HH(i45, i48, i47, i46, this.X[15], 15);
    int[] arrayOfInt1 = this.context;
    arrayOfInt1[0] = (i46 + arrayOfInt1[0]);
    int[] arrayOfInt2 = this.context;
    arrayOfInt2[1] = (i49 + arrayOfInt2[1]);
    int[] arrayOfInt3 = this.context;
    arrayOfInt3[2] = (i48 + arrayOfInt3[2]);
    int[] arrayOfInt4 = this.context;
    arrayOfInt4[3] = (i47 + arrayOfInt4[3]);
  }

  public Object clone()
  {
    return new MD4(this);
  }

  public byte[] engineDigest()
  {
    int i = (int)(this.count % 64L);
    if (i < 56);
    byte[] arrayOfByte1;
    for (int j = 56 - i; ; j = 120 - i)
    {
      arrayOfByte1 = new byte[j + 8];
      arrayOfByte1[0] = -128;
      for (int k = 0; k < 8; k++)
        arrayOfByte1[(j + k)] = ((byte)(int)(8L * this.count >>> k * 8));
    }
    engineUpdate(arrayOfByte1, 0, arrayOfByte1.length);
    byte[] arrayOfByte2 = new byte[16];
    for (int m = 0; m < 4; m++)
      for (int n = 0; n < 4; n++)
        arrayOfByte2[(n + m * 4)] = ((byte)(this.context[m] >>> n * 8));
    engineReset();
    return arrayOfByte2;
  }

  public void engineReset()
  {
    this.context[0] = 1732584193;
    this.context[1] = -271733879;
    this.context[2] = -1732584194;
    this.context[3] = 271733878;
    this.count = 0L;
    for (int i = 0; i < 64; i++)
      this.buffer[i] = 0;
  }

  public void engineUpdate(byte paramByte)
  {
    int i = (int)(this.count % 64L);
    this.count = (1L + this.count);
    this.buffer[i] = paramByte;
    if (i == 63)
      transform(this.buffer, 0);
  }

  public void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length))
      throw new ArrayIndexOutOfBoundsException();
    int i = (int)(this.count % 64L);
    this.count += paramInt2;
    int j = 64 - i;
    int k = 0;
    if (paramInt2 >= j)
    {
      System.arraycopy(paramArrayOfByte, paramInt1, this.buffer, i, j);
      transform(this.buffer, 0);
      for (k = j; -1 + (k + 64) < paramInt2; k += 64)
        transform(paramArrayOfByte, paramInt1 + k);
      i = 0;
    }
    if (k < paramInt2)
      System.arraycopy(paramArrayOfByte, paramInt1 + k, this.buffer, i, paramInt2 - k);
  }
}