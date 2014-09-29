package org.bouncycastle2.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.params.ElGamalParameters;

public class ElGamalParametersGenerator
{
  private int certainty;
  private SecureRandom random;
  private int size;

  public ElGamalParameters generateParameters()
  {
    BigInteger[] arrayOfBigInteger = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
    BigInteger localBigInteger = arrayOfBigInteger[0];
    return new ElGamalParameters(localBigInteger, DHParametersHelper.selectGenerator(localBigInteger, arrayOfBigInteger[1], this.random));
  }

  public void init(int paramInt1, int paramInt2, SecureRandom paramSecureRandom)
  {
    this.size = paramInt1;
    this.certainty = paramInt2;
    this.random = paramSecureRandom;
  }
}