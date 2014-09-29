package org.bouncycastle2.jce.provider.asymmetric.ec;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle2.jce.provider.JCEECPrivateKey;
import org.bouncycastle2.jce.provider.JCEECPublicKey;
import org.bouncycastle2.jce.provider.JDKKeyFactory;
import org.bouncycastle2.jce.provider.ProviderUtil;
import org.bouncycastle2.jce.spec.ECParameterSpec;

public class KeyFactory extends JDKKeyFactory
{
  String algorithm;

  KeyFactory(String paramString)
  {
    this.algorithm = paramString;
  }

  protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    if ((paramKeySpec instanceof PKCS8EncodedKeySpec))
      try
      {
        JCEECPrivateKey localJCEECPrivateKey1 = (JCEECPrivateKey)JDKKeyFactory.createPrivateKeyFromDERStream(((PKCS8EncodedKeySpec)paramKeySpec).getEncoded());
        JCEECPrivateKey localJCEECPrivateKey2 = new JCEECPrivateKey(this.algorithm, localJCEECPrivateKey1);
        return localJCEECPrivateKey2;
      }
      catch (Exception localException)
      {
        throw new InvalidKeySpecException(localException.toString());
      }
    if ((paramKeySpec instanceof org.bouncycastle2.jce.spec.ECPrivateKeySpec))
      return new JCEECPrivateKey(this.algorithm, (org.bouncycastle2.jce.spec.ECPrivateKeySpec)paramKeySpec);
    if ((paramKeySpec instanceof java.security.spec.ECPrivateKeySpec))
      return new JCEECPrivateKey(this.algorithm, (java.security.spec.ECPrivateKeySpec)paramKeySpec);
    throw new InvalidKeySpecException("Unknown KeySpec type: " + paramKeySpec.getClass().getName());
  }

  protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    if ((paramKeySpec instanceof X509EncodedKeySpec))
      try
      {
        JCEECPublicKey localJCEECPublicKey1 = (JCEECPublicKey)JDKKeyFactory.createPublicKeyFromDERStream(((X509EncodedKeySpec)paramKeySpec).getEncoded());
        JCEECPublicKey localJCEECPublicKey2 = new JCEECPublicKey(this.algorithm, localJCEECPublicKey1);
        return localJCEECPublicKey2;
      }
      catch (Exception localException)
      {
        throw new InvalidKeySpecException(localException.toString());
      }
    if ((paramKeySpec instanceof org.bouncycastle2.jce.spec.ECPublicKeySpec))
      return new JCEECPublicKey(this.algorithm, (org.bouncycastle2.jce.spec.ECPublicKeySpec)paramKeySpec);
    if ((paramKeySpec instanceof java.security.spec.ECPublicKeySpec))
      return new JCEECPublicKey(this.algorithm, (java.security.spec.ECPublicKeySpec)paramKeySpec);
    throw new InvalidKeySpecException("Unknown KeySpec type: " + paramKeySpec.getClass().getName());
  }

  protected KeySpec engineGetKeySpec(Key paramKey, Class paramClass)
    throws InvalidKeySpecException
  {
    if ((paramClass.isAssignableFrom(PKCS8EncodedKeySpec.class)) && (paramKey.getFormat().equals("PKCS#8")))
      return new PKCS8EncodedKeySpec(paramKey.getEncoded());
    if ((paramClass.isAssignableFrom(X509EncodedKeySpec.class)) && (paramKey.getFormat().equals("X.509")))
      return new X509EncodedKeySpec(paramKey.getEncoded());
    if ((paramClass.isAssignableFrom(java.security.spec.ECPublicKeySpec.class)) && ((paramKey instanceof ECPublicKey)))
    {
      ECPublicKey localECPublicKey = (ECPublicKey)paramKey;
      if (localECPublicKey.getParams() != null)
        return new java.security.spec.ECPublicKeySpec(localECPublicKey.getW(), localECPublicKey.getParams());
      ECParameterSpec localECParameterSpec2 = ProviderUtil.getEcImplicitlyCa();
      return new java.security.spec.ECPublicKeySpec(localECPublicKey.getW(), EC5Util.convertSpec(EC5Util.convertCurve(localECParameterSpec2.getCurve(), localECParameterSpec2.getSeed()), localECParameterSpec2));
    }
    if ((paramClass.isAssignableFrom(java.security.spec.ECPrivateKeySpec.class)) && ((paramKey instanceof ECPrivateKey)))
    {
      ECPrivateKey localECPrivateKey = (ECPrivateKey)paramKey;
      if (localECPrivateKey.getParams() != null)
        return new java.security.spec.ECPrivateKeySpec(localECPrivateKey.getS(), localECPrivateKey.getParams());
      ECParameterSpec localECParameterSpec1 = ProviderUtil.getEcImplicitlyCa();
      return new java.security.spec.ECPrivateKeySpec(localECPrivateKey.getS(), EC5Util.convertSpec(EC5Util.convertCurve(localECParameterSpec1.getCurve(), localECParameterSpec1.getSeed()), localECParameterSpec1));
    }
    throw new RuntimeException("not implemented yet " + paramKey + " " + paramClass);
  }

  protected Key engineTranslateKey(Key paramKey)
    throws InvalidKeyException
  {
    if ((paramKey instanceof ECPublicKey))
      return new JCEECPublicKey((ECPublicKey)paramKey);
    if ((paramKey instanceof ECPrivateKey))
      return new JCEECPrivateKey((ECPrivateKey)paramKey);
    throw new InvalidKeyException("key type unknown");
  }

  public static class EC extends KeyFactory
  {
    public EC()
    {
      super();
    }
  }

  public static class ECDH extends KeyFactory
  {
    public ECDH()
    {
      super();
    }
  }

  public static class ECDHC extends KeyFactory
  {
    public ECDHC()
    {
      super();
    }
  }

  public static class ECDSA extends KeyFactory
  {
    public ECDSA()
    {
      super();
    }
  }

  public static class ECGOST3410 extends KeyFactory
  {
    public ECGOST3410()
    {
      super();
    }
  }

  public static class ECMQV extends KeyFactory
  {
    public ECMQV()
    {
      super();
    }
  }
}