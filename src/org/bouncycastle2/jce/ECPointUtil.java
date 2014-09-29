package org.bouncycastle2.jce;

import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.EllipticCurve;
import org.bouncycastle2.math.ec.ECCurve;
import org.bouncycastle2.math.ec.ECCurve.F2m;
import org.bouncycastle2.math.ec.ECCurve.Fp;
import org.bouncycastle2.math.ec.ECFieldElement;

public class ECPointUtil
{
  public static java.security.spec.ECPoint decodePoint(EllipticCurve paramEllipticCurve, byte[] paramArrayOfByte)
  {
    Object localObject;
    if ((paramEllipticCurve.getField() instanceof ECFieldFp))
      localObject = new ECCurve.Fp(((ECFieldFp)paramEllipticCurve.getField()).getP(), paramEllipticCurve.getA(), paramEllipticCurve.getB());
    while (true)
    {
      org.bouncycastle2.math.ec.ECPoint localECPoint = ((ECCurve)localObject).decodePoint(paramArrayOfByte);
      return new java.security.spec.ECPoint(localECPoint.getX().toBigInteger(), localECPoint.getY().toBigInteger());
      int[] arrayOfInt = ((ECFieldF2m)paramEllipticCurve.getField()).getMidTermsOfReductionPolynomial();
      if (arrayOfInt.length == 3)
        localObject = new ECCurve.F2m(((ECFieldF2m)paramEllipticCurve.getField()).getM(), arrayOfInt[2], arrayOfInt[1], arrayOfInt[0], paramEllipticCurve.getA(), paramEllipticCurve.getB());
      else
        localObject = new ECCurve.F2m(((ECFieldF2m)paramEllipticCurve.getField()).getM(), arrayOfInt[0], paramEllipticCurve.getA(), paramEllipticCurve.getB());
    }
  }
}