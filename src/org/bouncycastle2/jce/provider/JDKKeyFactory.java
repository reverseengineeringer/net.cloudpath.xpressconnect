package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.jce.interfaces.ElGamalPrivateKey;
import org.bouncycastle2.jce.interfaces.ElGamalPublicKey;
import org.bouncycastle2.jce.spec.ElGamalPrivateKeySpec;
import org.bouncycastle2.jce.spec.ElGamalPublicKeySpec;
import org.bouncycastle2.jce.spec.GOST3410PrivateKeySpec;
import org.bouncycastle2.jce.spec.GOST3410PublicKeySpec;

public abstract class JDKKeyFactory extends KeyFactorySpi
{
  protected boolean elGamalFactory = false;

  protected static PrivateKey createPrivateKeyFromDERStream(byte[] paramArrayOfByte)
    throws IOException
  {
    return createPrivateKeyFromPrivateKeyInfo(new PrivateKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte)));
  }

  static PrivateKey createPrivateKeyFromPrivateKeyInfo(PrivateKeyInfo paramPrivateKeyInfo)
  {
    DERObjectIdentifier localDERObjectIdentifier = paramPrivateKeyInfo.getAlgorithmId().getObjectId();
    if (RSAUtil.isRsaOid(localDERObjectIdentifier))
      return new JCERSAPrivateCrtKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement))
      return new JCEDHPrivateKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber))
      return new JCEDHPrivateKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(OIWObjectIdentifiers.elGamalAlgorithm))
      return new JCEElGamalPrivateKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.id_dsa))
      return new JDKDSAPrivateKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.id_ecPublicKey))
      return new JCEECPrivateKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3410_94))
      return new JDKGOST3410PrivateKey(paramPrivateKeyInfo);
    if (localDERObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3410_2001))
      return new JCEECPrivateKey(paramPrivateKeyInfo);
    throw new RuntimeException("algorithm identifier " + localDERObjectIdentifier + " in key not recognised");
  }

  public static PublicKey createPublicKeyFromDERStream(byte[] paramArrayOfByte)
    throws IOException
  {
    return createPublicKeyFromPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(paramArrayOfByte)));
  }

  static PublicKey createPublicKeyFromPublicKeyInfo(SubjectPublicKeyInfo paramSubjectPublicKeyInfo)
  {
    DERObjectIdentifier localDERObjectIdentifier = paramSubjectPublicKeyInfo.getAlgorithmId().getObjectId();
    if (RSAUtil.isRsaOid(localDERObjectIdentifier))
      return new JCERSAPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(PKCSObjectIdentifiers.dhKeyAgreement))
      return new JCEDHPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.dhpublicnumber))
      return new JCEDHPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(OIWObjectIdentifiers.elGamalAlgorithm))
      return new JCEElGamalPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.id_dsa))
      return new JDKDSAPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(OIWObjectIdentifiers.dsaWithSHA1))
      return new JDKDSAPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(X9ObjectIdentifiers.id_ecPublicKey))
      return new JCEECPublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3410_94))
      return new JDKGOST3410PublicKey(paramSubjectPublicKeyInfo);
    if (localDERObjectIdentifier.equals(CryptoProObjectIdentifiers.gostR3410_2001))
      return new JCEECPublicKey(paramSubjectPublicKeyInfo);
    throw new RuntimeException("algorithm identifier " + localDERObjectIdentifier + " in key not recognised");
  }

  protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    if ((paramKeySpec instanceof PKCS8EncodedKeySpec))
      try
      {
        PrivateKey localPrivateKey = createPrivateKeyFromDERStream(((PKCS8EncodedKeySpec)paramKeySpec).getEncoded());
        return localPrivateKey;
      }
      catch (Exception localException)
      {
        throw new InvalidKeySpecException(localException.toString());
      }
    throw new InvalidKeySpecException("Unknown KeySpec type: " + paramKeySpec.getClass().getName());
  }

  protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
    throws InvalidKeySpecException
  {
    if ((paramKeySpec instanceof X509EncodedKeySpec))
      try
      {
        PublicKey localPublicKey = createPublicKeyFromDERStream(((X509EncodedKeySpec)paramKeySpec).getEncoded());
        return localPublicKey;
      }
      catch (Exception localException)
      {
        throw new InvalidKeySpecException(localException.toString());
      }
    throw new InvalidKeySpecException("Unknown KeySpec type: " + paramKeySpec.getClass().getName());
  }

  protected KeySpec engineGetKeySpec(Key paramKey, Class paramClass)
    throws InvalidKeySpecException
  {
    if ((paramClass.isAssignableFrom(PKCS8EncodedKeySpec.class)) && (paramKey.getFormat().equals("PKCS#8")))
      return new PKCS8EncodedKeySpec(paramKey.getEncoded());
    if ((paramClass.isAssignableFrom(X509EncodedKeySpec.class)) && (paramKey.getFormat().equals("X.509")))
      return new X509EncodedKeySpec(paramKey.getEncoded());
    if ((paramClass.isAssignableFrom(RSAPublicKeySpec.class)) && ((paramKey instanceof RSAPublicKey)))
    {
      RSAPublicKey localRSAPublicKey = (RSAPublicKey)paramKey;
      return new RSAPublicKeySpec(localRSAPublicKey.getModulus(), localRSAPublicKey.getPublicExponent());
    }
    if ((paramClass.isAssignableFrom(RSAPrivateKeySpec.class)) && ((paramKey instanceof RSAPrivateKey)))
    {
      RSAPrivateKey localRSAPrivateKey = (RSAPrivateKey)paramKey;
      return new RSAPrivateKeySpec(localRSAPrivateKey.getModulus(), localRSAPrivateKey.getPrivateExponent());
    }
    if ((paramClass.isAssignableFrom(RSAPrivateCrtKeySpec.class)) && ((paramKey instanceof RSAPrivateCrtKey)))
    {
      RSAPrivateCrtKey localRSAPrivateCrtKey = (RSAPrivateCrtKey)paramKey;
      return new RSAPrivateCrtKeySpec(localRSAPrivateCrtKey.getModulus(), localRSAPrivateCrtKey.getPublicExponent(), localRSAPrivateCrtKey.getPrivateExponent(), localRSAPrivateCrtKey.getPrimeP(), localRSAPrivateCrtKey.getPrimeQ(), localRSAPrivateCrtKey.getPrimeExponentP(), localRSAPrivateCrtKey.getPrimeExponentQ(), localRSAPrivateCrtKey.getCrtCoefficient());
    }
    if ((paramClass.isAssignableFrom(DHPrivateKeySpec.class)) && ((paramKey instanceof DHPrivateKey)))
    {
      DHPrivateKey localDHPrivateKey = (DHPrivateKey)paramKey;
      return new DHPrivateKeySpec(localDHPrivateKey.getX(), localDHPrivateKey.getParams().getP(), localDHPrivateKey.getParams().getG());
    }
    if ((paramClass.isAssignableFrom(DHPublicKeySpec.class)) && ((paramKey instanceof DHPublicKey)))
    {
      DHPublicKey localDHPublicKey = (DHPublicKey)paramKey;
      return new DHPublicKeySpec(localDHPublicKey.getY(), localDHPublicKey.getParams().getP(), localDHPublicKey.getParams().getG());
    }
    throw new RuntimeException("not implemented yet " + paramKey + " " + paramClass);
  }

  protected Key engineTranslateKey(Key paramKey)
    throws InvalidKeyException
  {
    if ((paramKey instanceof RSAPublicKey))
      return new JCERSAPublicKey((RSAPublicKey)paramKey);
    if ((paramKey instanceof RSAPrivateCrtKey))
      return new JCERSAPrivateCrtKey((RSAPrivateCrtKey)paramKey);
    if ((paramKey instanceof RSAPrivateKey))
      return new JCERSAPrivateKey((RSAPrivateKey)paramKey);
    if ((paramKey instanceof DHPublicKey))
    {
      if (this.elGamalFactory)
        return new JCEElGamalPublicKey((DHPublicKey)paramKey);
      return new JCEDHPublicKey((DHPublicKey)paramKey);
    }
    if ((paramKey instanceof DHPrivateKey))
    {
      if (this.elGamalFactory)
        return new JCEElGamalPrivateKey((DHPrivateKey)paramKey);
      return new JCEDHPrivateKey((DHPrivateKey)paramKey);
    }
    if ((paramKey instanceof DSAPublicKey))
      return new JDKDSAPublicKey((DSAPublicKey)paramKey);
    if ((paramKey instanceof DSAPrivateKey))
      return new JDKDSAPrivateKey((DSAPrivateKey)paramKey);
    if ((paramKey instanceof ElGamalPublicKey))
      return new JCEElGamalPublicKey((ElGamalPublicKey)paramKey);
    if ((paramKey instanceof ElGamalPrivateKey))
      return new JCEElGamalPrivateKey((ElGamalPrivateKey)paramKey);
    throw new InvalidKeyException("key type unknown");
  }

  public static class DH extends JDKKeyFactory
  {
    protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof DHPrivateKeySpec))
        return new JCEDHPrivateKey((DHPrivateKeySpec)paramKeySpec);
      return super.engineGeneratePrivate(paramKeySpec);
    }

    protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof DHPublicKeySpec))
        return new JCEDHPublicKey((DHPublicKeySpec)paramKeySpec);
      return super.engineGeneratePublic(paramKeySpec);
    }
  }

  public static class DSA extends JDKKeyFactory
  {
    protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof DSAPrivateKeySpec))
        return new JDKDSAPrivateKey((DSAPrivateKeySpec)paramKeySpec);
      return super.engineGeneratePrivate(paramKeySpec);
    }

    protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof DSAPublicKeySpec))
        return new JDKDSAPublicKey((DSAPublicKeySpec)paramKeySpec);
      return super.engineGeneratePublic(paramKeySpec);
    }
  }

  public static class ElGamal extends JDKKeyFactory
  {
    public ElGamal()
    {
      this.elGamalFactory = true;
    }

    protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof ElGamalPrivateKeySpec))
        return new JCEElGamalPrivateKey((ElGamalPrivateKeySpec)paramKeySpec);
      if ((paramKeySpec instanceof DHPrivateKeySpec))
        return new JCEElGamalPrivateKey((DHPrivateKeySpec)paramKeySpec);
      return super.engineGeneratePrivate(paramKeySpec);
    }

    protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof ElGamalPublicKeySpec))
        return new JCEElGamalPublicKey((ElGamalPublicKeySpec)paramKeySpec);
      if ((paramKeySpec instanceof DHPublicKeySpec))
        return new JCEElGamalPublicKey((DHPublicKeySpec)paramKeySpec);
      return super.engineGeneratePublic(paramKeySpec);
    }
  }

  public static class GOST3410 extends JDKKeyFactory
  {
    protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof GOST3410PrivateKeySpec))
        return new JDKGOST3410PrivateKey((GOST3410PrivateKeySpec)paramKeySpec);
      return super.engineGeneratePrivate(paramKeySpec);
    }

    protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof GOST3410PublicKeySpec))
        return new JDKGOST3410PublicKey((GOST3410PublicKeySpec)paramKeySpec);
      return super.engineGeneratePublic(paramKeySpec);
    }
  }

  public static class RSA extends JDKKeyFactory
  {
    protected PrivateKey engineGeneratePrivate(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof PKCS8EncodedKeySpec))
        try
        {
          PrivateKey localPrivateKey = JDKKeyFactory.createPrivateKeyFromDERStream(((PKCS8EncodedKeySpec)paramKeySpec).getEncoded());
          return localPrivateKey;
        }
        catch (Exception localException1)
        {
          try
          {
            JCERSAPrivateCrtKey localJCERSAPrivateCrtKey = new JCERSAPrivateCrtKey(new RSAPrivateKeyStructure((ASN1Sequence)ASN1Object.fromByteArray(((PKCS8EncodedKeySpec)paramKeySpec).getEncoded())));
            return localJCERSAPrivateCrtKey;
          }
          catch (Exception localException2)
          {
            throw new InvalidKeySpecException(localException2.toString());
          }
        }
      if ((paramKeySpec instanceof RSAPrivateCrtKeySpec))
        return new JCERSAPrivateCrtKey((RSAPrivateCrtKeySpec)paramKeySpec);
      if ((paramKeySpec instanceof RSAPrivateKeySpec))
        return new JCERSAPrivateKey((RSAPrivateKeySpec)paramKeySpec);
      throw new InvalidKeySpecException("Unknown KeySpec type: " + paramKeySpec.getClass().getName());
    }

    protected PublicKey engineGeneratePublic(KeySpec paramKeySpec)
      throws InvalidKeySpecException
    {
      if ((paramKeySpec instanceof RSAPublicKeySpec))
        return new JCERSAPublicKey((RSAPublicKeySpec)paramKeySpec);
      return super.engineGeneratePublic(paramKeySpec);
    }
  }

  public static class X509 extends JDKKeyFactory
  {
  }
}