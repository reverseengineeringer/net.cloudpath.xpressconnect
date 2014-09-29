package org.bouncycastle2.crypto.params;

import java.math.BigInteger;
import org.bouncycastle2.crypto.CipherParameters;

public class RSABlindingParameters
  implements CipherParameters
{
  private BigInteger blindingFactor;
  private RSAKeyParameters publicKey;

  public RSABlindingParameters(RSAKeyParameters paramRSAKeyParameters, BigInteger paramBigInteger)
  {
    if ((paramRSAKeyParameters instanceof RSAPrivateCrtKeyParameters))
      throw new IllegalArgumentException("RSA parameters should be for a public key");
    this.publicKey = paramRSAKeyParameters;
    this.blindingFactor = paramBigInteger;
  }

  public BigInteger getBlindingFactor()
  {
    return this.blindingFactor;
  }

  public RSAKeyParameters getPublicKey()
  {
    return this.publicKey;
  }
}