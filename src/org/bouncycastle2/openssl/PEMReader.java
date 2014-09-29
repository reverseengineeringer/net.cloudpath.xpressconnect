package org.bouncycastle2.openssl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CRL;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cms.ContentInfo;
import org.bouncycastle2.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.EncryptionScheme;
import org.bouncycastle2.asn1.pkcs.KeyDerivationFunc;
import org.bouncycastle2.asn1.pkcs.PBEParameter;
import org.bouncycastle2.asn1.pkcs.PBES2Parameters;
import org.bouncycastle2.asn1.pkcs.PBKDF2Params;
import org.bouncycastle2.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.sec.ECPrivateKeyStructure;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.jce.ECNamedCurveTable;
import org.bouncycastle2.jce.PKCS10CertificationRequest;
import org.bouncycastle2.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle2.util.encoders.Hex;
import org.bouncycastle2.util.io.pem.PemHeader;
import org.bouncycastle2.util.io.pem.PemObject;
import org.bouncycastle2.util.io.pem.PemObjectParser;
import org.bouncycastle2.util.io.pem.PemReader;
import org.bouncycastle2.x509.X509V2AttributeCertificate;

public class PEMReader extends PemReader
{
  private PasswordFinder pFinder;
  private final Map parsers = new HashMap();

  public PEMReader(Reader paramReader)
  {
    this(paramReader, null, "BC");
  }

  public PEMReader(Reader paramReader, PasswordFinder paramPasswordFinder)
  {
    this(paramReader, paramPasswordFinder, "BC");
  }

  public PEMReader(Reader paramReader, PasswordFinder paramPasswordFinder, String paramString)
  {
    this(paramReader, paramPasswordFinder, paramString, paramString);
  }

  public PEMReader(Reader paramReader, PasswordFinder paramPasswordFinder, String paramString1, String paramString2)
  {
    super(paramReader);
    this.pFinder = paramPasswordFinder;
    this.parsers.put("CERTIFICATE REQUEST", new PKCS10CertificationRequestParser(null));
    this.parsers.put("NEW CERTIFICATE REQUEST", new PKCS10CertificationRequestParser(null));
    this.parsers.put("CERTIFICATE", new X509CertificateParser(paramString2));
    this.parsers.put("X509 CERTIFICATE", new X509CertificateParser(paramString2));
    this.parsers.put("X509 CRL", new X509CRLParser(paramString2));
    this.parsers.put("PKCS7", new PKCS7Parser(null));
    this.parsers.put("ATTRIBUTE CERTIFICATE", new X509AttributeCertificateParser(null));
    this.parsers.put("EC PARAMETERS", new ECNamedCurveSpecParser(null));
    this.parsers.put("PUBLIC KEY", new PublicKeyParser(paramString2));
    this.parsers.put("RSA PUBLIC KEY", new RSAPublicKeyParser(paramString2));
    this.parsers.put("RSA PRIVATE KEY", new RSAKeyPairParser(paramString2));
    this.parsers.put("DSA PRIVATE KEY", new DSAKeyPairParser(paramString2));
    this.parsers.put("EC PRIVATE KEY", new ECDSAKeyPairParser(paramString2));
    this.parsers.put("ENCRYPTED PRIVATE KEY", new EncryptedPrivateKeyParser(paramString1, paramString2));
    this.parsers.put("PRIVATE KEY", new PrivateKeyParser(paramString2));
  }

  public Object readObject()
    throws IOException
  {
    PemObject localPemObject = readPemObject();
    if (localPemObject != null)
    {
      String str = localPemObject.getType();
      if (this.parsers.containsKey(str))
        return ((PemObjectParser)this.parsers.get(str)).parseObject(localPemObject);
      throw new IOException("unrecognised object: " + str);
    }
    return null;
  }

