package org.bouncycastle2.x509;

import java.io.ByteArrayInputStream;
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
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.TBSCertificateStructure;
import org.bouncycastle2.asn1.x509.Time;
import org.bouncycastle2.asn1.x509.V1TBSCertificateGenerator;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.asn1.x509.X509Name;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.jce.provider.X509CertificateObject;

public class X509V1CertificateGenerator
{
  private AlgorithmIdentifier sigAlgId;
  private DERObjectIdentifier sigOID;
  private String signatureAlgorithm;
  private V1TBSCertificateGenerator tbsGen = new V1TBSCertificateGenerator();

  private X509Certificate generateJcaObject(TBSCertificateStructure paramTBSCertificateStructure, byte[] paramArrayOfByte)
    throws CertificateEncodingException
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramTBSCertificateStructure);
    localASN1EncodableVector.add(this.sigAlgId);
    localASN1EncodableVector.add(new DERBitString(paramArrayOfByte));
    try
    {
      X509CertificateObject localX509CertificateObject = new X509CertificateObject(new X509CertificateStructure(new DERSequence(localASN1EncodableVector)));
      return localX509CertificateObject;
    }
    catch (CertificateParsingException localCertificateParsingException)
    {
      throw new ExtCertificateEncodingException("exception producing certificate object", localCertificateParsingException);
    }
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

  public X509Certificate generate(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    TBSCertificateStructure localTBSCertificateStructure = this.tbsGen.generateTBSCertificate();
    try
    {
      byte[] arrayOfByte = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, paramString, paramPrivateKey, paramSecureRandom, localTBSCertificateStructure);
      return generateJcaObject(localTBSCertificateStructure, arrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new ExtCertificateEncodingException("exception encoding TBS cert", localIOException);
    }
  }

  public X509Certificate generate(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws CertificateEncodingException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    TBSCertificateStructure localTBSCertificateStructure = this.tbsGen.generateTBSCertificate();
    try
    {
      byte[] arrayOfByte = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, paramPrivateKey, paramSecureRandom, localTBSCertificateStructure);
      return generateJcaObject(localTBSCertificateStructure, arrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new ExtCertificateEncodingException("exception encoding TBS cert", localIOException);
    }
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
    this.tbsGen = new V1TBSCertificateGenerator();
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

  public void setNotAfter(Date paramDate)
  {
    this.tbsGen.setEndDate(new Time(paramDate));
  }

  public void setNotBefore(Date paramDate)
  {
    this.tbsGen.setStartDate(new Time(paramDate));
  }

  public void setPublicKey(PublicKey paramPublicKey)
  {
    try
    {
      this.tbsGen.setSubjectPublicKeyInfo(new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(new ByteArrayInputStream(paramPublicKey.getEncoded())).readObject()));
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
    throw new IllegalArgumentException("Unknown signature type requested");
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
}