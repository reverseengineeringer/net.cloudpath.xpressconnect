package org.bouncycastle2.jce.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECPoint;

public class ECParameterSpec
  implements AlgorithmParameterSpec
{
  private ECPoint G;
  private ECCurve curve;
  private BigInteger h;
  private BigInteger n;
  private byte[] seed;

  public ECParameterSpec(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger)
  {
    this.curve = paramECCurve;
    this.G = paramECPoint;
    this.n = paramBigInteger;
    this.h = BigInteger.valueOf(1L);
    this.seed = null;
  }

  public ECParameterSpec(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    this.curve = paramECCurve;
    this.G = paramECPoint;
    this.n = paramBigInteger1;
    this.h = paramBigInteger2;
    this.seed = null;
  }

  public ECParameterSpec(ECCurve paramECCurve, ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfByte)
  {
    this.curve = paramECCurve;
    this.G = paramECPoint;
    this.n = paramBigInteger1;
    this.h = paramBigInteger2;
    this.seed = paramArrayOfByte;
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof ECParameterSpec));
    ECParameterSpec localECParameterSpec;
    do
    {
      return false;
      localECParameterSpec = (ECParameterSpec)paramObject;
    }
    while ((!getCurve().equals(localECParameterSpec.getCurve())) || (!getG().equals(localECParameterSpec.getG())));
    return true;
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

  public int hashCode()
  {
    return getCurve().hashCode() ^ getG().hashCode();
  }
}