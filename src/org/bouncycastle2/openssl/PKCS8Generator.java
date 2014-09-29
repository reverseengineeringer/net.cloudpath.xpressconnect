package org.bouncycastle2.openssl;

import java.io.IOException;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.EncryptionScheme;
import org.bouncycastle2.asn1.pkcs.KeyDerivationFunc;
import org.bouncycastle2.asn1.pkcs.PBES2Parameters;
import org.bouncycastle2.asn1.pkcs.PBKDF2Params;
import org.bouncycastle2.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.util.io.pem.PemGenerationException;
import org.bouncycastle2.util.io.pem.PemObject;
import org.bouncycastle2.util.io.pem.PemObjectGenerator;

public class PKCS8Generator
  implements PemObjectGenerator
{
  public static final String AES_128_CBC = NISTObjectIdentifiers.id_aes128_CBC.getId();
  public static final String AES_192_CBC = NISTObjectIdentifiers.id_aes192_CBC.getId();
  public static final String AES_256_CBC = NISTObjectIdentifiers.id_aes256_CBC.getId();
  public static final String DES3_CBC = PKCSObjectIdentifiers.des_EDE3_CBC.getId();
  public static final String PBE_SHA1_2DES = PKCSObjectIdentifiers.pbeWithSHAAnd2_KeyTripleDES_CBC.getId();
  public static final String PBE_SHA1_3DES;
  public static final String PBE_SHA1_RC2_128 = PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC2_CBC.getId();
  public static final String PBE_SHA1_RC2_40 = PKCSObjectIdentifiers.pbewithSHAAnd40BitRC2_CBC.getId();
  public static final String PBE_SHA1_RC4_128 = PKCSObjectIdentifiers.pbeWithSHAAnd128BitRC4.getId();
  public static final String PBE_SHA1_RC4_40 = PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC4.getId();
  private String algorithm;
  private Cipher cipher;
  private int iterationCount;
  private PrivateKey key;
  private AlgorithmParameterGenerator paramGen;
  private char[] password;
  private SecureRandom random;
  private SecretKeyFactory secKeyFact;

  static
  {
    PBE_SHA1_3DES = PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC.getId();
  }

  public PKCS8Generator(PrivateKey paramPrivateKey)
  {
    this.key = paramPrivateKey;
  }

  public PKCS8Generator(PrivateKey paramPrivateKey, String paramString1, String paramString2)
    throws NoSuchProviderException, NoSuchAlgorithmException
  {
    Provider localProvider = Security.getProvider(paramString2);
    if (localProvider == null)
      throw new NoSuchProviderException("cannot find provider: " + paramString2);
    init(paramPrivateKey, paramString1, localProvider);
  }

  public PKCS8Generator(PrivateKey paramPrivateKey, String paramString, Provider paramProvider)
    throws NoSuchAlgorithmException
  {
    init(paramPrivateKey, paramString, paramProvider);
  }

  private void init(PrivateKey paramPrivateKey, String paramString, Provider paramProvider)
    throws NoSuchAlgorithmException
  {
    this.key = paramPrivateKey;
    this.algorithm = paramString;
    this.iterationCount = 2048;
    try
    {
      this.cipher = Cipher.getInstance(paramString, paramProvider);
      if (PEMUtilities.isPKCS5Scheme2(new DERObjectIdentifier(paramString)))
      {
        this.paramGen = AlgorithmParameterGenerator.getInstance(paramString, paramProvider);
        return;
      }
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new NoSuchAlgorithmException(paramString + " found, but padding not available: " + localNoSuchPaddingException.getMessage());
    }
    this.secKeyFact = SecretKeyFactory.getInstance(paramString, paramProvider);
  }

  public PemObject generate()
    throws PemGenerationException
  {
    byte[] arrayOfByte1 = this.key.getEncoded();
    if (this.algorithm == null)
      return new PemObject("PRIVATE KEY", arrayOfByte1);
    DERObjectIdentifier localDERObjectIdentifier = new DERObjectIdentifier(this.algorithm);
    if (PEMUtilities.isPKCS5Scheme2(localDERObjectIdentifier))
    {
      byte[] arrayOfByte3 = new byte[20];
      if (this.random == null)
        this.random = new SecureRandom();
      this.random.nextBytes(arrayOfByte3);
      SecretKey localSecretKey = PEMUtilities.generateSecretKeyForPKCS5Scheme2(this.algorithm, this.password, arrayOfByte3, this.iterationCount);
      AlgorithmParameters localAlgorithmParameters = this.paramGen.generateParameters();
      try
      {
        this.cipher.init(1, localSecretKey, localAlgorithmParameters);
        EncryptionScheme localEncryptionScheme = new EncryptionScheme(new DERObjectIdentifier(this.algorithm), ASN1Object.fromByteArray(localAlgorithmParameters.getEncoded()));
        KeyDerivationFunc localKeyDerivationFunc = new KeyDerivationFunc(PKCSObjectIdentifiers.id_PBKDF2, new PBKDF2Params(arrayOfByte3, this.iterationCount));
        ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
        localASN1EncodableVector2.add(localKeyDerivationFunc);
        localASN1EncodableVector2.add(localEncryptionScheme);
        ASN1ObjectIdentifier localASN1ObjectIdentifier = PKCSObjectIdentifiers.id_PBES2;
        DERSequence localDERSequence = new DERSequence(localASN1EncodableVector2);
        PemObject localPemObject2 = new PemObject("ENCRYPTED PRIVATE KEY", new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(localASN1ObjectIdentifier, new PBES2Parameters(localDERSequence)), this.cipher.doFinal(arrayOfByte1)).getEncoded());
        return localPemObject2;
      }
      catch (IOException localIOException2)
      {
        throw new PemGenerationException(localIOException2.getMessage(), localIOException2);
      }
      catch (GeneralSecurityException localGeneralSecurityException2)
      {
        throw new PemGenerationException(localGeneralSecurityException2.getMessage(), localGeneralSecurityException2);
      }
    }
    if (PEMUtilities.isPKCS12(localDERObjectIdentifier))
    {
      byte[] arrayOfByte2 = new byte[20];
      if (this.random == null)
        this.random = new SecureRandom();
      this.random.nextBytes(arrayOfByte2);
      try
      {
        PBEKeySpec localPBEKeySpec = new PBEKeySpec(this.password);
        PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(arrayOfByte2, this.iterationCount);
        this.cipher.init(1, this.secKeyFact.generateSecret(localPBEKeySpec), localPBEParameterSpec);
        ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
        localASN1EncodableVector1.add(new DEROctetString(arrayOfByte2));
        localASN1EncodableVector1.add(new DERInteger(this.iterationCount));
        PemObject localPemObject1 = new PemObject("ENCRYPTED PRIVATE KEY", new EncryptedPrivateKeyInfo(new AlgorithmIdentifier(localDERObjectIdentifier, new PKCS12PBEParams(new DERSequence(localASN1EncodableVector1))), this.cipher.doFinal(arrayOfByte1)).getEncoded());
        return localPemObject1;
      }
      catch (IOException localIOException1)
      {
        throw new PemGenerationException(localIOException1.getMessage(), localIOException1);
      }
      catch (GeneralSecurityException localGeneralSecurityException1)
      {
        throw new PemGenerationException(localGeneralSecurityException1.getMessage(), localGeneralSecurityException1);
      }
    }
    throw new PemGenerationException("unknown algorithm: " + this.algorithm);
  }

  public PKCS8Generator setIterationCount(int paramInt)
  {
    this.iterationCount = paramInt;
    return this;
  }

  public PKCS8Generator setPassword(char[] paramArrayOfChar)
  {
    this.password = paramArrayOfChar;
    return this;
  }

  public PKCS8Generator setSecureRandom(SecureRandom paramSecureRandom)
  {
    this.random = paramSecureRandom;
    return this;
  }
}