package org.bouncycastle2.math.ec;

import java.math.BigInteger;

class Tnaf
{
  private static final BigInteger MINUS_ONE = ECConstants.ONE.negate();
  private static final BigInteger MINUS_THREE;
  private static final BigInteger MINUS_TWO = ECConstants.TWO.negate();
  public static final byte POW_2_WIDTH = 16;
  public static final byte WIDTH = 4;
  public static final ZTauElement[] alpha0;
  public static final byte[][] alpha0Tnaf;
  public static final ZTauElement[] alpha1;
  public static final byte[][] alpha1Tnaf = arrayOfByte5;

  static
  {
    MINUS_THREE = ECConstants.THREE.negate();
    ZTauElement[] arrayOfZTauElement1 = new ZTauElement[9];
    arrayOfZTauElement1[1] = new ZTauElement(ECConstants.ONE, ECConstants.ZERO);
    arrayOfZTauElement1[3] = new ZTauElement(MINUS_THREE, MINUS_ONE);
    arrayOfZTauElement1[5] = new ZTauElement(MINUS_ONE, MINUS_ONE);
    arrayOfZTauElement1[7] = new ZTauElement(ECConstants.ONE, MINUS_ONE);
    alpha0 = arrayOfZTauElement1;
    byte[][] arrayOfByte1 = new byte[8][];
    arrayOfByte1[1] = { 1 };
    byte[] arrayOfByte2 = new byte[3];
    arrayOfByte2[0] = -1;
    arrayOfByte2[2] = 1;
    arrayOfByte1[3] = arrayOfByte2;
    byte[] arrayOfByte3 = new byte[3];
    arrayOfByte3[0] = 1;
    arrayOfByte3[2] = 1;
    arrayOfByte1[5] = arrayOfByte3;
    byte[] arrayOfByte4 = new byte[4];
    arrayOfByte4[0] = -1;
    arrayOfByte4[3] = 1;
    arrayOfByte1[7] = arrayOfByte4;
    alpha0Tnaf = arrayOfByte1;
    ZTauElement[] arrayOfZTauElement2 = new ZTauElement[9];
    arrayOfZTauElement2[1] = new ZTauElement(ECConstants.ONE, ECConstants.ZERO);
    arrayOfZTauElement2[3] = new ZTauElement(MINUS_THREE, ECConstants.ONE);
    arrayOfZTauElement2[5] = new ZTauElement(MINUS_ONE, ECConstants.ONE);
    arrayOfZTauElement2[7] = new ZTauElement(ECConstants.ONE, ECConstants.ONE);
    alpha1 = arrayOfZTauElement2;
    byte[][] arrayOfByte5 = new byte[8][];
    arrayOfByte5[1] = { 1 };
    byte[] arrayOfByte6 = new byte[3];
    arrayOfByte6[0] = -1;
    arrayOfByte6[2] = 1;
    arrayOfByte5[3] = arrayOfByte6;
    byte[] arrayOfByte7 = new byte[3];
    arrayOfByte7[0] = 1;
    arrayOfByte7[2] = 1;
    arrayOfByte5[5] = arrayOfByte7;
    byte[] arrayOfByte8 = new byte[4];
    arrayOfByte8[0] = -1;
    arrayOfByte8[3] = -1;
    arrayOfByte5[7] = arrayOfByte8;
  }

  public static SimpleBigDecimal approximateDivisionByN(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, byte paramByte, int paramInt1, int paramInt2)
  {
    int i = paramInt2 + (paramInt1 + 5) / 2;
    BigInteger localBigInteger1 = paramBigInteger2.multiply(paramBigInteger1.shiftRight(paramByte + (-2 + (paramInt1 - i))));
    BigInteger localBigInteger2 = localBigInteger1.add(paramBigInteger3.multiply(localBigInteger1.shiftRight(paramInt1)));
    BigInteger localBigInteger3 = localBigInteger2.shiftRight(i - paramInt2);
    if (localBigInteger2.testBit(-1 + (i - paramInt2)))
      localBigInteger3 = localBigInteger3.add(ECConstants.ONE);
    return new SimpleBigDecimal(localBigInteger3, paramInt2);
  }

