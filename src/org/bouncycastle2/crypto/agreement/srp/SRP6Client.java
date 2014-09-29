package org.bouncycastle2.crypto.agreement.srp;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Digest;

public class SRP6Client
{
  protected BigInteger A;
  protected BigInteger B;
  protected BigInteger N;
  protected BigInteger S;
  protected BigInteger a;
  protected Digest digest;
  protected BigInteger g;
  protected SecureRandom random;
  protected BigInteger u;
  protected BigInteger x;

  private BigInteger calculateS()
  {
    BigInteger localBigInteger1 = SRP6Util.calculateK(this.digest, this.N, this.g);
    BigInteger localBigInteger2 = this.u.multiply(this.x).add(this.a);
    BigInteger localBigInteger3 = this.g.modPow(this.x, this.N).multiply(localBigInteger1).mod(this.N);
    return this.B.subtract(localBigInteger3).mod(this.N).modPow(localBigInteger2, this.N);
  }

  public BigInteger calculateSecret(BigInteger paramBigInteger)
    throws CryptoException
  {
    this.B = SRP6Util.validatePublicValue(this.N, paramBigInteger);
    this.u = SRP6Util.calculateU(this.digest, this.N, this.A, this.B);
    this.S = calculateS();
    return this.S;
  }

  public BigInteger generateClientCredentials(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    this.x = SRP6Util.calculateX(this.digest, this.N, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    this.a = selectPrivateValue();
    this.A = this.g.modPow(this.a, this.N);
    return this.A;
  }

  public void init(BigInteger paramBigInteger1, BigInteger paramBigInteger2, Digest paramDigest, SecureRandom paramSecureRandom)
  {
    this.N = paramBigInteger1;
    this.g = paramBigInteger2;
    this.digest = paramDigest;
    this.random = paramSecureRandom;
  }

  protected BigInteger selectPrivateValue()
  {
    return SRP6Util.generatePrivateValue(this.digest, this.N, this.g, this.random);
  }
}