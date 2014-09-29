package org.bouncycastle2.jce.spec;

import java.math.BigInteger;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.EllipticCurve;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECCurve.F2m;
import org.bouncycastle2.math.ec.ECCurve.Fp;
import org.bouncycastle2.math.ec.ECFieldElement;

public class ECNamedCurveSpec extends ECParameterSpec
{
  private String name;

  public ECNamedCurveSpec(String paramString, EllipticCurve paramEllipticCurve, java.security.spec.ECPoint paramECPoint, BigInteger paramBigInteger)
  {
    super(paramEllipticCurve, paramECPoint, paramBigInteger, 1);
    this.name = paramString;
  }

  public ECNamedCurveSpec(String paramString, EllipticCurve paramEllipticCurve, java.security.spec.ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(paramEllipticCurve, paramECPoint, paramBigInteger1, paramBigInteger2.intValue());
    this.name = paramString;
  }

  public ECNamedCurveSpec(String paramString, ECCurve paramECCurve, org.bouncycastle2.math.ec.ECPoint paramECPoint, BigInteger paramBigInteger)
  {
    super(convertCurve(paramECCurve, null), convertPoint(paramECPoint), paramBigInteger, 1);
    this.name = paramString;
  }

  public ECNamedCurveSpec(String paramString, ECCurve paramECCurve, org.bouncycastle2.math.ec.ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    super(convertCurve(paramECCurve, null), convertPoint(paramECPoint), paramBigInteger1, paramBigInteger2.intValue());
    this.name = paramString;
  }

  public ECNamedCurveSpec(String paramString, ECCurve paramECCurve, org.bouncycastle2.math.ec.ECPoint paramECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfByte)
  {
    super(convertCurve(paramECCurve, paramArrayOfByte), convertPoint(paramECPoint), paramBigInteger1, paramBigInteger2.intValue());
    this.name = paramString;
  }

  private static EllipticCurve convertCurve(ECCurve paramECCurve, byte[] paramArrayOfByte)
  {
    if ((paramECCurve instanceof ECCurve.Fp))
      return new EllipticCurve(new ECFieldFp(((ECCurve.Fp)paramECCurve).getQ()), paramECCurve.getA().toBigInteger(), paramECCurve.getB().toBigInteger(), paramArrayOfByte);
    ECCurve.F2m localF2m = (ECCurve.F2m)paramECCurve;
    if (localF2m.isTrinomial())
    {
      int[] arrayOfInt2 = new int[1];
      arrayOfInt2[0] = localF2m.getK1();
      return new EllipticCurve(new ECFieldF2m(localF2m.getM(), arrayOfInt2), paramECCurve.getA().toBigInteger(), paramECCurve.getB().toBigInteger(), paramArrayOfByte);
    }
    int[] arrayOfInt1 = new int[3];
    arrayOfInt1[0] = localF2m.getK3();
    arrayOfInt1[1] = localF2m.getK2();
    arrayOfInt1[2] = localF2m.getK1();
    return new EllipticCurve(new ECFieldF2m(localF2m.getM(), arrayOfInt1), paramECCurve.getA().toBigInteger(), paramECCurve.getB().toBigInteger(), paramArrayOfByte);
  }

  private static java.security.spec.ECPoint convertPoint(org.bouncycastle2.math.ec.ECPoint paramECPoint)
  {
    return new java.security.spec.ECPoint(paramECPoint.getX().toBigInteger(), paramECPoint.getY().toBigInteger());
  }

  public String getName()
  {
    return this.name;
  }
}