  public static BigInteger[] getLucas(byte paramByte, int paramInt, boolean paramBoolean)
  {
    if ((paramByte != 1) && (paramByte != -1))
      throw new IllegalArgumentException("mu must be 1 or -1");
    Object localObject1;
    if (paramBoolean)
      localObject1 = ECConstants.TWO;
    int i;
    for (Object localObject2 = BigInteger.valueOf(paramByte); ; localObject2 = ECConstants.ONE)
    {
      i = 1;
      if (i < paramInt)
        break;
      return new BigInteger[] { localObject1, localObject2 };
      localObject1 = ECConstants.ZERO;
    }
    if (paramByte == 1);
    for (Object localObject3 = localObject2; ; localObject3 = ((BigInteger)localObject2).negate())
    {
      BigInteger localBigInteger = ((BigInteger)localObject3).subtract(((BigInteger)localObject1).shiftLeft(1));
      localObject1 = localObject2;
      localObject2 = localBigInteger;
      i++;
      break;
    }
  }

  public static byte getMu(ECCurve.F2m paramF2m)
  {
    BigInteger localBigInteger = paramF2m.getA().toBigInteger();
    if (localBigInteger.equals(ECConstants.ZERO))
      return -1;
    if (localBigInteger.equals(ECConstants.ONE))
      return 1;
    throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
  }

  public static ECPoint.F2m[] getPreComp(ECPoint.F2m paramF2m, byte paramByte)
  {
    ECPoint.F2m[] arrayOfF2m = new ECPoint.F2m[16];
    arrayOfF2m[1] = paramF2m;
    byte[][] arrayOfByte;
    int i;
    if (paramByte == 0)
    {
      arrayOfByte = alpha0Tnaf;
      i = arrayOfByte.length;
    }
    for (int j = 3; ; j += 2)
    {
      if (j >= i)
      {
        return arrayOfF2m;
        arrayOfByte = alpha1Tnaf;
        break;
      }
      arrayOfF2m[j] = multiplyFromTnaf(paramF2m, arrayOfByte[j]);
    }
  }

  public static BigInteger[] getSi(ECCurve.F2m paramF2m)
  {
    if (!paramF2m.isKoblitz())
      throw new IllegalArgumentException("si is defined for Koblitz curves only");
    int i = paramF2m.getM();
    int j = paramF2m.getA().toBigInteger().intValue();
    byte b = paramF2m.getMu();
    int k = paramF2m.getH().intValue();
    BigInteger[] arrayOfBigInteger1 = getLucas(b, i + 3 - j, false);
    BigInteger localBigInteger1;
    if (b == 1)
      localBigInteger1 = ECConstants.ONE.subtract(arrayOfBigInteger1[1]);
    BigInteger[] arrayOfBigInteger2;
    for (BigInteger localBigInteger2 = ECConstants.ONE.subtract(arrayOfBigInteger1[0]); ; localBigInteger2 = ECConstants.ONE.add(arrayOfBigInteger1[0]))
    {
      arrayOfBigInteger2 = new BigInteger[2];
      if (k != 2)
        break label168;
      arrayOfBigInteger2[0] = localBigInteger1.shiftRight(1);
      arrayOfBigInteger2[1] = localBigInteger2.shiftRight(1).negate();
      return arrayOfBigInteger2;
      if (b != -1)
        break;
      localBigInteger1 = ECConstants.ONE.add(arrayOfBigInteger1[1]);
    }
    throw new IllegalArgumentException("mu must be 1 or -1");
    label168: if (k == 4)
    {
      arrayOfBigInteger2[0] = localBigInteger1.shiftRight(2);
      arrayOfBigInteger2[1] = localBigInteger2.shiftRight(2).negate();
      return arrayOfBigInteger2;
    }
    throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
  }

  public static BigInteger getTw(byte paramByte, int paramInt)
  {
    if (paramInt == 4)
    {
      if (paramByte == 1)
        return BigInteger.valueOf(6L);
      return BigInteger.valueOf(10L);
    }
    BigInteger[] arrayOfBigInteger = getLucas(paramByte, paramInt, false);
    BigInteger localBigInteger1 = ECConstants.ZERO.setBit(paramInt);
    BigInteger localBigInteger2 = arrayOfBigInteger[1].modInverse(localBigInteger1);
    return ECConstants.TWO.multiply(arrayOfBigInteger[0]).multiply(localBigInteger2).mod(localBigInteger1);
  }

