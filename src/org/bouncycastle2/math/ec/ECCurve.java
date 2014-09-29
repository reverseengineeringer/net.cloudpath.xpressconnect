package org.bouncycastle2.math.ec;

import java.math.BigInteger;
import java.util.Random;

public abstract class ECCurve
{
  ECFieldElement a;
  ECFieldElement b;

  public abstract ECPoint createPoint(BigInteger paramBigInteger1, BigInteger paramBigInteger2, boolean paramBoolean);

  public abstract ECPoint decodePoint(byte[] paramArrayOfByte);

  public abstract ECFieldElement fromBigInteger(BigInteger paramBigInteger);

  public ECFieldElement getA()
  {
    return this.a;
  }

  public ECFieldElement getB()
  {
    return this.b;
  }

  public abstract int getFieldSize();

  public abstract ECPoint getInfinity();

  public static class F2m extends ECCurve
  {
    private BigInteger h;
    private ECPoint.F2m infinity;
    private int k1;
    private int k2;
    private int k3;
    private int m;
    private byte mu = 0;
    private BigInteger n;
    private BigInteger[] si = null;

    public F2m(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    {
      this(paramInt1, paramInt2, paramInt3, paramInt4, paramBigInteger1, paramBigInteger2, null, null);
    }

    public F2m(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
    {
      this.m = paramInt1;
      this.k1 = paramInt2;
      this.k2 = paramInt3;
      this.k3 = paramInt4;
      this.n = paramBigInteger3;
      this.h = paramBigInteger4;
      if (paramInt2 == 0)
        throw new IllegalArgumentException("k1 must be > 0");
      if (paramInt3 == 0)
      {
        if (paramInt4 != 0)
          throw new IllegalArgumentException("k3 must be 0 if k2 == 0");
      }
      else
      {
        if (paramInt3 <= paramInt2)
          throw new IllegalArgumentException("k2 must be > k1");
        if (paramInt4 <= paramInt3)
          throw new IllegalArgumentException("k3 must be > k2");
      }
      this.a = fromBigInteger(paramBigInteger1);
      this.b = fromBigInteger(paramBigInteger2);
      this.infinity = new ECPoint.F2m(this, null, null);
    }

    public F2m(int paramInt1, int paramInt2, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    {
      this(paramInt1, paramInt2, 0, 0, paramBigInteger1, paramBigInteger2, null, null);
    }

    public F2m(int paramInt1, int paramInt2, BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
    {
      this(paramInt1, paramInt2, 0, 0, paramBigInteger1, paramBigInteger2, paramBigInteger3, paramBigInteger4);
    }

    private ECPoint decompressPoint(byte[] paramArrayOfByte, int paramInt)
    {
      ECFieldElement.F2m localF2m = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, paramArrayOfByte));
      Object localObject;
      int j;
      if (localF2m.toBigInteger().equals(ECConstants.ZERO))
      {
        localObject = (ECFieldElement.F2m)this.b;
        j = 0;
        if (j < -1 + this.m);
      }
      while (true)
      {
        return new ECPoint.F2m(this, localF2m, (ECFieldElement)localObject);
        localObject = ((ECFieldElement)localObject).square();
        j++;
        break;
        ECFieldElement localECFieldElement = solveQuadradicEquation(localF2m.add(this.a).add(this.b.multiply(localF2m.square().invert())));
        if (localECFieldElement == null)
          throw new RuntimeException("Invalid point compression");
        boolean bool = localECFieldElement.toBigInteger().testBit(0);
        int i = 0;
        if (bool)
          i = 1;
        if (i != paramInt)
          localECFieldElement = localECFieldElement.add(new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, ECConstants.ONE));
        localObject = localF2m.multiply(localECFieldElement);
      }
    }

    private ECFieldElement solveQuadradicEquation(ECFieldElement paramECFieldElement)
    {
      ECFieldElement.F2m localF2m1 = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, ECConstants.ZERO);
      if (paramECFieldElement.toBigInteger().equals(ECConstants.ZERO))
        return localF2m1;
      Random localRandom = new Random();
      Object localObject;
      do
      {
        ECFieldElement.F2m localF2m2 = new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(this.m, localRandom));
        localObject = localF2m1;
        ECFieldElement localECFieldElement1 = paramECFieldElement;
        for (int i = 1; ; i++)
        {
          if (i > -1 + this.m)
          {
            if (localECFieldElement1.toBigInteger().equals(ECConstants.ZERO))
              break;
            return null;
          }
          ECFieldElement localECFieldElement2 = localECFieldElement1.square();
          localObject = ((ECFieldElement)localObject).square().add(localECFieldElement2.multiply(localF2m2));
          localECFieldElement1 = localECFieldElement2.add(paramECFieldElement);
        }
      }
      while (((ECFieldElement)localObject).square().add((ECFieldElement)localObject).toBigInteger().equals(ECConstants.ZERO));
      return localObject;
    }

