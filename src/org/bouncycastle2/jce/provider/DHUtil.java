package org.bouncycastle2.jce.provider;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DHParameters;
import org.bouncycastle2.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DHPublicKeyParameters;

public class DHUtil
{
  public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if ((paramPrivateKey instanceof DHPrivateKey))
    {
      DHPrivateKey localDHPrivateKey = (DHPrivateKey)paramPrivateKey;
      return new DHPrivateKeyParameters(localDHPrivateKey.getX(), new DHParameters(localDHPrivateKey.getParams().getP(), localDHPrivateKey.getParams().getG(), null, localDHPrivateKey.getParams().getL()));
    }
    throw new InvalidKeyException("can't identify DH private key.");
  }

  public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    if ((paramPublicKey instanceof DHPublicKey))
    {
      DHPublicKey localDHPublicKey = (DHPublicKey)paramPublicKey;
      return new DHPublicKeyParameters(localDHPublicKey.getY(), new DHParameters(localDHPublicKey.getParams().getP(), localDHPublicKey.getParams().getG(), null, localDHPublicKey.getParams().getL()));
    }
    throw new InvalidKeyException("can't identify DH public key.");
  }
}