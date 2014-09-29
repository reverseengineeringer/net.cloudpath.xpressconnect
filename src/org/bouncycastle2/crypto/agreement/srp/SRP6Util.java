package org.bouncycastle2.crypto.agreement.srp;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.util.BigIntegers;

public class SRP6Util
{
  private static BigInteger ONE = BigInteger.valueOf(1L);
  private static BigInteger ZERO = BigInteger.valueOf(0L);

  public static BigInteger calculateK(Digest paramDigest, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    return hashPaddedPair(paramDigest, paramBigInteger1, paramBigInteger1, paramBigInteger2);
  }

  public static BigInteger calculateU(Digest paramDigest, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    return hashPaddedPair(paramDigest, paramBigInteger1, paramBigInteger2, paramBigInteger3);
  }

  public static BigInteger calculateX(Digest paramDigest, BigInteger paramBigInteger, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    byte[] arrayOfByte = new byte[paramDigest.getDigestSize()];
    paramDigest.update(paramArrayOfByte2, 0, paramArrayOfByte2.length);
    paramDigest.update((byte)58);
    paramDigest.update(paramArrayOfByte3, 0, paramArrayOfByte3.length);
    paramDigest.doFinal(arrayOfByte, 0);
    paramDigest.update(paramArrayOfByte1, 0, paramArrayOfByte1.length);
    paramDigest.update(arrayOfByte, 0, arrayOfByte.length);
    paramDigest.doFinal(arrayOfByte, 0);
    return new BigInteger(1, arrayOfByte).mod(paramBigInteger);
  }

  public static BigInteger generatePrivateValue(Digest paramDigest, BigInteger paramBigInteger1, BigInteger paramBigInteger2, SecureRandom paramSecureRandom)
  {
    int i = Math.min(256, paramBigInteger1.bitLength() / 2);
    return BigIntegers.createRandomInRange(ONE.shiftLeft(i - 1), paramBigInteger1.subtract(ONE), paramSecureRandom);
  }

  private static byte[] getPadded(BigInteger paramBigInteger, int paramInt)
  {
    Object localObject = BigIntegers.asUnsignedByteArray(paramBigInteger);
    if (localObject.length < paramInt)
    {
      byte[] arrayOfByte = new byte[paramInt];
      System.arraycopy(localObject, 0, arrayOfByte, paramInt - localObject.length, localObject.length);
      localObject = arrayOfByte;
    }
    return localObject;
  }

  private static BigInteger hashPaddedPair(Digest paramDigest, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
  {
    int i = (7 + paramBigInteger1.bitLength()) / 8;
    byte[] arrayOfByte1 = getPadded(paramBigInteger2, i);
    byte[] arrayOfByte2 = getPadded(paramBigInteger3, i);
    paramDigest.update(arrayOfByte1, 0, arrayOfByte1.length);
    paramDigest.update(arrayOfByte2, 0, arrayOfByte2.length);
    byte[] arrayOfByte3 = new byte[paramDigest.getDigestSize()];
    paramDigest.doFinal(arrayOfByte3, 0);
    return new BigInteger(1, arrayOfByte3).mod(paramBigInteger1);
  }

  public static BigInteger validatePublicValue(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    throws CryptoException
  {
    BigInteger localBigInteger = paramBigInteger2.mod(paramBigInteger1);
    if (localBigInteger.equals(ZERO))
      throw new CryptoException("Invalid public value: 0");
    return localBigInteger;
  }
}