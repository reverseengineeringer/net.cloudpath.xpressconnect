package org.bouncycastle2.crypto.engines;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.AsymmetricBlockCipher;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DataLengthException;
import org.bouncycastle2.crypto.params.ElGamalKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;
import org.bouncycastle2.util.BigIntegers;

public class ElGamalEngine
  implements AsymmetricBlockCipher
{
  private static final BigInteger ONE = BigInteger.valueOf(1L);
  private static final BigInteger TWO = BigInteger.valueOf(2L);
  private static final BigInteger ZERO = BigInteger.valueOf(0L);
  private int bitSize;
  private boolean forEncryption;
  private ElGamalKeyParameters key;
  private SecureRandom random;

  public int getInputBlockSize()
  {
    if (this.forEncryption)
      return (-1 + this.bitSize) / 8;
    return 2 * ((7 + this.bitSize) / 8);
  }

  public int getOutputBlockSize()
  {
    if (this.forEncryption)
      return 2 * ((7 + this.bitSize) / 8);
    return (-1 + this.bitSize) / 8;
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    ParametersWithRandom localParametersWithRandom;
    if ((paramCipherParameters instanceof ParametersWithRandom))
    {
      localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
      this.key = ((ElGamalKeyParameters)localParametersWithRandom.getParameters());
    }
    for (this.random = localParametersWithRandom.getRandom(); ; this.random = new SecureRandom())
    {
      this.forEncryption = paramBoolean;
      this.bitSize = this.key.getParameters().getP().bitLength();
      if (!paramBoolean)
        break;
      if ((this.key instanceof ElGamalPublicKeyParameters))
        return;
      throw new IllegalArgumentException("ElGamalPublicKeyParameters are required for encryption.");
      this.key = ((ElGamalKeyParameters)paramCipherParameters);
    }
    if (!(this.key instanceof ElGamalPrivateKeyParameters))
      throw new IllegalArgumentException("ElGamalPrivateKeyParameters are required for decryption.");
  }

  public byte[] processBlock(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (this.key == null)
      throw new IllegalStateException("ElGamal engine not initialised");
    if (this.forEncryption);
    for (int i = (7 + (-1 + this.bitSize)) / 8; paramInt2 > i; i = getInputBlockSize())
      throw new DataLengthException("input too large for ElGamal cipher.\n");
    BigInteger localBigInteger1 = this.key.getParameters().getP();
    if ((this.key instanceof ElGamalPrivateKeyParameters))
    {
      byte[] arrayOfByte5 = new byte[paramInt2 / 2];
      byte[] arrayOfByte6 = new byte[paramInt2 / 2];
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte5, 0, arrayOfByte5.length);
      System.arraycopy(paramArrayOfByte, paramInt1 + arrayOfByte5.length, arrayOfByte6, 0, arrayOfByte6.length);
      BigInteger localBigInteger6 = new BigInteger(1, arrayOfByte5);
      BigInteger localBigInteger7 = new BigInteger(1, arrayOfByte6);
      ElGamalPrivateKeyParameters localElGamalPrivateKeyParameters = (ElGamalPrivateKeyParameters)this.key;
      return BigIntegers.asUnsignedByteArray(localBigInteger6.modPow(localBigInteger1.subtract(ONE).subtract(localElGamalPrivateKeyParameters.getX()), localBigInteger1).multiply(localBigInteger7).mod(localBigInteger1));
    }
    byte[] arrayOfByte1;
    if ((paramInt1 != 0) || (paramInt2 != paramArrayOfByte.length))
    {
      arrayOfByte1 = new byte[paramInt2];
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte1, 0, paramInt2);
    }
    BigInteger localBigInteger2;
    while (true)
    {
      localBigInteger2 = new BigInteger(1, arrayOfByte1);
      if (localBigInteger2.bitLength() < localBigInteger1.bitLength())
        break;
      throw new DataLengthException("input too large for ElGamal cipher.\n");
      arrayOfByte1 = paramArrayOfByte;
    }
    ElGamalPublicKeyParameters localElGamalPublicKeyParameters = (ElGamalPublicKeyParameters)this.key;
    int j = localBigInteger1.bitLength();
    BigInteger localBigInteger3 = new BigInteger(j, this.random);
    byte[] arrayOfByte2;
    byte[] arrayOfByte3;
    byte[] arrayOfByte4;
    if ((!localBigInteger3.equals(ZERO)) && (localBigInteger3.compareTo(localBigInteger1.subtract(TWO)) <= 0))
    {
      BigInteger localBigInteger4 = this.key.getParameters().getG().modPow(localBigInteger3, localBigInteger1);
      BigInteger localBigInteger5 = localBigInteger2.multiply(localElGamalPublicKeyParameters.getY().modPow(localBigInteger3, localBigInteger1)).mod(localBigInteger1);
      arrayOfByte2 = localBigInteger4.toByteArray();
      arrayOfByte3 = localBigInteger5.toByteArray();
      arrayOfByte4 = new byte[getOutputBlockSize()];
      if (arrayOfByte2.length <= arrayOfByte4.length / 2)
        break label472;
      System.arraycopy(arrayOfByte2, 1, arrayOfByte4, arrayOfByte4.length / 2 - (-1 + arrayOfByte2.length), -1 + arrayOfByte2.length);
    }
    while (true)
    {
      if (arrayOfByte3.length <= arrayOfByte4.length / 2)
        break label495;
      System.arraycopy(arrayOfByte3, 1, arrayOfByte4, arrayOfByte4.length - (-1 + arrayOfByte3.length), -1 + arrayOfByte3.length);
      return arrayOfByte4;
      localBigInteger3 = new BigInteger(j, this.random);
      break;
      label472: System.arraycopy(arrayOfByte2, 0, arrayOfByte4, arrayOfByte4.length / 2 - arrayOfByte2.length, arrayOfByte2.length);
    }
    label495: System.arraycopy(arrayOfByte3, 0, arrayOfByte4, arrayOfByte4.length - arrayOfByte3.length, arrayOfByte3.length);
    return arrayOfByte4;
  }
}