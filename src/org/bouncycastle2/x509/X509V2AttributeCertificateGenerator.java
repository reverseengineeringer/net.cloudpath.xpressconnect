package org.bouncycastle2.x509;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.util.Date;
import java.util.Iterator;
import org.bouncycastle2.asn1.ASN1Encodable;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.AttCertIssuer;
import org.bouncycastle2.asn1.x509.Attribute;
import org.bouncycastle2.asn1.x509.AttributeCertificate;
import org.bouncycastle2.asn1.x509.AttributeCertificateInfo;
import org.bouncycastle2.asn1.x509.V2AttributeCertificateInfoGenerator;
import org.bouncycastle2.asn1.x509.X509ExtensionsGenerator;

public class X509V2AttributeCertificateGenerator
{
  private V2AttributeCertificateInfoGenerator acInfoGen = new V2AttributeCertificateInfoGenerator();
  private X509ExtensionsGenerator extGenerator = new X509ExtensionsGenerator();
  private AlgorithmIdentifier sigAlgId;
  private DERObjectIdentifier sigOID;
  private String signatureAlgorithm;

  public void addAttribute(X509Attribute paramX509Attribute)
  {
    this.acInfoGen.addAttribute(Attribute.getInstance(paramX509Attribute.toASN1Object()));
  }

  public void addExtension(String paramString, boolean paramBoolean, ASN1Encodable paramASN1Encodable)
    throws IOException
  {
    this.extGenerator.addExtension(new DERObjectIdentifier(paramString), paramBoolean, paramASN1Encodable);
  }

  public void addExtension(String paramString, boolean paramBoolean, byte[] paramArrayOfByte)
  {
    this.extGenerator.addExtension(new DERObjectIdentifier(paramString), paramBoolean, paramArrayOfByte);
  }

  public X509AttributeCertificate generate(PrivateKey paramPrivateKey, String paramString)
    throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, SignatureException, InvalidKeyException, NoSuchAlgorithmException
  {
    return generate(paramPrivateKey, paramString, null);
  }

  public X509AttributeCertificate generate(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws CertificateEncodingException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    if (!this.extGenerator.isEmpty())
      this.acInfoGen.setExtensions(this.extGenerator.generate());
    AttributeCertificateInfo localAttributeCertificateInfo = this.acInfoGen.generateAttributeCertificateInfo();
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(localAttributeCertificateInfo);
    localASN1EncodableVector.add(this.sigAlgId);
    try
    {
      localASN1EncodableVector.add(new DERBitString(X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, paramString, paramPrivateKey, paramSecureRandom, localAttributeCertificateInfo)));
      X509V2AttributeCertificate localX509V2AttributeCertificate = new X509V2AttributeCertificate(new AttributeCertificate(new DERSequence(localASN1EncodableVector)));
      return localX509V2AttributeCertificate;
    }
    catch (IOException localIOException)
    {
      throw new ExtCertificateEncodingException("constructed invalid certificate", localIOException);
    }
  }

  public X509AttributeCertificate generateCertificate(PrivateKey paramPrivateKey, String paramString)
    throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException
  {
    return generateCertificate(paramPrivateKey, paramString, null);
  }

  public X509AttributeCertificate generateCertificate(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509AttributeCertificate localX509AttributeCertificate = generate(paramPrivateKey, paramString, paramSecureRandom);
      return localX509AttributeCertificate;
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
      throw new SecurityException("exception creating certificate: " + localGeneralSecurityException);
    }
  }

  public Iterator getSignatureAlgNames()
  {
    return X509Util.getAlgNames();
  }

  public void reset()
  {
    this.acInfoGen = new V2AttributeCertificateInfoGenerator();
    this.extGenerator.reset();
  }

  public void setHolder(AttributeCertificateHolder paramAttributeCertificateHolder)
  {
    this.acInfoGen.setHolder(paramAttributeCertificateHolder.holder);
  }

  public void setIssuer(AttributeCertificateIssuer paramAttributeCertificateIssuer)
  {
    this.acInfoGen.setIssuer(AttCertIssuer.getInstance(paramAttributeCertificateIssuer.form));
  }

  public void setIssuerUniqueId(boolean[] paramArrayOfBoolean)
  {
    throw new RuntimeException("not implemented (yet)");
  }

  public void setNotAfter(Date paramDate)
  {
    this.acInfoGen.setEndDate(new DERGeneralizedTime(paramDate));
  }

  public void setNotBefore(Date paramDate)
  {
    this.acInfoGen.setStartDate(new DERGeneralizedTime(paramDate));
  }

  public void setSerialNumber(BigInteger paramBigInteger)
  {
    this.acInfoGen.setSerialNumber(new DERInteger(paramBigInteger));
  }

  public void setSignatureAlgorithm(String paramString)
  {
    this.signatureAlgorithm = paramString;
    try
    {
      this.sigOID = X509Util.getAlgorithmOID(paramString);
      this.sigAlgId = X509Util.getSigAlgID(this.sigOID, paramString);
      this.acInfoGen.setSignature(this.sigAlgId);
      return;
    }
    catch (Exception localException)
    {
    }
    throw new IllegalArgumentException("Unknown signature type requested");
  }
}