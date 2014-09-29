package org.bouncycastle2.math.ec;

import java.math.BigInteger;

public class ECAlgorithms
{
  private static ECPoint implShamirsTrick(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2)
  {
    int i = Math.max(paramBigInteger1.bitLength(), paramBigInteger2.bitLength());
    ECPoint localECPoint1 = paramECPoint1.add(paramECPoint2);
    ECPoint localECPoint2 = paramECPoint1.getCurve().getInfinity();
    int j = i - 1;
    if (j < 0)
      return localECPoint2;
    localECPoint2 = localECPoint2.twice();
    if (paramBigInteger1.testBit(j))
      if (paramBigInteger2.testBit(j))
        localECPoint2 = localECPoint2.add(localECPoint1);
    while (true)
    {
      j--;
      break;
      localECPoint2 = localECPoint2.add(paramECPoint1);
      continue;
      if (paramBigInteger2.testBit(j))
        localECPoint2 = localECPoint2.add(paramECPoint2);
    }
  }

  public static ECPoint shamirsTrick(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2)
  {
    if (!paramECPoint1.getCurve().equals(paramECPoint2.getCurve()))
      throw new IllegalArgumentException("P and Q must be on same curve");
    return implShamirsTrick(paramECPoint1, paramBigInteger1, paramECPoint2, paramBigInteger2);
  }

  public static ECPoint sumOfTwoMultiplies(ECPoint paramECPoint1, BigInteger paramBigInteger1, ECPoint paramECPoint2, BigInteger paramBigInteger2)
  {
    ECCurve localECCurve = paramECPoint1.getCurve();
    if (!localECCurve.equals(paramECPoint2.getCurve()))
      throw new IllegalArgumentException("P and Q must be on same curve");
    if (((localECCurve instanceof ECCurve.F2m)) && (((ECCurve.F2m)localECCurve).isKoblitz()))
      return paramECPoint1.multiply(paramBigInteger1).add(paramECPoint2.multiply(paramBigInteger2));
    return implShamirsTrick(paramECPoint1, paramBigInteger1, paramECPoint2, paramBigInteger2);
  }
}