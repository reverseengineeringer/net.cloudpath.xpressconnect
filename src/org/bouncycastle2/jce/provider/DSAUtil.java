package org.bouncycastle2.jce.provider;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.DSAParameters;
import org.bouncycastle2.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle2.crypto.params.DSAPublicKeyParameters;

public class DSAUtil
{
  public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if ((paramPrivateKey instanceof DSAPrivateKey))
    {
      DSAPrivateKey localDSAPrivateKey = (DSAPrivateKey)paramPrivateKey;
      return new DSAPrivateKeyParameters(localDSAPrivateKey.getX(), new DSAParameters(localDSAPrivateKey.getParams().getP(), localDSAPrivateKey.getParams().getQ(), localDSAPrivateKey.getParams().getG()));
    }
    throw new InvalidKeyException("can't identify DSA private key.");
  }

  public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    if ((paramPublicKey instanceof DSAPublicKey))
    {
      DSAPublicKey localDSAPublicKey = (DSAPublicKey)paramPublicKey;
      return new DSAPublicKeyParameters(localDSAPublicKey.getY(), new DSAParameters(localDSAPublicKey.getParams().getP(), localDSAPublicKey.getParams().getQ(), localDSAPublicKey.getParams().getG()));
    }
    throw new InvalidKeyException("can't identify DSA public key: " + paramPublicKey.getClass().getName());
  }
}