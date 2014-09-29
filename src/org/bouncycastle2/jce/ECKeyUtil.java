package org.bouncycastle2.jce;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.X962Parameters;
import org.bouncycastle2.asn1.x9.X9ECParameters;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.jce.provider.ProviderUtil;
import org.bouncycastle2.jce.provider.asymmetric.ec.ECUtil;
import org.bouncycastle2.jce.spec.ECParameterSpec;

public class ECKeyUtil
{
  public static PrivateKey privateToExplicitParameters(PrivateKey paramPrivateKey, String paramString)
    throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchProviderException
  {
    Provider localProvider = Security.getProvider(paramString);
    if (localProvider == null)
      throw new NoSuchProviderException("cannot find provider: " + paramString);
    return privateToExplicitParameters(paramPrivateKey, localProvider);
  }

  public static PrivateKey privateToExplicitParameters(PrivateKey paramPrivateKey, Provider paramProvider)
    throws IllegalArgumentException, NoSuchAlgorithmException
  {
    try
    {
      localPrivateKeyInfo1 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(paramPrivateKey.getEncoded()));
      if (localPrivateKeyInfo1.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001))
        throw new UnsupportedEncodingException("cannot convert GOST key to explicit parameters.");
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      PrivateKeyInfo localPrivateKeyInfo1;
      throw localIllegalArgumentException;
      X962Parameters localX962Parameters1 = new X962Parameters((DERObject)localPrivateKeyInfo1.getAlgorithmId().getParameters());
      X9ECParameters localX9ECParameters2;
      if (localX962Parameters1.isNamedCurve())
        localX9ECParameters2 = ECUtil.getNamedCurveByOid((DERObjectIdentifier)localX962Parameters1.getParameters());
      for (X9ECParameters localX9ECParameters1 = new X9ECParameters(localX9ECParameters2.getCurve(), localX9ECParameters2.getG(), localX9ECParameters2.getN(), localX9ECParameters2.getH()); ; localX9ECParameters1 = new X9ECParameters(ProviderUtil.getEcImplicitlyCa().getCurve(), ProviderUtil.getEcImplicitlyCa().getG(), ProviderUtil.getEcImplicitlyCa().getN(), ProviderUtil.getEcImplicitlyCa().getH()))
      {
        X962Parameters localX962Parameters2 = new X962Parameters(localX9ECParameters1);
        PrivateKeyInfo localPrivateKeyInfo2 = new PrivateKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, localX962Parameters2.getDERObject()), localPrivateKeyInfo1.getPrivateKey());
        return KeyFactory.getInstance(paramPrivateKey.getAlgorithm(), paramProvider).generatePrivate(new PKCS8EncodedKeySpec(localPrivateKeyInfo2.getEncoded()));
        if (!localX962Parameters1.isImplicitlyCA())
          break;
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw localNoSuchAlgorithmException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException(localException);
    }
    return paramPrivateKey;
  }

  public static PublicKey publicToExplicitParameters(PublicKey paramPublicKey, String paramString)
    throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchProviderException
  {
    Provider localProvider = Security.getProvider(paramString);
    if (localProvider == null)
      throw new NoSuchProviderException("cannot find provider: " + paramString);
    return publicToExplicitParameters(paramPublicKey, localProvider);
  }

  public static PublicKey publicToExplicitParameters(PublicKey paramPublicKey, Provider paramProvider)
    throws IllegalArgumentException, NoSuchAlgorithmException
  {
    try
    {
      localSubjectPublicKeyInfo1 = SubjectPublicKeyInfo.getInstance(ASN1Object.fromByteArray(paramPublicKey.getEncoded()));
      if (localSubjectPublicKeyInfo1.getAlgorithmId().getObjectId().equals(CryptoProObjectIdentifiers.gostR3410_2001))
        throw new IllegalArgumentException("cannot convert GOST key to explicit parameters.");
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      SubjectPublicKeyInfo localSubjectPublicKeyInfo1;
      throw localIllegalArgumentException;
      X962Parameters localX962Parameters1 = new X962Parameters((DERObject)localSubjectPublicKeyInfo1.getAlgorithmId().getParameters());
      X9ECParameters localX9ECParameters2;
      if (localX962Parameters1.isNamedCurve())
        localX9ECParameters2 = ECUtil.getNamedCurveByOid((DERObjectIdentifier)localX962Parameters1.getParameters());
      for (X9ECParameters localX9ECParameters1 = new X9ECParameters(localX9ECParameters2.getCurve(), localX9ECParameters2.getG(), localX9ECParameters2.getN(), localX9ECParameters2.getH()); ; localX9ECParameters1 = new X9ECParameters(ProviderUtil.getEcImplicitlyCa().getCurve(), ProviderUtil.getEcImplicitlyCa().getG(), ProviderUtil.getEcImplicitlyCa().getN(), ProviderUtil.getEcImplicitlyCa().getH()))
      {
        X962Parameters localX962Parameters2 = new X962Parameters(localX9ECParameters1);
        SubjectPublicKeyInfo localSubjectPublicKeyInfo2 = new SubjectPublicKeyInfo(new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, localX962Parameters2.getDERObject()), localSubjectPublicKeyInfo1.getPublicKeyData().getBytes());
        return KeyFactory.getInstance(paramPublicKey.getAlgorithm(), paramProvider).generatePublic(new X509EncodedKeySpec(localSubjectPublicKeyInfo2.getEncoded()));
        if (!localX962Parameters1.isImplicitlyCA())
          break;
      }
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw localNoSuchAlgorithmException;
    }
    catch (Exception localException)
    {
      throw new UnexpectedException(localException);
    }
    return paramPublicKey;
  }

  private static class UnexpectedException extends RuntimeException
  {
    private Throwable cause;

    UnexpectedException(Throwable paramThrowable)
    {
      super();
      this.cause = paramThrowable;
    }

    public Throwable getCause()
    {
      return this.cause;
    }
  }
}