  public static ECPoint.F2m multiplyFromTnaf(ECPoint.F2m paramF2m, byte[] paramArrayOfByte)
  {
    ECPoint.F2m localF2m = (ECPoint.F2m)((ECCurve.F2m)paramF2m.getCurve()).getInfinity();
    int i = -1 + paramArrayOfByte.length;
    if (i < 0)
      return localF2m;
    localF2m = tau(localF2m);
    if (paramArrayOfByte[i] == 1)
      localF2m = localF2m.addSimple(paramF2m);
    while (true)
    {
      i--;
      break;
      if (paramArrayOfByte[i] == -1)
        localF2m = localF2m.subtractSimple(paramF2m);
    }
  }

  public static ECPoint.F2m multiplyRTnaf(ECPoint.F2m paramF2m, BigInteger paramBigInteger)
  {
    ECCurve.F2m localF2m = (ECCurve.F2m)paramF2m.getCurve();
    int i = localF2m.getM();
    byte b1 = (byte)localF2m.getA().toBigInteger().intValue();
    byte b2 = localF2m.getMu();
    return multiplyTnaf(paramF2m, partModReduction(paramBigInteger, i, b1, localF2m.getSi(), b2, (byte)10));
  }

  public static ECPoint.F2m multiplyTnaf(ECPoint.F2m paramF2m, ZTauElement paramZTauElement)
  {
    return multiplyFromTnaf(paramF2m, tauAdicNaf(((ECCurve.F2m)paramF2m.getCurve()).getMu(), paramZTauElement));
  }

  public static BigInteger norm(byte paramByte, ZTauElement paramZTauElement)
  {
    BigInteger localBigInteger1 = paramZTauElement.u.multiply(paramZTauElement.u);
    BigInteger localBigInteger2 = paramZTauElement.u.multiply(paramZTauElement.v);
    BigInteger localBigInteger3 = paramZTauElement.v.multiply(paramZTauElement.v).shiftLeft(1);
    if (paramByte == 1)
      return localBigInteger1.add(localBigInteger2).add(localBigInteger3);
    if (paramByte == -1)
      return localBigInteger1.subtract(localBigInteger2).add(localBigInteger3);
    throw new IllegalArgumentException("mu must be 1 or -1");
  }

  public static SimpleBigDecimal norm(byte paramByte, SimpleBigDecimal paramSimpleBigDecimal1, SimpleBigDecimal paramSimpleBigDecimal2)
  {
    SimpleBigDecimal localSimpleBigDecimal1 = paramSimpleBigDecimal1.multiply(paramSimpleBigDecimal1);
    SimpleBigDecimal localSimpleBigDecimal2 = paramSimpleBigDecimal1.multiply(paramSimpleBigDecimal2);
    SimpleBigDecimal localSimpleBigDecimal3 = paramSimpleBigDecimal2.multiply(paramSimpleBigDecimal2).shiftLeft(1);
    if (paramByte == 1)
      return localSimpleBigDecimal1.add(localSimpleBigDecimal2).add(localSimpleBigDecimal3);
    if (paramByte == -1)
      return localSimpleBigDecimal1.subtract(localSimpleBigDecimal2).add(localSimpleBigDecimal3);
    throw new IllegalArgumentException("mu must be 1 or -1");
  }

  public static ZTauElement partModReduction(BigInteger paramBigInteger, int paramInt, byte paramByte1, BigInteger[] paramArrayOfBigInteger, byte paramByte2, byte paramByte3)
  {
    if (paramByte2 == 1);
    for (BigInteger localBigInteger1 = paramArrayOfBigInteger[0].add(paramArrayOfBigInteger[1]); ; localBigInteger1 = paramArrayOfBigInteger[0].subtract(paramArrayOfBigInteger[1]))
    {
      BigInteger localBigInteger2 = getLucas(paramByte2, paramInt, true)[1];
      ZTauElement localZTauElement = round(approximateDivisionByN(paramBigInteger, paramArrayOfBigInteger[0], localBigInteger2, paramByte1, paramInt, paramByte3), approximateDivisionByN(paramBigInteger, paramArrayOfBigInteger[1], localBigInteger2, paramByte1, paramInt, paramByte3), paramByte2);
      return new ZTauElement(paramBigInteger.subtract(localBigInteger1.multiply(localZTauElement.u)).subtract(BigInteger.valueOf(2L).multiply(paramArrayOfBigInteger[1]).multiply(localZTauElement.v)), paramArrayOfBigInteger[1].multiply(localZTauElement.u).subtract(paramArrayOfBigInteger[0].multiply(localZTauElement.v)));
    }
  }

