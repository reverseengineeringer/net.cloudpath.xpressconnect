package org.bouncycastle2.openssl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.cms.ContentInfo;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.DSAParameter;
import org.bouncycastle2.jce.PKCS10CertificationRequest;
import org.bouncycastle2.util.Strings;
import org.bouncycastle2.util.encoders.Hex;
import org.bouncycastle2.util.io.pem.PemGenerationException;
import org.bouncycastle2.util.io.pem.PemHeader;
import org.bouncycastle2.util.io.pem.PemObject;
import org.bouncycastle2.util.io.pem.PemObjectGenerator;
import org.bouncycastle2.x509.X509AttributeCertificate;
import org.bouncycastle2.x509.X509V2AttributeCertificate;

public class MiscPEMGenerator
  implements PemObjectGenerator
{
  private String algorithm;
  private Object obj;
  private char[] password;
  private Provider provider;
  private SecureRandom random;

  public MiscPEMGenerator(Object paramObject)
  {
    this.obj = paramObject;
  }

  public MiscPEMGenerator(Object paramObject, String paramString1, char[] paramArrayOfChar, SecureRandom paramSecureRandom, String paramString2)
    throws NoSuchProviderException
  {
    this.obj = paramObject;
    this.algorithm = paramString1;
    this.password = paramArrayOfChar;
    this.random = paramSecureRandom;
    if (paramString2 != null)
    {
      this.provider = Security.getProvider(paramString2);
      if (this.provider == null)
        throw new NoSuchProviderException("cannot find provider: " + paramString2);
    }
  }

  public MiscPEMGenerator(Object paramObject, String paramString, char[] paramArrayOfChar, SecureRandom paramSecureRandom, Provider paramProvider)
  {
    this.obj = paramObject;
    this.algorithm = paramString;
    this.password = paramArrayOfChar;
    this.random = paramSecureRandom;
    this.provider = paramProvider;
  }

  private PemObject createPemObject(Object paramObject)
    throws IOException
  {
    if ((paramObject instanceof PemObject))
      return (PemObject)paramObject;
    if ((paramObject instanceof PemObjectGenerator))
      return ((PemObjectGenerator)paramObject).generate();
    String str;
    if ((paramObject instanceof X509Certificate))
      str = "CERTIFICATE";
    while (true)
    {
      Object localObject;
      try
      {
        byte[] arrayOfByte2 = ((X509Certificate)paramObject).getEncoded();
        localObject = arrayOfByte2;
        return new PemObject(str, (byte[])localObject);
      }
      catch (CertificateEncodingException localCertificateEncodingException)
      {
        throw new PemGenerationException("Cannot encode object: " + localCertificateEncodingException.toString());
      }
      if ((paramObject instanceof X509CRL))
      {
        str = "X509 CRL";
        try
        {
          byte[] arrayOfByte1 = ((X509CRL)paramObject).getEncoded();
          localObject = arrayOfByte1;
        }
        catch (CRLException localCRLException)
        {
          throw new PemGenerationException("Cannot encode object: " + localCRLException.toString());
        }
      }
      else
      {
        if ((paramObject instanceof KeyPair))
          return createPemObject(((KeyPair)paramObject).getPrivate());
        if ((paramObject instanceof PrivateKey))
        {
          PrivateKeyInfo localPrivateKeyInfo = new PrivateKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(((Key)paramObject).getEncoded()));
          if ((paramObject instanceof RSAPrivateKey))
          {
            str = "RSA PRIVATE KEY";
            localObject = localPrivateKeyInfo.getPrivateKey().getEncoded();
          }
          else if ((paramObject instanceof DSAPrivateKey))
          {
            str = "DSA PRIVATE KEY";
            DSAParameter localDSAParameter = DSAParameter.getInstance(localPrivateKeyInfo.getAlgorithmId().getParameters());
            ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
            localASN1EncodableVector.add(new DERInteger(0));
            localASN1EncodableVector.add(new DERInteger(localDSAParameter.getP()));
            localASN1EncodableVector.add(new DERInteger(localDSAParameter.getQ()));
            localASN1EncodableVector.add(new DERInteger(localDSAParameter.getG()));
            BigInteger localBigInteger = ((DSAPrivateKey)paramObject).getX();
            localASN1EncodableVector.add(new DERInteger(localDSAParameter.getG().modPow(localBigInteger, localDSAParameter.getP())));
            localASN1EncodableVector.add(new DERInteger(localBigInteger));
            localObject = new DERSequence(localASN1EncodableVector).getEncoded();
          }
          else if (((PrivateKey)paramObject).getAlgorithm().equals("ECDSA"))
          {
            str = "EC PRIVATE KEY";
            localObject = localPrivateKeyInfo.getPrivateKey().getEncoded();
          }
          else
          {
            throw new IOException("Cannot identify private key");
          }
        }
        else if ((paramObject instanceof PublicKey))
        {
          str = "PUBLIC KEY";
          localObject = ((PublicKey)paramObject).getEncoded();
        }
        else if ((paramObject instanceof X509AttributeCertificate))
        {
          str = "ATTRIBUTE CERTIFICATE";
          localObject = ((X509V2AttributeCertificate)paramObject).getEncoded();
        }
        else if ((paramObject instanceof PKCS10CertificationRequest))
        {
          str = "CERTIFICATE REQUEST";
          localObject = ((PKCS10CertificationRequest)paramObject).getEncoded();
        }
        else
        {
          if (!(paramObject instanceof ContentInfo))
            break;
          str = "PKCS7";
          localObject = ((ContentInfo)paramObject).getEncoded();
        }
      }
    }
    throw new PemGenerationException("unknown object passed - can't encode.");
  }

  private PemObject createPemObject(Object paramObject, String paramString, char[] paramArrayOfChar, SecureRandom paramSecureRandom)
    throws IOException
  {
    if ((paramObject instanceof KeyPair))
      return createPemObject(((KeyPair)paramObject).getPrivate(), paramString, paramArrayOfChar, paramSecureRandom);
    byte[] arrayOfByte1 = (byte[])null;
    String str1;
    if ((paramObject instanceof RSAPrivateCrtKey))
    {
      str1 = "RSA PRIVATE KEY";
      RSAPrivateCrtKey localRSAPrivateCrtKey = (RSAPrivateCrtKey)paramObject;
      arrayOfByte1 = new RSAPrivateKeyStructure(localRSAPrivateCrtKey.getModulus(), localRSAPrivateCrtKey.getPublicExponent(), localRSAPrivateCrtKey.getPrivateExponent(), localRSAPrivateCrtKey.getPrimeP(), localRSAPrivateCrtKey.getPrimeQ(), localRSAPrivateCrtKey.getPrimeExponentP(), localRSAPrivateCrtKey.getPrimeExponentQ(), localRSAPrivateCrtKey.getCrtCoefficient()).getEncoded();
    }
    while ((str1 == null) || (arrayOfByte1 == null))
    {
      throw new IllegalArgumentException("Object type not supported: " + paramObject.getClass().getName());
      if ((paramObject instanceof DSAPrivateKey))
      {
        str1 = "DSA PRIVATE KEY";
        DSAPrivateKey localDSAPrivateKey = (DSAPrivateKey)paramObject;
        DSAParams localDSAParams = localDSAPrivateKey.getParams();
        ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
        localASN1EncodableVector.add(new DERInteger(0));
        localASN1EncodableVector.add(new DERInteger(localDSAParams.getP()));
        localASN1EncodableVector.add(new DERInteger(localDSAParams.getQ()));
        localASN1EncodableVector.add(new DERInteger(localDSAParams.getG()));
        BigInteger localBigInteger = localDSAPrivateKey.getX();
        localASN1EncodableVector.add(new DERInteger(localDSAParams.getG().modPow(localBigInteger, localDSAParams.getP())));
        localASN1EncodableVector.add(new DERInteger(localBigInteger));
        arrayOfByte1 = new DERSequence(localASN1EncodableVector).getEncoded();
      }
      else
      {
        boolean bool1 = paramObject instanceof PrivateKey;
        str1 = null;
        if (bool1)
        {
          boolean bool2 = "ECDSA".equals(((PrivateKey)paramObject).getAlgorithm());
          str1 = null;
          if (bool2)
          {
            str1 = "EC PRIVATE KEY";
            arrayOfByte1 = PrivateKeyInfo.getInstance(ASN1Object.fromByteArray(((PrivateKey)paramObject).getEncoded())).getPrivateKey().getEncoded();
          }
        }
      }
    }
    String str2 = Strings.toUpperCase(paramString);
    if (str2.equals("DESEDE"))
      str2 = "DES-EDE3-CBC";
    if (str2.startsWith("AES-"));
    for (int i = 16; ; i = 8)
    {
      byte[] arrayOfByte2 = new byte[i];
      paramSecureRandom.nextBytes(arrayOfByte2);
      byte[] arrayOfByte3 = PEMUtilities.crypt(true, this.provider, arrayOfByte1, paramArrayOfChar, str2, arrayOfByte2);
      ArrayList localArrayList = new ArrayList(2);
      localArrayList.add(new PemHeader("Proc-Type", "4,ENCRYPTED"));
      localArrayList.add(new PemHeader("DEK-Info", str2 + "," + getHexEncoded(arrayOfByte2)));
      return new PemObject(str1, localArrayList, arrayOfByte3);
    }
  }

  private String getHexEncoded(byte[] paramArrayOfByte)
    throws IOException
  {
    byte[] arrayOfByte = Hex.encode(paramArrayOfByte);
    char[] arrayOfChar = new char[arrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfByte.length)
        return new String(arrayOfChar);
      arrayOfChar[i] = ((char)arrayOfByte[i]);
    }
  }

  public PemObject generate()
    throws PemGenerationException
  {
    try
    {
      if (this.algorithm != null)
        return createPemObject(this.obj, this.algorithm, this.password, this.random);
      PemObject localPemObject = createPemObject(this.obj);
      return localPemObject;
    }
    catch (IOException localIOException)
    {
      throw new PemGenerationException("encoding exception: " + localIOException.getMessage(), localIOException);
    }
  }
}