package org.bouncycastle2.math.ec;

import java.math.BigInteger;

abstract interface ECMultiplier
{
  public abstract ECPoint multiply(ECPoint paramECPoint, BigInteger paramBigInteger, PreCompInfo paramPreCompInfo);
}