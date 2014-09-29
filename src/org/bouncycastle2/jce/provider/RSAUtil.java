package org.bouncycastle2.jce.provider;

import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.crypto.params.RSAKeyParameters;
import org.bouncycastle2.crypto.params.RSAPrivateCrtKeyParameters;

class RSAUtil
{
  static RSAKeyParameters generatePrivateKeyParameter(RSAPrivateKey paramRSAPrivateKey)
  {
    if ((paramRSAPrivateKey instanceof RSAPrivateCrtKey))
    {
      RSAPrivateCrtKey localRSAPrivateCrtKey = (RSAPrivateCrtKey)paramRSAPrivateKey;
      return new RSAPrivateCrtKeyParameters(localRSAPrivateCrtKey.getModulus(), localRSAPrivateCrtKey.getPublicExponent(), localRSAPrivateCrtKey.getPrivateExponent(), localRSAPrivateCrtKey.getPrimeP(), localRSAPrivateCrtKey.getPrimeQ(), localRSAPrivateCrtKey.getPrimeExponentP(), localRSAPrivateCrtKey.getPrimeExponentQ(), localRSAPrivateCrtKey.getCrtCoefficient());
    }
    return new RSAKeyParameters(true, paramRSAPrivateKey.getModulus(), paramRSAPrivateKey.getPrivateExponent());
  }

  static RSAKeyParameters generatePublicKeyParameter(RSAPublicKey paramRSAPublicKey)
  {
    return new RSAKeyParameters(false, paramRSAPublicKey.getModulus(), paramRSAPublicKey.getPublicExponent());
  }

  static boolean isRsaOid(DERObjectIdentifier paramDERObjectIdentifier)
  {
    return (paramDERObjectIdentifier.equals(PKCSObjectIdentifiers.rsaEncryption)) || (paramDERObjectIdentifier.equals(X509ObjectIdentifiers.id_ea_rsa)) || (paramDERObjectIdentifier.equals(PKCSObjectIdentifiers.id_RSASSA_PSS)) || (paramDERObjectIdentifier.equals(PKCSObjectIdentifiers.id_RSAES_OAEP));
  }
}