package org.bouncycastle2.jce.provider;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyStore.LoadStoreParameter;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle2.asn1.ASN1EncodableVector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1OctetString;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.BERConstructedOctetString;
import org.bouncycastle2.asn1.BEROutputStream;
import org.bouncycastle2.asn1.DERBMPString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.DEROutputStream;
import org.bouncycastle2.asn1.DERSequence;
import org.bouncycastle2.asn1.DERSet;
import org.bouncycastle2.asn1.pkcs.AuthenticatedSafe;
import org.bouncycastle2.asn1.pkcs.CertBag;
import org.bouncycastle2.asn1.pkcs.ContentInfo;
import org.bouncycastle2.asn1.pkcs.EncryptedData;
import org.bouncycastle2.asn1.pkcs.EncryptedPrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.MacData;
import org.bouncycastle2.asn1.pkcs.PKCS12PBEParams;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.Pfx;
import org.bouncycastle2.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle2.asn1.pkcs.SafeBag;
import org.bouncycastle2.asn1.util.ASN1Dump;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle2.asn1.x509.DigestInfo;
import org.bouncycastle2.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle2.jce.interfaces.BCKeyStore;
import org.bouncycastle2.jce.interfaces.PKCS12BagAttributeCarrier;
import org.bouncycastle2.util.Arrays;
import org.bouncycastle2.util.Strings;
import org.bouncycastle2.util.encoders.Hex;

