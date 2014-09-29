package org.bouncycastle2.crypto.tls;

import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ECPublicKeyParameters;
import org.bouncycastle2.crypto.signers.ECDSASigner;

class TlsECDSASigner extends TlsDSASigner
{
  protected DSA createDSAImpl()
  {
    return new ECDSASigner();
  }

  public boolean isValidPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    return paramAsymmetricKeyParameter instanceof ECPublicKeyParameters;
  }
}