package org.bouncycastle2.math.ec;

import java.math.BigInteger;
import java.util.Random;

public abstract class ECFieldElement
  implements ECConstants
{
  public abstract ECFieldElement add(ECFieldElement paramECFieldElement);

  public abstract ECFieldElement divide(ECFieldElement paramECFieldElement);

  public abstract String getFieldName();

  public abstract int getFieldSize();

  public abstract ECFieldElement invert();

  public abstract ECFieldElement multiply(ECFieldElement paramECFieldElement);

  public abstract ECFieldElement negate();

  public abstract ECFieldElement sqrt();

  public abstract ECFieldElement square();

  public abstract ECFieldElement subtract(ECFieldElement paramECFieldElement);

  public abstract BigInteger toBigInteger();

  public String toString()
  {
    return toBigInteger().toString(2);
  }

  public static class F2m extends ECFieldElement
  {
    public static final int GNB = 1;
    public static final int PPB = 3;
    public static final int TPB = 2;
    private int k1;
    private int k2;
    private int k3;
    private int m;
    private int representation;
    private int t;
    private IntArray x;

    public F2m(int paramInt1, int paramInt2, int paramInt3, int paramInt4, BigInteger paramBigInteger)
    {
      this.t = (paramInt1 + 31 >> 5);
      this.x = new IntArray(paramBigInteger, this.t);
      if ((paramInt3 == 0) && (paramInt4 == 0));
      for (this.representation = 2; paramBigInteger.signum() < 0; this.representation = 3)
      {
        throw new IllegalArgumentException("x value cannot be negative");
        if (paramInt3 >= paramInt4)
          throw new IllegalArgumentException("k2 must be smaller than k3");
        if (paramInt3 <= 0)
          throw new IllegalArgumentException("k2 must be larger than 0");
      }
      this.m = paramInt1;
      this.k1 = paramInt2;
      this.k2 = paramInt3;
      this.k3 = paramInt4;
    }

    private F2m(int paramInt1, int paramInt2, int paramInt3, int paramInt4, IntArray paramIntArray)
    {
      this.t = (paramInt1 + 31 >> 5);
      this.x = paramIntArray;
      this.m = paramInt1;
      this.k1 = paramInt2;
      this.k2 = paramInt3;
      this.k3 = paramInt4;
      if ((paramInt3 == 0) && (paramInt4 == 0))
      {
        this.representation = 2;
        return;
      }
      this.representation = 3;
    }

    public F2m(int paramInt1, int paramInt2, BigInteger paramBigInteger)
    {
      this(paramInt1, paramInt2, 0, 0, paramBigInteger);
    }

    public static void checkFieldElements(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2)
    {
      if ((!(paramECFieldElement1 instanceof F2m)) || (!(paramECFieldElement2 instanceof F2m)))
        throw new IllegalArgumentException("Field elements are not both instances of ECFieldElement.F2m");
      F2m localF2m1 = (F2m)paramECFieldElement1;
      F2m localF2m2 = (F2m)paramECFieldElement2;
      if ((localF2m1.m != localF2m2.m) || (localF2m1.k1 != localF2m2.k1) || (localF2m1.k2 != localF2m2.k2) || (localF2m1.k3 != localF2m2.k3))
        throw new IllegalArgumentException("Field elements are not elements of the same field F2m");
      if (localF2m1.representation != localF2m2.representation)
        throw new IllegalArgumentException("One of the field elements are not elements has incorrect representation");
    }

    public ECFieldElement add(ECFieldElement paramECFieldElement)
    {
      IntArray localIntArray = (IntArray)this.x.clone();
      localIntArray.addShifted(((F2m)paramECFieldElement).x, 0);
      return new F2m(this.m, this.k1, this.k2, this.k3, localIntArray);
    }

    public ECFieldElement divide(ECFieldElement paramECFieldElement)
    {
      return multiply(paramECFieldElement.invert());
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
      while ((this.m == localF2m.m) && (this.k1 == localF2m.k1) && (this.k2 == localF2m.k2) && (this.k3 == localF2m.k3) && (this.representation == localF2m.representation) && (this.x.equals(localF2m.x)));
      return false;
    }

    public String getFieldName()
    {
      return "F2m";
    }

    public int getFieldSize()
    {
      return this.m;
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

    public int getRepresentation()
    {
      return this.representation;
    }

    public int hashCode()
    {
      return this.x.hashCode() ^ this.m ^ this.k1 ^ this.k2 ^ this.k3;
    }

    public ECFieldElement invert()
    {
      Object localObject1 = (IntArray)this.x.clone();
      Object localObject2 = new IntArray(this.t);
      ((IntArray)localObject2).setBit(this.m);
      ((IntArray)localObject2).setBit(0);
      ((IntArray)localObject2).setBit(this.k1);
      if (this.representation == 3)
      {
        ((IntArray)localObject2).setBit(this.k2);
        ((IntArray)localObject2).setBit(this.k3);
      }
      Object localObject3 = new IntArray(this.t);
      ((IntArray)localObject3).setBit(0);
      Object localObject4 = new IntArray(this.t);
      while (true)
      {
        if (((IntArray)localObject1).isZero())
          return new F2m(this.m, this.k1, this.k2, this.k3, (IntArray)localObject4);
        int i = ((IntArray)localObject1).bitLength() - ((IntArray)localObject2).bitLength();
        if (i < 0)
        {
          Object localObject5 = localObject1;
          localObject1 = localObject2;
          localObject2 = localObject5;
          Object localObject6 = localObject3;
          localObject3 = localObject4;
          localObject4 = localObject6;
          i = -i;
        }
        int j = i >> 5;
        int k = i & 0x1F;
        ((IntArray)localObject1).addShifted(((IntArray)localObject2).shiftLeft(k), j);
        ((IntArray)localObject3).addShifted(((IntArray)localObject4).shiftLeft(k), j);
      }
    }

    public ECFieldElement multiply(ECFieldElement paramECFieldElement)
    {
      F2m localF2m = (F2m)paramECFieldElement;
      IntArray localIntArray = this.x.multiply(localF2m.x, this.m);
      int i = this.m;
      int[] arrayOfInt = new int[3];
      arrayOfInt[0] = this.k1;
      arrayOfInt[1] = this.k2;
      arrayOfInt[2] = this.k3;
      localIntArray.reduce(i, arrayOfInt);
      return new F2m(this.m, this.k1, this.k2, this.k3, localIntArray);
    }

    public ECFieldElement negate()
    {
      return this;
    }

    public ECFieldElement sqrt()
    {
      throw new RuntimeException("Not implemented");
    }

    public ECFieldElement square()
    {
      IntArray localIntArray = this.x.square(this.m);
      int i = this.m;
      int[] arrayOfInt = new int[3];
      arrayOfInt[0] = this.k1;
      arrayOfInt[1] = this.k2;
      arrayOfInt[2] = this.k3;
      localIntArray.reduce(i, arrayOfInt);
      return new F2m(this.m, this.k1, this.k2, this.k3, localIntArray);
    }

    public ECFieldElement subtract(ECFieldElement paramECFieldElement)
    {
      return add(paramECFieldElement);
    }

    public BigInteger toBigInteger()
    {
      return this.x.toBigInteger();
    }
  }

  public static class Fp extends ECFieldElement
  {
    BigInteger q;
    BigInteger x;

    public Fp(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
    {
      this.x = paramBigInteger2;
      if (paramBigInteger2.compareTo(paramBigInteger1) >= 0)
        throw new IllegalArgumentException("x value too large in field element");
      this.q = paramBigInteger1;
    }

    private static BigInteger[] lucasSequence(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
    {
      int i = paramBigInteger4.bitLength();
      int j = paramBigInteger4.getLowestSetBit();
      BigInteger localBigInteger1 = ECConstants.ONE;
      BigInteger localBigInteger2 = ECConstants.TWO;
      BigInteger localBigInteger3 = paramBigInteger2;
      BigInteger localBigInteger4 = ECConstants.ONE;
      BigInteger localBigInteger5 = ECConstants.ONE;
      int k = i - 1;
      BigInteger localBigInteger8;
      BigInteger localBigInteger9;
      BigInteger localBigInteger10;
      if (k < j + 1)
      {
        BigInteger localBigInteger6 = localBigInteger4.multiply(localBigInteger5).mod(paramBigInteger1);
        BigInteger localBigInteger7 = localBigInteger6.multiply(paramBigInteger3).mod(paramBigInteger1);
        localBigInteger8 = localBigInteger1.multiply(localBigInteger2).subtract(localBigInteger6).mod(paramBigInteger1);
        localBigInteger9 = localBigInteger3.multiply(localBigInteger2).subtract(paramBigInteger2.multiply(localBigInteger6)).mod(paramBigInteger1);
        localBigInteger10 = localBigInteger6.multiply(localBigInteger7).mod(paramBigInteger1);
      }
      for (int m = 1; ; m++)
      {
        if (m > j)
        {
          return new BigInteger[] { localBigInteger8, localBigInteger9 };
          localBigInteger4 = localBigInteger4.multiply(localBigInteger5).mod(paramBigInteger1);
          if (paramBigInteger4.testBit(k))
          {
            localBigInteger5 = localBigInteger4.multiply(paramBigInteger3).mod(paramBigInteger1);
            localBigInteger1 = localBigInteger1.multiply(localBigInteger3).mod(paramBigInteger1);
            localBigInteger2 = localBigInteger3.multiply(localBigInteger2).subtract(paramBigInteger2.multiply(localBigInteger4)).mod(paramBigInteger1);
            localBigInteger3 = localBigInteger3.multiply(localBigInteger3).subtract(localBigInteger5.shiftLeft(1)).mod(paramBigInteger1);
          }
          while (true)
          {
            k--;
            break;
            localBigInteger5 = localBigInteger4;
            localBigInteger1 = localBigInteger1.multiply(localBigInteger2).subtract(localBigInteger4).mod(paramBigInteger1);
            localBigInteger3 = localBigInteger3.multiply(localBigInteger2).subtract(paramBigInteger2.multiply(localBigInteger4)).mod(paramBigInteger1);
            localBigInteger2 = localBigInteger2.multiply(localBigInteger2).subtract(localBigInteger4.shiftLeft(1)).mod(paramBigInteger1);
          }
        }
        localBigInteger8 = localBigInteger8.multiply(localBigInteger9).mod(paramBigInteger1);
        localBigInteger9 = localBigInteger9.multiply(localBigInteger9).subtract(localBigInteger10.shiftLeft(1)).mod(paramBigInteger1);
        localBigInteger10 = localBigInteger10.multiply(localBigInteger10).mod(paramBigInteger1);
      }
    }

    public ECFieldElement add(ECFieldElement paramECFieldElement)
    {
      return new Fp(this.q, this.x.add(paramECFieldElement.toBigInteger()).mod(this.q));
    }

    public ECFieldElement divide(ECFieldElement paramECFieldElement)
    {
      return new Fp(this.q, this.x.multiply(paramECFieldElement.toBigInteger().modInverse(this.q)).mod(this.q));
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
      while ((this.q.equals(localFp.q)) && (this.x.equals(localFp.x)));
      return false;
    }

    public String getFieldName()
    {
      return "Fp";
    }

    public int getFieldSize()
    {
      return this.q.bitLength();
    }

    public BigInteger getQ()
    {
      return this.q;
    }

    public int hashCode()
    {
      return this.q.hashCode() ^ this.x.hashCode();
    }

    public ECFieldElement invert()
    {
      return new Fp(this.q, this.x.modInverse(this.q));
    }

    public ECFieldElement multiply(ECFieldElement paramECFieldElement)
    {
      return new Fp(this.q, this.x.multiply(paramECFieldElement.toBigInteger()).mod(this.q));
    }

    public ECFieldElement negate()
    {
      return new Fp(this.q, this.x.negate().mod(this.q));
    }

    public ECFieldElement sqrt()
    {
      if (!this.q.testBit(0))
        throw new RuntimeException("not done yet");
      if (this.q.testBit(1))
      {
        Fp localFp = new Fp(this.q, this.x.modPow(this.q.shiftRight(2).add(ECConstants.ONE), this.q));
        if (localFp.square().equals(this))
          return localFp;
        return null;
      }
      BigInteger localBigInteger1 = this.q.subtract(ECConstants.ONE);
      BigInteger localBigInteger2 = localBigInteger1.shiftRight(1);
      if (!this.x.modPow(localBigInteger2, this.q).equals(ECConstants.ONE))
        return null;
      BigInteger localBigInteger3 = localBigInteger1.shiftRight(2).shiftLeft(1).add(ECConstants.ONE);
      BigInteger localBigInteger4 = this.x;
      BigInteger localBigInteger5 = localBigInteger4.shiftLeft(2).mod(this.q);
      Random localRandom = new Random();
      BigInteger localBigInteger7;
      do
      {
        BigInteger localBigInteger6;
        do
          localBigInteger6 = new BigInteger(this.q.bitLength(), localRandom);
        while ((localBigInteger6.compareTo(this.q) >= 0) || (!localBigInteger6.multiply(localBigInteger6).subtract(localBigInteger5).modPow(localBigInteger2, this.q).equals(localBigInteger1)));
        BigInteger[] arrayOfBigInteger = lucasSequence(this.q, localBigInteger6, localBigInteger4, localBigInteger3);
        localBigInteger7 = arrayOfBigInteger[0];
        BigInteger localBigInteger8 = arrayOfBigInteger[1];
        if (localBigInteger8.multiply(localBigInteger8).mod(this.q).equals(localBigInteger5))
        {
          if (localBigInteger8.testBit(0))
            localBigInteger8 = localBigInteger8.add(this.q);
          BigInteger localBigInteger9 = localBigInteger8.shiftRight(1);
          return new Fp(this.q, localBigInteger9);
        }
      }
      while ((localBigInteger7.equals(ECConstants.ONE)) || (localBigInteger7.equals(localBigInteger1)));
      return null;
    }

    public ECFieldElement square()
    {
      return new Fp(this.q, this.x.multiply(this.x).mod(this.q));
    }

    public ECFieldElement subtract(ECFieldElement paramECFieldElement)
    {
      return new Fp(this.q, this.x.subtract(paramECFieldElement.toBigInteger()).mod(this.q));
    }

    public BigInteger toBigInteger()
    {
      return this.x;
    }
  }
}