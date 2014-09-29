package org.bouncycastle2.math.ec;

import java.math.BigInteger;
import org.bouncycastle2.asn1.x9.X9IntegerConverter;

public abstract class ECPoint
{
  private static X9IntegerConverter converter = new X9IntegerConverter();
  ECCurve curve;
  protected ECMultiplier multiplier = null;
  protected PreCompInfo preCompInfo = null;
  protected boolean withCompression;
  ECFieldElement x;
  ECFieldElement y;

  protected ECPoint(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2)
  {
    this.curve = paramECCurve;
    this.x = paramECFieldElement1;
    this.y = paramECFieldElement2;
  }

  public abstract ECPoint add(ECPoint paramECPoint);

  void assertECMultiplier()
  {
    try
    {
      if (this.multiplier == null)
        this.multiplier = new FpNafMultiplier();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    ECPoint localECPoint;
    do
    {
      return true;
      if (!(paramObject instanceof ECPoint))
        return false;
      localECPoint = (ECPoint)paramObject;
      if (isInfinity())
        return localECPoint.isInfinity();
    }
    while ((this.x.equals(localECPoint.x)) && (this.y.equals(localECPoint.y)));
    return false;
  }

  public ECCurve getCurve()
  {
    return this.curve;
  }

  public abstract byte[] getEncoded();

  public ECFieldElement getX()
  {
    return this.x;
  }

  public ECFieldElement getY()
  {
    return this.y;
  }

  public int hashCode()
  {
    if (isInfinity())
      return 0;
    return this.x.hashCode() ^ this.y.hashCode();
  }

  public boolean isCompressed()
  {
    return this.withCompression;
  }

  public boolean isInfinity()
  {
    return (this.x == null) && (this.y == null);
  }

  public ECPoint multiply(BigInteger paramBigInteger)
  {
    if (paramBigInteger.signum() < 0)
      throw new IllegalArgumentException("The multiplicator cannot be negative");
    if (isInfinity())
      return this;
    if (paramBigInteger.signum() == 0)
      return this.curve.getInfinity();
    assertECMultiplier();
    return this.multiplier.multiply(this, paramBigInteger, this.preCompInfo);
  }

  public abstract ECPoint negate();

  void setPreCompInfo(PreCompInfo paramPreCompInfo)
  {
    this.preCompInfo = paramPreCompInfo;
  }

  public abstract ECPoint subtract(ECPoint paramECPoint);

  public abstract ECPoint twice();

  public static class F2m extends ECPoint
  {
    public F2m(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2)
    {
      this(paramECCurve, paramECFieldElement1, paramECFieldElement2, false);
    }

    public F2m(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, boolean paramBoolean)
    {
      super(paramECFieldElement1, paramECFieldElement2);
      if (((paramECFieldElement1 != null) && (paramECFieldElement2 == null)) || ((paramECFieldElement1 == null) && (paramECFieldElement2 != null)))
        throw new IllegalArgumentException("Exactly one of the field elements is null");
      if (paramECFieldElement1 != null)
      {
        ECFieldElement.F2m.checkFieldElements(this.x, this.y);
        if (paramECCurve != null)
          ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
      }
      this.withCompression = paramBoolean;
    }

    private static void checkPoints(ECPoint paramECPoint1, ECPoint paramECPoint2)
    {
      if (!paramECPoint1.curve.equals(paramECPoint2.curve))
        throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
    }

    public ECPoint add(ECPoint paramECPoint)
    {
      checkPoints(this, paramECPoint);
      return addSimple((F2m)paramECPoint);
    }

    public F2m addSimple(F2m paramF2m)
    {
      if (isInfinity())
        return paramF2m;
      if (paramF2m.isInfinity())
        return this;
      ECFieldElement.F2m localF2m1 = (ECFieldElement.F2m)paramF2m.getX();
      ECFieldElement.F2m localF2m2 = (ECFieldElement.F2m)paramF2m.getY();
      if (this.x.equals(localF2m1))
      {
        if (this.y.equals(localF2m2))
          return (F2m)twice();
        return (F2m)this.curve.getInfinity();
      }
      ECFieldElement.F2m localF2m3 = (ECFieldElement.F2m)this.y.add(localF2m2).divide(this.x.add(localF2m1));
      ECFieldElement.F2m localF2m4 = (ECFieldElement.F2m)localF2m3.square().add(localF2m3).add(this.x).add(localF2m1).add(this.curve.getA());
      ECFieldElement.F2m localF2m5 = (ECFieldElement.F2m)localF2m3.multiply(this.x.add(localF2m4)).add(localF2m4).add(this.y);
      return new F2m(this.curve, localF2m4, localF2m5, this.withCompression);
    }

    void assertECMultiplier()
    {
      try
      {
        if (this.multiplier == null)
          if (!((ECCurve.F2m)this.curve).isKoblitz())
            break label36;
        label36: for (this.multiplier = new WTauNafMultiplier(); ; this.multiplier = new WNafMultiplier())
          return;
      }
      finally
      {
      }
    }

    public byte[] getEncoded()
    {
      if (isInfinity())
        return new byte[1];
      int i = ECPoint.converter.getByteLength(this.x);
      byte[] arrayOfByte1 = ECPoint.converter.integerToBytes(getX().toBigInteger(), i);
      if (this.withCompression)
      {
        byte[] arrayOfByte4 = new byte[i + 1];
        arrayOfByte4[0] = 2;
        if ((!getX().toBigInteger().equals(ECConstants.ZERO)) && (getY().multiply(getX().invert()).toBigInteger().testBit(0)))
          arrayOfByte4[0] = 3;
        System.arraycopy(arrayOfByte1, 0, arrayOfByte4, 1, i);
        return arrayOfByte4;
      }
      byte[] arrayOfByte2 = ECPoint.converter.integerToBytes(getY().toBigInteger(), i);
      byte[] arrayOfByte3 = new byte[1 + (i + i)];
      arrayOfByte3[0] = 4;
      System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 1, i);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, i + 1, i);
      return arrayOfByte3;
    }

