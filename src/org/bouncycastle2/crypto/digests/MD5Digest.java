package org.bouncycastle2.crypto.digests;

public class MD5Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 16;
  private static final int S11 = 7;
  private static final int S12 = 12;
  private static final int S13 = 17;
  private static final int S14 = 22;
  private static final int S21 = 5;
  private static final int S22 = 9;
  private static final int S23 = 14;
  private static final int S24 = 20;
  private static final int S31 = 4;
  private static final int S32 = 11;
  private static final int S33 = 16;
  private static final int S34 = 23;
  private static final int S41 = 6;
  private static final int S42 = 10;
  private static final int S43 = 15;
  private static final int S44 = 21;
  private int H1;
  private int H2;
  private int H3;
  private int H4;
  private int[] X = new int[16];
  private int xOff;

  public MD5Digest()
  {
    reset();
  }

  public MD5Digest(MD5Digest paramMD5Digest)
  {
    super(paramMD5Digest);
    this.H1 = paramMD5Digest.H1;
    this.H2 = paramMD5Digest.H2;
    this.H3 = paramMD5Digest.H3;
    this.H4 = paramMD5Digest.H4;
    System.arraycopy(paramMD5Digest.X, 0, this.X, 0, paramMD5Digest.X.length);
    this.xOff = paramMD5Digest.xOff;
  }

  private int F(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }

  private int G(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt3 | paramInt2 & (paramInt3 ^ 0xFFFFFFFF);
  }

  private int H(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 ^ paramInt2);
  }

  private int K(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt2 ^ (paramInt1 | paramInt3 ^ 0xFFFFFFFF);
  }

  private int rotateLeft(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
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
    unpackWord(this.H1, paramArrayOfByte, paramInt);
    unpackWord(this.H2, paramArrayOfByte, paramInt + 4);
    unpackWord(this.H3, paramArrayOfByte, paramInt + 8);
    unpackWord(this.H4, paramArrayOfByte, paramInt + 12);
    reset();
    return 16;
  }

  public String getAlgorithmName()
  {
    return "MD5";
  }

  public int getDigestSize()
  {
    return 16;
  }

  protected void processBlock()
  {
    int i = this.H1;
    int j = this.H2;
    int k = this.H3;
    int m = this.H4;
    int n = j + rotateLeft(-680876936 + (i + F(j, k, m) + this.X[0]), 7);
    int i1 = n + rotateLeft(-389564586 + (m + F(n, j, k) + this.X[1]), 12);
    int i2 = i1 + rotateLeft(606105819 + (k + F(i1, n, j) + this.X[2]), 17);
    int i3 = i2 + rotateLeft(-1044525330 + (j + F(i2, i1, n) + this.X[3]), 22);
    int i4 = i3 + rotateLeft(-176418897 + (n + F(i3, i2, i1) + this.X[4]), 7);
    int i5 = i4 + rotateLeft(1200080426 + (i1 + F(i4, i3, i2) + this.X[5]), 12);
    int i6 = i5 + rotateLeft(-1473231341 + (i2 + F(i5, i4, i3) + this.X[6]), 17);
    int i7 = i6 + rotateLeft(-45705983 + (i3 + F(i6, i5, i4) + this.X[7]), 22);
    int i8 = i7 + rotateLeft(1770035416 + (i4 + F(i7, i6, i5) + this.X[8]), 7);
    int i9 = i8 + rotateLeft(-1958414417 + (i5 + F(i8, i7, i6) + this.X[9]), 12);
    int i10 = i9 + rotateLeft(-42063 + (i6 + F(i9, i8, i7) + this.X[10]), 17);
    int i11 = i10 + rotateLeft(-1990404162 + (i7 + F(i10, i9, i8) + this.X[11]), 22);
    int i12 = i11 + rotateLeft(1804603682 + (i8 + F(i11, i10, i9) + this.X[12]), 7);
    int i13 = i12 + rotateLeft(-40341101 + (i9 + F(i12, i11, i10) + this.X[13]), 12);
    int i14 = i13 + rotateLeft(-1502002290 + (i10 + F(i13, i12, i11) + this.X[14]), 17);
    int i15 = i14 + rotateLeft(1236535329 + (i11 + F(i14, i13, i12) + this.X[15]), 22);
    int i16 = i15 + rotateLeft(-165796510 + (i12 + G(i15, i14, i13) + this.X[1]), 5);
    int i17 = i16 + rotateLeft(-1069501632 + (i13 + G(i16, i15, i14) + this.X[6]), 9);
    int i18 = i17 + rotateLeft(643717713 + (i14 + G(i17, i16, i15) + this.X[11]), 14);
    int i19 = i18 + rotateLeft(-373897302 + (i15 + G(i18, i17, i16) + this.X[0]), 20);
    int i20 = i19 + rotateLeft(-701558691 + (i16 + G(i19, i18, i17) + this.X[5]), 5);
    int i21 = i20 + rotateLeft(38016083 + (i17 + G(i20, i19, i18) + this.X[10]), 9);
    int i22 = i21 + rotateLeft(-660478335 + (i18 + G(i21, i20, i19) + this.X[15]), 14);
    int i23 = i22 + rotateLeft(-405537848 + (i19 + G(i22, i21, i20) + this.X[4]), 20);
    int i24 = i23 + rotateLeft(568446438 + (i20 + G(i23, i22, i21) + this.X[9]), 5);
    int i25 = i24 + rotateLeft(-1019803690 + (i21 + G(i24, i23, i22) + this.X[14]), 9);
    int i26 = i25 + rotateLeft(-187363961 + (i22 + G(i25, i24, i23) + this.X[3]), 14);
    int i27 = i26 + rotateLeft(1163531501 + (i23 + G(i26, i25, i24) + this.X[8]), 20);
    int i28 = i27 + rotateLeft(-1444681467 + (i24 + G(i27, i26, i25) + this.X[13]), 5);
    int i29 = i28 + rotateLeft(-51403784 + (i25 + G(i28, i27, i26) + this.X[2]), 9);
    int i30 = i29 + rotateLeft(1735328473 + (i26 + G(i29, i28, i27) + this.X[7]), 14);
    int i31 = i30 + rotateLeft(-1926607734 + (i27 + G(i30, i29, i28) + this.X[12]), 20);
    int i32 = i31 + rotateLeft(-378558 + (i28 + H(i31, i30, i29) + this.X[5]), 4);
    int i33 = i32 + rotateLeft(-2022574463 + (i29 + H(i32, i31, i30) + this.X[8]), 11);
    int i34 = i33 + rotateLeft(1839030562 + (i30 + H(i33, i32, i31) + this.X[11]), 16);
    int i35 = i34 + rotateLeft(-35309556 + (i31 + H(i34, i33, i32) + this.X[14]), 23);
    int i36 = i35 + rotateLeft(-1530992060 + (i32 + H(i35, i34, i33) + this.X[1]), 4);
    int i37 = i36 + rotateLeft(1272893353 + (i33 + H(i36, i35, i34) + this.X[4]), 11);
    int i38 = i37 + rotateLeft(-155497632 + (i34 + H(i37, i36, i35) + this.X[7]), 16);
    int i39 = i38 + rotateLeft(-1094730640 + (i35 + H(i38, i37, i36) + this.X[10]), 23);
    int i40 = i39 + rotateLeft(681279174 + (i36 + H(i39, i38, i37) + this.X[13]), 4);
    int i41 = i40 + rotateLeft(-358537222 + (i37 + H(i40, i39, i38) + this.X[0]), 11);
    int i42 = i41 + rotateLeft(-722521979 + (i38 + H(i41, i40, i39) + this.X[3]), 16);
    int i43 = i42 + rotateLeft(76029189 + (i39 + H(i42, i41, i40) + this.X[6]), 23);
    int i44 = i43 + rotateLeft(-640364487 + (i40 + H(i43, i42, i41) + this.X[9]), 4);
    int i45 = i44 + rotateLeft(-421815835 + (i41 + H(i44, i43, i42) + this.X[12]), 11);
    int i46 = i45 + rotateLeft(530742520 + (i42 + H(i45, i44, i43) + this.X[15]), 16);
    int i47 = i46 + rotateLeft(-995338651 + (i43 + H(i46, i45, i44) + this.X[2]), 23);
    int i48 = i47 + rotateLeft(-198630844 + (i44 + K(i47, i46, i45) + this.X[0]), 6);
    int i49 = i48 + rotateLeft(1126891415 + (i45 + K(i48, i47, i46) + this.X[7]), 10);
    int i50 = i49 + rotateLeft(-1416354905 + (i46 + K(i49, i48, i47) + this.X[14]), 15);
    int i51 = i50 + rotateLeft(-57434055 + (i47 + K(i50, i49, i48) + this.X[5]), 21);
    int i52 = i51 + rotateLeft(1700485571 + (i48 + K(i51, i50, i49) + this.X[12]), 6);
    int i53 = i52 + rotateLeft(-1894986606 + (i49 + K(i52, i51, i50) + this.X[3]), 10);
    int i54 = i53 + rotateLeft(-1051523 + (i50 + K(i53, i52, i51) + this.X[10]), 15);
    int i55 = i54 + rotateLeft(-2054922799 + (i51 + K(i54, i53, i52) + this.X[1]), 21);
    int i56 = i55 + rotateLeft(1873313359 + (i52 + K(i55, i54, i53) + this.X[8]), 6);
    int i57 = i56 + rotateLeft(-30611744 + (i53 + K(i56, i55, i54) + this.X[15]), 10);
    int i58 = i57 + rotateLeft(-1560198380 + (i54 + K(i57, i56, i55) + this.X[6]), 15);
    int i59 = i58 + rotateLeft(1309151649 + (i55 + K(i58, i57, i56) + this.X[13]), 21);
    int i60 = i59 + rotateLeft(-145523070 + (i56 + K(i59, i58, i57) + this.X[4]), 6);
    int i61 = i60 + rotateLeft(-1120210379 + (i57 + K(i60, i59, i58) + this.X[11]), 10);
    int i62 = i61 + rotateLeft(718787259 + (i58 + K(i61, i60, i59) + this.X[2]), 15);
    int i63 = i62 + rotateLeft(-343485551 + (i59 + K(i62, i61, i60) + this.X[9]), 21);
    this.H1 = (i60 + this.H1);
    this.H2 = (i63 + this.H2);
    this.H3 = (i62 + this.H3);
    this.H4 = (i61 + this.H4);
    this.xOff = 0;
    for (int i64 = 0; ; i64++)
    {
      if (i64 == this.X.length)
        return;
      this.X[i64] = 0;
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
    this.H1 = 1732584193;
    this.H2 = -271733879;
    this.H3 = -1732584194;
    this.H4 = 271733878;
    this.xOff = 0;
    for (int i = 0; ; i++)
    {
      if (i == this.X.length)
        return;
      this.X[i] = 0;
    }
  }
}