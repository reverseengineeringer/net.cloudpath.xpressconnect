package org.bouncycastle2.crypto.generators;

import java.io.PrintStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Vector;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle2.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle2.crypto.KeyGenerationParameters;
import org.bouncycastle2.crypto.params.NaccacheSternKeyGenerationParameters;
import org.bouncycastle2.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle2.crypto.params.NaccacheSternPrivateKeyParameters;

public class NaccacheSternKeyPairGenerator
  implements AsymmetricCipherKeyPairGenerator
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private static int[] smallPrimes = { 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557 };
  private NaccacheSternKeyGenerationParameters param;

  private static Vector findFirstPrimes(int paramInt)
  {
    Vector localVector = new Vector(paramInt);
    for (int i = 0; ; i++)
    {
      if (i == paramInt)
        return localVector;
      localVector.addElement(BigInteger.valueOf(smallPrimes[i]));
    }
  }

  private static BigInteger generatePrime(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    for (BigInteger localBigInteger = new BigInteger(paramInt1, paramInt2, paramSecureRandom); ; localBigInteger = new BigInteger(paramInt1, paramInt2, paramSecureRandom))
      if (localBigInteger.bitLength() == paramInt1)
        return localBigInteger;
  }

  private static int getInt(SecureRandom paramSecureRandom, int paramInt)
  {
    if ((paramInt & -paramInt) == paramInt)
      return (int)(paramInt * (0x7FFFFFFF & paramSecureRandom.nextInt()) >> 31);
    int i;
    int j;
    do
    {
      i = 0x7FFFFFFF & paramSecureRandom.nextInt();
      j = i % paramInt;
    }
    while (i - j + (paramInt - 1) < 0);
    return j;
  }

  private static Vector permuteList(Vector paramVector, SecureRandom paramSecureRandom)
  {
    Vector localVector1 = new Vector();
    Vector localVector2 = new Vector();
    int i = 0;
    if (i >= paramVector.size())
    {
      localVector1.addElement(localVector2.elementAt(0));
      localVector2.removeElementAt(0);
    }
    while (true)
    {
      if (localVector2.size() == 0)
      {
        return localVector1;
        localVector2.addElement(paramVector.elementAt(i));
        i++;
        break;
      }
      localVector1.insertElementAt(localVector2.elementAt(0), getInt(paramSecureRandom, 1 + localVector1.size()));
      localVector2.removeElementAt(0);
    }
  }

  public AsymmetricCipherKeyPair generateKeyPair()
  {
    int i = this.param.getStrength();
    SecureRandom localSecureRandom = this.param.getRandom();
    int j = this.param.getCertainty();
    boolean bool = this.param.isDebug();
    if (bool)
      System.out.println("Fetching first " + this.param.getCntSmallPrimes() + " primes.");
    Vector localVector1 = permuteList(findFirstPrimes(this.param.getCntSmallPrimes()), localSecureRandom);
    BigInteger localBigInteger1 = ONE;
    BigInteger localBigInteger2 = ONE;
    int k = 0;
    int m = localVector1.size() / 2;
    if (k >= m);
    BigInteger localBigInteger5;
    BigInteger localBigInteger6;
    BigInteger localBigInteger7;
    long l1;
    BigInteger localBigInteger10;
    BigInteger localBigInteger11;
    BigInteger localBigInteger12;
    BigInteger localBigInteger13;
    for (int n = localVector1.size() / 2; ; n++)
    {
      int i1 = localVector1.size();
      if (n >= i1)
      {
        localBigInteger5 = localBigInteger1.multiply(localBigInteger2);
        int i2 = -48 + (i - localBigInteger5.bitLength());
        localBigInteger6 = generatePrime(1 + i2 / 2, j, localSecureRandom);
        localBigInteger7 = generatePrime(1 + i2 / 2, j, localSecureRandom);
        l1 = 0L;
        if (bool)
          System.out.println("generating p and q");
        BigInteger localBigInteger8 = localBigInteger6.multiply(localBigInteger1).shiftLeft(1);
        BigInteger localBigInteger9 = localBigInteger7.multiply(localBigInteger2).shiftLeft(1);
        while (true)
        {
          l1 += 1L;
          localBigInteger10 = generatePrime(24, j, localSecureRandom);
          localBigInteger11 = localBigInteger10.multiply(localBigInteger8).add(ONE);
          if (localBigInteger11.isProbablePrime(j))
          {
            do
            {
              do
                localBigInteger12 = generatePrime(24, j, localSecureRandom);
              while (localBigInteger10.equals(localBigInteger12));
              localBigInteger13 = localBigInteger12.multiply(localBigInteger9).add(ONE);
            }
            while (!localBigInteger13.isProbablePrime(j));
            if (localBigInteger5.gcd(localBigInteger10.multiply(localBigInteger12)).equals(ONE))
            {
              if (localBigInteger11.multiply(localBigInteger13).bitLength() >= i)
                break;
              if (bool)
                System.out.println("key size too small. Should be " + i + " but is actually " + localBigInteger11.multiply(localBigInteger13).bitLength());
            }
          }
        }
        BigInteger localBigInteger3 = (BigInteger)localVector1.elementAt(k);
        localBigInteger1 = localBigInteger1.multiply(localBigInteger3);
        k++;
        break;
      }
      BigInteger localBigInteger4 = (BigInteger)localVector1.elementAt(n);
      localBigInteger2 = localBigInteger2.multiply(localBigInteger4);
    }
    if (bool)
      System.out.println("needed " + l1 + " tries to generate p and q.");
    BigInteger localBigInteger14 = localBigInteger11.multiply(localBigInteger13);
    BigInteger localBigInteger15 = localBigInteger11.subtract(ONE).multiply(localBigInteger13.subtract(ONE));
    long l2 = 0L;
    if (bool)
      System.out.println("generating g");
    label538: BigInteger localBigInteger18;
    while (true)
    {
      Vector localVector2 = new Vector();
      int i3 = 0;
      int i4 = localVector1.size();
      int i5;
      if (i3 == i4)
      {
        localBigInteger18 = ONE;
        i5 = 0;
        label560: int i6 = localVector1.size();
        if (i5 < i6)
          break label727;
      }
      label727: label855: for (int i7 = 0; ; i7++)
      {
        int i8 = localVector1.size();
        int i9 = i7;
        int i10 = 0;
        if (i9 >= i8);
        while (true)
        {
          if (i10 != 0)
            break label853;
          if (!localBigInteger18.modPow(localBigInteger15.divide(BigInteger.valueOf(4L)), localBigInteger14).equals(ONE))
            break label861;
          if (!bool)
            break;
          System.out.println("g has order phi(n)/4\n g:" + localBigInteger18);
          break;
          BigInteger localBigInteger16 = localBigInteger15.divide((BigInteger)localVector1.elementAt(i3));
          BigInteger localBigInteger17;
          do
          {
            l2 += 1L;
            localBigInteger17 = new BigInteger(i, j, localSecureRandom);
          }
          while (localBigInteger17.modPow(localBigInteger16, localBigInteger14).equals(ONE));
          localVector2.addElement(localBigInteger17);
          i3++;
          break label538;
          localBigInteger18 = localBigInteger18.multiply(((BigInteger)localVector2.elementAt(i5)).modPow(localBigInteger5.divide((BigInteger)localVector1.elementAt(i5)), localBigInteger14)).mod(localBigInteger14);
          i5++;
          break label560;
          if (!localBigInteger18.modPow(localBigInteger15.divide((BigInteger)localVector1.elementAt(i7)), localBigInteger14).equals(ONE))
            break label855;
          if (bool)
            System.out.println("g has order phi(n)/" + localVector1.elementAt(i7) + "\n g: " + localBigInteger18);
          i10 = 1;
        }
        break;
      }
      label853: label861: if (localBigInteger18.modPow(localBigInteger15.divide(localBigInteger10), localBigInteger14).equals(ONE))
      {
        if (bool)
          System.out.println("g has order phi(n)/p'\n g: " + localBigInteger18);
      }
      else if (localBigInteger18.modPow(localBigInteger15.divide(localBigInteger12), localBigInteger14).equals(ONE))
      {
        if (bool)
          System.out.println("g has order phi(n)/q'\n g: " + localBigInteger18);
      }
      else if (localBigInteger18.modPow(localBigInteger15.divide(localBigInteger6), localBigInteger14).equals(ONE))
      {
        if (bool)
          System.out.println("g has order phi(n)/a\n g: " + localBigInteger18);
      }
      else
      {
        if (!localBigInteger18.modPow(localBigInteger15.divide(localBigInteger7), localBigInteger14).equals(ONE))
          break;
        if (bool)
          System.out.println("g has order phi(n)/b\n g: " + localBigInteger18);
      }
    }
    if (bool)
    {
      System.out.println("needed " + l2 + " tries to generate g");
      System.out.println();
      System.out.println("found new NaccacheStern cipher variables:");
      System.out.println("smallPrimes: " + localVector1);
      System.out.println("sigma:...... " + localBigInteger5 + " (" + localBigInteger5.bitLength() + " bits)");
      System.out.println("a:.......... " + localBigInteger6);
      System.out.println("b:.......... " + localBigInteger7);
      System.out.println("p':......... " + localBigInteger10);
      System.out.println("q':......... " + localBigInteger12);
      System.out.println("p:.......... " + localBigInteger11);
      System.out.println("q:.......... " + localBigInteger13);
      System.out.println("n:.......... " + localBigInteger14);
      System.out.println("phi(n):..... " + localBigInteger15);
      System.out.println("g:.......... " + localBigInteger18);
      System.out.println();
    }
    NaccacheSternKeyParameters localNaccacheSternKeyParameters = new NaccacheSternKeyParameters(false, localBigInteger18, localBigInteger14, localBigInteger5.bitLength());
    AsymmetricCipherKeyPair localAsymmetricCipherKeyPair = new AsymmetricCipherKeyPair(localNaccacheSternKeyParameters, new NaccacheSternPrivateKeyParameters(localBigInteger18, localBigInteger14, localBigInteger5.bitLength(), localVector1, localBigInteger15));
    return localAsymmetricCipherKeyPair;
  }

  public void init(KeyGenerationParameters paramKeyGenerationParameters)
  {
    this.param = ((NaccacheSternKeyGenerationParameters)paramKeyGenerationParameters);
  }
}