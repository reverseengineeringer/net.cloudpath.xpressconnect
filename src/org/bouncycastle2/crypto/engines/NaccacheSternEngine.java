package org.bouncycastle2.crypto.engines;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.Vector;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.InvalidCipherTextException;
import org.bouncycastle2.crypto.params.NaccacheSternKeyParameters;
import org.bouncycastle2.crypto.params.NaccacheSternPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.util.Arrays;

public class NaccacheSternEngine
  implements AsymmetricBlockCipher
{
  private static BigInteger ONE = BigInteger.valueOf(1L);
  private static BigInteger ZERO = BigInteger.valueOf(0L);
  private boolean debug = false;
  private boolean forEncryption;
  private NaccacheSternKeyParameters key;
  private Vector[] lookup = null;

  private static BigInteger chineseRemainder(Vector paramVector1, Vector paramVector2)
  {
    BigInteger localBigInteger1 = ZERO;
    BigInteger localBigInteger2 = ONE;
    int i = 0;
    if (i >= paramVector2.size());
    for (int j = 0; ; j++)
    {
      if (j >= paramVector2.size())
      {
        return localBigInteger1.mod(localBigInteger2);
        localBigInteger2 = localBigInteger2.multiply((BigInteger)paramVector2.elementAt(i));
        i++;
        break;
      }
      BigInteger localBigInteger3 = (BigInteger)paramVector2.elementAt(j);
      BigInteger localBigInteger4 = localBigInteger2.divide(localBigInteger3);
      localBigInteger1 = localBigInteger1.add(localBigInteger4.multiply(localBigInteger4.modInverse(localBigInteger3)).multiply((BigInteger)paramVector1.elementAt(j)));
    }
  }

  public byte[] addCryptedBlocks(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws InvalidCipherTextException
  {
    if (this.forEncryption)
    {
      if ((paramArrayOfByte1.length > getOutputBlockSize()) || (paramArrayOfByte2.length > getOutputBlockSize()))
        throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
    }
    else if ((paramArrayOfByte1.length > getInputBlockSize()) || (paramArrayOfByte2.length > getInputBlockSize()))
      throw new InvalidCipherTextException("BlockLength too large for simple addition.\n");
    BigInteger localBigInteger1 = new BigInteger(1, paramArrayOfByte1);
    BigInteger localBigInteger2 = new BigInteger(1, paramArrayOfByte2);
    BigInteger localBigInteger3 = localBigInteger1.multiply(localBigInteger2).mod(this.key.getModulus());
    if (this.debug)
    {
      System.out.println("c(m1) as BigInteger:....... " + localBigInteger1);
      System.out.println("c(m2) as BigInteger:....... " + localBigInteger2);
      System.out.println("c(m1)*c(m2)%n = c(m1+m2)%n: " + localBigInteger3);
    }
    byte[] arrayOfByte = this.key.getModulus().toByteArray();
    Arrays.fill(arrayOfByte, (byte)0);
    System.arraycopy(localBigInteger3.toByteArray(), 0, arrayOfByte, arrayOfByte.length - localBigInteger3.toByteArray().length, localBigInteger3.toByteArray().length);
    return arrayOfByte;
  }

  public byte[] encrypt(BigInteger paramBigInteger)
  {
    byte[] arrayOfByte1 = this.key.getModulus().toByteArray();
    Arrays.fill(arrayOfByte1, (byte)0);
    byte[] arrayOfByte2 = this.key.getG().modPow(paramBigInteger, this.key.getModulus()).toByteArray();
    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, arrayOfByte1.length - arrayOfByte2.length, arrayOfByte2.length);
    if (this.debug)
      System.out.println("Encrypted value is:  " + new BigInteger(arrayOfByte1));
    return arrayOfByte1;
  }

  public int getInputBlockSize()
  {
    if (this.forEncryption)
      return -1 + (7 + this.key.getLowerSigmaBound()) / 8;
    return this.key.getModulus().toByteArray().length;
  }

  public int getOutputBlockSize()
  {
    if (this.forEncryption)
      return this.key.getModulus().toByteArray().length;
    return -1 + (7 + this.key.getLowerSigmaBound()) / 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    this.forEncryption = paramBoolean;
    if ((paramCipherParameters instanceof ParametersWithRandom))
      paramCipherParameters = ((ParametersWithRandom)paramCipherParameters).getParameters();
    this.key = ((NaccacheSternKeyParameters)paramCipherParameters);
    NaccacheSternPrivateKeyParameters localNaccacheSternPrivateKeyParameters;
    Vector localVector;
    int i;
    if (!this.forEncryption)
    {
      if (this.debug)
        System.out.println("Constructing lookup Array");
      localNaccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
      localVector = localNaccacheSternPrivateKeyParameters.getSmallPrimes();
      this.lookup = new Vector[localVector.size()];
      i = 0;
      if (i < localVector.size());
    }
    else
    {
      return;
    }
    BigInteger localBigInteger1 = (BigInteger)localVector.elementAt(i);
    int j = localBigInteger1.intValue();
    this.lookup[i] = new Vector();
    this.lookup[i].addElement(ONE);
    if (this.debug)
      System.out.println("Constructing lookup ArrayList for " + j);
    BigInteger localBigInteger2 = ZERO;
    for (int k = 1; ; k++)
    {
      if (k >= j)
      {
        i++;
        break;
      }
      localBigInteger2 = localBigInteger2.add(localNaccacheSternPrivateKeyParameters.getPhi_n());
      BigInteger localBigInteger3 = localBigInteger2.divide(localBigInteger1);
      this.lookup[i].addElement(localNaccacheSternPrivateKeyParameters.getG().modPow(localBigInteger3, localNaccacheSternPrivateKeyParameters.getModulus()));
    }
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws InvalidCipherTextException
  {
    if (this.key == null)
      throw new IllegalStateException("NaccacheStern engine not initialised");
    if (paramInt2 > 1 + getInputBlockSize())
      throw new DataLengthException("input too large for Naccache-Stern cipher.\n");
    if ((!this.forEncryption) && (paramInt2 < getInputBlockSize()))
      throw new InvalidCipherTextException("BlockLength does not match modulus for Naccache-Stern cipher.\n");
    byte[] arrayOfByte;
    if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte.length))
    {
      arrayOfByte = new byte[paramInt2];
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    }
    BigInteger localBigInteger1;
    while (true)
    {
      localBigInteger1 = new BigInteger(1, arrayOfByte);
      if (this.debug)
        System.out.println("input as BigInteger: " + localBigInteger1);
      if (!this.forEncryption)
        break;
      return encrypt(localBigInteger1);
      arrayOfByte = paramArrayOfByte;
    }
    Vector localVector1 = new Vector();
    NaccacheSternPrivateKeyParameters localNaccacheSternPrivateKeyParameters = (NaccacheSternPrivateKeyParameters)this.key;
    Vector localVector2 = localNaccacheSternPrivateKeyParameters.getSmallPrimes();
    for (int i = 0; ; i++)
    {
      if (i >= localVector2.size())
        return chineseRemainder(localVector1, localVector2).toByteArray();
      BigInteger localBigInteger2 = localBigInteger1.modPow(localNaccacheSternPrivateKeyParameters.getPhi_n().divide((BigInteger)localVector2.elementAt(i)), localNaccacheSternPrivateKeyParameters.getModulus());
      Vector localVector3 = this.lookup[i];
      if (this.lookup[i].size() != ((BigInteger)localVector2.elementAt(i)).intValue())
      {
        if (this.debug)
          System.out.println("Prime is " + localVector2.elementAt(i) + ", lookup table has size " + localVector3.size());
        StringBuilder localStringBuilder = new StringBuilder("Error in lookup Array for ");
        InvalidCipherTextException localInvalidCipherTextException = new InvalidCipherTextException(((BigInteger)localVector2.elementAt(i)).intValue() + ": Size mismatch. Expected ArrayList with length " + ((BigInteger)localVector2.elementAt(i)).intValue() + " but found ArrayList of length " + this.lookup[i].size());
        throw localInvalidCipherTextException;
      }
      int j = localVector3.indexOf(localBigInteger2);
      if (j == -1)
      {
        if (this.debug)
        {
          System.out.println("Actual prime is " + localVector2.elementAt(i));
          System.out.println("Decrypted value is " + localBigInteger2);
          System.out.println("LookupList for " + localVector2.elementAt(i) + " with size " + this.lookup[i].size() + " is: ");
        }
        for (int k = 0; ; k++)
        {
          if (k >= this.lookup[i].size())
            throw new InvalidCipherTextException("Lookup failed");
          System.out.println(this.lookup[i].elementAt(k));
        }
      }
      localVector1.addElement(BigInteger.valueOf(j));
    }
  }

  public byte[] processData(byte[] paramArrayOfByte)
    throws InvalidCipherTextException
  {
    if (this.debug)
      System.out.println();
    if (paramArrayOfByte.length > getInputBlockSize())
    {
      int i = getInputBlockSize();
      int j = getOutputBlockSize();
      if (this.debug)
      {
        System.out.println("Input blocksize is:  " + i + " bytes");
        System.out.println("Output blocksize is: " + j + " bytes");
        System.out.println("Data has length:.... " + paramArrayOfByte.length + " bytes");
      }
      int k = 0;
      int m = 0;
      byte[] arrayOfByte1 = new byte[j * (1 + paramArrayOfByte.length / i)];
      if (k >= paramArrayOfByte.length)
      {
        byte[] arrayOfByte3 = new byte[m];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, m);
        if (this.debug)
          System.out.println("returning " + arrayOfByte3.length + " bytes");
        return arrayOfByte3;
      }
      byte[] arrayOfByte2;
      if (k + i < paramArrayOfByte.length)
      {
        arrayOfByte2 = processBlock(paramArrayOfByte, k, i);
        k += i;
      }
      while (true)
      {
        if (this.debug)
          System.out.println("new datapos is " + k);
        if (arrayOfByte2 == null)
          break label313;
        System.arraycopy(arrayOfByte2, 0, arrayOfByte1, m, arrayOfByte2.length);
        m += arrayOfByte2.length;
        break;
        arrayOfByte2 = processBlock(paramArrayOfByte, k, paramArrayOfByte.length - k);
        k += paramArrayOfByte.length - k;
      }
      label313: if (this.debug)
        System.out.println("cipher returned null");
      throw new InvalidCipherTextException("cipher returned null");
    }
    if (this.debug)
      System.out.println("data size is less then input block size, processing directly");
    return processBlock(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void setDebug(boolean paramBoolean)
  {
    this.debug = paramBoolean;
  }
}