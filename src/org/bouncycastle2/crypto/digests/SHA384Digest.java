package org.bouncycastle2.crypto.digests;

import org.bouncycastle2.crypto.util.Pack;

public class SHA384Digest extends LongDigest
{
  private static final int DIGEST_LENGTH = 48;

  public SHA384Digest()
  {
  }

  public SHA384Digest(SHA384Digest paramSHA384Digest)
  {
    super(paramSHA384Digest);
  }

  public int doFinal(byte[] paramArrayOfByte, int paramInt)
  {
    finish();
    Pack.longToBigEndian(this.H1, paramArrayOfByte, paramInt);
    Pack.longToBigEndian(this.H2, paramArrayOfByte, paramInt + 8);
    Pack.longToBigEndian(this.H3, paramArrayOfByte, paramInt + 16);
    Pack.longToBigEndian(this.H4, paramArrayOfByte, paramInt + 24);
    Pack.longToBigEndian(this.H5, paramArrayOfByte, paramInt + 32);
    Pack.longToBigEndian(this.H6, paramArrayOfByte, paramInt + 40);
    reset();
    return 48;
  }

  public String getAlgorithmName()
  {
    return "SHA-384";
  }

  public int getDigestSize()
  {
    return 48;
  }

  public void reset()
  {
    super.reset();
    this.H1 = -3766243637369397544L;
    this.H2 = 7105036623409894663L;
    this.H3 = -7973340178411365097L;
    this.H4 = 1526699215303891257L;
    this.H5 = 7436329637833083697L;
    this.H6 = -8163818279084223215L;
    this.H7 = -2662702644619276377L;
    this.H8 = 5167115440072839076L;
  }
}