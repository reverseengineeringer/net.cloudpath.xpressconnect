package org.bouncycastle2.x509;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle2.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.util.Strings;

class X509Util
{
  private static Hashtable algorithms = new Hashtable();
  private static Set noParams;
  private static Hashtable params = new Hashtable();

  static
  {
    noParams = new HashSet();
    algorithms.put("MD2WITHRSAENCRYPTION", PKCSObjectIdentifiers.md2WithRSAEncryption);
    algorithms.put("MD2WITHRSA", PKCSObjectIdentifiers.md2WithRSAEncryption);
    algorithms.put("MD5WITHRSAENCRYPTION", PKCSObjectIdentifiers.md5WithRSAEncryption);
    algorithms.put("MD5WITHRSA", PKCSObjectIdentifiers.md5WithRSAEncryption);
    algorithms.put("SHA1WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha1WithRSAEncryption);
    algorithms.put("SHA1WITHRSA", PKCSObjectIdentifiers.sha1WithRSAEncryption);
    algorithms.put("SHA224WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha224WithRSAEncryption);
    algorithms.put("SHA224WITHRSA", PKCSObjectIdentifiers.sha224WithRSAEncryption);
    algorithms.put("SHA256WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha256WithRSAEncryption);
    algorithms.put("SHA256WITHRSA", PKCSObjectIdentifiers.sha256WithRSAEncryption);
    algorithms.put("SHA384WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha384WithRSAEncryption);
    algorithms.put("SHA384WITHRSA", PKCSObjectIdentifiers.sha384WithRSAEncryption);
    algorithms.put("SHA512WITHRSAENCRYPTION", PKCSObjectIdentifiers.sha512WithRSAEncryption);
    algorithms.put("SHA512WITHRSA", PKCSObjectIdentifiers.sha512WithRSAEncryption);
    algorithms.put("SHA1WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
    algorithms.put("SHA224WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
    algorithms.put("SHA256WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
    algorithms.put("SHA384WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
    algorithms.put("SHA512WITHRSAANDMGF1", PKCSObjectIdentifiers.id_RSASSA_PSS);
    algorithms.put("RIPEMD160WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
    algorithms.put("RIPEMD160WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
    algorithms.put("RIPEMD128WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
    algorithms.put("RIPEMD128WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
    algorithms.put("RIPEMD256WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
    algorithms.put("RIPEMD256WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
    algorithms.put("SHA1WITHDSA", X9ObjectIdentifiers.id_dsa_with_sha1);
    algorithms.put("DSAWITHSHA1", X9ObjectIdentifiers.id_dsa_with_sha1);
    algorithms.put("SHA224WITHDSA", NISTObjectIdentifiers.dsa_with_sha224);
    algorithms.put("SHA256WITHDSA", NISTObjectIdentifiers.dsa_with_sha256);
    algorithms.put("SHA384WITHDSA", NISTObjectIdentifiers.dsa_with_sha384);
    algorithms.put("SHA512WITHDSA", NISTObjectIdentifiers.dsa_with_sha512);
    algorithms.put("SHA1WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA1);
    algorithms.put("ECDSAWITHSHA1", X9ObjectIdentifiers.ecdsa_with_SHA1);
    algorithms.put("SHA224WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA224);
    algorithms.put("SHA256WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA256);
    algorithms.put("SHA384WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA384);
    algorithms.put("SHA512WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA512);
    algorithms.put("GOST3411WITHGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
    algorithms.put("GOST3411WITHGOST3410-94", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
    algorithms.put("GOST3411WITHECGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    algorithms.put("GOST3411WITHECGOST3410-2001", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    algorithms.put("GOST3411WITHGOST3410-2001", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA1);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA224);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA256);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA384);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA512);
    noParams.add(X9ObjectIdentifiers.id_dsa_with_sha1);
    noParams.add(NISTObjectIdentifiers.dsa_with_sha224);
    noParams.add(NISTObjectIdentifiers.dsa_with_sha256);
    noParams.add(NISTObjectIdentifiers.dsa_with_sha384);
    noParams.add(NISTObjectIdentifiers.dsa_with_sha512);
    noParams.add(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
    noParams.add(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    AlgorithmIdentifier localAlgorithmIdentifier1 = new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1, new DERNull());
    params.put("SHA1WITHRSAANDMGF1", creatPSSParams(localAlgorithmIdentifier1, 20));
    AlgorithmIdentifier localAlgorithmIdentifier2 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha224, new DERNull());
    params.put("SHA224WITHRSAANDMGF1", creatPSSParams(localAlgorithmIdentifier2, 28));
    AlgorithmIdentifier localAlgorithmIdentifier3 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256, new DERNull());
    params.put("SHA256WITHRSAANDMGF1", creatPSSParams(localAlgorithmIdentifier3, 32));
    AlgorithmIdentifier localAlgorithmIdentifier4 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384, new DERNull());
    params.put("SHA384WITHRSAANDMGF1", creatPSSParams(localAlgorithmIdentifier4, 48));
    AlgorithmIdentifier localAlgorithmIdentifier5 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512, new DERNull());
    params.put("SHA512WITHRSAANDMGF1", creatPSSParams(localAlgorithmIdentifier5, 64));
  }

  static byte[] calculateSignature(DERObjectIdentifier paramDERObjectIdentifier, String paramString1, String paramString2, PrivateKey paramPrivateKey, SecureRandom paramSecureRandom, ASN1Encodable paramASN1Encodable)
    throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException
  {
    if (paramDERObjectIdentifier == null)
      throw new IllegalStateException("no signature algorithm specified");
    Signature localSignature = getSignatureInstance(paramString1, paramString2);
    if (paramSecureRandom != null)
      localSignature.initSign(paramPrivateKey, paramSecureRandom);
    while (true)
    {
      localSignature.update(paramASN1Encodable.getEncoded("DER"));
      return localSignature.sign();
      localSignature.initSign(paramPrivateKey);
    }
  }

  static byte[] calculateSignature(DERObjectIdentifier paramDERObjectIdentifier, String paramString, PrivateKey paramPrivateKey, SecureRandom paramSecureRandom, ASN1Encodable paramASN1Encodable)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException
  {
    if (paramDERObjectIdentifier == null)
      throw new IllegalStateException("no signature algorithm specified");
    Signature localSignature = getSignatureInstance(paramString);
    if (paramSecureRandom != null)
      localSignature.initSign(paramPrivateKey, paramSecureRandom);
    while (true)
    {
      localSignature.update(paramASN1Encodable.getEncoded("DER"));
      return localSignature.sign();
      localSignature.initSign(paramPrivateKey);
    }
  }

  static X509Principal convertPrincipal(X500Principal paramX500Principal)
  {
    try
    {
      X509Principal localX509Principal = new X509Principal(paramX500Principal.getEncoded());
      return localX509Principal;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("cannot convert principal");
  }

  private static RSASSAPSSparams creatPSSParams(AlgorithmIdentifier paramAlgorithmIdentifier, int paramInt)
  {
    return new RSASSAPSSparams(paramAlgorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, paramAlgorithmIdentifier), new DERInteger(paramInt), new DERInteger(1));
  }

  static Iterator getAlgNames()
  {
    Enumeration localEnumeration = algorithms.keys();
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      if (!localEnumeration.hasMoreElements())
        return localArrayList.iterator();
      localArrayList.add(localEnumeration.nextElement());
    }
  }

  static DERObjectIdentifier getAlgorithmOID(String paramString)
  {
    String str = Strings.toUpperCase(paramString);
    if (algorithms.containsKey(str))
      return (DERObjectIdentifier)algorithms.get(str);
    return new DERObjectIdentifier(str);
  }

  static Implementation getImplementation(String paramString1, String paramString2)
    throws NoSuchAlgorithmException
  {
    Provider[] arrayOfProvider = Security.getProviders();
    int i = 0;
    while (true)
    {
      if (i == arrayOfProvider.length)
        throw new NoSuchAlgorithmException("cannot find implementation " + paramString2);
      Implementation localImplementation = getImplementation(paramString1, Strings.toUpperCase(paramString2), arrayOfProvider[i]);
      if (localImplementation != null)
        return localImplementation;
      try
      {
        getImplementation(paramString1, paramString2, arrayOfProvider[i]);
        label67: i++;
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        break label67;
      }
    }
  }

  static Implementation getImplementation(String paramString1, String paramString2, Provider paramProvider)
    throws NoSuchAlgorithmException
  {
    String str1;
    for (Object localObject1 = Strings.toUpperCase(paramString2); ; localObject1 = str1)
    {
      str1 = paramProvider.getProperty("Alg.Alias." + paramString1 + "." + (String)localObject1);
      if (str1 == null)
      {
        String str2 = paramProvider.getProperty(paramString1 + "." + (String)localObject1);
        if (str2 != null)
          try
          {
            ClassLoader localClassLoader = paramProvider.getClass().getClassLoader();
            if (localClassLoader != null);
            Class localClass;
            for (Object localObject2 = localClassLoader.loadClass(str2); ; localObject2 = localClass)
            {
              return new Implementation(((Class)localObject2).newInstance(), paramProvider);
              localClass = Class.forName(str2);
            }
          }
          catch (ClassNotFoundException localClassNotFoundException)
          {
            throw new IllegalStateException("algorithm " + (String)localObject1 + " in provider " + paramProvider.getName() + " but no class \"" + str2 + "\" found!");
          }
          catch (Exception localException)
          {
            throw new IllegalStateException("algorithm " + (String)localObject1 + " in provider " + paramProvider.getName() + " but class \"" + str2 + "\" inaccessible!");
          }
        throw new NoSuchAlgorithmException("cannot find implementation " + (String)localObject1 + " for provider " + paramProvider.getName());
      }
    }
  }

  static Provider getProvider(String paramString)
    throws NoSuchProviderException
  {
    Provider localProvider = Security.getProvider(paramString);
    if (localProvider == null)
      throw new NoSuchProviderException("Provider " + paramString + " not found");
    return localProvider;
  }

  static AlgorithmIdentifier getSigAlgID(DERObjectIdentifier paramDERObjectIdentifier, String paramString)
  {
    if (noParams.contains(paramDERObjectIdentifier))
      return new AlgorithmIdentifier(paramDERObjectIdentifier);
    String str = Strings.toUpperCase(paramString);
    if (params.containsKey(str))
      return new AlgorithmIdentifier(paramDERObjectIdentifier, (DEREncodable)params.get(str));
    return new AlgorithmIdentifier(paramDERObjectIdentifier, new DERNull());
  }

  static Signature getSignatureInstance(String paramString)
    throws NoSuchAlgorithmException
  {
    return Signature.getInstance(paramString);
  }

  static Signature getSignatureInstance(String paramString1, String paramString2)
    throws NoSuchProviderException, NoSuchAlgorithmException
  {
    if (paramString2 != null)
      return Signature.getInstance(paramString1, paramString2);
    return Signature.getInstance(paramString1);
  }

  static class Implementation
  {
    Object engine;
    Provider provider;

    Implementation(Object paramObject, Provider paramProvider)
    {
      this.engine = paramObject;
      this.provider = paramProvider;
    }

    Object getEngine()
    {
      return this.engine;
    }

    Provider getProvider()
    {
      return this.provider;
    }
  }
}