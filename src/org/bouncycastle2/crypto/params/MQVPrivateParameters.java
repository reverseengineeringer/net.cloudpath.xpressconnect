package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class MQVPrivateParameters
  implements CipherParameters
{
  private ECPrivateKeyParameters ephemeralPrivateKey;
  private ECPublicKeyParameters ephemeralPublicKey;
  private ECPrivateKeyParameters staticPrivateKey;

  public MQVPrivateParameters(ECPrivateKeyParameters paramECPrivateKeyParameters1, ECPrivateKeyParameters paramECPrivateKeyParameters2)
  {
    this(paramECPrivateKeyParameters1, paramECPrivateKeyParameters2, null);
  }

  public MQVPrivateParameters(ECPrivateKeyParameters paramECPrivateKeyParameters1, ECPrivateKeyParameters paramECPrivateKeyParameters2, ECPublicKeyParameters paramECPublicKeyParameters)
  {
    this.staticPrivateKey = paramECPrivateKeyParameters1;
    this.ephemeralPrivateKey = paramECPrivateKeyParameters2;
    this.ephemeralPublicKey = paramECPublicKeyParameters;
  }

  public ECPrivateKeyParameters getEphemeralPrivateKey()
  {
    return this.ephemeralPrivateKey;
  }

  public ECPublicKeyParameters getEphemeralPublicKey()
  {
    return this.ephemeralPublicKey;
  }

  public ECPrivateKeyParameters getStaticPrivateKey()
  {
    return this.staticPrivateKey;
  }
}