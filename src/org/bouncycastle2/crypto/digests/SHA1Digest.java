package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.util.Pack;

public class SHA1Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 20;
  private static final int Y1 = 1518500249;
  private static final int Y2 = 1859775393;
  private static final int Y3 = -1894007588;
  private static final int Y4 = -899497514;
  private int H1;
  private int H2;
  private int H3;
  private int H4;
  private int H5;
  private int[] X = new int[80];
  private int xOff;

  public SHA1Digest()
  {
    reset();
  }

  public SHA1Digest(SHA1Digest paramSHA1Digest)
  {
    super(paramSHA1Digest);
    this.H1 = paramSHA1Digest.H1;
    this.H2 = paramSHA1Digest.H2;
    this.H3 = paramSHA1Digest.H3;
    this.H4 = paramSHA1Digest.H4;
    this.H5 = paramSHA1Digest.H5;
    System.arraycopy(paramSHA1Digest.X, 0, this.X, 0, paramSHA1Digest.X.length);
    this.xOff = paramSHA1Digest.xOff;
  }

  private int f(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }

  private int g(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
  }

  private int h(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 ^ paramInt2);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    Pack.intToBigEndian(this.H1, paramArrayOfByte, paramInt);
    Pack.intToBigEndian(this.H2, paramArrayOfByte, paramInt + 4);
    Pack.intToBigEndian(this.H3, paramArrayOfByte, paramInt + 8);
    Pack.intToBigEndian(this.H4, paramArrayOfByte, paramInt + 12);
    Pack.intToBigEndian(this.H5, paramArrayOfByte, paramInt + 16);
    reset();
    return 20;
  }

  public String getAlgorithmName()
  {
    return "SHA-1";
  }

  public int getDigestSize()
  {
    return 20;
  }

  protected void processBlock()
  {
    int i = 16;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    label44: int i20;
    label53: int i36;
    label62: int i52;
    if (i >= 80)
    {
      k = this.H1;
      m = this.H2;
      n = this.H3;
      i1 = this.H4;
      i2 = this.H5;
      i3 = 0;
      i4 = 0;
      if (i3 < 4)
        break label206;
      i20 = 0;
      if (i20 < 4)
        break label525;
      i36 = 0;
      if (i36 < 4)
        break label844;
      i52 = 0;
      label71: if (i52 <= 3)
        break label1163;
      this.H1 = (k + this.H1);
      this.H2 = (m + this.H2);
      this.H3 = (n + this.H3);
      this.H4 = (i1 + this.H4);
      this.H5 = (i2 + this.H5);
      this.xOff = 0;
    }
    for (int i68 = 0; ; i68++)
    {
      if (i68 >= 16)
      {
        return;
        int j = this.X[(i - 3)] ^ this.X[(i - 8)] ^ this.X[(i - 14)] ^ this.X[(i - 16)];
        this.X[i] = (j << 1 | j >>> 31);
        i++;
        break;
        label206: int i5 = (k << 5 | k >>> 27) + f(m, n, i1);
        int[] arrayOfInt1 = this.X;
        int i6 = i4 + 1;
        int i7 = i2 + (1518500249 + (i5 + arrayOfInt1[i4]));
        int i8 = m << 30 | m >>> 2;
        int i9 = (i7 << 5 | i7 >>> 27) + f(k, i8, n);
        int[] arrayOfInt2 = this.X;
        int i10 = i6 + 1;
        int i11 = i1 + (1518500249 + (i9 + arrayOfInt2[i6]));
        int i12 = k << 30 | k >>> 2;
        int i13 = (i11 << 5 | i11 >>> 27) + f(i7, i12, i8);
        int[] arrayOfInt3 = this.X;
        int i14 = i10 + 1;
        int i15 = n + (1518500249 + (i13 + arrayOfInt3[i10]));
        i2 = i7 << 30 | i7 >>> 2;
        int i16 = (i15 << 5 | i15 >>> 27) + f(i11, i2, i12);
        int[] arrayOfInt4 = this.X;
        int i17 = i14 + 1;
        m = i8 + (1518500249 + (i16 + arrayOfInt4[i14]));
        i1 = i11 << 30 | i11 >>> 2;
        int i18 = (m << 5 | m >>> 27) + f(i15, i1, i2);
        int[] arrayOfInt5 = this.X;
        int i19 = i17 + 1;
        k = i12 + (1518500249 + (i18 + arrayOfInt5[i17]));
        n = i15 << 30 | i15 >>> 2;
        i3++;
        i4 = i19;
        break label44;
        label525: int i21 = (k << 5 | k >>> 27) + h(m, n, i1);
        int[] arrayOfInt6 = this.X;
        int i22 = i4 + 1;
        int i23 = i2 + (1859775393 + (i21 + arrayOfInt6[i4]));
        int i24 = m << 30 | m >>> 2;
        int i25 = (i23 << 5 | i23 >>> 27) + h(k, i24, n);
        int[] arrayOfInt7 = this.X;
        int i26 = i22 + 1;
        int i27 = i1 + (1859775393 + (i25 + arrayOfInt7[i22]));
        int i28 = k << 30 | k >>> 2;
        int i29 = (i27 << 5 | i27 >>> 27) + h(i23, i28, i24);
        int[] arrayOfInt8 = this.X;
        int i30 = i26 + 1;
        int i31 = n + (1859775393 + (i29 + arrayOfInt8[i26]));
        i2 = i23 << 30 | i23 >>> 2;
        int i32 = (i31 << 5 | i31 >>> 27) + h(i27, i2, i28);
        int[] arrayOfInt9 = this.X;
        int i33 = i30 + 1;
        m = i24 + (1859775393 + (i32 + arrayOfInt9[i30]));
        i1 = i27 << 30 | i27 >>> 2;
        int i34 = (m << 5 | m >>> 27) + h(i31, i1, i2);
        int[] arrayOfInt10 = this.X;
        int i35 = i33 + 1;
        k = i28 + (1859775393 + (i34 + arrayOfInt10[i33]));
        n = i31 << 30 | i31 >>> 2;
        i20++;
        i4 = i35;
        break label53;
        label844: int i37 = (k << 5 | k >>> 27) + g(m, n, i1);
        int[] arrayOfInt11 = this.X;
        int i38 = i4 + 1;
        int i39 = i2 + (-1894007588 + (i37 + arrayOfInt11[i4]));
        int i40 = m << 30 | m >>> 2;
        int i41 = (i39 << 5 | i39 >>> 27) + g(k, i40, n);
        int[] arrayOfInt12 = this.X;
        int i42 = i38 + 1;
        int i43 = i1 + (-1894007588 + (i41 + arrayOfInt12[i38]));
        int i44 = k << 30 | k >>> 2;
        int i45 = (i43 << 5 | i43 >>> 27) + g(i39, i44, i40);
        int[] arrayOfInt13 = this.X;
        int i46 = i42 + 1;
        int i47 = n + (-1894007588 + (i45 + arrayOfInt13[i42]));
        i2 = i39 << 30 | i39 >>> 2;
        int i48 = (i47 << 5 | i47 >>> 27) + g(i43, i2, i44);
        int[] arrayOfInt14 = this.X;
        int i49 = i46 + 1;
        m = i40 + (-1894007588 + (i48 + arrayOfInt14[i46]));
        i1 = i43 << 30 | i43 >>> 2;
        int i50 = (m << 5 | m >>> 27) + g(i47, i1, i2);
        int[] arrayOfInt15 = this.X;
        int i51 = i49 + 1;
        k = i44 + (-1894007588 + (i50 + arrayOfInt15[i49]));
        n = i47 << 30 | i47 >>> 2;
        i36++;
        i4 = i51;
        break label62;
        label1163: int i53 = (k << 5 | k >>> 27) + h(m, n, i1);
        int[] arrayOfInt16 = this.X;
        int i54 = i4 + 1;
        int i55 = i2 + (-899497514 + (i53 + arrayOfInt16[i4]));
        int i56 = m << 30 | m >>> 2;
        int i57 = (i55 << 5 | i55 >>> 27) + h(k, i56, n);
        int[] arrayOfInt17 = this.X;
        int i58 = i54 + 1;
        int i59 = i1 + (-899497514 + (i57 + arrayOfInt17[i54]));
        int i60 = k << 30 | k >>> 2;
        int i61 = (i59 << 5 | i59 >>> 27) + h(i55, i60, i56);
        int[] arrayOfInt18 = this.X;
        int i62 = i58 + 1;
        int i63 = n + (-899497514 + (i61 + arrayOfInt18[i58]));
        i2 = i55 << 30 | i55 >>> 2;
        int i64 = (i63 << 5 | i63 >>> 27) + h(i59, i2, i60);
        int[] arrayOfInt19 = this.X;
        int i65 = i62 + 1;
        m = i56 + (-899497514 + (i64 + arrayOfInt19[i62]));
        i1 = i59 << 30 | i59 >>> 2;
        int i66 = (m << 5 | m >>> 27) + h(i63, i1, i2);
        int[] arrayOfInt20 = this.X;
        int i67 = i65 + 1;
        k = i60 + (-899497514 + (i66 + arrayOfInt20[i65]));
        n = i63 << 30 | i63 >>> 2;
        i52++;
        i4 = i67;
        break label71;
      }
      this.X[i68] = 0;
    }
  }

  protected void processLength(long paramLong)
  {
    if (this.xOff > 14)
      processBlock();
    this.X[14] = ((int)(paramLong >>> 32));
    this.X[15] = ((int)(0xFFFFFFFF & paramLong));
  }

  protected void processWord(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[paramInt] << 24;
    int j = paramInt + 1;
    int k = i | (0xFF & paramArrayOfByte[j]) << 16;
    int m = j + 1;
    int n = k | (0xFF & paramArrayOfByte[m]) << 8 | 0xFF & paramArrayOfByte[(m + 1)];
    this.X[this.xOff] = n;
    int i1 = 1 + this.xOff;
    this.xOff = i1;
    if (i1 == 16)
      processBlock();
  }

  public void reset()
  {
    super.reset();
    this.H1 = 1732584193;
    this.H2 = -271733879;
    this.H3 = -1732584194;
    this.H4 = 271733878;
    this.H5 = -1009589776;
    this.xOff = 0;
    for (int i = 0; ; i++)
    {
      if (i == this.X.length)
        return;
      this.X[i] = 0;
    }
  }
}