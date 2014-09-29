package org.bouncycastle2.jce.provider;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import org.bouncycastle2.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle2.crypto.params.ElGamalParameters;
import org.bouncycastle2.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle2.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle2.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle2.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle2.jce.spec.ElGamalParameterSpec;

public class ElGamalUtil
{
  public static AsymmetricKeyParameter generatePrivateKeyParameter(PrivateKey paramPrivateKey)
    throws InvalidKeyException
  {
    if ((paramPrivateKey instanceof ElGamalPrivateKey))
    {
      ElGamalPrivateKey localElGamalPrivateKey = (ElGamalPrivateKey)paramPrivateKey;
      return new ElGamalPrivateKeyParameters(localElGamalPrivateKey.getX(), new ElGamalParameters(localElGamalPrivateKey.getParameters().getP(), localElGamalPrivateKey.getParameters().getG()));
    }
    if ((paramPrivateKey instanceof DHPrivateKey))
    {
      DHPrivateKey localDHPrivateKey = (DHPrivateKey)paramPrivateKey;
      return new ElGamalPrivateKeyParameters(localDHPrivateKey.getX(), new ElGamalParameters(localDHPrivateKey.getParams().getP(), localDHPrivateKey.getParams().getG()));
    }
    throw new InvalidKeyException("can't identify private key for El Gamal.");
  }

  public static AsymmetricKeyParameter generatePublicKeyParameter(PublicKey paramPublicKey)
    throws InvalidKeyException
  {
    if ((paramPublicKey instanceof ElGamalPublicKey))
    {
      ElGamalPublicKey localElGamalPublicKey = (ElGamalPublicKey)paramPublicKey;
      return new ElGamalPublicKeyParameters(localElGamalPublicKey.getY(), new ElGamalParameters(localElGamalPublicKey.getParameters().getP(), localElGamalPublicKey.getParameters().getG()));
    }
    if ((paramPublicKey instanceof DHPublicKey))
    {
      DHPublicKey localDHPublicKey = (DHPublicKey)paramPublicKey;
      return new ElGamalPublicKeyParameters(localDHPublicKey.getY(), new ElGamalParameters(localDHPublicKey.getParams().getP(), localDHPublicKey.getParams().getG()));
    }
    throw new InvalidKeyException("can't identify public key for El Gamal.");
  }
}