  public static ZTauElement round(SimpleBigDecimal paramSimpleBigDecimal1, SimpleBigDecimal paramSimpleBigDecimal2, byte paramByte)
  {
    int i = paramSimpleBigDecimal1.getScale();
    if (paramSimpleBigDecimal2.getScale() != i)
      throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
    if ((paramByte != 1) && (paramByte != -1))
      throw new IllegalArgumentException("mu must be 1 or -1");
    BigInteger localBigInteger1 = paramSimpleBigDecimal1.round();
    BigInteger localBigInteger2 = paramSimpleBigDecimal2.round();
    SimpleBigDecimal localSimpleBigDecimal1 = paramSimpleBigDecimal1.subtract(localBigInteger1);
    SimpleBigDecimal localSimpleBigDecimal2 = paramSimpleBigDecimal2.subtract(localBigInteger2);
    SimpleBigDecimal localSimpleBigDecimal3 = localSimpleBigDecimal1.add(localSimpleBigDecimal1);
    SimpleBigDecimal localSimpleBigDecimal4;
    SimpleBigDecimal localSimpleBigDecimal5;
    SimpleBigDecimal localSimpleBigDecimal6;
    SimpleBigDecimal localSimpleBigDecimal7;
    SimpleBigDecimal localSimpleBigDecimal8;
    label140: int j;
    int m;
    if (paramByte == 1)
    {
      localSimpleBigDecimal4 = localSimpleBigDecimal3.add(localSimpleBigDecimal2);
      localSimpleBigDecimal5 = localSimpleBigDecimal2.add(localSimpleBigDecimal2).add(localSimpleBigDecimal2);
      localSimpleBigDecimal6 = localSimpleBigDecimal5.add(localSimpleBigDecimal2);
      if (paramByte != 1)
        break label237;
      localSimpleBigDecimal7 = localSimpleBigDecimal1.subtract(localSimpleBigDecimal5);
      localSimpleBigDecimal8 = localSimpleBigDecimal1.add(localSimpleBigDecimal6);
      j = 0;
      if (localSimpleBigDecimal4.compareTo(ECConstants.ONE) < 0)
        break label267;
      if (localSimpleBigDecimal7.compareTo(MINUS_ONE) >= 0)
        break label258;
      m = paramByte;
      label168: if (localSimpleBigDecimal4.compareTo(MINUS_ONE) >= 0)
        break label303;
      if (localSimpleBigDecimal7.compareTo(ECConstants.ONE) < 0)
        break label297;
      m = (byte)-paramByte;
    }
    while (true)
    {
      return new ZTauElement(localBigInteger1.add(BigInteger.valueOf(j)), localBigInteger2.add(BigInteger.valueOf(m)));
      localSimpleBigDecimal4 = localSimpleBigDecimal3.subtract(localSimpleBigDecimal2);
      break;
      label237: localSimpleBigDecimal7 = localSimpleBigDecimal1.add(localSimpleBigDecimal5);
      localSimpleBigDecimal8 = localSimpleBigDecimal1.subtract(localSimpleBigDecimal6);
      break label140;
      label258: j = 1;
      m = 0;
      break label168;
      label267: int k = localSimpleBigDecimal8.compareTo(ECConstants.TWO);
      j = 0;
      m = 0;
      if (k < 0)
        break label168;
      m = paramByte;
      j = 0;
      break label168;
      label297: j = -1;
      continue;
      label303: if (localSimpleBigDecimal8.compareTo(MINUS_TWO) < 0)
        m = (byte)-paramByte;
    }
  }

  public static ECPoint.F2m tau(ECPoint.F2m paramF2m)
  {
    if (paramF2m.isInfinity())
      return paramF2m;
    ECFieldElement localECFieldElement1 = paramF2m.getX();
    ECFieldElement localECFieldElement2 = paramF2m.getY();
    return new ECPoint.F2m(paramF2m.getCurve(), localECFieldElement1.square(), localECFieldElement2.square(), paramF2m.isCompressed());
  }

