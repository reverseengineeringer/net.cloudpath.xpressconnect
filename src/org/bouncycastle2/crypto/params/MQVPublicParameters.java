package org.bouncycastle2.crypto.params;

import org.bouncycastle2.crypto.CipherParameters;

public class MQVPublicParameters
  implements CipherParameters
{
  private ECPublicKeyParameters ephemeralPublicKey;
  private ECPublicKeyParameters staticPublicKey;

  public MQVPublicParameters(ECPublicKeyParameters paramECPublicKeyParameters1, ECPublicKeyParameters paramECPublicKeyParameters2)
  {
    this.staticPublicKey = paramECPublicKeyParameters1;
    this.ephemeralPublicKey = paramECPublicKeyParameters2;
  }

  public ECPublicKeyParameters getEphemeralPublicKey()
  {
    return this.ephemeralPublicKey;
  }

  public ECPublicKeyParameters getStaticPublicKey()
  {
    return this.staticPublicKey;
  }
}