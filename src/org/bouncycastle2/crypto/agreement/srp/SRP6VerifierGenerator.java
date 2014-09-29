package org.bouncycastle2.crypto.agreement.srp;

import java.math.BigInteger;
import org.bouncycastle2.crypto.Digest;

public class SRP6VerifierGenerator
{
  protected BigInteger N;
  protected Digest digest;
  protected BigInteger g;

  public BigInteger generateVerifier(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    BigInteger localBigInteger = SRP6Util.calculateX(this.digest, this.N, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
    return this.g.modPow(localBigInteger, this.N);
  }

  public void init(BigInteger paramBigInteger1, BigInteger paramBigInteger2, Digest paramDigest)
  {
    this.N = paramBigInteger1;
    this.g = paramBigInteger2;
    this.digest = paramDigest;
  }
}