    public ECPoint negate()
    {
      return new F2m(this.curve, getX(), getY().add(getX()), this.withCompression);
    }

    public ECPoint subtract(ECPoint paramECPoint)
    {
      checkPoints(this, paramECPoint);
      return subtractSimple((F2m)paramECPoint);
    }

    public F2m subtractSimple(F2m paramF2m)
    {
      if (paramF2m.isInfinity())
        return this;
      return addSimple((F2m)paramF2m.negate());
    }

    public ECPoint twice()
    {
      if (isInfinity())
        return this;
      if (this.x.toBigInteger().signum() == 0)
        return this.curve.getInfinity();
      ECFieldElement.F2m localF2m1 = (ECFieldElement.F2m)this.x.add(this.y.divide(this.x));
      ECFieldElement.F2m localF2m2 = (ECFieldElement.F2m)localF2m1.square().add(localF2m1).add(this.curve.getA());
      ECFieldElement localECFieldElement = this.curve.fromBigInteger(ECConstants.ONE);
      ECFieldElement.F2m localF2m3 = (ECFieldElement.F2m)this.x.square().add(localF2m2.multiply(localF2m1.add(localECFieldElement)));
      return new F2m(this.curve, localF2m2, localF2m3, this.withCompression);
    }
  }

  public static class Fp extends ECPoint
  {
    public Fp(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2)
    {
      this(paramECCurve, paramECFieldElement1, paramECFieldElement2, false);
    }

    public Fp(ECCurve paramECCurve, ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, boolean paramBoolean)
    {
      super(paramECFieldElement1, paramECFieldElement2);
      if (((paramECFieldElement1 != null) && (paramECFieldElement2 == null)) || ((paramECFieldElement1 == null) && (paramECFieldElement2 != null)))
        throw new IllegalArgumentException("Exactly one of the field elements is null");
      this.withCompression = paramBoolean;
    }

    public ECPoint add(ECPoint paramECPoint)
    {
      if (isInfinity())
        return paramECPoint;
      if (paramECPoint.isInfinity())
        return this;
      if (this.x.equals(paramECPoint.x))
      {
        if (this.y.equals(paramECPoint.y))
          return twice();
        return this.curve.getInfinity();
      }
      ECFieldElement localECFieldElement1 = paramECPoint.y.subtract(this.y).divide(paramECPoint.x.subtract(this.x));
      ECFieldElement localECFieldElement2 = localECFieldElement1.square().subtract(this.x).subtract(paramECPoint.x);
      ECFieldElement localECFieldElement3 = localECFieldElement1.multiply(this.x.subtract(localECFieldElement2)).subtract(this.y);
      return new Fp(this.curve, localECFieldElement2, localECFieldElement3);
    }

    void assertECMultiplier()
    {
      try
      {
        if (this.multiplier == null)
          this.multiplier = new WNafMultiplier();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public byte[] getEncoded()
    {
      if (isInfinity())
        return new byte[1];
      int i = ECPoint.converter.getByteLength(this.x);
      if (this.withCompression)
      {
        if (getY().toBigInteger().testBit(0));
        for (int j = 3; ; j = 2)
        {
          byte[] arrayOfByte4 = ECPoint.converter.integerToBytes(getX().toBigInteger(), i);
          byte[] arrayOfByte5 = new byte[1 + arrayOfByte4.length];
          arrayOfByte5[0] = j;
          System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 1, arrayOfByte4.length);
          return arrayOfByte5;
        }
      }
      byte[] arrayOfByte1 = ECPoint.converter.integerToBytes(getX().toBigInteger(), i);
      byte[] arrayOfByte2 = ECPoint.converter.integerToBytes(getY().toBigInteger(), i);
      byte[] arrayOfByte3 = new byte[1 + (arrayOfByte1.length + arrayOfByte2.length)];
      arrayOfByte3[0] = 4;
      System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 1, arrayOfByte1.length);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 1 + arrayOfByte1.length, arrayOfByte2.length);
      return arrayOfByte3;
    }

    public ECPoint negate()
    {
      return new Fp(this.curve, this.x, this.y.negate(), this.withCompression);
    }

    public ECPoint subtract(ECPoint paramECPoint)
    {
      if (paramECPoint.isInfinity())
        return this;
      return add(paramECPoint.negate());
    }

    public ECPoint twice()
    {
      if (isInfinity())
        return this;
      if (this.y.toBigInteger().signum() == 0)
        return this.curve.getInfinity();
      ECFieldElement localECFieldElement1 = this.curve.fromBigInteger(BigInteger.valueOf(2L));
      ECFieldElement localECFieldElement2 = this.curve.fromBigInteger(BigInteger.valueOf(3L));
      ECFieldElement localECFieldElement3 = this.x.square().multiply(localECFieldElement2).add(this.curve.a).divide(this.y.multiply(localECFieldElement1));
      ECFieldElement localECFieldElement4 = localECFieldElement3.square().subtract(this.x.multiply(localECFieldElement1));
      ECFieldElement localECFieldElement5 = localECFieldElement3.multiply(this.x.subtract(localECFieldElement4)).subtract(this.y);
      return new Fp(this.curve, localECFieldElement4, localECFieldElement5, this.withCompression);
    }
  }
}