public class JDKPKCS12KeyStore extends KeyStoreSpi
  implements PKCSObjectIdentifiers, X509ObjectIdentifiers, BCKeyStore
{
  static final int CERTIFICATE = 1;
  static final int KEY = 2;
  static final int KEY_PRIVATE = 0;
  static final int KEY_PUBLIC = 1;
  static final int KEY_SECRET = 2;
  private static final int MIN_ITERATIONS = 1024;
  static final int NULL = 0;
  private static final int SALT_SIZE = 20;
  static final int SEALED = 4;
  static final int SECRET = 3;
  private static final Provider bcProvider = new BouncyCastleProvider();
  private DERObjectIdentifier certAlgorithm;
  private CertificateFactory certFact;
  private IgnoresCaseHashtable certs = new IgnoresCaseHashtable(null);
  private Hashtable chainCerts = new Hashtable();
  private DERObjectIdentifier keyAlgorithm;
  private Hashtable keyCerts = new Hashtable();
  private IgnoresCaseHashtable keys = new IgnoresCaseHashtable(null);
  private Hashtable localIds = new Hashtable();
  protected SecureRandom random = new SecureRandom();

  public JDKPKCS12KeyStore(Provider paramProvider, DERObjectIdentifier paramDERObjectIdentifier1, DERObjectIdentifier paramDERObjectIdentifier2)
  {
    this.keyAlgorithm = paramDERObjectIdentifier1;
    this.certAlgorithm = paramDERObjectIdentifier2;
    if (paramProvider != null);
    try
    {
      this.certFact = CertificateFactory.getInstance("X.509", paramProvider);
      return;
      this.certFact = CertificateFactory.getInstance("X.509");
      return;
    }
    catch (Exception localException)
    {
      throw new IllegalArgumentException("can't create cert factory - " + localException.toString());
    }
  }

  private static byte[] calculatePbeMac(DERObjectIdentifier paramDERObjectIdentifier, byte[] paramArrayOfByte1, int paramInt, char[] paramArrayOfChar, boolean paramBoolean, byte[] paramArrayOfByte2)
    throws Exception
  {
    SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance(paramDERObjectIdentifier.getId(), bcProvider);
    PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(paramArrayOfByte1, paramInt);
    JCEPBEKey localJCEPBEKey = (JCEPBEKey)localSecretKeyFactory.generateSecret(new PBEKeySpec(paramArrayOfChar));
    localJCEPBEKey.setTryWrongPKCS12Zero(paramBoolean);
    Mac localMac = Mac.getInstance(paramDERObjectIdentifier.getId(), bcProvider);
    localMac.init(localJCEPBEKey, localPBEParameterSpec);
    localMac.update(paramArrayOfByte2);
    return localMac.doFinal();
  }

  private SubjectKeyIdentifier createSubjectKeyId(PublicKey paramPublicKey)
  {
    try
    {
      SubjectKeyIdentifier localSubjectKeyIdentifier = new SubjectKeyIdentifier(new SubjectPublicKeyInfo((ASN1Sequence)ASN1Object.fromByteArray(paramPublicKey.getEncoded())));
      return localSubjectKeyIdentifier;
    }
    catch (Exception localException)
    {
    }
    throw new RuntimeException("error creating key");
  }

  private void doStore(OutputStream paramOutputStream, char[] paramArrayOfChar, boolean paramBoolean)
    throws IOException
  {
    if (paramArrayOfChar == null)
      throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
    ASN1EncodableVector localASN1EncodableVector1 = new ASN1EncodableVector();
    Enumeration localEnumeration1 = this.keys.keys();
    ASN1EncodableVector localASN1EncodableVector6;
    Hashtable localHashtable;
    Enumeration localEnumeration3;
    Enumeration localEnumeration5;
    Enumeration localEnumeration7;
    label179: ByteArrayOutputStream localByteArrayOutputStream;
    Object localObject1;
    ContentInfo localContentInfo;
    byte[] arrayOfByte7;
    byte[] arrayOfByte8;
    if (!localEnumeration1.hasMoreElements())
    {
      byte[] arrayOfByte3 = new DERSequence(localASN1EncodableVector1).getDEREncoded();
      BERConstructedOctetString localBERConstructedOctetString = new BERConstructedOctetString(arrayOfByte3);
      byte[] arrayOfByte4 = new byte[20];
      this.random.nextBytes(arrayOfByte4);
      localASN1EncodableVector6 = new ASN1EncodableVector();
      PKCS12PBEParams localPKCS12PBEParams2 = new PKCS12PBEParams(arrayOfByte4, 1024);
      AlgorithmIdentifier localAlgorithmIdentifier2 = new AlgorithmIdentifier(this.certAlgorithm, localPKCS12PBEParams2.getDERObject());
      localHashtable = new Hashtable();
      localEnumeration3 = this.keys.keys();
      if (localEnumeration3.hasMoreElements())
        break label942;
      localEnumeration5 = this.certs.keys();
      if (localEnumeration5.hasMoreElements())
        break label1375;
      localEnumeration7 = this.chainCerts.keys();
      if (localEnumeration7.hasMoreElements())
        break label1753;
      byte[] arrayOfByte5 = cryptData(true, localAlgorithmIdentifier2, paramArrayOfChar, false, new DERSequence(localASN1EncodableVector6).getDEREncoded());
      EncryptedData localEncryptedData = new EncryptedData(data, localAlgorithmIdentifier2, new BERConstructedOctetString(arrayOfByte5));
      ContentInfo[] arrayOfContentInfo = new ContentInfo[2];
      arrayOfContentInfo[0] = new ContentInfo(data, localBERConstructedOctetString);
      arrayOfContentInfo[1] = new ContentInfo(encryptedData, localEncryptedData.getDERObject());
      AuthenticatedSafe localAuthenticatedSafe = new AuthenticatedSafe(arrayOfContentInfo);
      localByteArrayOutputStream = new ByteArrayOutputStream();
      if (!paramBoolean)
        break label2000;
      localObject1 = new DEROutputStream(localByteArrayOutputStream);
      ((DEROutputStream)localObject1).writeObject(localAuthenticatedSafe);
      byte[] arrayOfByte6 = localByteArrayOutputStream.toByteArray();
      localContentInfo = new ContentInfo(data, new BERConstructedOctetString(arrayOfByte6));
      arrayOfByte7 = new byte[20];
      this.random.nextBytes(arrayOfByte7);
      arrayOfByte8 = ((ASN1OctetString)localContentInfo.getContent()).getOctets();
    }
    while (true)
    {
      try
      {
        label942: label1375: ASN1EncodableVector localASN1EncodableVector14;
        label1753: PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier4;
        Enumeration localEnumeration8;
        while (true)
        {
          byte[] arrayOfByte9 = calculatePbeMac(id_SHA1, arrayOfByte7, 1024, paramArrayOfChar, false, arrayOfByte8);
          AlgorithmIdentifier localAlgorithmIdentifier3 = new AlgorithmIdentifier(id_SHA1, new DERNull());
          DigestInfo localDigestInfo = new DigestInfo(localAlgorithmIdentifier3, arrayOfByte9);
          MacData localMacData = new MacData(localDigestInfo, arrayOfByte7, 1024);
          Pfx localPfx = new Pfx(localContentInfo, localMacData);
          if (!paramBoolean)
            break label2045;
          localObject2 = new DEROutputStream(paramOutputStream);
          ((DEROutputStream)localObject2).writeObject(localPfx);
          return;
          byte[] arrayOfByte1 = new byte[20];
          this.random.nextBytes(arrayOfByte1);
          String str1 = (String)localEnumeration1.nextElement();
          PrivateKey localPrivateKey = (PrivateKey)this.keys.get(str1);
          PKCS12PBEParams localPKCS12PBEParams1 = new PKCS12PBEParams(arrayOfByte1, 1024);
          byte[] arrayOfByte2 = wrapKey(this.keyAlgorithm.getId(), localPrivateKey, localPKCS12PBEParams1, paramArrayOfChar);
          AlgorithmIdentifier localAlgorithmIdentifier1 = new AlgorithmIdentifier(this.keyAlgorithm, localPKCS12PBEParams1.getDERObject());
          EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(localAlgorithmIdentifier1, arrayOfByte2);
          ASN1EncodableVector localASN1EncodableVector2 = new ASN1EncodableVector();
          boolean bool1 = localPrivateKey instanceof PKCS12BagAttributeCarrier;
          int i = 0;
          PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier1;
          Enumeration localEnumeration2;
          if (bool1)
          {
            localPKCS12BagAttributeCarrier1 = (PKCS12BagAttributeCarrier)localPrivateKey;
            DERBMPString localDERBMPString1 = (DERBMPString)localPKCS12BagAttributeCarrier1.getBagAttribute(pkcs_9_at_friendlyName);
            if ((localDERBMPString1 == null) || (!localDERBMPString1.getString().equals(str1)))
              localPKCS12BagAttributeCarrier1.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(str1));
            if (localPKCS12BagAttributeCarrier1.getBagAttribute(pkcs_9_at_localKeyId) == null)
            {
              Certificate localCertificate2 = engineGetCertificate(str1);
              localPKCS12BagAttributeCarrier1.setBagAttribute(pkcs_9_at_localKeyId, createSubjectKeyId(localCertificate2.getPublicKey()));
            }
            localEnumeration2 = localPKCS12BagAttributeCarrier1.getBagAttributeKeys();
          }
          while (true)
          {
            if (!localEnumeration2.hasMoreElements())
            {
              if (i == 0)
              {
                ASN1EncodableVector localASN1EncodableVector3 = new ASN1EncodableVector();
                Certificate localCertificate1 = engineGetCertificate(str1);
                localASN1EncodableVector3.add(pkcs_9_at_localKeyId);
                localASN1EncodableVector3.add(new DERSet(createSubjectKeyId(localCertificate1.getPublicKey())));
                localASN1EncodableVector2.add(new DERSequence(localASN1EncodableVector3));
                ASN1EncodableVector localASN1EncodableVector4 = new ASN1EncodableVector();
                localASN1EncodableVector4.add(pkcs_9_at_friendlyName);
                localASN1EncodableVector4.add(new DERSet(new DERBMPString(str1)));
                localASN1EncodableVector2.add(new DERSequence(localASN1EncodableVector4));
              }
              SafeBag localSafeBag1 = new SafeBag(pkcs8ShroudedKeyBag, localEncryptedPrivateKeyInfo.getDERObject(), new DERSet(localASN1EncodableVector2));
              localASN1EncodableVector1.add(localSafeBag1);
              break;
            }
            DERObjectIdentifier localDERObjectIdentifier1 = (DERObjectIdentifier)localEnumeration2.nextElement();
            ASN1EncodableVector localASN1EncodableVector5 = new ASN1EncodableVector();
            localASN1EncodableVector5.add(localDERObjectIdentifier1);
            localASN1EncodableVector5.add(new DERSet(localPKCS12BagAttributeCarrier1.getBagAttribute(localDERObjectIdentifier1)));
            i = 1;
            localASN1EncodableVector2.add(new DERSequence(localASN1EncodableVector5));
          }
          while (true)
          {
            ASN1EncodableVector localASN1EncodableVector7;
            PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier2;
            Enumeration localEnumeration4;
            try
            {
              String str2 = (String)localEnumeration3.nextElement();
              Certificate localCertificate3 = engineGetCertificate(str2);
              CertBag localCertBag1 = new CertBag(x509Certificate, new DEROctetString(localCertificate3.getEncoded()));
              localASN1EncodableVector7 = new ASN1EncodableVector();
              boolean bool2 = localCertificate3 instanceof PKCS12BagAttributeCarrier;
              j = 0;
              if (bool2)
              {
                localPKCS12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier)localCertificate3;
                DERBMPString localDERBMPString2 = (DERBMPString)localPKCS12BagAttributeCarrier2.getBagAttribute(pkcs_9_at_friendlyName);
                if ((localDERBMPString2 == null) || (!localDERBMPString2.getString().equals(str2)))
                  localPKCS12BagAttributeCarrier2.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(str2));
                if (localPKCS12BagAttributeCarrier2.getBagAttribute(pkcs_9_at_localKeyId) == null)
                  localPKCS12BagAttributeCarrier2.setBagAttribute(pkcs_9_at_localKeyId, createSubjectKeyId(localCertificate3.getPublicKey()));
                localEnumeration4 = localPKCS12BagAttributeCarrier2.getBagAttributeKeys();
                if (localEnumeration4.hasMoreElements());
              }
              else
              {
                if (j == 0)
                {
                  ASN1EncodableVector localASN1EncodableVector8 = new ASN1EncodableVector();
                  localASN1EncodableVector8.add(pkcs_9_at_localKeyId);
                  localASN1EncodableVector8.add(new DERSet(createSubjectKeyId(localCertificate3.getPublicKey())));
                  localASN1EncodableVector7.add(new DERSequence(localASN1EncodableVector8));
                  ASN1EncodableVector localASN1EncodableVector9 = new ASN1EncodableVector();
                  localASN1EncodableVector9.add(pkcs_9_at_friendlyName);
                  localASN1EncodableVector9.add(new DERSet(new DERBMPString(str2)));
                  localASN1EncodableVector7.add(new DERSequence(localASN1EncodableVector9));
                }
                SafeBag localSafeBag2 = new SafeBag(certBag, localCertBag1.getDERObject(), new DERSet(localASN1EncodableVector7));
                localASN1EncodableVector6.add(localSafeBag2);
                localHashtable.put(localCertificate3, localCertificate3);
              }
            }
            catch (CertificateEncodingException localCertificateEncodingException1)
            {
              throw new IOException("Error encoding certificate: " + localCertificateEncodingException1.toString());
            }
            DERObjectIdentifier localDERObjectIdentifier2 = (DERObjectIdentifier)localEnumeration4.nextElement();
            ASN1EncodableVector localASN1EncodableVector10 = new ASN1EncodableVector();
            localASN1EncodableVector10.add(localDERObjectIdentifier2);
            localASN1EncodableVector10.add(new DERSet(localPKCS12BagAttributeCarrier2.getBagAttribute(localDERObjectIdentifier2)));
            localASN1EncodableVector7.add(new DERSequence(localASN1EncodableVector10));
            int j = 1;
          }
          while (true)
          {
            ASN1EncodableVector localASN1EncodableVector11;
            int k;
            PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier3;
            Enumeration localEnumeration6;
            try
            {
              String str3 = (String)localEnumeration5.nextElement();
              Certificate localCertificate4 = (Certificate)this.certs.get(str3);
              if (this.keys.get(str3) != null)
                break;
              CertBag localCertBag2 = new CertBag(x509Certificate, new DEROctetString(localCertificate4.getEncoded()));
              localASN1EncodableVector11 = new ASN1EncodableVector();
              boolean bool3 = localCertificate4 instanceof PKCS12BagAttributeCarrier;
              k = 0;
              if (bool3)
              {
                localPKCS12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier)localCertificate4;
                DERBMPString localDERBMPString3 = (DERBMPString)localPKCS12BagAttributeCarrier3.getBagAttribute(pkcs_9_at_friendlyName);
                if ((localDERBMPString3 == null) || (!localDERBMPString3.getString().equals(str3)))
                  localPKCS12BagAttributeCarrier3.setBagAttribute(pkcs_9_at_friendlyName, new DERBMPString(str3));
                localEnumeration6 = localPKCS12BagAttributeCarrier3.getBagAttributeKeys();
                if (localEnumeration6.hasMoreElements());
              }
              else
              {
                if (k == 0)
                {
                  ASN1EncodableVector localASN1EncodableVector12 = new ASN1EncodableVector();
                  localASN1EncodableVector12.add(pkcs_9_at_friendlyName);
                  localASN1EncodableVector12.add(new DERSet(new DERBMPString(str3)));
                  localASN1EncodableVector11.add(new DERSequence(localASN1EncodableVector12));
                }
                SafeBag localSafeBag3 = new SafeBag(certBag, localCertBag2.getDERObject(), new DERSet(localASN1EncodableVector11));
                localASN1EncodableVector6.add(localSafeBag3);
                localHashtable.put(localCertificate4, localCertificate4);
              }
            }
            catch (CertificateEncodingException localCertificateEncodingException2)
            {
              throw new IOException("Error encoding certificate: " + localCertificateEncodingException2.toString());
            }
            DERObjectIdentifier localDERObjectIdentifier3 = (DERObjectIdentifier)localEnumeration6.nextElement();
            if (!localDERObjectIdentifier3.equals(BCKeyStore.pkcs_9_at_localKeyId))
            {
              ASN1EncodableVector localASN1EncodableVector13 = new ASN1EncodableVector();
              localASN1EncodableVector13.add(localDERObjectIdentifier3);
              localASN1EncodableVector13.add(new DERSet(localPKCS12BagAttributeCarrier3.getBagAttribute(localDERObjectIdentifier3)));
              localASN1EncodableVector11.add(new DERSequence(localASN1EncodableVector13));
              k = 1;
            }
          }
          try
          {
            CertId localCertId = (CertId)localEnumeration7.nextElement();
            Certificate localCertificate5 = (Certificate)this.chainCerts.get(localCertId);
            if (localHashtable.get(localCertificate5) != null)
              break label179;
            CertBag localCertBag3 = new CertBag(x509Certificate, new DEROctetString(localCertificate5.getEncoded()));
            localASN1EncodableVector14 = new ASN1EncodableVector();
            if ((localCertificate5 instanceof PKCS12BagAttributeCarrier))
            {
              localPKCS12BagAttributeCarrier4 = (PKCS12BagAttributeCarrier)localCertificate5;
              localEnumeration8 = localPKCS12BagAttributeCarrier4.getBagAttributeKeys();
              if (localEnumeration8.hasMoreElements());
            }
            else
            {
              SafeBag localSafeBag4 = new SafeBag(certBag, localCertBag3.getDERObject(), new DERSet(localASN1EncodableVector14));
              localASN1EncodableVector6.add(localSafeBag4);
            }
          }
          catch (CertificateEncodingException localCertificateEncodingException3)
          {
            throw new IOException("Error encoding certificate: " + localCertificateEncodingException3.toString());
          }
        }
        DERObjectIdentifier localDERObjectIdentifier4 = (DERObjectIdentifier)localEnumeration8.nextElement();
        if (localDERObjectIdentifier4.equals(BCKeyStore.pkcs_9_at_localKeyId))
          continue;
        ASN1EncodableVector localASN1EncodableVector15 = new ASN1EncodableVector();
        localASN1EncodableVector15.add(localDERObjectIdentifier4);
        localASN1EncodableVector15.add(new DERSet(localPKCS12BagAttributeCarrier4.getBagAttribute(localDERObjectIdentifier4)));
        localASN1EncodableVector14.add(new DERSequence(localASN1EncodableVector15));
        continue;
        label2000: localObject1 = new BEROutputStream(localByteArrayOutputStream);
      }
      catch (Exception localException)
      {
        throw new IOException("error constructing MAC: " + localException.toString());
      }
      label2045: Object localObject2 = new BEROutputStream(paramOutputStream);
    }
  }

  protected byte[] cryptData(boolean paramBoolean1, AlgorithmIdentifier paramAlgorithmIdentifier, char[] paramArrayOfChar, boolean paramBoolean2, byte[] paramArrayOfByte)
    throws IOException
  {
    String str = paramAlgorithmIdentifier.getObjectId().getId();
    PKCS12PBEParams localPKCS12PBEParams = new PKCS12PBEParams((ASN1Sequence)paramAlgorithmIdentifier.getParameters());
    PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramArrayOfChar);
    try
    {
      SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance(str, bcProvider);
      PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(localPKCS12PBEParams.getIV(), localPKCS12PBEParams.getIterations().intValue());
      JCEPBEKey localJCEPBEKey = (JCEPBEKey)localSecretKeyFactory.generateSecret(localPBEKeySpec);
      localJCEPBEKey.setTryWrongPKCS12Zero(paramBoolean2);
      Cipher localCipher = Cipher.getInstance(str, bcProvider);
      if (paramBoolean1);
      for (int i = 1; ; i = 2)
      {
        localCipher.init(i, localJCEPBEKey, localPBEParameterSpec);
        byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte);
        return arrayOfByte;
      }
    }
    catch (Exception localException)
    {
      throw new IOException("exception decrypting data - " + localException.toString());
    }
  }

  public Enumeration engineAliases()
  {
    Hashtable localHashtable = new Hashtable();
    Enumeration localEnumeration1 = this.certs.keys();
    Enumeration localEnumeration2;
    if (!localEnumeration1.hasMoreElements())
      localEnumeration2 = this.keys.keys();
    while (true)
    {
      if (!localEnumeration2.hasMoreElements())
      {
        return localHashtable.keys();
        localHashtable.put(localEnumeration1.nextElement(), "cert");
        break;
      }
      String str = (String)localEnumeration2.nextElement();
      if (localHashtable.get(str) == null)
        localHashtable.put(str, "key");
    }
  }

  public boolean engineContainsAlias(String paramString)
  {
    return (this.certs.get(paramString) != null) || (this.keys.get(paramString) != null);
  }

  public void engineDeleteEntry(String paramString)
    throws KeyStoreException
  {
    Key localKey = (Key)this.keys.remove(paramString);
    Certificate localCertificate = (Certificate)this.certs.remove(paramString);
    if (localCertificate != null)
      this.chainCerts.remove(new CertId(localCertificate.getPublicKey()));
    if (localKey != null)
    {
      String str = (String)this.localIds.remove(paramString);
      if (str != null)
        localCertificate = (Certificate)this.keyCerts.remove(str);
      if (localCertificate != null)
        this.chainCerts.remove(new CertId(localCertificate.getPublicKey()));
    }
    if ((localCertificate == null) && (localKey == null))
      throw new KeyStoreException("no such entry as " + paramString);
  }

  public Certificate engineGetCertificate(String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("null alias passed to getCertificate.");
    Certificate localCertificate = (Certificate)this.certs.get(paramString);
    if (localCertificate == null)
    {
      String str = (String)this.localIds.get(paramString);
      if (str != null)
        localCertificate = (Certificate)this.keyCerts.get(str);
    }
    else
    {
      return localCertificate;
    }
    return (Certificate)this.keyCerts.get(paramString);
  }

  public String engineGetCertificateAlias(Certificate paramCertificate)
  {
    Enumeration localEnumeration1 = this.certs.elements();
    Enumeration localEnumeration2 = this.certs.keys();
    Enumeration localEnumeration3;
    Enumeration localEnumeration4;
    if (!localEnumeration1.hasMoreElements())
    {
      localEnumeration3 = this.keyCerts.elements();
      localEnumeration4 = this.keyCerts.keys();
    }
    Certificate localCertificate2;
    String str2;
    do
    {
      if (!localEnumeration3.hasMoreElements())
      {
        return null;
        Certificate localCertificate1 = (Certificate)localEnumeration1.nextElement();
        String str1 = (String)localEnumeration2.nextElement();
        if (!localCertificate1.equals(paramCertificate))
          break;
        return str1;
      }
      localCertificate2 = (Certificate)localEnumeration3.nextElement();
      str2 = (String)localEnumeration4.nextElement();
    }
    while (!localCertificate2.equals(paramCertificate));
    return str2;
  }

  public Certificate[] engineGetCertificateChain(String paramString)
  {
    if (paramString == null)
      throw new IllegalArgumentException("null alias passed to getCertificateChain.");
    Certificate[] arrayOfCertificate;
    if (!engineIsKeyEntry(paramString))
      arrayOfCertificate = null;
    Object localObject1;
    Vector localVector;
    while (true)
    {
      return arrayOfCertificate;
      localObject1 = engineGetCertificate(paramString);
      if (localObject1 == null)
        break label340;
      localVector = new Vector();
      if (localObject1 != null)
        break;
      arrayOfCertificate = new Certificate[localVector.size()];
      for (int i = 0; i != arrayOfCertificate.length; i++)
        arrayOfCertificate[i] = ((Certificate)localVector.elementAt(i));
    }
    X509Certificate localX509Certificate1 = (X509Certificate)localObject1;
    byte[] arrayOfByte1 = localX509Certificate1.getExtensionValue(X509Extensions.AuthorityKeyIdentifier.getId());
    Object localObject2 = null;
    if (arrayOfByte1 != null);
    while (true)
      while (true)
      {
        Principal localPrincipal;
        Enumeration localEnumeration;
        try
        {
          AuthorityKeyIdentifier localAuthorityKeyIdentifier = new AuthorityKeyIdentifier((ASN1Sequence)new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(arrayOfByte1).readObject()).getOctets()).readObject());
          byte[] arrayOfByte2 = localAuthorityKeyIdentifier.getKeyIdentifier();
          localObject2 = null;
          if (arrayOfByte2 != null)
          {
            Hashtable localHashtable = this.chainCerts;
            CertId localCertId = new CertId(localAuthorityKeyIdentifier.getKeyIdentifier());
            localObject2 = (Certificate)localHashtable.get(localCertId);
          }
          if (localObject2 == null)
          {
            localPrincipal = localX509Certificate1.getIssuerDN();
            if (!localPrincipal.equals(localX509Certificate1.getSubjectDN()))
            {
              localEnumeration = this.chainCerts.keys();
              if (localEnumeration.hasMoreElements())
                break label284;
            }
          }
          localVector.addElement(localObject1);
          if (localObject2 == localObject1)
            break label335;
          localObject1 = localObject2;
        }
        catch (IOException localIOException)
        {
          throw new RuntimeException(localIOException.toString());
        }
        label284: X509Certificate localX509Certificate2 = (X509Certificate)this.chainCerts.get(localEnumeration.nextElement());
        if (localX509Certificate2.getSubjectDN().equals(localPrincipal))
          try
          {
            localX509Certificate1.verify(localX509Certificate2.getPublicKey());
            localObject2 = localX509Certificate2;
            continue;
            label335: localObject1 = null;
            break;
            label340: return null;
          }
          catch (Exception localException)
          {
          }
      }
  }

  public Date engineGetCreationDate(String paramString)
  {
    return new Date();
  }

  public Key engineGetKey(String paramString, char[] paramArrayOfChar)
    throws NoSuchAlgorithmException, UnrecoverableKeyException
  {
    if (paramString == null)
      throw new IllegalArgumentException("null alias passed to getKey.");
    return (Key)this.keys.get(paramString);
  }

  public boolean engineIsCertificateEntry(String paramString)
  {
    return (this.certs.get(paramString) != null) && (this.keys.get(paramString) == null);
  }

  public boolean engineIsKeyEntry(String paramString)
  {
    return this.keys.get(paramString) != null;
  }

  public void engineLoad(InputStream paramInputStream, char[] paramArrayOfChar)
    throws IOException
  {
    if (paramInputStream == null);
    Vector localVector;
    boolean bool1;
    label303: int i;
    ContentInfo[] arrayOfContentInfo;
    int j;
    int i6;
    int i7;
    do
    {
      return;
      if (paramArrayOfChar == null)
        throw new NullPointerException("No password supplied for PKCS#12 KeyStore.");
      BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
      localBufferedInputStream.mark(10);
      if (localBufferedInputStream.read() != 48)
        throw new IOException("stream does not represent a PKCS12 key store");
      localBufferedInputStream.reset();
      ASN1InputStream localASN1InputStream1 = new ASN1InputStream(localBufferedInputStream);
      ASN1Sequence localASN1Sequence1 = (ASN1Sequence)localASN1InputStream1.readObject();
      Pfx localPfx = new Pfx(localASN1Sequence1);
      ContentInfo localContentInfo = localPfx.getAuthSafe();
      localVector = new Vector();
      MacData localMacData1 = localPfx.getMacData();
      bool1 = false;
      if (localMacData1 != null)
      {
        MacData localMacData2 = localPfx.getMacData();
        DigestInfo localDigestInfo = localMacData2.getMac();
        AlgorithmIdentifier localAlgorithmIdentifier = localDigestInfo.getAlgorithmId();
        byte[] arrayOfByte1 = localMacData2.getSalt();
        int i8 = localMacData2.getIterationCount().intValue();
        byte[] arrayOfByte2 = ((ASN1OctetString)localContentInfo.getContent()).getOctets();
        try
        {
          byte[] arrayOfByte3 = calculatePbeMac(localAlgorithmIdentifier.getObjectId(), arrayOfByte1, i8, paramArrayOfChar, false, arrayOfByte2);
          arrayOfByte4 = localDigestInfo.getDigest();
          boolean bool3 = Arrays.constantTimeAreEqual(arrayOfByte3, arrayOfByte4);
          bool1 = false;
          if (bool3)
            break label303;
          if (paramArrayOfChar.length > 0)
            throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
        }
        catch (IOException localIOException)
        {
          byte[] arrayOfByte4;
          throw localIOException;
          if (!Arrays.constantTimeAreEqual(calculatePbeMac(localAlgorithmIdentifier.getObjectId(), arrayOfByte1, i8, paramArrayOfChar, true, arrayOfByte2), arrayOfByte4))
            throw new IOException("PKCS12 key store mac invalid - wrong password or corrupted file.");
        }
        catch (Exception localException2)
        {
          throw new IOException("error constructing MAC: " + localException2.toString());
        }
        bool1 = true;
      }
      this.keys = new IgnoresCaseHashtable(null);
      this.localIds = new Hashtable();
      boolean bool2 = localContentInfo.getContentType().equals(data);
      i = 0;
      if (bool2)
      {
        ASN1InputStream localASN1InputStream2 = new ASN1InputStream(((ASN1OctetString)localContentInfo.getContent()).getOctets());
        AuthenticatedSafe localAuthenticatedSafe = new AuthenticatedSafe((ASN1Sequence)localASN1InputStream2.readObject());
        arrayOfContentInfo = localAuthenticatedSafe.getContentInfo();
        j = 0;
        int k = arrayOfContentInfo.length;
        if (j != k)
          break;
      }
      this.certs = new IgnoresCaseHashtable(null);
      this.chainCerts = new Hashtable();
      this.keyCerts = new Hashtable();
      i6 = 0;
      i7 = localVector.size();
    }
    while (i6 == i7);
    SafeBag localSafeBag3 = (SafeBag)localVector.elementAt(i6);
    CertBag localCertBag = new CertBag((ASN1Sequence)localSafeBag3.getBagValue());
    if (!localCertBag.getCertId().equals(x509Certificate))
    {
      throw new RuntimeException("Unsupported certificate type: " + localCertBag.getCertId());
      ASN1Sequence localASN1Sequence2;
      int m;
      if (arrayOfContentInfo[j].getContentType().equals(data))
      {
        ASN1InputStream localASN1InputStream3 = new ASN1InputStream(((ASN1OctetString)arrayOfContentInfo[j].getContent()).getOctets());
        localASN1Sequence2 = (ASN1Sequence)localASN1InputStream3.readObject();
        m = 0;
        int n = localASN1Sequence2.size();
        if (m != n);
      }
      while (true)
      {
        j++;
        break;
        SafeBag localSafeBag1 = new SafeBag((ASN1Sequence)localASN1Sequence2.getObjectAt(m));
        PrivateKey localPrivateKey1;
        PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier1;
        String str1;
        ASN1OctetString localASN1OctetString1;
        Enumeration localEnumeration1;
        label706: String str2;
        if (localSafeBag1.getBagId().equals(pkcs8ShroudedKeyBag))
        {
          EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo1 = new EncryptedPrivateKeyInfo((ASN1Sequence)localSafeBag1.getBagValue());
          localPrivateKey1 = unwrapKey(localEncryptedPrivateKeyInfo1.getEncryptionAlgorithm(), localEncryptedPrivateKeyInfo1.getEncryptedData(), paramArrayOfChar, bool1);
          localPKCS12BagAttributeCarrier1 = (PKCS12BagAttributeCarrier)localPrivateKey1;
          ASN1Set localASN1Set1 = localSafeBag1.getBagAttributes();
          str1 = null;
          localASN1OctetString1 = null;
          if (localASN1Set1 != null)
          {
            localEnumeration1 = localSafeBag1.getBagAttributes().getObjects();
            if (localEnumeration1.hasMoreElements());
          }
          else
          {
            if (localASN1OctetString1 == null)
              break label944;
            str2 = new String(Hex.encode(localASN1OctetString1.getOctets()));
            if (str1 != null)
              break label929;
            this.keys.put(str2, localPrivateKey1);
          }
        }
        while (true)
        {
          m++;
          break;
          ASN1Sequence localASN1Sequence3 = (ASN1Sequence)localEnumeration1.nextElement();
          DERObjectIdentifier localDERObjectIdentifier1 = (DERObjectIdentifier)localASN1Sequence3.getObjectAt(0);
          ASN1Set localASN1Set2 = (ASN1Set)localASN1Sequence3.getObjectAt(1);
          int i1 = localASN1Set2.size();
          DERObject localDERObject1 = null;
          if (i1 > 0)
          {
            localDERObject1 = (DERObject)localASN1Set2.getObjectAt(0);
            DEREncodable localDEREncodable1 = localPKCS12BagAttributeCarrier1.getBagAttribute(localDERObjectIdentifier1);
            if (localDEREncodable1 != null)
            {
              if (!localDEREncodable1.getDERObject().equals(localDERObject1))
                throw new IOException("attempt to add existing attribute with different value");
            }
            else
              localPKCS12BagAttributeCarrier1.setBagAttribute(localDERObjectIdentifier1, localDERObject1);
          }
          if (localDERObjectIdentifier1.equals(pkcs_9_at_friendlyName))
          {
            str1 = ((DERBMPString)localDERObject1).getString();
            this.keys.put(str1, localPrivateKey1);
            break label706;
          }
          if (!localDERObjectIdentifier1.equals(pkcs_9_at_localKeyId))
            break label706;
          localASN1OctetString1 = (ASN1OctetString)localDERObject1;
          break label706;
          label929: this.localIds.put(str1, str2);
          continue;
          label944: i = 1;
          this.keys.put("unmarked", localPrivateKey1);
          continue;
          if (localSafeBag1.getBagId().equals(certBag))
          {
            localVector.addElement(localSafeBag1);
          }
          else
          {
            System.out.println("extra in data " + localSafeBag1.getBagId());
            System.out.println(ASN1Dump.dumpAsString(localSafeBag1));
          }
        }
        if (arrayOfContentInfo[j].getContentType().equals(encryptedData))
        {
          EncryptedData localEncryptedData = new EncryptedData((ASN1Sequence)arrayOfContentInfo[j].getContent());
          ASN1Sequence localASN1Sequence4 = (ASN1Sequence)ASN1Object.fromByteArray(cryptData(false, localEncryptedData.getEncryptionAlgorithm(), paramArrayOfChar, bool1, localEncryptedData.getContent().getOctets()));
          int i2 = 0;
          int i3 = localASN1Sequence4.size();
          if (i2 != i3)
          {
            SafeBag localSafeBag2 = new SafeBag((ASN1Sequence)localASN1Sequence4.getObjectAt(i2));
            if (localSafeBag2.getBagId().equals(certBag))
              localVector.addElement(localSafeBag2);
            while (true)
            {
              i2++;
              break;
              if (localSafeBag2.getBagId().equals(pkcs8ShroudedKeyBag))
              {
                EncryptedPrivateKeyInfo localEncryptedPrivateKeyInfo2 = new EncryptedPrivateKeyInfo((ASN1Sequence)localSafeBag2.getBagValue());
                PrivateKey localPrivateKey2 = unwrapKey(localEncryptedPrivateKeyInfo2.getEncryptionAlgorithm(), localEncryptedPrivateKeyInfo2.getEncryptedData(), paramArrayOfChar, bool1);
                PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier2 = (PKCS12BagAttributeCarrier)localPrivateKey2;
                String str3 = null;
                ASN1OctetString localASN1OctetString2 = null;
                Enumeration localEnumeration2 = localSafeBag2.getBagAttributes().getObjects();
                String str4;
                while (true)
                {
                  if (!localEnumeration2.hasMoreElements())
                  {
                    str4 = new String(Hex.encode(localASN1OctetString2.getOctets()));
                    if (str3 != null)
                      break label1444;
                    this.keys.put(str4, localPrivateKey2);
                    break;
                  }
                  ASN1Sequence localASN1Sequence5 = (ASN1Sequence)localEnumeration2.nextElement();
                  DERObjectIdentifier localDERObjectIdentifier2 = (DERObjectIdentifier)localASN1Sequence5.getObjectAt(0);
                  ASN1Set localASN1Set3 = (ASN1Set)localASN1Sequence5.getObjectAt(1);
                  int i4 = localASN1Set3.size();
                  DERObject localDERObject2 = null;
                  if (i4 > 0)
                  {
                    localDERObject2 = (DERObject)localASN1Set3.getObjectAt(0);
                    DEREncodable localDEREncodable2 = localPKCS12BagAttributeCarrier2.getBagAttribute(localDERObjectIdentifier2);
                    if (localDEREncodable2 != null)
                    {
                      if (!localDEREncodable2.getDERObject().equals(localDERObject2))
                        throw new IOException("attempt to add existing attribute with different value");
                    }
                    else
                      localPKCS12BagAttributeCarrier2.setBagAttribute(localDERObjectIdentifier2, localDERObject2);
                  }
                  if (localDERObjectIdentifier2.equals(pkcs_9_at_friendlyName))
                  {
                    str3 = ((DERBMPString)localDERObject2).getString();
                    this.keys.put(str3, localPrivateKey2);
                  }
                  else if (localDERObjectIdentifier2.equals(pkcs_9_at_localKeyId))
                  {
                    localASN1OctetString2 = (ASN1OctetString)localDERObject2;
                  }
                }
                label1444: this.localIds.put(str3, str4);
              }
              else if (localSafeBag2.getBagId().equals(keyBag))
              {
                PrivateKeyInfo localPrivateKeyInfo = new PrivateKeyInfo((ASN1Sequence)localSafeBag2.getBagValue());
                PrivateKey localPrivateKey3 = JDKKeyFactory.createPrivateKeyFromPrivateKeyInfo(localPrivateKeyInfo);
                PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier3 = (PKCS12BagAttributeCarrier)localPrivateKey3;
                String str5 = null;
                ASN1OctetString localASN1OctetString3 = null;
                Enumeration localEnumeration3 = localSafeBag2.getBagAttributes().getObjects();
                String str6;
                while (true)
                {
                  if (!localEnumeration3.hasMoreElements())
                  {
                    str6 = new String(Hex.encode(localASN1OctetString3.getOctets()));
                    if (str5 != null)
                      break label1735;
                    this.keys.put(str6, localPrivateKey3);
                    break;
                  }
                  ASN1Sequence localASN1Sequence6 = (ASN1Sequence)localEnumeration3.nextElement();
                  DERObjectIdentifier localDERObjectIdentifier3 = (DERObjectIdentifier)localASN1Sequence6.getObjectAt(0);
                  ASN1Set localASN1Set4 = (ASN1Set)localASN1Sequence6.getObjectAt(1);
                  int i5 = localASN1Set4.size();
                  DERObject localDERObject3 = null;
                  if (i5 > 0)
                  {
                    localDERObject3 = (DERObject)localASN1Set4.getObjectAt(0);
                    DEREncodable localDEREncodable3 = localPKCS12BagAttributeCarrier3.getBagAttribute(localDERObjectIdentifier3);
                    if (localDEREncodable3 != null)
                    {
                      if (!localDEREncodable3.getDERObject().equals(localDERObject3))
                        throw new IOException("attempt to add existing attribute with different value");
                    }
                    else
                      localPKCS12BagAttributeCarrier3.setBagAttribute(localDERObjectIdentifier3, localDERObject3);
                  }
                  if (localDERObjectIdentifier3.equals(pkcs_9_at_friendlyName))
                  {
                    str5 = ((DERBMPString)localDERObject3).getString();
                    this.keys.put(str5, localPrivateKey3);
                  }
                  else if (localDERObjectIdentifier3.equals(pkcs_9_at_localKeyId))
                  {
                    localASN1OctetString3 = (ASN1OctetString)localDERObject3;
                  }
                }
                label1735: this.localIds.put(str5, str6);
              }
              else
              {
                System.out.println("extra in encryptedData " + localSafeBag2.getBagId());
                System.out.println(ASN1Dump.dumpAsString(localSafeBag2));
              }
            }
          }
        }
        else
        {
          System.out.println("extra " + arrayOfContentInfo[j].getContentType().getId());
          System.out.println("extra " + ASN1Dump.dumpAsString(arrayOfContentInfo[j].getContent()));
        }
      }
    }
    while (true)
    {
      Certificate localCertificate;
      String str7;
      ASN1OctetString localASN1OctetString4;
      Enumeration localEnumeration4;
      try
      {
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(((ASN1OctetString)localCertBag.getCertValue()).getOctets());
        localCertificate = this.certFact.generateCertificate(localByteArrayInputStream);
        ASN1Set localASN1Set5 = localSafeBag3.getBagAttributes();
        str7 = null;
        localASN1OctetString4 = null;
        if (localASN1Set5 != null)
        {
          localEnumeration4 = localSafeBag3.getBagAttributes().getObjects();
          if (localEnumeration4.hasMoreElements());
        }
        else
        {
          this.chainCerts.put(new CertId(localCertificate.getPublicKey()), localCertificate);
          if (i == 0)
            break label2197;
          if (this.keyCerts.isEmpty())
          {
            String str9 = new String(Hex.encode(createSubjectKeyId(localCertificate.getPublicKey()).getKeyIdentifier()));
            this.keyCerts.put(str9, localCertificate);
            this.keys.put(str9, this.keys.remove("unmarked"));
          }
          i6++;
        }
      }
      catch (Exception localException1)
      {
        throw new RuntimeException(localException1.toString());
      }
      ASN1Sequence localASN1Sequence7 = (ASN1Sequence)localEnumeration4.nextElement();
      DERObjectIdentifier localDERObjectIdentifier4 = (DERObjectIdentifier)localASN1Sequence7.getObjectAt(0);
      DERObject localDERObject4 = (DERObject)((ASN1Set)localASN1Sequence7.getObjectAt(1)).getObjectAt(0);
      if ((localCertificate instanceof PKCS12BagAttributeCarrier))
      {
        PKCS12BagAttributeCarrier localPKCS12BagAttributeCarrier4 = (PKCS12BagAttributeCarrier)localCertificate;
        DEREncodable localDEREncodable4 = localPKCS12BagAttributeCarrier4.getBagAttribute(localDERObjectIdentifier4);
        if (localDEREncodable4 != null)
        {
          if (!localDEREncodable4.getDERObject().equals(localDERObject4))
            throw new IOException("attempt to add existing attribute with different value");
        }
        else
          localPKCS12BagAttributeCarrier4.setBagAttribute(localDERObjectIdentifier4, localDERObject4);
      }
      if (localDERObjectIdentifier4.equals(pkcs_9_at_friendlyName))
      {
        str7 = ((DERBMPString)localDERObject4).getString();
      }
      else if (localDERObjectIdentifier4.equals(pkcs_9_at_localKeyId))
      {
        localASN1OctetString4 = (ASN1OctetString)localDERObject4;
        continue;
        label2197: if (localASN1OctetString4 != null)
        {
          String str8 = new String(Hex.encode(localASN1OctetString4.getOctets()));
          this.keyCerts.put(str8, localCertificate);
        }
        if (str7 != null)
          this.certs.put(str7, localCertificate);
      }
    }
  }

  public void engineSetCertificateEntry(String paramString, Certificate paramCertificate)
    throws KeyStoreException
  {
    if (this.keys.get(paramString) != null)
      throw new KeyStoreException("There is a key entry with the name " + paramString + ".");
    this.certs.put(paramString, paramCertificate);
    this.chainCerts.put(new CertId(paramCertificate.getPublicKey()), paramCertificate);
  }

  public void engineSetKeyEntry(String paramString, Key paramKey, char[] paramArrayOfChar, Certificate[] paramArrayOfCertificate)
    throws KeyStoreException
  {
    if (((paramKey instanceof PrivateKey)) && (paramArrayOfCertificate == null))
      throw new KeyStoreException("no certificate chain for private key");
    if (this.keys.get(paramString) != null)
      engineDeleteEntry(paramString);
    this.keys.put(paramString, paramKey);
    this.certs.put(paramString, paramArrayOfCertificate[0]);
    for (int i = 0; ; i++)
    {
      if (i == paramArrayOfCertificate.length)
        return;
      this.chainCerts.put(new CertId(paramArrayOfCertificate[i].getPublicKey()), paramArrayOfCertificate[i]);
    }
  }

  public void engineSetKeyEntry(String paramString, byte[] paramArrayOfByte, Certificate[] paramArrayOfCertificate)
    throws KeyStoreException
  {
    throw new RuntimeException("operation not supported");
  }

  public int engineSize()
  {
    Hashtable localHashtable = new Hashtable();
    Enumeration localEnumeration1 = this.certs.keys();
    Enumeration localEnumeration2;
    if (!localEnumeration1.hasMoreElements())
      localEnumeration2 = this.keys.keys();
    while (true)
    {
      if (!localEnumeration2.hasMoreElements())
      {
        return localHashtable.size();
        localHashtable.put(localEnumeration1.nextElement(), "cert");
        break;
      }
      String str = (String)localEnumeration2.nextElement();
      if (localHashtable.get(str) == null)
        localHashtable.put(str, "key");
    }
  }

  public void engineStore(OutputStream paramOutputStream, char[] paramArrayOfChar)
    throws IOException
  {
    doStore(paramOutputStream, paramArrayOfChar, false);
  }

  public void engineStore(KeyStore.LoadStoreParameter paramLoadStoreParameter)
    throws IOException, NoSuchAlgorithmException, CertificateException
  {
    if (paramLoadStoreParameter == null)
      throw new IllegalArgumentException("'param' arg cannot be null");
    if (!(paramLoadStoreParameter instanceof JDKPKCS12StoreParameter))
      throw new IllegalArgumentException("No support for 'param' of type " + paramLoadStoreParameter.getClass().getName());
    JDKPKCS12StoreParameter localJDKPKCS12StoreParameter = (JDKPKCS12StoreParameter)paramLoadStoreParameter;
    KeyStore.ProtectionParameter localProtectionParameter = paramLoadStoreParameter.getProtectionParameter();
    if (localProtectionParameter == null);
    for (char[] arrayOfChar = (char[])null; ; arrayOfChar = ((KeyStore.PasswordProtection)localProtectionParameter).getPassword())
    {
      doStore(localJDKPKCS12StoreParameter.getOutputStream(), arrayOfChar, localJDKPKCS12StoreParameter.isUseDEREncoding());
      return;
      if (!(localProtectionParameter instanceof KeyStore.PasswordProtection))
        break;
    }
    throw new IllegalArgumentException("No support for protection parameter of type " + localProtectionParameter.getClass().getName());
  }

  public void setRandom(SecureRandom paramSecureRandom)
  {
    this.random = paramSecureRandom;
  }

  protected PrivateKey unwrapKey(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfByte, char[] paramArrayOfChar, boolean paramBoolean)
    throws IOException
  {
    String str = paramAlgorithmIdentifier.getObjectId().getId();
    PKCS12PBEParams localPKCS12PBEParams = new PKCS12PBEParams((ASN1Sequence)paramAlgorithmIdentifier.getParameters());
    PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramArrayOfChar);
    try
    {
      SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance(str, bcProvider);
      PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(localPKCS12PBEParams.getIV(), localPKCS12PBEParams.getIterations().intValue());
      SecretKey localSecretKey = localSecretKeyFactory.generateSecret(localPBEKeySpec);
      ((JCEPBEKey)localSecretKey).setTryWrongPKCS12Zero(paramBoolean);
      Cipher localCipher = Cipher.getInstance(str, bcProvider);
      localCipher.init(4, localSecretKey, localPBEParameterSpec);
      PrivateKey localPrivateKey = (PrivateKey)localCipher.unwrap(paramArrayOfByte, "", 2);
      return localPrivateKey;
    }
    catch (Exception localException)
    {
      throw new IOException("exception unwrapping private key - " + localException.toString());
    }
  }

  protected byte[] wrapKey(String paramString, Key paramKey, PKCS12PBEParams paramPKCS12PBEParams, char[] paramArrayOfChar)
    throws IOException
  {
    PBEKeySpec localPBEKeySpec = new PBEKeySpec(paramArrayOfChar);
    try
    {
      SecretKeyFactory localSecretKeyFactory = SecretKeyFactory.getInstance(paramString, bcProvider);
      PBEParameterSpec localPBEParameterSpec = new PBEParameterSpec(paramPKCS12PBEParams.getIV(), paramPKCS12PBEParams.getIterations().intValue());
      Cipher localCipher = Cipher.getInstance(paramString, bcProvider);
      localCipher.init(3, localSecretKeyFactory.generateSecret(localPBEKeySpec), localPBEParameterSpec);
      byte[] arrayOfByte = localCipher.wrap(paramKey);
      return arrayOfByte;
    }
    catch (Exception localException)
    {
      throw new IOException("exception encrypting data - " + localException.toString());
    }
  }

  public static class BCPKCS12KeyStore extends JDKPKCS12KeyStore
  {
    public BCPKCS12KeyStore()
    {
      super(pbeWithSHAAnd3_KeyTripleDES_CBC, pbewithSHAAnd40BitRC2_CBC);
    }
  }

  public static class BCPKCS12KeyStore3DES extends JDKPKCS12KeyStore
  {
    public BCPKCS12KeyStore3DES()
    {
      super(pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd3_KeyTripleDES_CBC);
    }
  }

  private class CertId
  {
    byte[] id;

    CertId(PublicKey arg2)
    {
      PublicKey localPublicKey;
      this.id = JDKPKCS12KeyStore.this.createSubjectKeyId(localPublicKey).getKeyIdentifier();
    }

    CertId(byte[] arg2)
    {
      Object localObject;
      this.id = localObject;
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this)
        return true;
      if (!(paramObject instanceof CertId))
        return false;
      CertId localCertId = (CertId)paramObject;
      return Arrays.areEqual(this.id, localCertId.id);
    }

    public int hashCode()
    {
      return Arrays.hashCode(this.id);
    }
  }

  public static class DefPKCS12KeyStore extends JDKPKCS12KeyStore
  {
    public DefPKCS12KeyStore()
    {
      super(pbeWithSHAAnd3_KeyTripleDES_CBC, pbewithSHAAnd40BitRC2_CBC);
    }
  }

  public static class DefPKCS12KeyStore3DES extends JDKPKCS12KeyStore
  {
    public DefPKCS12KeyStore3DES()
    {
      super(pbeWithSHAAnd3_KeyTripleDES_CBC, pbeWithSHAAnd3_KeyTripleDES_CBC);
    }
  }

  private static class IgnoresCaseHashtable
  {
    private Hashtable keys = new Hashtable();
    private Hashtable orig = new Hashtable();

    public Enumeration elements()
    {
      return this.orig.elements();
    }

    public Object get(String paramString)
    {
      String str = (String)this.keys.get(Strings.toLowerCase(paramString));
      if (str == null)
        return null;
      return this.orig.get(str);
    }

    public Enumeration keys()
    {
      return this.orig.keys();
    }

    public void put(String paramString, Object paramObject)
    {
      String str1 = Strings.toLowerCase(paramString);
      String str2 = (String)this.keys.get(str1);
      if (str2 != null)
        this.orig.remove(str2);
      this.keys.put(str1, paramString);
      this.orig.put(paramString, paramObject);
    }

    public Object remove(String paramString)
    {
      String str = (String)this.keys.remove(Strings.toLowerCase(paramString));
      if (str == null)
        return null;
      return this.orig.remove(str);
    }
  }
}