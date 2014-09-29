package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.util.Pack;

public class SHA256Digest extends GeneralDigest
{
  private static final int DIGEST_LENGTH = 32;
  static final int[] K = { 1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998 };
  private int H1;
  private int H2;
  private int H3;
  private int H4;
  private int H5;
  private int H6;
  private int H7;
  private int H8;
  private int[] X = new int[64];
  private int xOff;

  public SHA256Digest()
  {
    reset();
  }

  public SHA256Digest(SHA256Digest paramSHA256Digest)
  {
    super(paramSHA256Digest);
    this.H1 = paramSHA256Digest.H1;
    this.H2 = paramSHA256Digest.H2;
    this.H3 = paramSHA256Digest.H3;
    this.H4 = paramSHA256Digest.H4;
    this.H5 = paramSHA256Digest.H5;
    this.H6 = paramSHA256Digest.H6;
    this.H7 = paramSHA256Digest.H7;
    this.H8 = paramSHA256Digest.H8;
    System.arraycopy(paramSHA256Digest.X, 0, this.X, 0, paramSHA256Digest.X.length);
    this.xOff = paramSHA256Digest.xOff;
  }

  private int Ch(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 ^ paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }

  private int Maj(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 ^ paramInt1 & paramInt3 ^ paramInt2 & paramInt3;
  }

  private int Sum0(int paramInt)
  {
    return (paramInt >>> 2 | paramInt << 30) ^ (paramInt >>> 13 | paramInt << 19) ^ (paramInt >>> 22 | paramInt << 10);
  }

  private int Sum1(int paramInt)
  {
    return (paramInt >>> 6 | paramInt << 26) ^ (paramInt >>> 11 | paramInt << 21) ^ (paramInt >>> 25 | paramInt << 7);
  }

  private int Theta0(int paramInt)
  {
    return (paramInt >>> 7 | paramInt << 25) ^ (paramInt >>> 18 | paramInt << 14) ^ paramInt >>> 3;
  }

  private int Theta1(int paramInt)
  {
    return (paramInt >>> 17 | paramInt << 15) ^ (paramInt >>> 19 | paramInt << 13) ^ paramInt >>> 10;
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    Pack.intToBigEndian(this.H1, paramArrayOfByte, paramInt);
    Pack.intToBigEndian(this.H2, paramArrayOfByte, paramInt + 4);
    Pack.intToBigEndian(this.H3, paramArrayOfByte, paramInt + 8);
    Pack.intToBigEndian(this.H4, paramArrayOfByte, paramInt + 12);
    Pack.intToBigEndian(this.H5, paramArrayOfByte, paramInt + 16);
    Pack.intToBigEndian(this.H6, paramArrayOfByte, paramInt + 20);
    Pack.intToBigEndian(this.H7, paramArrayOfByte, paramInt + 24);
    Pack.intToBigEndian(this.H8, paramArrayOfByte, paramInt + 28);
    reset();
    return 32;
  }

  public String getAlgorithmName()
  {
    return "SHA-256";
  }

  public int getDigestSize()
  {
    return 32;
  }

  protected void processBlock()
  {
    int i = 16;
    int j;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    if (i > 63)
    {
      j = this.H1;
      k = this.H2;
      m = this.H3;
      n = this.H4;
      i1 = this.H5;
      i2 = this.H6;
      i3 = this.H7;
      i4 = this.H8;
      i5 = 0;
      i6 = 0;
      label61: if (i6 < 8)
        break label228;
      this.H1 = (j + this.H1);
      this.H2 = (k + this.H2);
      this.H3 = (m + this.H3);
      this.H4 = (n + this.H4);
      this.H5 = (i1 + this.H5);
      this.H6 = (i2 + this.H6);
      this.H7 = (i3 + this.H7);
      this.H8 = (i4 + this.H8);
      this.xOff = 0;
    }
    for (int i30 = 0; ; i30++)
    {
      if (i30 >= 16)
      {
        return;
        this.X[i] = (Theta1(this.X[(i - 2)]) + this.X[(i - 7)] + Theta0(this.X[(i - 15)]) + this.X[(i - 16)]);
        i++;
        break;
        label228: int i7 = i4 + (Sum1(i1) + Ch(i1, i2, i3) + K[i5] + this.X[i5]);
        int i8 = n + i7;
        int i9 = i7 + (Sum0(j) + Maj(j, k, m));
        int i10 = i5 + 1;
        int i11 = i3 + (Sum1(i8) + Ch(i8, i1, i2) + K[i10] + this.X[i10]);
        int i12 = m + i11;
        int i13 = i11 + (Sum0(i9) + Maj(i9, j, k));
        int i14 = i10 + 1;
        int i15 = i2 + (Sum1(i12) + Ch(i12, i8, i1) + K[i14] + this.X[i14]);
        int i16 = k + i15;
        int i17 = i15 + (Sum0(i13) + Maj(i13, i9, j));
        int i18 = i14 + 1;
        int i19 = i1 + (Sum1(i16) + Ch(i16, i12, i8) + K[i18] + this.X[i18]);
        int i20 = j + i19;
        int i21 = i19 + (Sum0(i17) + Maj(i17, i13, i9));
        int i22 = i18 + 1;
        int i23 = i8 + (Sum1(i20) + Ch(i20, i16, i12) + K[i22] + this.X[i22]);
        i4 = i9 + i23;
        n = i23 + (Sum0(i21) + Maj(i21, i17, i13));
        int i24 = i22 + 1;
        int i25 = i12 + (Sum1(i4) + Ch(i4, i20, i16) + K[i24] + this.X[i24]);
        i3 = i13 + i25;
        m = i25 + (Sum0(n) + Maj(n, i21, i17));
        int i26 = i24 + 1;
        int i27 = i16 + (Sum1(i3) + Ch(i3, i4, i20) + K[i26] + this.X[i26]);
        i2 = i17 + i27;
        k = i27 + (Sum0(m) + Maj(m, n, i21));
        int i28 = i26 + 1;
        int i29 = i20 + (Sum1(i2) + Ch(i2, i3, i4) + K[i28] + this.X[i28]);
        i1 = i21 + i29;
        j = i29 + (Sum0(k) + Maj(k, m, n));
        i5 = i28 + 1;
        i6++;
        break label61;
      }
      this.X[i30] = 0;
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
    this.H1 = 1779033703;
    this.H2 = -1150833019;
    this.H3 = 1013904242;
    this.H4 = -1521486534;
    this.H5 = 1359893119;
    this.H6 = -1694144372;
    this.H7 = 528734635;
    this.H8 = 1541459225;
    this.xOff = 0;
    for (int i = 0; ; i++)
    {
      if (i == this.X.length)
        return;
      this.X[i] = 0;
    }
  }
}