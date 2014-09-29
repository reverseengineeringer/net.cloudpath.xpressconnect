package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.params.GOST3410Parameters;
import org.bouncycastle2.crypto.params.GOST3410ValidationParameters;

public class GOST3410ParametersGenerator
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private static final BigInteger TWO = BigInteger.valueOf(2L);
  private SecureRandom init_random;
  private int size;
  private int typeproc;

  private int procedure_A(int paramInt1, int paramInt2, BigInteger[] paramArrayOfBigInteger, int paramInt3)
  {
    label10: BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    int[] arrayOfInt1;
    int i;
    if ((paramInt1 >= 0) && (paramInt1 <= 65536))
    {
      if ((paramInt2 < 0) || (paramInt2 > 65536) || (paramInt2 / 2 == 0))
        break label161;
      localBigInteger1 = new BigInteger(Integer.toString(paramInt2));
      localBigInteger2 = new BigInteger("19381");
      arrayOfBigInteger1 = new BigInteger[1];
      arrayOfBigInteger1[0] = new BigInteger(Integer.toString(paramInt1));
      arrayOfInt1 = new int[1];
      arrayOfInt1[0] = paramInt3;
      i = 0;
    }
    BigInteger[] arrayOfBigInteger2;
    int n;
    int i1;
    for (int j = 0; ; j++)
    {
      if (arrayOfInt1[j] < 17)
      {
        arrayOfBigInteger2 = new BigInteger[i + 1];
        arrayOfBigInteger2[i] = new BigInteger("8003", 16);
        n = i - 1;
        i1 = 0;
        if (i1 < i)
          break label251;
        return arrayOfBigInteger1[0].intValue();
        paramInt1 = this.init_random.nextInt() / 32768;
        break;
        label161: paramInt2 = 1 + this.init_random.nextInt() / 32768;
        break label10;
      }
      int[] arrayOfInt2 = new int[1 + arrayOfInt1.length];
      int k = arrayOfInt1.length;
      System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, k);
      arrayOfInt1 = new int[arrayOfInt2.length];
      int m = arrayOfInt2.length;
      System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, m);
      arrayOfInt1[(j + 1)] = (arrayOfInt1[j] / 2);
      i = j + 1;
    }
    label251: int i2 = arrayOfInt1[n] / 16;
    label261: BigInteger[] arrayOfBigInteger3 = new BigInteger[arrayOfBigInteger1.length];
    int i3 = arrayOfBigInteger1.length;
    System.arraycopy(arrayOfBigInteger1, 0, arrayOfBigInteger3, 0, i3);
    BigInteger[] arrayOfBigInteger1 = new BigInteger[i2 + 1];
    int i4 = arrayOfBigInteger3.length;
    System.arraycopy(arrayOfBigInteger3, 0, arrayOfBigInteger1, 0, i4);
    int i5 = 0;
    label313: BigInteger localBigInteger3;
    int i6;
    label334: BigInteger localBigInteger4;
    if (i5 >= i2)
    {
      localBigInteger3 = new BigInteger("0");
      i6 = 0;
      if (i6 < i2)
        break label630;
      arrayOfBigInteger1[0] = arrayOfBigInteger1[i2];
      localBigInteger4 = TWO.pow(-1 + arrayOfInt1[n]).divide(arrayOfBigInteger2[(n + 1)]).add(TWO.pow(-1 + arrayOfInt1[n]).multiply(localBigInteger3).divide(arrayOfBigInteger2[(n + 1)].multiply(TWO.pow(i2 * 16))));
      if (localBigInteger4.mod(TWO).compareTo(ONE) == 0)
        localBigInteger4 = localBigInteger4.add(ONE);
    }
    for (int i7 = 0; ; i7 += 2)
    {
      arrayOfBigInteger2[n] = arrayOfBigInteger2[(n + 1)].multiply(localBigInteger4.add(BigInteger.valueOf(i7))).add(ONE);
      if (arrayOfBigInteger2[n].compareTo(TWO.pow(arrayOfInt1[n])) == 1)
        break label261;
      if ((TWO.modPow(arrayOfBigInteger2[(n + 1)].multiply(localBigInteger4.add(BigInteger.valueOf(i7))), arrayOfBigInteger2[n]).compareTo(ONE) == 0) && (TWO.modPow(localBigInteger4.add(BigInteger.valueOf(i7)), arrayOfBigInteger2[n]).compareTo(ONE) != 0))
      {
        n--;
        if (n < 0)
          break label668;
        i1++;
        break;
        arrayOfBigInteger1[(i5 + 1)] = arrayOfBigInteger1[i5].multiply(localBigInteger2).add(localBigInteger1).mod(TWO.pow(16));
        i5++;
        break label313;
        label630: localBigInteger3 = localBigInteger3.add(arrayOfBigInteger1[i6].multiply(TWO.pow(i6 * 16)));
        i6++;
        break label334;
      }
    }
    label668: paramArrayOfBigInteger[0] = arrayOfBigInteger2[0];
    paramArrayOfBigInteger[1] = arrayOfBigInteger2[1];
    return arrayOfBigInteger1[0].intValue();
  }

  private long procedure_Aa(long paramLong1, long paramLong2, BigInteger[] paramArrayOfBigInteger, int paramInt)
  {
    label14: BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    int[] arrayOfInt1;
    int i;
    if ((paramLong1 >= 0L) && (paramLong1 <= 4294967296L))
    {
      if ((paramLong2 < 0L) || (paramLong2 > 4294967296L) || (paramLong2 / 2L == 0L))
        break label173;
      localBigInteger1 = new BigInteger(Long.toString(paramLong2));
      localBigInteger2 = new BigInteger("97781173");
      arrayOfBigInteger1 = new BigInteger[1];
      arrayOfBigInteger1[0] = new BigInteger(Long.toString(paramLong1));
      arrayOfInt1 = new int[1];
      arrayOfInt1[0] = paramInt;
      i = 0;
    }
    BigInteger[] arrayOfBigInteger2;
    int n;
    int i1;
    for (int j = 0; ; j++)
    {
      if (arrayOfInt1[j] < 33)
      {
        arrayOfBigInteger2 = new BigInteger[i + 1];
        arrayOfBigInteger2[i] = new BigInteger("8000000B", 16);
        n = i - 1;
        i1 = 0;
        if (i1 < i)
          break label263;
        return arrayOfBigInteger1[0].longValue();
        paramLong1 = 2 * this.init_random.nextInt();
        break;
        label173: paramLong2 = 1 + 2 * this.init_random.nextInt();
        break label14;
      }
      int[] arrayOfInt2 = new int[1 + arrayOfInt1.length];
      int k = arrayOfInt1.length;
      System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, k);
      arrayOfInt1 = new int[arrayOfInt2.length];
      int m = arrayOfInt2.length;
      System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, m);
      arrayOfInt1[(j + 1)] = (arrayOfInt1[j] / 2);
      i = j + 1;
    }
    label263: int i2 = arrayOfInt1[n] / 32;
    label273: BigInteger[] arrayOfBigInteger3 = new BigInteger[arrayOfBigInteger1.length];
    int i3 = arrayOfBigInteger1.length;
    System.arraycopy(arrayOfBigInteger1, 0, arrayOfBigInteger3, 0, i3);
    BigInteger[] arrayOfBigInteger1 = new BigInteger[i2 + 1];
    int i4 = arrayOfBigInteger3.length;
    System.arraycopy(arrayOfBigInteger3, 0, arrayOfBigInteger1, 0, i4);
    int i5 = 0;
    label325: BigInteger localBigInteger3;
    int i6;
    label346: BigInteger localBigInteger4;
    if (i5 >= i2)
    {
      localBigInteger3 = new BigInteger("0");
      i6 = 0;
      if (i6 < i2)
        break label642;
      arrayOfBigInteger1[0] = arrayOfBigInteger1[i2];
      localBigInteger4 = TWO.pow(-1 + arrayOfInt1[n]).divide(arrayOfBigInteger2[(n + 1)]).add(TWO.pow(-1 + arrayOfInt1[n]).multiply(localBigInteger3).divide(arrayOfBigInteger2[(n + 1)].multiply(TWO.pow(i2 * 32))));
      if (localBigInteger4.mod(TWO).compareTo(ONE) == 0)
        localBigInteger4 = localBigInteger4.add(ONE);
    }
    for (int i7 = 0; ; i7 += 2)
    {
      arrayOfBigInteger2[n] = arrayOfBigInteger2[(n + 1)].multiply(localBigInteger4.add(BigInteger.valueOf(i7))).add(ONE);
      if (arrayOfBigInteger2[n].compareTo(TWO.pow(arrayOfInt1[n])) == 1)
        break label273;
      if ((TWO.modPow(arrayOfBigInteger2[(n + 1)].multiply(localBigInteger4.add(BigInteger.valueOf(i7))), arrayOfBigInteger2[n]).compareTo(ONE) == 0) && (TWO.modPow(localBigInteger4.add(BigInteger.valueOf(i7)), arrayOfBigInteger2[n]).compareTo(ONE) != 0))
      {
        n--;
        if (n < 0)
          break label680;
        i1++;
        break;
        arrayOfBigInteger1[(i5 + 1)] = arrayOfBigInteger1[i5].multiply(localBigInteger2).add(localBigInteger1).mod(TWO.pow(32));
        i5++;
        break label325;
        label642: localBigInteger3 = localBigInteger3.add(arrayOfBigInteger1[i6].multiply(TWO.pow(i6 * 32)));
        i6++;
        break label346;
      }
    }
    label680: paramArrayOfBigInteger[0] = arrayOfBigInteger2[0];
    paramArrayOfBigInteger[1] = arrayOfBigInteger2[1];
    return arrayOfBigInteger1[0].longValue();
  }

  private void procedure_B(int paramInt1, int paramInt2, BigInteger[] paramArrayOfBigInteger)
  {
    label10: BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    BigInteger localBigInteger3;
    BigInteger localBigInteger4;
    BigInteger[] arrayOfBigInteger2;
    label118: int k;
    label121: BigInteger localBigInteger5;
    int m;
    label142: BigInteger localBigInteger6;
    if ((paramInt1 >= 0) && (paramInt1 <= 65536))
    {
      if ((paramInt2 < 0) || (paramInt2 > 65536) || (paramInt2 / 2 == 0))
        break label391;
      BigInteger[] arrayOfBigInteger1 = new BigInteger[2];
      localBigInteger1 = new BigInteger(Integer.toString(paramInt2));
      localBigInteger2 = new BigInteger("19381");
      int i = procedure_A(paramInt1, paramInt2, arrayOfBigInteger1, 256);
      localBigInteger3 = arrayOfBigInteger1[0];
      int j = procedure_A(i, paramInt2, arrayOfBigInteger1, 512);
      localBigInteger4 = arrayOfBigInteger1[0];
      arrayOfBigInteger2 = new BigInteger[65];
      arrayOfBigInteger2[0] = new BigInteger(Integer.toString(j));
      k = 0;
      if (k < 64)
        break label407;
      localBigInteger5 = new BigInteger("0");
      m = 0;
      if (m < 64)
        break label446;
      arrayOfBigInteger2[0] = arrayOfBigInteger2[64];
      localBigInteger6 = TWO.pow(1023).divide(localBigInteger3.multiply(localBigInteger4)).add(TWO.pow(1023).multiply(localBigInteger5).divide(localBigInteger3.multiply(localBigInteger4).multiply(TWO.pow(1024))));
      if (localBigInteger6.mod(TWO).compareTo(ONE) == 0)
        localBigInteger6 = localBigInteger6.add(ONE);
    }
    for (int n = 0; ; n += 2)
    {
      BigInteger localBigInteger7 = localBigInteger3.multiply(localBigInteger4).multiply(localBigInteger6.add(BigInteger.valueOf(n))).add(ONE);
      if (localBigInteger7.compareTo(TWO.pow(1024)) == 1)
        break label118;
      if ((TWO.modPow(localBigInteger3.multiply(localBigInteger4).multiply(localBigInteger6.add(BigInteger.valueOf(n))), localBigInteger7).compareTo(ONE) == 0) && (TWO.modPow(localBigInteger3.multiply(localBigInteger6.add(BigInteger.valueOf(n))), localBigInteger7).compareTo(ONE) != 0))
      {
        paramArrayOfBigInteger[0] = localBigInteger7;
        paramArrayOfBigInteger[1] = localBigInteger3;
        return;
        paramInt1 = this.init_random.nextInt() / 32768;
        break;
        label391: paramInt2 = 1 + this.init_random.nextInt() / 32768;
        break label10;
        label407: arrayOfBigInteger2[(k + 1)] = arrayOfBigInteger2[k].multiply(localBigInteger2).add(localBigInteger1).mod(TWO.pow(16));
        k++;
        break label121;
        label446: localBigInteger5 = localBigInteger5.add(arrayOfBigInteger2[m].multiply(TWO.pow(m * 16)));
        m++;
        break label142;
      }
    }
  }

  private void procedure_Bb(long paramLong1, long paramLong2, BigInteger[] paramArrayOfBigInteger)
  {
    label14: BigInteger localBigInteger1;
    BigInteger localBigInteger2;
    BigInteger localBigInteger3;
    BigInteger localBigInteger4;
    BigInteger[] arrayOfBigInteger2;
    label130: int i;
    label133: BigInteger localBigInteger5;
    int j;
    label154: BigInteger localBigInteger6;
    if ((paramLong1 >= 0L) && (paramLong1 <= 4294967296L))
    {
      if ((paramLong2 < 0L) || (paramLong2 > 4294967296L) || (paramLong2 / 2L == 0L))
        break label405;
      BigInteger[] arrayOfBigInteger1 = new BigInteger[2];
      localBigInteger1 = new BigInteger(Long.toString(paramLong2));
      localBigInteger2 = new BigInteger("97781173");
      long l1 = procedure_Aa(paramLong1, paramLong2, arrayOfBigInteger1, 256);
      localBigInteger3 = arrayOfBigInteger1[0];
      long l2 = procedure_Aa(l1, paramLong2, arrayOfBigInteger1, 512);
      localBigInteger4 = arrayOfBigInteger1[0];
      arrayOfBigInteger2 = new BigInteger[33];
      arrayOfBigInteger2[0] = new BigInteger(Long.toString(l2));
      i = 0;
      if (i < 32)
        break label421;
      localBigInteger5 = new BigInteger("0");
      j = 0;
      if (j < 32)
        break label460;
      arrayOfBigInteger2[0] = arrayOfBigInteger2[32];
      localBigInteger6 = TWO.pow(1023).divide(localBigInteger3.multiply(localBigInteger4)).add(TWO.pow(1023).multiply(localBigInteger5).divide(localBigInteger3.multiply(localBigInteger4).multiply(TWO.pow(1024))));
      if (localBigInteger6.mod(TWO).compareTo(ONE) == 0)
        localBigInteger6 = localBigInteger6.add(ONE);
    }
    for (int k = 0; ; k += 2)
    {
      BigInteger localBigInteger7 = localBigInteger3.multiply(localBigInteger4).multiply(localBigInteger6.add(BigInteger.valueOf(k))).add(ONE);
      if (localBigInteger7.compareTo(TWO.pow(1024)) == 1)
        break label130;
      if ((TWO.modPow(localBigInteger3.multiply(localBigInteger4).multiply(localBigInteger6.add(BigInteger.valueOf(k))), localBigInteger7).compareTo(ONE) == 0) && (TWO.modPow(localBigInteger3.multiply(localBigInteger6.add(BigInteger.valueOf(k))), localBigInteger7).compareTo(ONE) != 0))
      {
        paramArrayOfBigInteger[0] = localBigInteger7;
        paramArrayOfBigInteger[1] = localBigInteger3;
        return;
        paramLong1 = 2 * this.init_random.nextInt();
        break;
        label405: paramLong2 = 1 + 2 * this.init_random.nextInt();
        break label14;
        label421: arrayOfBigInteger2[(i + 1)] = arrayOfBigInteger2[i].multiply(localBigInteger2).add(localBigInteger1).mod(TWO.pow(32));
        i++;
        break label133;
        label460: localBigInteger5 = localBigInteger5.add(arrayOfBigInteger2[j].multiply(TWO.pow(j * 32)));
        j++;
        break label154;
      }
    }
  }

  private BigInteger procedure_C(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    BigInteger localBigInteger1 = paramBigInteger1.subtract(ONE);
    BigInteger localBigInteger2 = localBigInteger1.divide(paramBigInteger2);
    int i = paramBigInteger1.bitLength();
    BigInteger localBigInteger4;
    do
    {
      BigInteger localBigInteger3;
      do
        localBigInteger3 = new BigInteger(i, this.init_random);
      while ((localBigInteger3.compareTo(ONE) <= 0) || (localBigInteger3.compareTo(localBigInteger1) >= 0));
      localBigInteger4 = localBigInteger3.modPow(localBigInteger2, paramBigInteger1);
    }
    while (localBigInteger4.compareTo(ONE) == 0);
    return localBigInteger4;
  }

  public GOST3410Parameters generateParameters()
  {
    BigInteger[] arrayOfBigInteger = new BigInteger[2];
    if (this.typeproc == 1)
    {
      int i = this.init_random.nextInt();
      int j = this.init_random.nextInt();
      switch (this.size)
      {
      default:
        throw new IllegalArgumentException("Ooops! key size 512 or 1024 bit.");
      case 512:
        procedure_A(i, j, arrayOfBigInteger, 512);
      case 1024:
      }
      while (true)
      {
        BigInteger localBigInteger3 = arrayOfBigInteger[0];
        BigInteger localBigInteger4 = arrayOfBigInteger[1];
        return new GOST3410Parameters(localBigInteger3, localBigInteger4, procedure_C(localBigInteger3, localBigInteger4), new GOST3410ValidationParameters(i, j));
        procedure_B(i, j, arrayOfBigInteger);
      }
    }
    long l1 = this.init_random.nextLong();
    long l2 = this.init_random.nextLong();
    switch (this.size)
    {
    default:
      throw new IllegalStateException("Ooops! key size 512 or 1024 bit.");
    case 512:
      procedure_Aa(l1, l2, arrayOfBigInteger, 512);
    case 1024:
    }
    while (true)
    {
      BigInteger localBigInteger1 = arrayOfBigInteger[0];
      BigInteger localBigInteger2 = arrayOfBigInteger[1];
      return new GOST3410Parameters(localBigInteger1, localBigInteger2, procedure_C(localBigInteger1, localBigInteger2), new GOST3410ValidationParameters(l1, l2));
      procedure_Bb(l1, l2, arrayOfBigInteger);
    }
  }

  public void init(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    this.size = paramInt1;
    this.typeproc = paramInt2;
    this.init_random = paramSecureRandom;
  }
}