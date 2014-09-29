package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import org.bouncycastle2.math.ec.ECConstants;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECPoint;

public class ECDomainParameters
  implements ECConstants
{
  ECPoint G;
  ECCurve curve;
  BigInteger h;
  BigInteger n;
  byte[] seed;

  public ECDomainParameters(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger)
  {
    this.curve = paramECCurve;
    this.G = paramECPoint;
    this.n = paramBigInteger;
    this.h = ONE;
    this.seed = null;
  }

  public ECDomainParameters(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this.curve = paramECCurve;
    this.G = paramECPoint;
    this.n = paramBigInteger1;
    this.h = paramBigInteger2;
    this.seed = null;
  }

  public ECDomainParameters(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfByte)
  {
    this.curve = paramECCurve;
    this.G = paramECPoint;
    this.n = paramBigInteger1;
    this.h = paramBigInteger2;
    this.seed = paramArrayOfByte;
  }

  public ECCurve getCurve()
  {
    return this.curve;
  }

  public ECPoint getG()
  {
    return this.G;
  }

  public BigInteger getH()
  {
    return this.h;
  }

  public BigInteger getN()
  {
    return this.n;
  }

  public byte[] getSeed()
  {
    return this.seed;
  }
}