  private class DSAKeyPairParser extends PEMReader.KeyPairParser
  {
    public DSAKeyPairParser(String arg2)
    {
      super(str);
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        localASN1Sequence = readKeyPair(paramPemObject);
        if (localASN1Sequence.size() != 6)
          throw new PEMException("malformed sequence in DSA private key");
      }
      catch (IOException localIOException)
      {
        ASN1Sequence localASN1Sequence;
        throw localIOException;
        DERInteger localDERInteger1 = (DERInteger)localASN1Sequence.getObjectAt(1);
        DERInteger localDERInteger2 = (DERInteger)localASN1Sequence.getObjectAt(2);
        DERInteger localDERInteger3 = (DERInteger)localASN1Sequence.getObjectAt(3);
        DERInteger localDERInteger4 = (DERInteger)localASN1Sequence.getObjectAt(4);
        DSAPrivateKeySpec localDSAPrivateKeySpec = new DSAPrivateKeySpec(((DERInteger)localASN1Sequence.getObjectAt(5)).getValue(), localDERInteger1.getValue(), localDERInteger2.getValue(), localDERInteger3.getValue());
        DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(localDERInteger4.getValue(), localDERInteger1.getValue(), localDERInteger2.getValue(), localDERInteger3.getValue());
        KeyFactory localKeyFactory = KeyFactory.getInstance("DSA", this.provider);
        KeyPair localKeyPair = new KeyPair(localKeyFactory.generatePublic(localDSAPublicKeySpec), localKeyFactory.generatePrivate(localDSAPrivateKeySpec));
        return localKeyPair;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem creating DSA private key: " + localException.toString(), localException);
      }
    }
  }

  private class ECDSAKeyPairParser extends PEMReader.KeyPairParser
  {
    public ECDSAKeyPairParser(String arg2)
    {
      super(str);
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        ECPrivateKeyStructure localECPrivateKeyStructure = new ECPrivateKeyStructure(readKeyPair(paramPemObject));
        AlgorithmIdentifier localAlgorithmIdentifier = new AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, localECPrivateKeyStructure.getParameters());
        PrivateKeyInfo localPrivateKeyInfo = new PrivateKeyInfo(localAlgorithmIdentifier, localECPrivateKeyStructure.getDERObject());
        SubjectPublicKeyInfo localSubjectPublicKeyInfo = new SubjectPublicKeyInfo(localAlgorithmIdentifier, localECPrivateKeyStructure.getPublicKey().getBytes());
        PKCS8EncodedKeySpec localPKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(localPrivateKeyInfo.getEncoded());
        X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(localSubjectPublicKeyInfo.getEncoded());
        KeyFactory localKeyFactory = KeyFactory.getInstance("ECDSA", this.provider);
        KeyPair localKeyPair = new KeyPair(localKeyFactory.generatePublic(localX509EncodedKeySpec), localKeyFactory.generatePrivate(localPKCS8EncodedKeySpec));
        return localKeyPair;
      }
      catch (IOException localIOException)
      {
        throw localIOException;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem creating EC private key: " + localException.toString(), localException);
      }
    }
  }

  private class ECNamedCurveSpecParser
    implements PemObjectParser
  {
    private ECNamedCurveSpecParser()
    {
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      ECNamedCurveParameterSpec localECNamedCurveParameterSpec;
      try
      {
        localECNamedCurveParameterSpec = ECNamedCurveTable.getParameterSpec(((DERObjectIdentifier)ASN1Object.fromByteArray(paramPemObject.getContent())).getId());
        if (localECNamedCurveParameterSpec == null)
          throw new IOException("object ID not found in EC curve table");
      }
      catch (IOException localIOException)
      {
        throw localIOException;
      }
      catch (Exception localException)
      {
        throw new PEMException("exception extracting EC named curve: " + localException.toString());
      }
      return localECNamedCurveParameterSpec;
    }
  }

  private class EncryptedPrivateKeyParser
    implements PemObjectParser
  {
    private String asymProvider;
    private String symProvider;

    public EncryptedPrivateKeyParser(String paramString1, String arg3)
    {
      this.symProvider = paramString1;
      Object localObject;
      this.asymProvider = localObject;
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        localEncryptedPrivateKeyInfo = EncryptedPrivateKeyInfo.getInstance(ASN1Object.fromByteArray(paramPemObject.getContent()));
        localAlgorithmIdentifier = localEncryptedPrivateKeyInfo.getEncryptionAlgorithm();
        if (PEMReader.this.pFinder == null)
          throw new PEMException("no PasswordFinder specified");
      }
      catch (IOException localIOException)
      {
        EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo;
        AlgorithmIdentifier localAlgorithmIdentifier;
        throw localIOException;
        if (PEMUtilities.isPKCS5Scheme2(localAlgorithmIdentifier.getAlgorithm()))
        {
          PBES2Parameters localPBES2Parameters = PBES2Parameters.getInstance(localAlgorithmIdentifier.getParameters());
          KeyDerivationFunc localKeyDerivationFunc = localPBES2Parameters.getKeyDerivationFunc();
          EncryptionScheme localEncryptionScheme = localPBES2Parameters.getEncryptionScheme();
          PBKDF2Params localPBKDF2Params = (PBKDF2Params)localKeyDerivationFunc.getParameters();
          int i = localPBKDF2Params.getIterationCount().intValue();
          byte[] arrayOfByte = localPBKDF2Params.getSalt();
          String str3 = localEncryptionScheme.getAlgorithm().getId();
          SecretKey localSecretKey = PEMUtilities.generateSecretKeyForPKCS5Scheme2(str3, PEMReader.this.pFinder.getPassword(), arrayOfByte, i);
          Cipher localCipher3 = Cipher.getInstance(str3, this.symProvider);
          AlgorithmParameters localAlgorithmParameters = AlgorithmParameters.getInstance(str3, this.symProvider);
          localAlgorithmParameters.init(localEncryptionScheme.getParameters().getDERObject().getEncoded());
          localCipher3.init(2, localSecretKey, localAlgorithmParameters);
          PrivateKeyInfo localPrivateKeyInfo3 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(localCipher3.doFinal(localEncryptedPrivateKeyInfo.getEncryptedData())));
          PKCS8EncodedKeySpec localPKCS8EncodedKeySpec3 = new PKCS8EncodedKeySpec(localPrivateKeyInfo3.getEncoded());
          return KeyFactory.getInstance(localPrivateKeyInfo3.getAlgorithmId().getAlgorithm().getId(), this.asymProvider).generatePrivate(localPKCS8EncodedKeySpec3);
        }
        if (PEMUtilities.isPKCS12(localAlgorithmIdentifier.getAlgorithm()))
        {
          PKCS12PBEParams localPKCS12PBEParams = PKCS12PBEParams.getInstance(localAlgorithmIdentifier.getParameters());
          String str2 = localAlgorithmIdentifier.getAlgorithm().getId();
          PBEKeySpec localPBEKeySpec2 = new PBEKeySpec(PEMReader.this.pFinder.getPassword());
          SecretKeyFactory localSecretKeyFactory2 = SecretKeyFactory.getInstance(str2, this.symProvider);
          PBEParameterSpec localPBEParameterSpec2 = new PBEParameterSpec(localPKCS12PBEParams.getIV(), localPKCS12PBEParams.getIterations().intValue());
          Cipher localCipher2 = Cipher.getInstance(str2, this.symProvider);
          localCipher2.init(2, localSecretKeyFactory2.generateSecret(localPBEKeySpec2), localPBEParameterSpec2);
          PrivateKeyInfo localPrivateKeyInfo2 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(localCipher2.doFinal(localEncryptedPrivateKeyInfo.getEncryptedData())));
          PKCS8EncodedKeySpec localPKCS8EncodedKeySpec2 = new PKCS8EncodedKeySpec(localPrivateKeyInfo2.getEncoded());
          return KeyFactory.getInstance(localPrivateKeyInfo2.getAlgorithmId().getAlgorithm().getId(), this.asymProvider).generatePrivate(localPKCS8EncodedKeySpec2);
        }
        if (PEMUtilities.isPKCS5Scheme1(localAlgorithmIdentifier.getAlgorithm()))
        {
          PBEParameter localPBEParameter = PBEParameter.getInstance(localAlgorithmIdentifier.getParameters());
          String str1 = localAlgorithmIdentifier.getAlgorithm().getId();
          PBEKeySpec localPBEKeySpec1 = new PBEKeySpec(PEMReader.this.pFinder.getPassword());
          SecretKeyFactory localSecretKeyFactory1 = SecretKeyFactory.getInstance(str1, this.symProvider);
          PBEParameterSpec localPBEParameterSpec1 = new PBEParameterSpec(localPBEParameter.getSalt(), localPBEParameter.getIterationCount().intValue());
          Cipher localCipher1 = Cipher.getInstance(str1, this.symProvider);
          localCipher1.init(2, localSecretKeyFactory1.generateSecret(localPBEKeySpec1), localPBEParameterSpec1);
          PrivateKeyInfo localPrivateKeyInfo1 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(localCipher1.doFinal(localEncryptedPrivateKeyInfo.getEncryptedData())));
          PKCS8EncodedKeySpec localPKCS8EncodedKeySpec1 = new PKCS8EncodedKeySpec(localPrivateKeyInfo1.getEncoded());
          return KeyFactory.getInstance(localPrivateKeyInfo1.getAlgorithmId().getAlgorithm().getId(), this.asymProvider).generatePrivate(localPKCS8EncodedKeySpec1);
        }
        throw new PEMException("Unknown algorithm: " + localAlgorithmIdentifier.getAlgorithm());
      }
      catch (Exception localException)
      {
        PEMException localPEMException = new PEMException("problem parsing ENCRYPTED PRIVATE KEY: " + localException.toString(), localException);
        throw localPEMException;
      }
    }
  }

  private abstract class KeyPairParser
    implements PemObjectParser
  {
    protected String provider;

    public KeyPairParser(String arg2)
    {
      Object localObject;
      this.provider = localObject;
    }

    protected ASN1Sequence readKeyPair(PemObject paramPemObject)
      throws IOException
    {
      int i = 0;
      String str1 = null;
      Iterator localIterator = paramPemObject.getHeaders().iterator();
      while (true)
      {
        if (!localIterator.hasNext())
        {
          arrayOfByte1 = paramPemObject.getContent();
          if (i == 0)
            break label196;
          if (PEMReader.this.pFinder != null)
            break;
          throw new PasswordException("No password finder specified, but a password is required");
        }
        PemHeader localPemHeader = (PemHeader)localIterator.next();
        if ((localPemHeader.getName().equals("Proc-Type")) && (localPemHeader.getValue().equals("4,ENCRYPTED")))
          i = 1;
        else if (localPemHeader.getName().equals("DEK-Info"))
          str1 = localPemHeader.getValue();
      }
      char[] arrayOfChar = PEMReader.this.pFinder.getPassword();
      if (arrayOfChar == null)
        throw new PasswordException("Password is null, but a password is required");
      StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",");
      String str2 = localStringTokenizer.nextToken();
      byte[] arrayOfByte2 = Hex.decode(localStringTokenizer.nextToken());
      byte[] arrayOfByte1 = PEMUtilities.crypt(false, this.provider, arrayOfByte1, arrayOfChar, str2, arrayOfByte2);
      try
      {
        label196: ASN1Sequence localASN1Sequence = (ASN1Sequence)ASN1Object.fromByteArray(arrayOfByte1);
        return localASN1Sequence;
      }
      catch (IOException localIOException)
      {
        if (i != 0)
          throw new PEMException("exception decoding - please check password and data.", localIOException);
        throw new PEMException(localIOException.getMessage(), localIOException);
      }
      catch (ClassCastException localClassCastException)
      {
        if (i != 0)
          throw new PEMException("exception decoding - please check password and data.", localClassCastException);
        throw new PEMException(localClassCastException.getMessage(), localClassCastException);
      }
    }
  }

  private class PKCS10CertificationRequestParser
    implements PemObjectParser
  {
    private PKCS10CertificationRequestParser()
    {
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        PKCS10CertificationRequest localPKCS10CertificationRequest = new PKCS10CertificationRequest(paramPemObject.getContent());
        return localPKCS10CertificationRequest;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem parsing certrequest: " + localException.toString(), localException);
      }
    }
  }

  private class PKCS7Parser
    implements PemObjectParser
  {
    private PKCS7Parser()
    {
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        ContentInfo localContentInfo = ContentInfo.getInstance(new ASN1InputStream(paramPemObject.getContent()).readObject());
        return localContentInfo;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem parsing PKCS7 object: " + localException.toString(), localException);
      }
    }
  }

  private class PrivateKeyParser
    implements PemObjectParser
  {
    private String provider;

    public PrivateKeyParser(String arg2)
    {
      Object localObject;
      this.provider = localObject;
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        PrivateKeyInfo localPrivateKeyInfo = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(paramPemObject.getContent()));
        PKCS8EncodedKeySpec localPKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(paramPemObject.getContent());
        PrivateKey localPrivateKey = KeyFactory.getInstance(localPrivateKeyInfo.getAlgorithmId().getAlgorithm().getId(), this.provider).generatePrivate(localPKCS8EncodedKeySpec);
        return localPrivateKey;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem parsing PRIVATE KEY: " + localException.toString(), localException);
      }
    }
  }

  private class PublicKeyParser
    implements PemObjectParser
  {
    private String provider;

    public PublicKeyParser(String arg2)
    {
      Object localObject;
      this.provider = localObject;
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(paramPemObject.getContent());
      String[] arrayOfString = { "DSA", "RSA" };
      int i = 0;
      while (true)
      {
        if (i >= arrayOfString.length)
          return null;
        try
        {
          PublicKey localPublicKey = KeyFactory.getInstance(arrayOfString[i], this.provider).generatePublic(localX509EncodedKeySpec);
          return localPublicKey;
        }
        catch (NoSuchProviderException localNoSuchProviderException)
        {
          throw new RuntimeException("can't find provider " + this.provider);
        }
        catch (InvalidKeySpecException localInvalidKeySpecException)
        {
          i++;
        }
        catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
        {
          label90: break label90;
        }
      }
    }
  }

  private class RSAKeyPairParser extends PEMReader.KeyPairParser
  {
    public RSAKeyPairParser(String arg2)
    {
      super(str);
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        localASN1Sequence = readKeyPair(paramPemObject);
        if (localASN1Sequence.size() != 9)
          throw new PEMException("malformed sequence in RSA private key");
      }
      catch (IOException localIOException)
      {
        ASN1Sequence localASN1Sequence;
        throw localIOException;
        DERInteger localDERInteger1 = (DERInteger)localASN1Sequence.getObjectAt(1);
        DERInteger localDERInteger2 = (DERInteger)localASN1Sequence.getObjectAt(2);
        DERInteger localDERInteger3 = (DERInteger)localASN1Sequence.getObjectAt(3);
        DERInteger localDERInteger4 = (DERInteger)localASN1Sequence.getObjectAt(4);
        DERInteger localDERInteger5 = (DERInteger)localASN1Sequence.getObjectAt(5);
        DERInteger localDERInteger6 = (DERInteger)localASN1Sequence.getObjectAt(6);
        DERInteger localDERInteger7 = (DERInteger)localASN1Sequence.getObjectAt(7);
        DERInteger localDERInteger8 = (DERInteger)localASN1Sequence.getObjectAt(8);
        RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(localDERInteger1.getValue(), localDERInteger2.getValue());
        RSAPrivateCrtKeySpec localRSAPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(localDERInteger1.getValue(), localDERInteger2.getValue(), localDERInteger3.getValue(), localDERInteger4.getValue(), localDERInteger5.getValue(), localDERInteger6.getValue(), localDERInteger7.getValue(), localDERInteger8.getValue());
        KeyFactory localKeyFactory = KeyFactory.getInstance("RSA", this.provider);
        KeyPair localKeyPair = new KeyPair(localKeyFactory.generatePublic(localRSAPublicKeySpec), localKeyFactory.generatePrivate(localRSAPrivateCrtKeySpec));
        return localKeyPair;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem creating RSA private key: " + localException.toString(), localException);
      }
    }
  }

  private class RSAPublicKeyParser
    implements PemObjectParser
  {
    private String provider;

    public RSAPublicKeyParser(String arg2)
    {
      Object localObject;
      this.provider = localObject;
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      try
      {
        RSAPublicKeyStructure localRSAPublicKeyStructure = new RSAPublicKeyStructure((ASN1Sequence)new ASN1InputStream(paramPemObject.getContent()).readObject());
        RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(localRSAPublicKeyStructure.getModulus(), localRSAPublicKeyStructure.getPublicExponent());
        PublicKey localPublicKey = KeyFactory.getInstance("RSA", this.provider).generatePublic(localRSAPublicKeySpec);
        return localPublicKey;
      }
      catch (IOException localIOException)
      {
        throw localIOException;
      }
      catch (NoSuchProviderException localNoSuchProviderException)
      {
        throw new IOException("can't find provider " + this.provider);
      }
      catch (Exception localException)
      {
        throw new PEMException("problem extracting key: " + localException.toString(), localException);
      }
    }
  }

  private class X509AttributeCertificateParser
    implements PemObjectParser
  {
    private X509AttributeCertificateParser()
    {
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      return new X509V2AttributeCertificate(paramPemObject.getContent());
    }
  }

  private class X509CRLParser
    implements PemObjectParser
  {
    private String provider;

    public X509CRLParser(String arg2)
    {
      Object localObject;
      this.provider = localObject;
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramPemObject.getContent());
      try
      {
        CRL localCRL = CertificateFactory.getInstance("X.509", this.provider).generateCRL(localByteArrayInputStream);
        return localCRL;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem parsing cert: " + localException.toString(), localException);
      }
    }
  }

  private class X509CertificateParser
    implements PemObjectParser
  {
    private String provider;

    public X509CertificateParser(String arg2)
    {
      Object localObject;
      this.provider = localObject;
    }

    public Object parseObject(PemObject paramPemObject)
      throws IOException
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramPemObject.getContent());
      try
      {
        Certificate localCertificate = CertificateFactory.getInstance("X.509", this.provider).generateCertificate(localByteArrayInputStream);
        return localCertificate;
      }
      catch (Exception localException)
      {
        throw new PEMException("problem parsing cert: " + localException.toString(), localException);
      }
    }
  }
}