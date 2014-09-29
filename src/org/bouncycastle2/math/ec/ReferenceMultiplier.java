package org.bouncycastle2.math.ec;

import java.math.BigInteger;

class ReferenceMultiplier
  implements ECMultiplier
{
  public ECPoint multiply(ECPoint paramECPoint, BigInteger paramBigInteger, PreCompInfo paramPreCompInfo)
  {
    ECPoint localECPoint = paramECPoint.getCurve().getInfinity();
    int i = paramBigInteger.bitLength();
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return localECPoint;
      if (paramBigInteger.testBit(j))
        localECPoint = localECPoint.add(paramECPoint);
      paramECPoint = paramECPoint.twice();
    }
  }
}