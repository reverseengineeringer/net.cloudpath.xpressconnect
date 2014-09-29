package org.bouncycastle2.crypto.digests;

public class MD4Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 16;
  private static final int S11 = 3;
  private static final int S12 = 7;
  private static final int S13 = 11;
  private static final int S14 = 19;
  private static final int S21 = 3;
  private static final int S22 = 5;
  private static final int S23 = 9;
  private static final int S24 = 13;
  private static final int S31 = 3;
  private static final int S32 = 9;
  private static final int S33 = 11;
  private static final int S34 = 15;
  private int H1;
  private int H2;
  private int H3;
  private int H4;
  private int[] X = new int[16];
  private int xOff;

  public MD4Digest()
  {
    reset();
  }

  public MD4Digest(MD4Digest paramMD4Digest)
  {
    super(paramMD4Digest);
    this.H1 = paramMD4Digest.H1;
    this.H2 = paramMD4Digest.H2;
    this.H3 = paramMD4Digest.H3;
    this.H4 = paramMD4Digest.H4;
    System.arraycopy(paramMD4Digest.X, 0, this.X, 0, paramMD4Digest.X.length);
    this.xOff = paramMD4Digest.xOff;
  }

  private int F(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }

  private int G(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
  }

  private int H(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ (paramInt1 ^ paramInt2);
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
    return "MD4";
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
    int n = rotateLeft(i + F(j, k, m) + this.X[0], 3);
    int i1 = rotateLeft(m + F(n, j, k) + this.X[1], 7);
    int i2 = rotateLeft(k + F(i1, n, j) + this.X[2], 11);
    int i3 = rotateLeft(j + F(i2, i1, n) + this.X[3], 19);
    int i4 = rotateLeft(n + F(i3, i2, i1) + this.X[4], 3);
    int i5 = rotateLeft(i1 + F(i4, i3, i2) + this.X[5], 7);
    int i6 = rotateLeft(i2 + F(i5, i4, i3) + this.X[6], 11);
    int i7 = rotateLeft(i3 + F(i6, i5, i4) + this.X[7], 19);
    int i8 = rotateLeft(i4 + F(i7, i6, i5) + this.X[8], 3);
    int i9 = rotateLeft(i5 + F(i8, i7, i6) + this.X[9], 7);
    int i10 = rotateLeft(i6 + F(i9, i8, i7) + this.X[10], 11);
    int i11 = rotateLeft(i7 + F(i10, i9, i8) + this.X[11], 19);
    int i12 = rotateLeft(i8 + F(i11, i10, i9) + this.X[12], 3);
    int i13 = rotateLeft(i9 + F(i12, i11, i10) + this.X[13], 7);
    int i14 = rotateLeft(i10 + F(i13, i12, i11) + this.X[14], 11);
    int i15 = rotateLeft(i11 + F(i14, i13, i12) + this.X[15], 19);
    int i16 = rotateLeft(1518500249 + (i12 + G(i15, i14, i13) + this.X[0]), 3);
    int i17 = rotateLeft(1518500249 + (i13 + G(i16, i15, i14) + this.X[4]), 5);
    int i18 = rotateLeft(1518500249 + (i14 + G(i17, i16, i15) + this.X[8]), 9);
    int i19 = rotateLeft(1518500249 + (i15 + G(i18, i17, i16) + this.X[12]), 13);
    int i20 = rotateLeft(1518500249 + (i16 + G(i19, i18, i17) + this.X[1]), 3);
    int i21 = rotateLeft(1518500249 + (i17 + G(i20, i19, i18) + this.X[5]), 5);
    int i22 = rotateLeft(1518500249 + (i18 + G(i21, i20, i19) + this.X[9]), 9);
    int i23 = rotateLeft(1518500249 + (i19 + G(i22, i21, i20) + this.X[13]), 13);
    int i24 = rotateLeft(1518500249 + (i20 + G(i23, i22, i21) + this.X[2]), 3);
    int i25 = rotateLeft(1518500249 + (i21 + G(i24, i23, i22) + this.X[6]), 5);
    int i26 = rotateLeft(1518500249 + (i22 + G(i25, i24, i23) + this.X[10]), 9);
    int i27 = rotateLeft(1518500249 + (i23 + G(i26, i25, i24) + this.X[14]), 13);
    int i28 = rotateLeft(1518500249 + (i24 + G(i27, i26, i25) + this.X[3]), 3);
    int i29 = rotateLeft(1518500249 + (i25 + G(i28, i27, i26) + this.X[7]), 5);
    int i30 = rotateLeft(1518500249 + (i26 + G(i29, i28, i27) + this.X[11]), 9);
    int i31 = rotateLeft(1518500249 + (i27 + G(i30, i29, i28) + this.X[15]), 13);
    int i32 = rotateLeft(1859775393 + (i28 + H(i31, i30, i29) + this.X[0]), 3);
    int i33 = rotateLeft(1859775393 + (i29 + H(i32, i31, i30) + this.X[8]), 9);
    int i34 = rotateLeft(1859775393 + (i30 + H(i33, i32, i31) + this.X[4]), 11);
    int i35 = rotateLeft(1859775393 + (i31 + H(i34, i33, i32) + this.X[12]), 15);
    int i36 = rotateLeft(1859775393 + (i32 + H(i35, i34, i33) + this.X[2]), 3);
    int i37 = rotateLeft(1859775393 + (i33 + H(i36, i35, i34) + this.X[10]), 9);
    int i38 = rotateLeft(1859775393 + (i34 + H(i37, i36, i35) + this.X[6]), 11);
    int i39 = rotateLeft(1859775393 + (i35 + H(i38, i37, i36) + this.X[14]), 15);
    int i40 = rotateLeft(1859775393 + (i36 + H(i39, i38, i37) + this.X[1]), 3);
    int i41 = rotateLeft(1859775393 + (i37 + H(i40, i39, i38) + this.X[9]), 9);
    int i42 = rotateLeft(1859775393 + (i38 + H(i41, i40, i39) + this.X[5]), 11);
    int i43 = rotateLeft(1859775393 + (i39 + H(i42, i41, i40) + this.X[13]), 15);
    int i44 = rotateLeft(1859775393 + (i40 + H(i43, i42, i41) + this.X[3]), 3);
    int i45 = rotateLeft(1859775393 + (i41 + H(i44, i43, i42) + this.X[11]), 9);
    int i46 = rotateLeft(1859775393 + (i42 + H(i45, i44, i43) + this.X[7]), 11);
    int i47 = rotateLeft(1859775393 + (i43 + H(i46, i45, i44) + this.X[15]), 15);
    this.H1 = (i44 + this.H1);
    this.H2 = (i47 + this.H2);
    this.H3 = (i46 + this.H3);
    this.H4 = (i45 + this.H4);
    this.xOff = 0;
    for (int i48 = 0; ; i48++)
    {
      if (i48 == this.X.length)
        return;
      this.X[i48] = 0;
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