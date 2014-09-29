package org.bouncycastle2.crypto.digests;

public class RIPEMD256Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 32;
  private int H0;
  private int H1;
  private int H2;
  private int H3;
  private int H4;
  private int H5;
  private int H6;
  private int H7;
  private int[] X = new int[16];
  private int xOff;

  public RIPEMD256Digest()
  {
    reset();
  }

  public RIPEMD256Digest(RIPEMD256Digest paramRIPEMD256Digest)
  {
    super(paramRIPEMD256Digest);
    this.H0 = paramRIPEMD256Digest.H0;
    this.H1 = paramRIPEMD256Digest.H1;
    this.H2 = paramRIPEMD256Digest.H2;
    this.H3 = paramRIPEMD256Digest.H3;
    this.H4 = paramRIPEMD256Digest.H4;
    this.H5 = paramRIPEMD256Digest.H5;
    this.H6 = paramRIPEMD256Digest.H6;
    this.H7 = paramRIPEMD256Digest.H7;
    System.arraycopy(paramRIPEMD256Digest.X, 0, this.X, 0, paramRIPEMD256Digest.X.length);
    this.xOff = paramRIPEMD256Digest.xOff;
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
    unpackWord(this.H4, paramArrayOfByte, paramInt + 16);
    unpackWord(this.H5, paramArrayOfByte, paramInt + 20);
    unpackWord(this.H6, paramArrayOfByte, paramInt + 24);
    unpackWord(this.H7, paramArrayOfByte, paramInt + 28);
    reset();
    return 32;
  }

  public String getAlgorithmName()
  {
    return "RIPEMD256";
  }

  public int getDigestSize()
  {
    return 32;
  }

  protected void processBlock()
  {
    int i = this.H0;
    int j = this.H1;
    int k = this.H2;
    int m = this.H3;
    int n = this.H4;
    int i1 = this.H5;
    int i2 = this.H6;
    int i3 = this.H7;
    int i4 = F1(i, j, k, m, this.X[0], 11);
    int i5 = F1(m, i4, j, k, this.X[1], 14);
    int i6 = F1(k, i5, i4, j, this.X[2], 15);
    int i7 = F1(j, i6, i5, i4, this.X[3], 12);
    int i8 = F1(i4, i7, i6, i5, this.X[4], 5);
    int i9 = F1(i5, i8, i7, i6, this.X[5], 8);
    int i10 = F1(i6, i9, i8, i7, this.X[6], 7);
    int i11 = F1(i7, i10, i9, i8, this.X[7], 9);
    int i12 = F1(i8, i11, i10, i9, this.X[8], 11);
    int i13 = F1(i9, i12, i11, i10, this.X[9], 13);
    int i14 = F1(i10, i13, i12, i11, this.X[10], 14);
    int i15 = F1(i11, i14, i13, i12, this.X[11], 15);
    int i16 = F1(i12, i15, i14, i13, this.X[12], 6);
    int i17 = F1(i13, i16, i15, i14, this.X[13], 7);
    int i18 = F1(i14, i17, i16, i15, this.X[14], 9);
    int i19 = F1(i15, i18, i17, i16, this.X[15], 8);
    int i20 = FF4(n, i1, i2, i3, this.X[5], 8);
    int i21 = FF4(i3, i20, i1, i2, this.X[14], 9);
    int i22 = FF4(i2, i21, i20, i1, this.X[7], 9);
    int i23 = FF4(i1, i22, i21, i20, this.X[0], 11);
    int i24 = FF4(i20, i23, i22, i21, this.X[9], 13);
    int i25 = FF4(i21, i24, i23, i22, this.X[2], 15);
    int i26 = FF4(i22, i25, i24, i23, this.X[11], 15);
    int i27 = FF4(i23, i26, i25, i24, this.X[4], 5);
    int i28 = FF4(i24, i27, i26, i25, this.X[13], 7);
    int i29 = FF4(i25, i28, i27, i26, this.X[6], 7);
    int i30 = FF4(i26, i29, i28, i27, this.X[15], 8);
    int i31 = FF4(i27, i30, i29, i28, this.X[8], 11);
    int i32 = FF4(i28, i31, i30, i29, this.X[1], 14);
    int i33 = FF4(i29, i32, i31, i30, this.X[10], 14);
    int i34 = FF4(i30, i33, i32, i31, this.X[3], 12);
    int i35 = FF4(i31, i34, i33, i32, this.X[12], 6);
    int i36 = F2(i32, i19, i18, i17, this.X[7], 7);
    int i37 = F2(i17, i36, i19, i18, this.X[4], 6);
    int i38 = F2(i18, i37, i36, i19, this.X[13], 8);
    int i39 = F2(i19, i38, i37, i36, this.X[1], 13);
    int i40 = F2(i36, i39, i38, i37, this.X[10], 11);
    int i41 = F2(i37, i40, i39, i38, this.X[6], 9);
    int i42 = F2(i38, i41, i40, i39, this.X[15], 7);
    int i43 = F2(i39, i42, i41, i40, this.X[3], 15);
    int i44 = F2(i40, i43, i42, i41, this.X[12], 7);
    int i45 = F2(i41, i44, i43, i42, this.X[0], 12);
    int i46 = F2(i42, i45, i44, i43, this.X[9], 15);
    int i47 = F2(i43, i46, i45, i44, this.X[5], 9);
    int i48 = F2(i44, i47, i46, i45, this.X[2], 11);
    int i49 = F2(i45, i48, i47, i46, this.X[14], 7);
    int i50 = F2(i46, i49, i48, i47, this.X[11], 13);
    int i51 = F2(i47, i50, i49, i48, this.X[8], 12);
    int i52 = FF3(i16, i35, i34, i33, this.X[6], 9);
    int i53 = FF3(i33, i52, i35, i34, this.X[11], 13);
    int i54 = FF3(i34, i53, i52, i35, this.X[3], 15);
    int i55 = FF3(i35, i54, i53, i52, this.X[7], 7);
    int i56 = FF3(i52, i55, i54, i53, this.X[0], 12);
    int i57 = FF3(i53, i56, i55, i54, this.X[13], 8);
    int i58 = FF3(i54, i57, i56, i55, this.X[5], 9);
    int i59 = FF3(i55, i58, i57, i56, this.X[10], 11);
    int i60 = FF3(i56, i59, i58, i57, this.X[14], 7);
    int i61 = FF3(i57, i60, i59, i58, this.X[15], 7);
    int i62 = FF3(i58, i61, i60, i59, this.X[8], 12);
    int i63 = FF3(i59, i62, i61, i60, this.X[12], 7);
    int i64 = FF3(i60, i63, i62, i61, this.X[4], 6);
    int i65 = FF3(i61, i64, i63, i62, this.X[9], 15);
    int i66 = FF3(i62, i65, i64, i63, this.X[1], 13);
    int i67 = FF3(i63, i66, i65, i64, this.X[2], 11);
    int i68 = F3(i48, i67, i50, i49, this.X[3], 11);
    int i69 = F3(i49, i68, i67, i50, this.X[10], 13);
    int i70 = F3(i50, i69, i68, i67, this.X[14], 6);
    int i71 = F3(i67, i70, i69, i68, this.X[4], 7);
    int i72 = F3(i68, i71, i70, i69, this.X[9], 14);
    int i73 = F3(i69, i72, i71, i70, this.X[15], 9);
    int i74 = F3(i70, i73, i72, i71, this.X[8], 13);
    int i75 = F3(i71, i74, i73, i72, this.X[1], 15);
    int i76 = F3(i72, i75, i74, i73, this.X[2], 14);
    int i77 = F3(i73, i76, i75, i74, this.X[7], 8);
    int i78 = F3(i74, i77, i76, i75, this.X[0], 13);
    int i79 = F3(i75, i78, i77, i76, this.X[6], 6);
    int i80 = F3(i76, i79, i78, i77, this.X[13], 5);
    int i81 = F3(i77, i80, i79, i78, this.X[11], 12);
    int i82 = F3(i78, i81, i80, i79, this.X[5], 7);
    int i83 = F3(i79, i82, i81, i80, this.X[12], 5);
    int i84 = FF2(i64, i51, i66, i65, this.X[15], 9);
    int i85 = FF2(i65, i84, i51, i66, this.X[5], 7);
    int i86 = FF2(i66, i85, i84, i51, this.X[1], 15);
    int i87 = FF2(i51, i86, i85, i84, this.X[3], 11);
    int i88 = FF2(i84, i87, i86, i85, this.X[7], 8);
    int i89 = FF2(i85, i88, i87, i86, this.X[14], 6);
    int i90 = FF2(i86, i89, i88, i87, this.X[6], 6);
    int i91 = FF2(i87, i90, i89, i88, this.X[9], 14);
    int i92 = FF2(i88, i91, i90, i89, this.X[11], 12);
    int i93 = FF2(i89, i92, i91, i90, this.X[8], 13);
    int i94 = FF2(i90, i93, i92, i91, this.X[12], 5);
    int i95 = FF2(i91, i94, i93, i92, this.X[2], 14);
    int i96 = FF2(i92, i95, i94, i93, this.X[10], 13);
    int i97 = FF2(i93, i96, i95, i94, this.X[0], 13);
    int i98 = FF2(i94, i97, i96, i95, this.X[4], 7);
    int i99 = FF2(i95, i98, i97, i96, this.X[13], 5);
    int i100 = F4(i80, i83, i98, i81, this.X[1], 11);
    int i101 = F4(i81, i100, i83, i98, this.X[9], 12);
    int i102 = F4(i98, i101, i100, i83, this.X[11], 14);
    int i103 = F4(i83, i102, i101, i100, this.X[10], 15);
    int i104 = F4(i100, i103, i102, i101, this.X[0], 14);
    int i105 = F4(i101, i104, i103, i102, this.X[8], 15);
    int i106 = F4(i102, i105, i104, i103, this.X[12], 9);
    int i107 = F4(i103, i106, i105, i104, this.X[4], 8);
    int i108 = F4(i104, i107, i106, i105, this.X[13], 9);
    int i109 = F4(i105, i108, i107, i106, this.X[3], 14);
    int i110 = F4(i106, i109, i108, i107, this.X[7], 5);
    int i111 = F4(i107, i110, i109, i108, this.X[15], 6);
    int i112 = F4(i108, i111, i110, i109, this.X[14], 8);
    int i113 = F4(i109, i112, i111, i110, this.X[5], 6);
    int i114 = F4(i110, i113, i112, i111, this.X[6], 5);
    int i115 = F4(i111, i114, i113, i112, this.X[2], 12);
    int i116 = FF1(i96, i99, i82, i97, this.X[8], 15);
    int i117 = FF1(i97, i116, i99, i82, this.X[6], 5);
    int i118 = FF1(i82, i117, i116, i99, this.X[4], 8);
    int i119 = FF1(i99, i118, i117, i116, this.X[1], 11);
    int i120 = FF1(i116, i119, i118, i117, this.X[3], 14);
    int i121 = FF1(i117, i120, i119, i118, this.X[11], 14);
    int i122 = FF1(i118, i121, i120, i119, this.X[15], 6);
    int i123 = FF1(i119, i122, i121, i120, this.X[0], 14);
    int i124 = FF1(i120, i123, i122, i121, this.X[5], 6);
    int i125 = FF1(i121, i124, i123, i122, this.X[12], 9);
    int i126 = FF1(i122, i125, i124, i123, this.X[2], 12);
    int i127 = FF1(i123, i126, i125, i124, this.X[13], 9);
    int i128 = FF1(i124, i127, i126, i125, this.X[9], 12);
    int i129 = FF1(i125, i128, i127, i126, this.X[7], 5);
    int i130 = FF1(i126, i129, i128, i127, this.X[10], 15);
    int i131 = FF1(i127, i130, i129, i128, this.X[14], 8);
    this.H0 = (i112 + this.H0);
    this.H1 = (i115 + this.H1);
    this.H2 = (i114 + this.H2);
    this.H3 = (i129 + this.H3);
    this.H4 = (i128 + this.H4);
    this.H5 = (i131 + this.H5);
    this.H6 = (i130 + this.H6);
    this.H7 = (i113 + this.H7);
    this.xOff = 0;
    for (int i132 = 0; ; i132++)
    {
      int i133 = this.X.length;
      if (i132 == i133)
        return;
      this.X[i132] = 0;
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
    this.H4 = 1985229328;
    this.H5 = -19088744;
    this.H6 = -1985229329;
    this.H7 = 19088743;
    this.xOff = 0;
    for (int i = 0; ; i++)
    {
      if (i == this.X.length)
        return;
      this.X[i] = 0;
    }
  }
}