package org.bouncycastle2.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.TBSCertificateStructure;
import org.bouncycastle2.asn1.x509.Time;
import org.bouncycastle2.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle2.asn1.x509.X509Name;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.jce.provider.X509CertificateObject;
import org.bouncycastle2.x509.extension.X509ExtensionUtil;

public class X509V3CertificateGenerator
{
  private X509ExtensionsGenerator extGenerator = new X509ExtensionsGenerator();
  private AlgorithmIdentifier sigAlgId;
  private DERObjectIdentifier sigOID;
  private String signatureAlgorithm;
  private V3TBSCertificateGenerator tbsGen = new V3TBSCertificateGenerator();

  private DERBitString booleanToBitString(boolean[] paramArrayOfBoolean)
  {
    byte[] arrayOfByte = new byte[(7 + paramArrayOfBoolean.length) / 8];
    int i = 0;
    int n;
    if (i == paramArrayOfBoolean.length)
    {
      n = paramArrayOfBoolean.length % 8;
      if (n == 0)
        return new DERBitString(arrayOfByte);
    }
    else
    {
      int j = i / 8;
      int k = arrayOfByte[j];
      if (paramArrayOfBoolean[i] != 0);
      for (int m = 1 << 7 - i % 8; ; m = 0)
      {
        arrayOfByte[j] = ((byte)(m | k));
        i++;
        break;
      }
    }
    return new DERBitString(arrayOfByte, 8 - n);
  }

