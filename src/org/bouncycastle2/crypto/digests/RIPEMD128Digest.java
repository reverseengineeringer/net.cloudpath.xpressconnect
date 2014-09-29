package org.bouncycastle2.crypto.digests;

public class RIPEMD128Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 16;
  private int H0;
  private int H1;
  private int H2;
  private int H3;
  private int[] X = new int[16];
  private int xOff;

  public RIPEMD128Digest()
  {
    reset();
  }

  public RIPEMD128Digest(RIPEMD128Digest paramRIPEMD128Digest)
  {
    super(paramRIPEMD128Digest);
    this.H0 = paramRIPEMD128Digest.H0;
    this.H1 = paramRIPEMD128Digest.H1;
    this.H2 = paramRIPEMD128Digest.H2;
    this.H3 = paramRIPEMD128Digest.H3;
    System.arraycopy(paramRIPEMD128Digest.X, 0, this.X, 0, paramRIPEMD128Digest.X.length);
    this.xOff = paramRIPEMD128Digest.xOff;
  }

  private int F1(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(paramInt5 + (paramInt1 + f1(paramInt2, paramInt3, paramInt4)), paramInt6);
  }

  private int F2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(1518500249 + (paramInt5 + (paramInt1 + f2(paramInt2, paramInt3, paramInt4))), paramInt6);
  }

  private int F3(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(1859775393 + (paramInt5 + (paramInt1 + f3(paramInt2, paramInt3, paramInt4))), paramInt6);
  }

  private int F4(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(-1894007588 + (paramInt5 + (paramInt1 + f4(paramInt2, paramInt3, paramInt4))), paramInt6);
  }

  private int FF1(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(paramInt5 + (paramInt1 + f1(paramInt2, paramInt3, paramInt4)), paramInt6);
  }

  private int FF2(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(1836072691 + (paramInt5 + (paramInt1 + f2(paramInt2, paramInt3, paramInt4))), paramInt6);
  }

  private int FF3(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(1548603684 + (paramInt5 + (paramInt1 + f3(paramInt2, paramInt3, paramInt4))), paramInt6);
  }

  private int FF4(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    return RL(1352829926 + (paramInt5 + (paramInt1 + f4(paramInt2, paramInt3, paramInt4))), paramInt6);
  }

  private int RL(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }

  private int f1(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 ^ paramInt2);
  }

  private int f2(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }

  private int f3(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 | paramInt2 ^ 0xFFFFFFFF);
  }

  private int f4(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt3 | paramInt2 & (paramInt3 ^ 0xFFFFFFFF);
  }

  private void unpackWord(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)paramInt1);
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >>> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >>> 16));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(paramInt1 >>> 24));
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    unpackWord(this.H0, paramArrayOfByte, paramInt);
    unpackWord(this.H1, paramArrayOfByte, paramInt + 4);
    unpackWord(this.H2, paramArrayOfByte, paramInt + 8);
    unpackWord(this.H3, paramArrayOfByte, paramInt + 12);
    reset();
    return 16;
  }

  public String getAlgorithmName()
  {
    return "RIPEMD128";
  }

  public int getDigestSize()
  {
    return 16;
  }

  protected void processBlock()
  {
    int i = this.H0;
    int j = this.H1;
    int k = this.H2;
    int m = this.H3;
    int n = F1(i, j, k, m, this.X[0], 11);
    int i1 = F1(m, n, j, k, this.X[1], 14);
    int i2 = F1(k, i1, n, j, this.X[2], 15);
    int i3 = F1(j, i2, i1, n, this.X[3], 12);
    int i4 = F1(n, i3, i2, i1, this.X[4], 5);
    int i5 = F1(i1, i4, i3, i2, this.X[5], 8);
    int i6 = F1(i2, i5, i4, i3, this.X[6], 7);
    int i7 = F1(i3, i6, i5, i4, this.X[7], 9);
    int i8 = F1(i4, i7, i6, i5, this.X[8], 11);
    int i9 = F1(i5, i8, i7, i6, this.X[9], 13);
    int i10 = F1(i6, i9, i8, i7, this.X[10], 14);
    int i11 = F1(i7, i10, i9, i8, this.X[11], 15);
    int i12 = F1(i8, i11, i10, i9, this.X[12], 6);
    int i13 = F1(i9, i12, i11, i10, this.X[13], 7);
    int i14 = F1(i10, i13, i12, i11, this.X[14], 9);
    int i15 = F1(i11, i14, i13, i12, this.X[15], 8);
    int i16 = F2(i12, i15, i14, i13, this.X[7], 7);
    int i17 = F2(i13, i16, i15, i14, this.X[4], 6);
    int i18 = F2(i14, i17, i16, i15, this.X[13], 8);
    int i19 = F2(i15, i18, i17, i16, this.X[1], 13);
    int i20 = F2(i16, i19, i18, i17, this.X[10], 11);
    int i21 = F2(i17, i20, i19, i18, this.X[6], 9);
    int i22 = F2(i18, i21, i20, i19, this.X[15], 7);
    int i23 = F2(i19, i22, i21, i20, this.X[3], 15);
    int i24 = F2(i20, i23, i22, i21, this.X[12], 7);
    int i25 = F2(i21, i24, i23, i22, this.X[0], 12);
    int i26 = F2(i22, i25, i24, i23, this.X[9], 15);
    int i27 = F2(i23, i26, i25, i24, this.X[5], 9);
    int i28 = F2(i24, i27, i26, i25, this.X[2], 11);
    int i29 = F2(i25, i28, i27, i26, this.X[14], 7);
    int i30 = F2(i26, i29, i28, i27, this.X[11], 13);
    int i31 = F2(i27, i30, i29, i28, this.X[8], 12);
    int i32 = F3(i28, i31, i30, i29, this.X[3], 11);
    int i33 = F3(i29, i32, i31, i30, this.X[10], 13);
    int i34 = F3(i30, i33, i32, i31, this.X[14], 6);
    int i35 = F3(i31, i34, i33, i32, this.X[4], 7);
    int i36 = F3(i32, i35, i34, i33, this.X[9], 14);
    int i37 = F3(i33, i36, i35, i34, this.X[15], 9);
    int i38 = F3(i34, i37, i36, i35, this.X[8], 13);
    int i39 = F3(i35, i38, i37, i36, this.X[1], 15);
    int i40 = F3(i36, i39, i38, i37, this.X[2], 14);
    int i41 = F3(i37, i40, i39, i38, this.X[7], 8);
    int i42 = F3(i38, i41, i40, i39, this.X[0], 13);
    int i43 = F3(i39, i42, i41, i40, this.X[6], 6);
    int i44 = F3(i40, i43, i42, i41, this.X[13], 5);
    int i45 = F3(i41, i44, i43, i42, this.X[11], 12);
    int i46 = F3(i42, i45, i44, i43, this.X[5], 7);
    int i47 = F3(i43, i46, i45, i44, this.X[12], 5);
    int i48 = F4(i44, i47, i46, i45, this.X[1], 11);
    int i49 = F4(i45, i48, i47, i46, this.X[9], 12);
    int i50 = F4(i46, i49, i48, i47, this.X[11], 14);
    int i51 = F4(i47, i50, i49, i48, this.X[10], 15);
    int i52 = F4(i48, i51, i50, i49, this.X[0], 14);
    int i53 = F4(i49, i52, i51, i50, this.X[8], 15);
    int i54 = F4(i50, i53, i52, i51, this.X[12], 9);
    int i55 = F4(i51, i54, i53, i52, this.X[4], 8);
    int i56 = F4(i52, i55, i54, i53, this.X[13], 9);
    int i57 = F4(i53, i56, i55, i54, this.X[3], 14);
    int i58 = F4(i54, i57, i56, i55, this.X[7], 5);
    int i59 = F4(i55, i58, i57, i56, this.X[15], 6);
    int i60 = F4(i56, i59, i58, i57, this.X[14], 8);
    int i61 = F4(i57, i60, i59, i58, this.X[5], 6);
    int i62 = F4(i58, i61, i60, i59, this.X[6], 5);
    int i63 = F4(i59, i62, i61, i60, this.X[2], 12);
    int i64 = FF4(i, j, k, m, this.X[5], 8);
    int i65 = FF4(m, i64, j, k, this.X[14], 9);
    int i66 = FF4(k, i65, i64, j, this.X[7], 9);
    int i67 = FF4(j, i66, i65, i64, this.X[0], 11);
    int i68 = FF4(i64, i67, i66, i65, this.X[9], 13);
    int i69 = FF4(i65, i68, i67, i66, this.X[2], 15);
    int i70 = FF4(i66, i69, i68, i67, this.X[11], 15);
    int i71 = FF4(i67, i70, i69, i68, this.X[4], 5);
    int i72 = FF4(i68, i71, i70, i69, this.X[13], 7);
    int i73 = FF4(i69, i72, i71, i70, this.X[6], 7);
    int i74 = FF4(i70, i73, i72, i71, this.X[15], 8);
    int i75 = FF4(i71, i74, i73, i72, this.X[8], 11);
    int i76 = FF4(i72, i75, i74, i73, this.X[1], 14);
    int i77 = FF4(i73, i76, i75, i74, this.X[10], 14);
    int i78 = FF4(i74, i77, i76, i75, this.X[3], 12);
    int i79 = FF4(i75, i78, i77, i76, this.X[12], 6);
    int i80 = FF3(i76, i79, i78, i77, this.X[6], 9);
    int i81 = FF3(i77, i80, i79, i78, this.X[11], 13);
    int i82 = FF3(i78, i81, i80, i79, this.X[3], 15);
    int i83 = FF3(i79, i82, i81, i80, this.X[7], 7);
    int i84 = FF3(i80, i83, i82, i81, this.X[0], 12);
    int i85 = FF3(i81, i84, i83, i82, this.X[13], 8);
    int i86 = FF3(i82, i85, i84, i83, this.X[5], 9);
    int i87 = FF3(i83, i86, i85, i84, this.X[10], 11);
    int i88 = FF3(i84, i87, i86, i85, this.X[14], 7);
    int i89 = FF3(i85, i88, i87, i86, this.X[15], 7);
    int i90 = FF3(i86, i89, i88, i87, this.X[8], 12);
    int i91 = FF3(i87, i90, i89, i88, this.X[12], 7);
    int i92 = FF3(i88, i91, i90, i89, this.X[4], 6);
    int i93 = FF3(i89, i92, i91, i90, this.X[9], 15);
    int i94 = FF3(i90, i93, i92, i91, this.X[1], 13);
    int i95 = FF3(i91, i94, i93, i92, this.X[2], 11);
    int i96 = FF2(i92, i95, i94, i93, this.X[15], 9);
    int i97 = FF2(i93, i96, i95, i94, this.X[5], 7);
    int i98 = FF2(i94, i97, i96, i95, this.X[1], 15);
    int i99 = FF2(i95, i98, i97, i96, this.X[3], 11);
    int i100 = FF2(i96, i99, i98, i97, this.X[7], 8);
    int i101 = FF2(i97, i100, i99, i98, this.X[14], 6);
    int i102 = FF2(i98, i101, i100, i99, this.X[6], 6);
    int i103 = FF2(i99, i102, i101, i100, this.X[9], 14);
    int i104 = FF2(i100, i103, i102, i101, this.X[11], 12);
    int i105 = FF2(i101, i104, i103, i102, this.X[8], 13);
    int i106 = FF2(i102, i105, i104, i103, this.X[12], 5);
    int i107 = FF2(i103, i106, i105, i104, this.X[2], 14);
    int i108 = FF2(i104, i107, i106, i105, this.X[10], 13);
    int i109 = FF2(i105, i108, i107, i106, this.X[0], 13);
    int i110 = FF2(i106, i109, i108, i107, this.X[4], 7);
    int i111 = FF2(i107, i110, i109, i108, this.X[13], 5);
    int i112 = FF1(i108, i111, i110, i109, this.X[8], 15);
    int i113 = FF1(i109, i112, i111, i110, this.X[6], 5);
    int i114 = FF1(i110, i113, i112, i111, this.X[4], 8);
    int i115 = FF1(i111, i114, i113, i112, this.X[1], 11);
    int i116 = FF1(i112, i115, i114, i113, this.X[3], 14);
    int i117 = FF1(i113, i116, i115, i114, this.X[11], 14);
    int i118 = FF1(i114, i117, i116, i115, this.X[15], 6);
    int i119 = FF1(i115, i118, i117, i116, this.X[0], 14);
    int i120 = FF1(i116, i119, i118, i117, this.X[5], 6);
    int i121 = FF1(i117, i120, i119, i118, this.X[12], 9);
    int i122 = FF1(i118, i121, i120, i119, this.X[2], 12);
    int i123 = FF1(i119, i122, i121, i120, this.X[13], 9);
    int i124 = FF1(i120, i123, i122, i121, this.X[9], 12);
    int i125 = FF1(i121, i124, i123, i122, this.X[7], 5);
    int i126 = FF1(i122, i125, i124, i123, this.X[10], 15);
    int i127 = FF1(i123, i126, i125, i124, this.X[14], 8);
    int i128 = i125 + (i62 + this.H1);
    this.H1 = (i124 + (i61 + this.H2));
    this.H2 = (i127 + (i60 + this.H3));
    this.H3 = (i126 + (i63 + this.H0));
    this.H0 = i128;
    this.xOff = 0;
    for (int i129 = 0; ; i129++)
    {
      int i130 = this.X.length;
      if (i129 == i130)
        return;
      this.X[i129] = 0;
    }
  }

  protected void processLength(long paramLong)
  {
    if (this.xOff > 14)
      processBlock();
    this.X[14] = ((int)(0xFFFFFFFF & paramLong));
    this.X[15] = ((int)(paramLong >>> 32));
  }

  protected void processWord(byte[] paramArrayOfByte, int paramInt)
  {
    int[] arrayOfInt = this.X;
    int i = this.xOff;
    this.xOff = (i + 1);
    arrayOfInt[i] = (0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24);
    if (this.xOff == 16)
      processBlock();
  }

  public void reset()
  {
    super.reset();
    this.H0 = 1732584193;
    this.H1 = -271733879;
    this.H2 = -1732584194;
    this.H3 = 271733878;
    this.xOff = 0;
    for (int i = 0; ; i++)
    {
      if (i == this.X.length)
        return;
      this.X[i] = 0;
    }
  }
}