package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.digests.SHA256Digest;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAValidationParameters;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.BigIntegers;

public class DSAParametersGenerator
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private static final BigInteger TWO = BigInteger.valueOf(2L);
  private static final BigInteger ZERO = BigInteger.valueOf(0L);
  private int L;
  private int N;
  private int certainty;
  private SecureRandom random;

  private static BigInteger calculateGenerator_FIPS186_2(BigInteger paramBigInteger1, BigInteger paramBigInteger2, SecureRandom paramSecureRandom)
  {
    BigInteger localBigInteger1 = paramBigInteger1.subtract(ONE).divide(paramBigInteger2);
    BigInteger localBigInteger2 = paramBigInteger1.subtract(TWO);
    BigInteger localBigInteger3;
    do
      localBigInteger3 = BigIntegers.createRandomInRange(TWO, localBigInteger2, paramSecureRandom).modPow(localBigInteger1, paramBigInteger1);
    while (localBigInteger3.bitLength() <= 1);
    return localBigInteger3;
  }

  private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(BigInteger paramBigInteger1, BigInteger paramBigInteger2, SecureRandom paramSecureRandom)
  {
    return calculateGenerator_FIPS186_2(paramBigInteger1, paramBigInteger2, paramSecureRandom);
  }

  private DSAParameters generateParameters_FIPS186_2()
  {
    byte[] arrayOfByte1 = new byte[20];
    byte[] arrayOfByte2 = new byte[20];
    byte[] arrayOfByte3 = new byte[20];
    byte[] arrayOfByte4 = new byte[20];
    SHA1Digest localSHA1Digest = new SHA1Digest();
    int i = (-1 + this.L) / 160;
    byte[] arrayOfByte5 = new byte[this.L / 8];
    int j;
    label91: BigInteger localBigInteger1;
    do
    {
      this.random.nextBytes(arrayOfByte1);
      hash(localSHA1Digest, arrayOfByte1, arrayOfByte2);
      System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
      inc(arrayOfByte3);
      hash(localSHA1Digest, arrayOfByte3, arrayOfByte3);
      j = 0;
      if (j != arrayOfByte4.length)
        break;
      arrayOfByte4[0] = ((byte)(0xFFFFFF80 | arrayOfByte4[0]));
      arrayOfByte4[19] = ((byte)(0x1 | arrayOfByte4[19]));
      localBigInteger1 = new BigInteger(1, arrayOfByte4);
    }
    while (!localBigInteger1.isProbablePrime(this.certainty));
    byte[] arrayOfByte6 = Arrays.clone(arrayOfByte1);
    inc(arrayOfByte6);
    int k = 0;
    label162: int m;
    label173: BigInteger localBigInteger3;
    if (k < 4096)
    {
      m = 0;
      if (m < i)
        break label308;
      inc(arrayOfByte6);
      hash(localSHA1Digest, arrayOfByte6, arrayOfByte2);
      System.arraycopy(arrayOfByte2, arrayOfByte2.length - (arrayOfByte5.length - i * arrayOfByte2.length), arrayOfByte5, 0, arrayOfByte5.length - i * arrayOfByte2.length);
      arrayOfByte5[0] = ((byte)(0xFFFFFF80 | arrayOfByte5[0]));
      BigInteger localBigInteger2 = new BigInteger(1, arrayOfByte5);
      localBigInteger3 = localBigInteger2.subtract(localBigInteger2.mod(localBigInteger1.shiftLeft(1)).subtract(ONE));
      if (localBigInteger3.bitLength() == this.L)
        break label347;
    }
    label308: label347: 
    while (!localBigInteger3.isProbablePrime(this.certainty))
    {
      k++;
      break label162;
      break;
      arrayOfByte4[j] = ((byte)(arrayOfByte2[j] ^ arrayOfByte3[j]));
      j++;
      break label91;
      inc(arrayOfByte6);
      hash(localSHA1Digest, arrayOfByte6, arrayOfByte2);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte5, arrayOfByte5.length - (m + 1) * arrayOfByte2.length, arrayOfByte2.length);
      m++;
      break label173;
    }
    BigInteger localBigInteger4 = calculateGenerator_FIPS186_2(localBigInteger3, localBigInteger1, this.random);
    DSAValidationParameters localDSAValidationParameters = new DSAValidationParameters(arrayOfByte1, k);
    DSAParameters localDSAParameters = new DSAParameters(localBigInteger3, localBigInteger1, localBigInteger4, localDSAValidationParameters);
    return localDSAParameters;
  }

  private DSAParameters generateParameters_FIPS186_3()
  {
    SHA256Digest localSHA256Digest = new SHA256Digest();
    int i = 8 * localSHA256Digest.getDigestSize();
    byte[] arrayOfByte1 = new byte[this.N / 8];
    int j = (-1 + this.L) / i;
    int k = (-1 + this.L) % i;
    byte[] arrayOfByte2 = new byte[localSHA256Digest.getDigestSize()];
    BigInteger localBigInteger3;
    do
    {
      this.random.nextBytes(arrayOfByte1);
      hash(localSHA256Digest, arrayOfByte1, arrayOfByte2);
      BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte2);
      BigInteger localBigInteger2 = localBigInteger1.mod(ONE.shiftLeft(-1 + this.N));
      localBigInteger3 = ONE.shiftLeft(-1 + this.N).add(localBigInteger2).add(ONE).subtract(localBigInteger2.mod(TWO));
    }
    while (!localBigInteger3.isProbablePrime(this.certainty));
    byte[] arrayOfByte3 = Arrays.clone(arrayOfByte1);
    int m = 4 * this.L;
    int n = 0;
    label169: BigInteger localBigInteger4;
    int i1;
    int i2;
    label187: BigInteger localBigInteger7;
    if (n < m)
    {
      localBigInteger4 = ZERO;
      i1 = 0;
      i2 = 0;
      if (i1 <= j)
        break label255;
      BigInteger localBigInteger6 = localBigInteger4.add(ONE.shiftLeft(-1 + this.L));
      localBigInteger7 = localBigInteger6.subtract(localBigInteger6.mod(localBigInteger3.shiftLeft(1)).subtract(ONE));
      if (localBigInteger7.bitLength() == this.L)
        break label328;
    }
    label255: 
    while (!localBigInteger7.isProbablePrime(this.certainty))
    {
      n++;
      break label169;
      break;
      inc(arrayOfByte3);
      hash(localSHA256Digest, arrayOfByte3, arrayOfByte2);
      BigInteger localBigInteger5 = new BigInteger(1, arrayOfByte2);
      if (i1 == j)
        localBigInteger5 = localBigInteger5.mod(ONE.shiftLeft(k));
      localBigInteger4 = localBigInteger4.add(localBigInteger5.shiftLeft(i2));
      i1++;
      i2 += i;
      break label187;
    }
    label328: BigInteger localBigInteger8 = calculateGenerator_FIPS186_3_Unverifiable(localBigInteger7, localBigInteger3, this.random);
    DSAValidationParameters localDSAValidationParameters = new DSAValidationParameters(arrayOfByte1, n);
    DSAParameters localDSAParameters = new DSAParameters(localBigInteger7, localBigInteger3, localBigInteger8, localDSAValidationParameters);
    return localDSAParameters;
  }

  private static int getDefaultN(int paramInt)
  {
    if (paramInt > 1024)
      return 256;
    return 160;
  }

  private static void hash(Digest paramDigest, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    paramDigest.update(paramArrayOfByte1, 0, paramArrayOfByte1.length);
    paramDigest.doFinal(paramArrayOfByte2, 0);
  }

  private static void inc(byte[] paramArrayOfByte)
  {
    for (int i = -1 + paramArrayOfByte.length; ; i--)
    {
      if (i < 0);
      int j;
      do
      {
        return;
        j = (byte)(0xFF & 1 + paramArrayOfByte[i]);
        paramArrayOfByte[i] = j;
      }
      while (j != 0);
    }
  }

  private void init(int paramInt1, int paramInt2, int paramInt3, SecureRandom paramSecureRandom)
  {
    this.L = paramInt1;
    this.N = paramInt2;
    this.certainty = paramInt3;
    this.random = paramSecureRandom;
  }

  public DSAParameters generateParameters()
  {
    if (this.L > 1024)
      return generateParameters_FIPS186_3();
    return generateParameters_FIPS186_2();
  }

  public void init(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    init(paramInt1, getDefaultN(paramInt1), paramInt2, paramSecureRandom);
  }
}