  private X509Certificate generateJcaObject(TBSCertificateStructure paramTBSCertificateStructure, byte[] paramArrayOfByte)
    throws CertificateParsingException
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramTBSCertificateStructure);
    localASN1EncodableVector.add(this.sigAlgId);
    localASN1EncodableVector.add(new DERBitString(paramArrayOfByte));
    return new X509CertificateObject(new X509CertificateStructure(new DERSequence(localASN1EncodableVector)));
  }

  private TBSCertificateStructure generateTbsCert()
  {
    if (!this.extGenerator.isEmpty())
      this.tbsGen.setExtensions(this.extGenerator.generate());
    return this.tbsGen.generateTBSCertificate();
  }

  public void addExtension(String paramString, boolean paramBoolean, DEREncodable paramDEREncodable)
  {
    addExtension(new DERObjectIdentifier(paramString), paramBoolean, paramDEREncodable);
  }

  public void addExtension(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    addExtension(new DERObjectIdentifier(paramString), paramBoolean, paramArrayOfByte);
  }

  public void addExtension(DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, DEREncodable paramDEREncodable)
  {
    this.extGenerator.addExtension(paramDERObjectIdentifier, paramBoolean, paramDEREncodable);
  }

  public void addExtension(DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    this.extGenerator.addExtension(paramDERObjectIdentifier, paramBoolean, paramArrayOfByte);
  }

  public void copyAndAddExtension(String paramString, boolean paramBoolean, X509Certificate paramX509Certificate)
    throws CertificateParsingException
  {
    byte[] arrayOfByte = paramX509Certificate.getExtensionValue(paramString);
    if (arrayOfByte == null)
      throw new CertificateParsingException("extension " + paramString + " not present");
    try
    {
      addExtension(paramString, paramBoolean, X509ExtensionUtil.fromExtensionValue(arrayOfByte));
      return;
    }
    catch (IOException localIOException)
    {
      throw new CertificateParsingException(localIOException.toString());
    }
  }

  public void copyAndAddExtension(DERObjectIdentifier paramDERObjectIdentifier, boolean paramBoolean, X509Certificate paramX509Certificate)
    throws CertificateParsingException
  {
    copyAndAddExtension(paramDERObjectIdentifier.getId(), paramBoolean, paramX509Certificate);
  }

  public X509Certificate generate(PrivateKey paramPrivateKey)
    throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    return generate(paramPrivateKey, null);
  }

  public X509Certificate generate(PrivateKey paramPrivateKey, String paramString)
    throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    return generate(paramPrivateKey, paramString, null);
  }

  // ERROR //
  public X509Certificate generate(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 162	org/bouncycastle2/x509/X509V3CertificateGenerator:generateTbsCert	()Lorg/bouncycastle2/asn1/x509/TBSCertificateStructure;
    //   4: astore 4
    //   6: aload_0
    //   7: getfield 164	org/bouncycastle2/x509/X509V3CertificateGenerator:sigOID	Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   10: aload_0
    //   11: getfield 166	org/bouncycastle2/x509/X509V3CertificateGenerator:signatureAlgorithm	Ljava/lang/String;
    //   14: aload_2
    //   15: aload_1
    //   16: aload_3
    //   17: aload 4
    //   19: invokestatic 172	org/bouncycastle2/x509/X509Util:calculateSignature	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;Ljava/lang/String;Ljava/lang/String;Ljava/security/PrivateKey;Ljava/security/SecureRandom;Lorg/bouncycastle2/asn1/ASN1Encodable;)[B
    //   22: astore 6
    //   24: aload_0
    //   25: aload 4
    //   27: aload 6
    //   29: invokespecial 174	org/bouncycastle2/x509/X509V3CertificateGenerator:generateJcaObject	(Lorg/bouncycastle2/asn1/x509/TBSCertificateStructure;[B)Ljava/security/cert/X509Certificate;
    //   32: astore 8
    //   34: aload 8
    //   36: areturn
    //   37: astore 5
    //   39: new 176	org/bouncycastle2/x509/ExtCertificateEncodingException
    //   42: dup
    //   43: ldc 178
    //   45: aload 5
    //   47: invokespecial 181	org/bouncycastle2/x509/ExtCertificateEncodingException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   50: athrow
    //   51: astore 7
    //   53: new 176	org/bouncycastle2/x509/ExtCertificateEncodingException
    //   56: dup
    //   57: ldc 183
    //   59: aload 7
    //   61: invokespecial 181	org/bouncycastle2/x509/ExtCertificateEncodingException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   64: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   6	24	37	java/io/IOException
    //   24	34	51	java/security/cert/CertificateParsingException
  }

  // ERROR //
  public X509Certificate generate(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 162	org/bouncycastle2/x509/X509V3CertificateGenerator:generateTbsCert	()Lorg/bouncycastle2/asn1/x509/TBSCertificateStructure;
    //   4: astore_3
    //   5: aload_0
    //   6: getfield 164	org/bouncycastle2/x509/X509V3CertificateGenerator:sigOID	Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   9: aload_0
    //   10: getfield 166	org/bouncycastle2/x509/X509V3CertificateGenerator:signatureAlgorithm	Ljava/lang/String;
    //   13: aload_1
    //   14: aload_2
    //   15: aload_3
    //   16: invokestatic 186	org/bouncycastle2/x509/X509Util:calculateSignature	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;Ljava/lang/String;Ljava/security/PrivateKey;Ljava/security/SecureRandom;Lorg/bouncycastle2/asn1/ASN1Encodable;)[B
    //   19: astore 5
    //   21: aload_0
    //   22: aload_3
    //   23: aload 5
    //   25: invokespecial 174	org/bouncycastle2/x509/X509V3CertificateGenerator:generateJcaObject	(Lorg/bouncycastle2/asn1/x509/TBSCertificateStructure;[B)Ljava/security/cert/X509Certificate;
    //   28: astore 7
    //   30: aload 7
    //   32: areturn
    //   33: astore 4
    //   35: new 176	org/bouncycastle2/x509/ExtCertificateEncodingException
    //   38: dup
    //   39: ldc 178
    //   41: aload 4
    //   43: invokespecial 181	org/bouncycastle2/x509/ExtCertificateEncodingException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   46: athrow
    //   47: astore 6
    //   49: new 176	org/bouncycastle2/x509/ExtCertificateEncodingException
    //   52: dup
    //   53: ldc 183
    //   55: aload 6
    //   57: invokespecial 181	org/bouncycastle2/x509/ExtCertificateEncodingException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   60: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   5	21	33	java/io/IOException
    //   21	30	47	java/security/cert/CertificateParsingException
  }

  public X509Certificate generateX509Certificate(PrivateKey paramPrivateKey)
    throws SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509Certificate localX509Certificate = generateX509Certificate(paramPrivateKey, "BC", null);
      return localX509Certificate;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
    }
    throw new SecurityException("BC provider not installed!");
  }

  public X509Certificate generateX509Certificate(PrivateKey paramPrivateKey, String paramString)
    throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException
  {
    return generateX509Certificate(paramPrivateKey, paramString, null);
  }

  public X509Certificate generateX509Certificate(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509Certificate localX509Certificate = generate(paramPrivateKey, paramString, paramSecureRandom);
      return localX509Certificate;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw localNoSuchProviderException;
    }
    catch (SignatureException localSignatureException)
    {
      throw localSignatureException;
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      throw localInvalidKeyException;
    }
    catch (GeneralSecurityException localGeneralSecurityException)
    {
      throw new SecurityException("exception: " + localGeneralSecurityException);
    }
  }

  public X509Certificate generateX509Certificate(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509Certificate localX509Certificate = generateX509Certificate(paramPrivateKey, "BC", paramSecureRandom);
      return localX509Certificate;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
    }
    throw new SecurityException("BC provider not installed!");
  }

  public Iterator getSignatureAlgNames()
  {
    return X509Util.getAlgNames();
  }

  public void reset()
  {
    this.tbsGen = new V3TBSCertificateGenerator();
    this.extGenerator.reset();
  }

  public void setIssuerDN(X500Principal paramX500Principal)
  {
    try
    {
      this.tbsGen.setIssuer(new X509Principal(paramX500Principal.getEncoded()));
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("can't process principal: " + localIOException);
    }
  }

  public void setIssuerDN(X509Name paramX509Name)
  {
    this.tbsGen.setIssuer(paramX509Name);
  }

  public void setIssuerUniqueID(boolean[] paramArrayOfBoolean)
  {
    this.tbsGen.setIssuerUniqueID(booleanToBitString(paramArrayOfBoolean));
  }

  public void setNotAfter(Date paramDate)
  {
    this.tbsGen.setEndDate(new Time(paramDate));
  }

  public void setNotBefore(Date paramDate)
  {
    this.tbsGen.setStartDate(new Time(paramDate));
  }

  public void setPublicKey(PublicKey paramPublicKey)
    throws IllegalArgumentException
  {
    try
    {
      this.tbsGen.setSubjectPublicKeyInfo(SubjectPublicKeyInfo.getInstance(new ASN1InputStream(paramPublicKey.getEncoded()).readObject()));
      return;
    }
    catch (Exception localException)
    {
      throw new IllegalArgumentException("unable to process key - " + localException.toString());
    }
  }

  public void setSerialNumber(BigInteger paramBigInteger)
  {
    if (paramBigInteger.compareTo(BigInteger.ZERO) <= 0)
      throw new IllegalArgumentException("serial number must be a positive integer");
    this.tbsGen.setSerialNumber(new DERInteger(paramBigInteger));
  }

  public void setSignatureAlgorithm(String paramString)
  {
    this.signatureAlgorithm = paramString;
    try
    {
      this.sigOID = X509Util.getAlgorithmOID(paramString);
      this.sigAlgId = X509Util.getSigAlgID(this.sigOID, paramString);
      this.tbsGen.setSignature(this.sigAlgId);
      return;
    }
    catch (Exception localException)
    {
    }
    throw new IllegalArgumentException("Unknown signature type requested: " + paramString);
  }

  public void setSubjectDN(X500Principal paramX500Principal)
  {
    try
    {
      this.tbsGen.setSubject(new X509Principal(paramX500Principal.getEncoded()));
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("can't process principal: " + localIOException);
    }
  }

  public void setSubjectDN(X509Name paramX509Name)
  {
    this.tbsGen.setSubject(paramX509Name);
  }

  public void setSubjectUniqueID(boolean[] paramArrayOfBoolean)
  {
    this.tbsGen.setSubjectUniqueID(booleanToBitString(paramArrayOfBoolean));
  }
}