    public ECPoint createPoint(BigInteger paramBigInteger1, BigInteger paramBigInteger2, boolean paramBoolean)
    {
      return new ECPoint.F2m(this, fromBigInteger(paramBigInteger1), fromBigInteger(paramBigInteger2), paramBoolean);
    }

    public ECPoint decodePoint(byte[] paramArrayOfByte)
    {
      switch (paramArrayOfByte[0])
      {
      case 1:
      case 5:
      default:
        throw new RuntimeException("Invalid point encoding 0x" + Integer.toString(paramArrayOfByte[0], 16));
      case 0:
        if (paramArrayOfByte.length > 1)
          throw new RuntimeException("Invalid point encoding");
        return getInfinity();
      case 2:
      case 3:
        byte[] arrayOfByte3 = new byte[-1 + paramArrayOfByte.length];
        System.arraycopy(paramArrayOfByte, 1, arrayOfByte3, 0, arrayOfByte3.length);
        if (paramArrayOfByte[0] == 2)
          return decompressPoint(arrayOfByte3, 0);
        return decompressPoint(arrayOfByte3, 1);
      case 4:
      case 6:
      case 7:
      }
      byte[] arrayOfByte1 = new byte[(-1 + paramArrayOfByte.length) / 2];
      byte[] arrayOfByte2 = new byte[(-1 + paramArrayOfByte.length) / 2];
      System.arraycopy(paramArrayOfByte, 1, arrayOfByte1, 0, arrayOfByte1.length);
      System.arraycopy(paramArrayOfByte, 1 + arrayOfByte1.length, arrayOfByte2, 0, arrayOfByte2.length);
      return new ECPoint.F2m(this, new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, arrayOfByte1)), new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, new BigInteger(1, arrayOfByte2)), false);
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      F2m localF2m;
      do
      {
        return true;
        if (!(paramObject instanceof F2m))
          return false;
        localF2m = (F2m)paramObject;
      }
      while ((this.m == localF2m.m) && (this.k1 == localF2m.k1) && (this.k2 == localF2m.k2) && (this.k3 == localF2m.k3) && (this.a.equals(localF2m.a)) && (this.b.equals(localF2m.b)));
      return false;
    }

    public ECFieldElement fromBigInteger(BigInteger paramBigInteger)
    {
      return new ECFieldElement.F2m(this.m, this.k1, this.k2, this.k3, paramBigInteger);
    }

    public int getFieldSize()
    {
      return this.m;
    }

    public BigInteger getH()
    {
      return this.h;
    }

    public ECPoint getInfinity()
    {
      return this.infinity;
    }

    public int getK1()
    {
      return this.k1;
    }

    public int getK2()
    {
      return this.k2;
    }

    public int getK3()
    {
      return this.k3;
    }

    public int getM()
    {
      return this.m;
    }

    byte getMu()
    {
      try
      {
        if (this.mu == 0)
          this.mu = Tnaf.getMu(this);
        byte b = this.mu;
        return b;
      }
      finally
      {
      }
    }

    public BigInteger getN()
    {
      return this.n;
    }

    BigInteger[] getSi()
    {
      try
      {
        if (this.si == null)
          this.si = Tnaf.getSi(this);
        BigInteger[] arrayOfBigInteger = this.si;
        return arrayOfBigInteger;
      }
      finally
      {
      }
    }

    public int hashCode()
    {
      return this.a.hashCode() ^ this.b.hashCode() ^ this.m ^ this.k1 ^ this.k2 ^ this.k3;
    }

    public boolean isKoblitz()
    {
      return (this.n != null) && (this.h != null) && ((this.a.toBigInteger().equals(ECConstants.ZERO)) || (this.a.toBigInteger().equals(ECConstants.ONE))) && (this.b.toBigInteger().equals(ECConstants.ONE));
    }

    public boolean isTrinomial()
    {
      return (this.k2 == 0) && (this.k3 == 0);
    }
  }

  public static class Fp extends ECCurve
  {
    ECPoint.Fp infinity;
    BigInteger q;

    public Fp(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
    {
      this.q = paramBigInteger1;
      this.a = fromBigInteger(paramBigInteger2);
      this.b = fromBigInteger(paramBigInteger3);
      this.infinity = new ECPoint.Fp(this, null, null);
    }

    public ECPoint createPoint(BigInteger paramBigInteger1, BigInteger paramBigInteger2, boolean paramBoolean)
    {
      return new ECPoint.Fp(this, fromBigInteger(paramBigInteger1), fromBigInteger(paramBigInteger2), paramBoolean);
    }

    public ECPoint decodePoint(byte[] paramArrayOfByte)
    {
      switch (paramArrayOfByte[0])
      {
      case 1:
      case 5:
      default:
        throw new RuntimeException("Invalid point encoding 0x" + Integer.toString(paramArrayOfByte[0], 16));
      case 0:
        if (paramArrayOfByte.length > 1)
          throw new RuntimeException("Invalid point encoding");
        return getInfinity();
      case 2:
      case 3:
        int i = 0x1 & paramArrayOfByte[0];
        byte[] arrayOfByte3 = new byte[-1 + paramArrayOfByte.length];
        System.arraycopy(paramArrayOfByte, 1, arrayOfByte3, 0, arrayOfByte3.length);
        ECFieldElement.Fp localFp = new ECFieldElement.Fp(this.q, new BigInteger(1, arrayOfByte3));
        ECFieldElement localECFieldElement = localFp.multiply(localFp.square().add(this.a)).add(this.b).sqrt();
        if (localECFieldElement == null)
          throw new RuntimeException("Invalid point compression");
        boolean bool = localECFieldElement.toBigInteger().testBit(0);
        int j = 0;
        if (bool)
          j = 1;
        if (j == i)
          return new ECPoint.Fp(this, localFp, localECFieldElement, true);
        return new ECPoint.Fp(this, localFp, new ECFieldElement.Fp(this.q, this.q.subtract(localECFieldElement.toBigInteger())), true);
      case 4:
      case 6:
      case 7:
      }
      byte[] arrayOfByte1 = new byte[(-1 + paramArrayOfByte.length) / 2];
      byte[] arrayOfByte2 = new byte[(-1 + paramArrayOfByte.length) / 2];
      System.arraycopy(paramArrayOfByte, 1, arrayOfByte1, 0, arrayOfByte1.length);
      System.arraycopy(paramArrayOfByte, 1 + arrayOfByte1.length, arrayOfByte2, 0, arrayOfByte2.length);
      return new ECPoint.Fp(this, new ECFieldElement.Fp(this.q, new BigInteger(1, arrayOfByte1)), new ECFieldElement.Fp(this.q, new BigInteger(1, arrayOfByte2)));
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this);
      Fp localFp;
      do
      {
        return true;
        if (!(paramObject instanceof Fp))
          return false;
        localFp = (Fp)paramObject;
      }
      while ((this.q.equals(localFp.q)) && (this.a.equals(localFp.a)) && (this.b.equals(localFp.b)));
      return false;
    }

    public ECFieldElement fromBigInteger(BigInteger paramBigInteger)
    {
      return new ECFieldElement.Fp(this.q, paramBigInteger);
    }

    public int getFieldSize()
    {
      return this.q.bitLength();
    }

    public ECPoint getInfinity()
    {
      return this.infinity;
    }

    public BigInteger getQ()
    {
      return this.q;
    }

    public int hashCode()
    {
      return this.a.hashCode() ^ this.b.hashCode() ^ this.q.hashCode();
    }
  }
}