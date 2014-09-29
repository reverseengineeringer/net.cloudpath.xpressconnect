package org.bouncycastle2.crypto.tls;

import org.bouncycastle2.crypto.DSA;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle2.crypto.signers.DSASigner;

class TlsDSSSigner extends TlsDSASigner
{
  protected DSA createDSAImpl()
  {
    return new DSASigner();
  }

  public boolean isValidPublicKey(AsymmetricKeyParameter paramAsymmetricKeyParameter)
  {
    return paramAsymmetricKeyParameter instanceof DSAPublicKeyParameters;
  }
}