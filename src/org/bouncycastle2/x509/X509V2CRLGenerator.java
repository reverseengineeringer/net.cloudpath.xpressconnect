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
import java.security.cert.CRLException;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.CertificateList;
import org.bouncycastle2.asn1.x509.TBSCertList;
import org.bouncycastle2.asn1.x509.Time;
import org.bouncycastle2.asn1.x509.V2TBSCertListGenerator;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle2.asn1.x509.X509Name;
import org.bouncycastle2.jce.X509Principal;
import org.bouncycastle2.jce.provider.X509CRLObject;

public class X509V2CRLGenerator
{
  private X509ExtensionsGenerator extGenerator = new X509ExtensionsGenerator();
  private AlgorithmIdentifier sigAlgId;
  private DERObjectIdentifier sigOID;
  private String signatureAlgorithm;
  private V2TBSCertListGenerator tbsGen = new V2TBSCertListGenerator();

  private TBSCertList generateCertList()
  {
    if (!this.extGenerator.isEmpty())
      this.tbsGen.setExtensions(this.extGenerator.generate());
    return this.tbsGen.generateTBSCertList();
  }

  private X509CRL generateJcaObject(TBSCertList paramTBSCertList, byte[] paramArrayOfByte)
    throws CRLException
  {
    ASN1EncodableVector localASN1EncodableVector = new ASN1EncodableVector();
    localASN1EncodableVector.add(paramTBSCertList);
    localASN1EncodableVector.add(this.sigAlgId);
    localASN1EncodableVector.add(new DERBitString(paramArrayOfByte));
    return new X509CRLObject(new CertificateList(new DERSequence(localASN1EncodableVector)));
  }

  public void addCRL(X509CRL paramX509CRL)
    throws CRLException
  {
    Set localSet = paramX509CRL.getRevokedCertificates();
    Iterator localIterator;
    if (localSet != null)
      localIterator = localSet.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ASN1InputStream localASN1InputStream = new ASN1InputStream(((X509CRLEntry)localIterator.next()).getEncoded());
      try
      {
        this.tbsGen.addCRLEntry(ASN1Sequence.getInstance(localASN1InputStream.readObject()));
      }
      catch (IOException localIOException)
      {
        throw new CRLException("exception processing encoding of CRL: " + localIOException.toString());
      }
    }
  }

  public void addCRLEntry(BigInteger paramBigInteger, Date paramDate, int paramInt)
  {
    this.tbsGen.addCRLEntry(new DERInteger(paramBigInteger), new Time(paramDate), paramInt);
  }

  public void addCRLEntry(BigInteger paramBigInteger, Date paramDate1, int paramInt, Date paramDate2)
  {
    this.tbsGen.addCRLEntry(new DERInteger(paramBigInteger), new Time(paramDate1), paramInt, new DERGeneralizedTime(paramDate2));
  }

  public void addCRLEntry(BigInteger paramBigInteger, Date paramDate, X509Extensions paramX509Extensions)
  {
    this.tbsGen.addCRLEntry(new DERInteger(paramBigInteger), new Time(paramDate), paramX509Extensions);
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

  public X509CRL generate(PrivateKey paramPrivateKey)
    throws CRLException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    return generate(paramPrivateKey, null);
  }

  public X509CRL generate(PrivateKey paramPrivateKey, String paramString)
    throws CRLException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    return generate(paramPrivateKey, paramString, null);
  }

  public X509CRL generate(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws CRLException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    TBSCertList localTBSCertList = generateCertList();
    try
    {
      byte[] arrayOfByte = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, paramString, paramPrivateKey, paramSecureRandom, localTBSCertList);
      return generateJcaObject(localTBSCertList, arrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new ExtCRLException("cannot generate CRL encoding", localIOException);
    }
  }

  public X509CRL generate(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws CRLException, IllegalStateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    TBSCertList localTBSCertList = generateCertList();
    try
    {
      byte[] arrayOfByte = X509Util.calculateSignature(this.sigOID, this.signatureAlgorithm, paramPrivateKey, paramSecureRandom, localTBSCertList);
      return generateJcaObject(localTBSCertList, arrayOfByte);
    }
    catch (IOException localIOException)
    {
      throw new ExtCRLException("cannot generate CRL encoding", localIOException);
    }
  }

  public X509CRL generateX509CRL(PrivateKey paramPrivateKey)
    throws SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509CRL localX509CRL = generateX509CRL(paramPrivateKey, "BC", null);
      return localX509CRL;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
    }
    throw new SecurityException("BC provider not installed!");
  }

  public X509CRL generateX509CRL(PrivateKey paramPrivateKey, String paramString)
    throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException
  {
    return generateX509CRL(paramPrivateKey, paramString, null);
  }

  public X509CRL generateX509CRL(PrivateKey paramPrivateKey, String paramString, SecureRandom paramSecureRandom)
    throws NoSuchProviderException, SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509CRL localX509CRL = generate(paramPrivateKey, paramString, paramSecureRandom);
      return localX509CRL;
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

  public X509CRL generateX509CRL(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
    throws SecurityException, SignatureException, InvalidKeyException
  {
    try
    {
      X509CRL localX509CRL = generateX509CRL(paramPrivateKey, "BC", paramSecureRandom);
      return localX509CRL;
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
    this.tbsGen = new V2TBSCertListGenerator();
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

  public void setNextUpdate(Date paramDate)
  {
    this.tbsGen.setNextUpdate(new Time(paramDate));
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

  public void setThisUpdate(Date paramDate)
  {
    this.tbsGen.setThisUpdate(new Time(paramDate));
  }

  private static class ExtCRLException extends CRLException
  {
    Throwable cause;

    ExtCRLException(String paramString, Throwable paramThrowable)
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