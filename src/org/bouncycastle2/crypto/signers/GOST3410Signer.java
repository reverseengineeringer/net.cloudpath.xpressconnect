package org.bouncycastle2.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.CipherParameters;
import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.params.GOST3410KeyParameters;
import org.bouncycastle2.crypto.params.GOST3410Parameters;
import org.bouncycastle2.crypto.params.GOST3410PrivateKeyParameters;
import org.bouncycastle2.crypto.params.GOST3410PublicKeyParameters;
import org.bouncycastle2.crypto.params.ParametersWithRandom;

public class GOST3410Signer
  implements DSA
{
  GOST3410KeyParameters key;
  SecureRandom random;

  public BigInteger[] generateSignature(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte.length)
      {
        BigInteger localBigInteger1 = new BigInteger(1, arrayOfByte);
        GOST3410Parameters localGOST3410Parameters = this.key.getParameters();
        BigInteger localBigInteger2;
        do
          localBigInteger2 = new BigInteger(localGOST3410Parameters.getQ().bitLength(), this.random);
        while (localBigInteger2.compareTo(localGOST3410Parameters.getQ()) >= 0);
        BigInteger localBigInteger3 = localGOST3410Parameters.getA().modPow(localBigInteger2, localGOST3410Parameters.getP()).mod(localGOST3410Parameters.getQ());
        return new BigInteger[] { localBigInteger3, localBigInteger2.multiply(localBigInteger1).add(((GOST3410PrivateKeyParameters)this.key).getX().multiply(localBigInteger3)).mod(localGOST3410Parameters.getQ()) };
      }
      arrayOfByte[i] = paramArrayOfByte[(-1 + arrayOfByte.length - i)];
    }
  }

  public void init(boolean paramBoolean, CipherParameters paramCipherParameters)
  {
    if (paramBoolean)
    {
      if ((paramCipherParameters instanceof ParametersWithRandom))
      {
        ParametersWithRandom localParametersWithRandom = (ParametersWithRandom)paramCipherParameters;
        this.random = localParametersWithRandom.getRandom();
        this.key = ((GOST3410PrivateKeyParameters)localParametersWithRandom.getParameters());
        return;
      }
      this.random = new SecureRandom();
      this.key = ((GOST3410PrivateKeyParameters)paramCipherParameters);
      return;
    }
    this.key = ((GOST3410PublicKeyParameters)paramCipherParameters);
  }

  public boolean verifySignature(byte[] paramArrayOfByte, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    int i = 0;
    BigInteger localBigInteger1;
    GOST3410Parameters localGOST3410Parameters;
    BigInteger localBigInteger2;
    if (i == arrayOfByte.length)
    {
      localBigInteger1 = new BigInteger(1, arrayOfByte);
      localGOST3410Parameters = this.key.getParameters();
      localBigInteger2 = BigInteger.valueOf(0L);
      if ((localBigInteger2.compareTo(paramBigInteger1) < 0) && (localGOST3410Parameters.getQ().compareTo(paramBigInteger1) > 0))
        break label88;
    }
    label88: 
    while ((localBigInteger2.compareTo(paramBigInteger2) >= 0) || (localGOST3410Parameters.getQ().compareTo(paramBigInteger2) <= 0))
    {
      return false;
      arrayOfByte[i] = paramArrayOfByte[(-1 + arrayOfByte.length - i)];
      i++;
      break;
    }
    BigInteger localBigInteger3 = localBigInteger1.modPow(localGOST3410Parameters.getQ().subtract(new BigInteger("2")), localGOST3410Parameters.getQ());
    BigInteger localBigInteger4 = paramBigInteger2.multiply(localBigInteger3).mod(localGOST3410Parameters.getQ());
    BigInteger localBigInteger5 = localGOST3410Parameters.getQ().subtract(paramBigInteger1).multiply(localBigInteger3).mod(localGOST3410Parameters.getQ());
    return localGOST3410Parameters.getA().modPow(localBigInteger4, localGOST3410Parameters.getP()).multiply(((GOST3410PublicKeyParameters)this.key).getY().modPow(localBigInteger5, localGOST3410Parameters.getP())).mod(localGOST3410Parameters.getP()).mod(localGOST3410Parameters.getQ()).equals(paramBigInteger1);
  }
}