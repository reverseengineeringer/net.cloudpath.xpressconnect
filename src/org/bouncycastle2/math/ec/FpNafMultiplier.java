package org.bouncycastle2.math.ec;

import java.math.BigInteger;

class FpNafMultiplier
  implements ECMultiplier
{
  public ECPoint multiply(ECPoint paramECPoint, BigInteger paramBigInteger, PreCompInfo paramPreCompInfo)
  {
    BigInteger localBigInteger = paramBigInteger.multiply(BigInteger.valueOf(3L));
    ECPoint localECPoint1 = paramECPoint.negate();
    ECPoint localECPoint2 = paramECPoint;
    int i = -2 + localBigInteger.bitLength();
    if (i <= 0)
      return localECPoint2;
    localECPoint2 = localECPoint2.twice();
    boolean bool = localBigInteger.testBit(i);
    if (bool != paramBigInteger.testBit(i))
      if (!bool)
        break label89;
    label89: for (ECPoint localECPoint3 = paramECPoint; ; localECPoint3 = localECPoint1)
    {
      localECPoint2 = localECPoint2.add(localECPoint3);
      i--;
      break;
    }
  }
}