  public static byte[] tauAdicNaf(byte paramByte, ZTauElement paramZTauElement)
  {
    if ((paramByte != 1) && (paramByte != -1))
      throw new IllegalArgumentException("mu must be 1 or -1");
    int i = norm(paramByte, paramZTauElement).bitLength();
    if (i > 30);
    byte[] arrayOfByte1;
    int k;
    int m;
    BigInteger localBigInteger2;
    for (int j = i + 4; ; j = 34)
    {
      arrayOfByte1 = new byte[j];
      k = 0;
      m = 0;
      localBigInteger1 = paramZTauElement.u;
      localBigInteger2 = paramZTauElement.v;
      if ((!localBigInteger1.equals(ECConstants.ZERO)) || (!localBigInteger2.equals(ECConstants.ZERO)))
        break;
      int n = m + 1;
      byte[] arrayOfByte2 = new byte[n];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, n);
      return arrayOfByte2;
    }
    label174: label178: BigInteger localBigInteger3;
    BigInteger localBigInteger4;
    if (localBigInteger1.testBit(0))
    {
      arrayOfByte1[k] = ((byte)ECConstants.TWO.subtract(localBigInteger1.subtract(localBigInteger2.shiftLeft(1)).mod(ECConstants.FOUR)).intValue());
      if (arrayOfByte1[k] == 1)
      {
        localBigInteger1 = localBigInteger1.clearBit(0);
        m = k;
        localBigInteger3 = localBigInteger1;
        localBigInteger4 = localBigInteger1.shiftRight(1);
        if (paramByte != 1)
          break label243;
      }
    }
    label243: for (BigInteger localBigInteger1 = localBigInteger2.add(localBigInteger4); ; localBigInteger1 = localBigInteger2.subtract(localBigInteger4))
    {
      localBigInteger2 = localBigInteger3.shiftRight(1).negate();
      k++;
      break;
      localBigInteger1 = localBigInteger1.add(ECConstants.ONE);
      break label174;
      arrayOfByte1[k] = 0;
      break label178;
    }
  }

  public static byte[] tauAdicWNaf(byte paramByte1, ZTauElement paramZTauElement, byte paramByte2, BigInteger paramBigInteger1, BigInteger paramBigInteger2, ZTauElement[] paramArrayOfZTauElement)
  {
    if ((paramByte1 != 1) && (paramByte1 != -1))
      throw new IllegalArgumentException("mu must be 1 or -1");
    int i = norm(paramByte1, paramZTauElement).bitLength();
    if (i > 30);
    byte[] arrayOfByte;
    BigInteger localBigInteger1;
    BigInteger localBigInteger3;
    int k;
    for (int j = paramByte2 + (i + 4); ; j = paramByte2 + 34)
    {
      arrayOfByte = new byte[j];
      localBigInteger1 = paramBigInteger1.shiftRight(1);
      localBigInteger2 = paramZTauElement.u;
      localBigInteger3 = paramZTauElement.v;
      k = 0;
      if ((!localBigInteger2.equals(ECConstants.ZERO)) || (!localBigInteger3.equals(ECConstants.ZERO)))
        break;
      return arrayOfByte;
    }
    BigInteger localBigInteger5;
    int m;
    label156: label215: BigInteger localBigInteger4;
    if (localBigInteger2.testBit(0))
    {
      localBigInteger5 = localBigInteger2.add(localBigInteger3.multiply(paramBigInteger2)).mod(paramBigInteger1);
      if (localBigInteger5.compareTo(localBigInteger1) >= 0)
      {
        m = (byte)localBigInteger5.subtract(paramBigInteger1).intValue();
        arrayOfByte[k] = m;
        int n = 1;
        if (m < 0)
        {
          n = 0;
          m = (byte)-m;
        }
        if (n == 0)
          break label265;
        localBigInteger2 = localBigInteger2.subtract(paramArrayOfZTauElement[m].u);
        localBigInteger3 = localBigInteger3.subtract(paramArrayOfZTauElement[m].v);
        localBigInteger4 = localBigInteger2;
        if (paramByte1 != 1)
          break label307;
      }
    }
    label265: label307: for (BigInteger localBigInteger2 = localBigInteger3.add(localBigInteger2.shiftRight(1)); ; localBigInteger2 = localBigInteger3.subtract(localBigInteger2.shiftRight(1)))
    {
      localBigInteger3 = localBigInteger4.shiftRight(1).negate();
      k++;
      break;
      m = (byte)localBigInteger5.intValue();
      break label156;
      localBigInteger2 = localBigInteger2.add(paramArrayOfZTauElement[m].u);
      localBigInteger3 = localBigInteger3.add(paramArrayOfZTauElement[m].v);
      break label215;
      arrayOfByte[k] = 0;
      break label215;
    }
  }
}