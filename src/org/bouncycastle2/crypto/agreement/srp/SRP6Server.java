package org.bouncycastle2.crypto.agreement.srp;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle2.crypto.CryptoException;
import org.bouncycastle2.crypto.Digest;

public class SRP6Server
{
  protected BigInteger A;
  protected BigInteger B;
  protected BigInteger N;
  protected BigInteger S;
  protected BigInteger b;
  protected Digest digest;
  protected BigInteger g;
  protected SecureRandom random;
  protected BigInteger u;
  protected BigInteger v;

  private BigInteger calculateS()
  {
    return this.v.modPow(this.u, this.N).multiply(this.A).mod(this.N).modPow(this.b, this.N);
  }

  public BigInteger calculateSecret(BigInteger paramBigInteger)
    throws CryptoException
  {
    this.A = SRP6Util.validatePublicValue(this.N, paramBigInteger);
    this.u = SRP6Util.calculateU(this.digest, this.N, this.A, this.B);
    this.S = calculateS();
    return this.S;
  }

  public BigInteger generateServerCredentials()
  {
    BigInteger localBigInteger = SRP6Util.calculateK(this.digest, this.N, this.g);
    this.b = selectPrivateValue();
    this.B = localBigInteger.multiply(this.v).mod(this.N).add(this.g.modPow(this.b, this.N)).mod(this.N);
    return this.B;
  }

  public void init(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, Digest paramDigest, SecureRandom paramSecureRandom)
  {
    this.N = paramBigInteger1;
    this.g = paramBigInteger2;
    this.v = paramBigInteger3;
    this.random = paramSecureRandom;
    this.digest = paramDigest;
  }

  protected BigInteger selectPrivateValue()
  {
    return SRP6Util.generatePrivateValue(this.digest, this.N, this.g, this.random);
  }
}