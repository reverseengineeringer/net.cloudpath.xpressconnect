package org.bouncycastle2.jce;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.ASN1Set;
import org.bouncycastle2.asn1.DERBitString;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERInteger;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.CertificationRequest;
import org.bouncycastle2.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle2.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle2.asn1.x509.X509Name;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle2.jce.provider.BouncyCastleProvider;

public class PKCS10CertificationRequest extends CertificationRequest
{
  private static Hashtable algorithms = new Hashtable();
  private static Hashtable keyAlgorithms;
  private static Set noParams;
  private static Hashtable oids;
  private static Hashtable params = new Hashtable();

  static
  {
    keyAlgorithms = new Hashtable();
    oids = new Hashtable();
    noParams = new HashSet();
    algorithms.put("MD2WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
    algorithms.put("MD2WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.2"));
    algorithms.put("MD5WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
    algorithms.put("MD5WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
    algorithms.put("RSAWITHMD5", new DERObjectIdentifier("1.2.840.113549.1.1.4"));
    algorithms.put("SHA1WITHRSAENCRYPTION", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
    algorithms.put("SHA1WITHRSA", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
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
    algorithms.put("RSAWITHSHA1", new DERObjectIdentifier("1.2.840.113549.1.1.5"));
    algorithms.put("RIPEMD128WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
    algorithms.put("RIPEMD128WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd128);
    algorithms.put("RIPEMD160WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
    algorithms.put("RIPEMD160WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd160);
    algorithms.put("RIPEMD256WITHRSAENCRYPTION", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
    algorithms.put("RIPEMD256WITHRSA", TeleTrusTObjectIdentifiers.rsaSignatureWithripemd256);
    algorithms.put("SHA1WITHDSA", new DERObjectIdentifier("1.2.840.10040.4.3"));
    algorithms.put("DSAWITHSHA1", new DERObjectIdentifier("1.2.840.10040.4.3"));
    algorithms.put("SHA224WITHDSA", NISTObjectIdentifiers.dsa_with_sha224);
    algorithms.put("SHA256WITHDSA", NISTObjectIdentifiers.dsa_with_sha256);
    algorithms.put("SHA384WITHDSA", NISTObjectIdentifiers.dsa_with_sha384);
    algorithms.put("SHA512WITHDSA", NISTObjectIdentifiers.dsa_with_sha512);
    algorithms.put("SHA1WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA1);
    algorithms.put("SHA224WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA224);
    algorithms.put("SHA256WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA256);
    algorithms.put("SHA384WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA384);
    algorithms.put("SHA512WITHECDSA", X9ObjectIdentifiers.ecdsa_with_SHA512);
    algorithms.put("ECDSAWITHSHA1", X9ObjectIdentifiers.ecdsa_with_SHA1);
    algorithms.put("GOST3411WITHGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
    algorithms.put("GOST3410WITHGOST3411", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94);
    algorithms.put("GOST3411WITHECGOST3410", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    algorithms.put("GOST3411WITHECGOST3410-2001", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    algorithms.put("GOST3411WITHGOST3410-2001", CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001);
    oids.put(new DERObjectIdentifier("1.2.840.113549.1.1.5"), "SHA1WITHRSA");
    oids.put(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224WITHRSA");
    oids.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256WITHRSA");
    oids.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384WITHRSA");
    oids.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512WITHRSA");
    oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94, "GOST3411WITHGOST3410");
    oids.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001, "GOST3411WITHECGOST3410");
    oids.put(new DERObjectIdentifier("1.2.840.113549.1.1.4"), "MD5WITHRSA");
    oids.put(new DERObjectIdentifier("1.2.840.113549.1.1.2"), "MD2WITHRSA");
    oids.put(new DERObjectIdentifier("1.2.840.10040.4.3"), "SHA1WITHDSA");
    oids.put(X9ObjectIdentifiers.ecdsa_with_SHA1, "SHA1WITHECDSA");
    oids.put(X9ObjectIdentifiers.ecdsa_with_SHA224, "SHA224WITHECDSA");
    oids.put(X9ObjectIdentifiers.ecdsa_with_SHA256, "SHA256WITHECDSA");
    oids.put(X9ObjectIdentifiers.ecdsa_with_SHA384, "SHA384WITHECDSA");
    oids.put(X9ObjectIdentifiers.ecdsa_with_SHA512, "SHA512WITHECDSA");
    oids.put(OIWObjectIdentifiers.sha1WithRSA, "SHA1WITHRSA");
    oids.put(OIWObjectIdentifiers.dsaWithSHA1, "SHA1WITHDSA");
    oids.put(NISTObjectIdentifiers.dsa_with_sha224, "SHA224WITHDSA");
    oids.put(NISTObjectIdentifiers.dsa_with_sha256, "SHA256WITHDSA");
    keyAlgorithms.put(PKCSObjectIdentifiers.rsaEncryption, "RSA");
    keyAlgorithms.put(X9ObjectIdentifiers.id_dsa, "DSA");
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA1);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA224);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA256);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA384);
    noParams.add(X9ObjectIdentifiers.ecdsa_with_SHA512);
    noParams.add(X9ObjectIdentifiers.id_dsa_with_sha1);
    noParams.add(NISTObjectIdentifiers.dsa_with_sha224);
    noParams.add(NISTObjectIdentifiers.dsa_with_sha256);
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

  public PKCS10CertificationRequest(String paramString, X500Principal paramX500Principal, PublicKey paramPublicKey, ASN1Set paramASN1Set, PrivateKey paramPrivateKey)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    this(paramString, convertName(paramX500Principal), paramPublicKey, paramASN1Set, paramPrivateKey, BouncyCastleProvider.PROVIDER_NAME);
  }

  public PKCS10CertificationRequest(String paramString1, X500Principal paramX500Principal, PublicKey paramPublicKey, ASN1Set paramASN1Set, PrivateKey paramPrivateKey, String paramString2)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    this(paramString1, convertName(paramX500Principal), paramPublicKey, paramASN1Set, paramPrivateKey, paramString2);
  }

  public PKCS10CertificationRequest(String paramString, X509Name paramX509Name, PublicKey paramPublicKey, ASN1Set paramASN1Set, PrivateKey paramPrivateKey)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    this(paramString, paramX509Name, paramPublicKey, paramASN1Set, paramPrivateKey, BouncyCastleProvider.PROVIDER_NAME);
  }

  // ERROR //
  public PKCS10CertificationRequest(String paramString1, X509Name paramX509Name, PublicKey paramPublicKey, ASN1Set paramASN1Set, PrivateKey paramPrivateKey, String paramString2)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 288	org/bouncycastle2/asn1/pkcs/CertificationRequest:<init>	()V
    //   4: aload_1
    //   5: invokestatic 294	org/bouncycastle2/util/Strings:toUpperCase	(Ljava/lang/String;)Ljava/lang/String;
    //   8: astore 7
    //   10: getstatic 20	org/bouncycastle2/jce/PKCS10CertificationRequest:algorithms	Ljava/util/Hashtable;
    //   13: aload 7
    //   15: invokevirtual 298	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   18: checkcast 35	org/bouncycastle2/asn1/DERObjectIdentifier
    //   21: astore 8
    //   23: aload 8
    //   25: ifnonnull +14 -> 39
    //   28: new 35	org/bouncycastle2/asn1/DERObjectIdentifier
    //   31: dup
    //   32: aload 7
    //   34: invokespecial 40	org/bouncycastle2/asn1/DERObjectIdentifier:<init>	(Ljava/lang/String;)V
    //   37: astore 8
    //   39: aload_2
    //   40: ifnonnull +27 -> 67
    //   43: new 300	java/lang/IllegalArgumentException
    //   46: dup
    //   47: ldc_w 302
    //   50: invokespecial 303	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   53: athrow
    //   54: astore 12
    //   56: new 300	java/lang/IllegalArgumentException
    //   59: dup
    //   60: ldc_w 305
    //   63: invokespecial 303	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   66: athrow
    //   67: aload_3
    //   68: ifnonnull +14 -> 82
    //   71: new 300	java/lang/IllegalArgumentException
    //   74: dup
    //   75: ldc_w 307
    //   78: invokespecial 303	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   81: athrow
    //   82: getstatic 31	org/bouncycastle2/jce/PKCS10CertificationRequest:noParams	Ljava/util/Set;
    //   85: aload 8
    //   87: invokeinterface 310 2 0
    //   92: ifeq +99 -> 191
    //   95: aload_0
    //   96: new 234	org/bouncycastle2/asn1/x509/AlgorithmIdentifier
    //   99: dup
    //   100: aload 8
    //   102: invokespecial 313	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:<init>	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;)V
    //   105: putfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   108: aload_0
    //   109: new 319	org/bouncycastle2/asn1/pkcs/CertificationRequestInfo
    //   112: dup
    //   113: aload_2
    //   114: new 321	org/bouncycastle2/asn1/x509/SubjectPublicKeyInfo
    //   117: dup
    //   118: aload_3
    //   119: invokeinterface 327 1 0
    //   124: invokestatic 333	org/bouncycastle2/asn1/ASN1Object:fromByteArray	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   127: checkcast 335	org/bouncycastle2/asn1/ASN1Sequence
    //   130: invokespecial 338	org/bouncycastle2/asn1/x509/SubjectPublicKeyInfo:<init>	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   133: aload 4
    //   135: invokespecial 341	org/bouncycastle2/asn1/pkcs/CertificationRequestInfo:<init>	(Lorg/bouncycastle2/asn1/x509/X509Name;Lorg/bouncycastle2/asn1/x509/SubjectPublicKeyInfo;Lorg/bouncycastle2/asn1/ASN1Set;)V
    //   138: putfield 345	org/bouncycastle2/jce/PKCS10CertificationRequest:reqInfo	Lorg/bouncycastle2/asn1/pkcs/CertificationRequestInfo;
    //   141: aload 6
    //   143: ifnonnull +118 -> 261
    //   146: aload_1
    //   147: invokestatic 351	java/security/Signature:getInstance	(Ljava/lang/String;)Ljava/security/Signature;
    //   150: astore 10
    //   152: aload 10
    //   154: aload 5
    //   156: invokevirtual 355	java/security/Signature:initSign	(Ljava/security/PrivateKey;)V
    //   159: aload 10
    //   161: aload_0
    //   162: getfield 345	org/bouncycastle2/jce/PKCS10CertificationRequest:reqInfo	Lorg/bouncycastle2/asn1/pkcs/CertificationRequestInfo;
    //   165: ldc_w 357
    //   168: invokevirtual 360	org/bouncycastle2/asn1/pkcs/CertificationRequestInfo:getEncoded	(Ljava/lang/String;)[B
    //   171: invokevirtual 364	java/security/Signature:update	([B)V
    //   174: aload_0
    //   175: new 366	org/bouncycastle2/asn1/DERBitString
    //   178: dup
    //   179: aload 10
    //   181: invokevirtual 369	java/security/Signature:sign	()[B
    //   184: invokespecial 371	org/bouncycastle2/asn1/DERBitString:<init>	([B)V
    //   187: putfield 375	org/bouncycastle2/jce/PKCS10CertificationRequest:sigBits	Lorg/bouncycastle2/asn1/DERBitString;
    //   190: return
    //   191: getstatic 22	org/bouncycastle2/jce/PKCS10CertificationRequest:params	Ljava/util/Hashtable;
    //   194: aload 7
    //   196: invokevirtual 378	java/util/Hashtable:containsKey	(Ljava/lang/Object;)Z
    //   199: ifeq +30 -> 229
    //   202: aload_0
    //   203: new 234	org/bouncycastle2/asn1/x509/AlgorithmIdentifier
    //   206: dup
    //   207: aload 8
    //   209: getstatic 22	org/bouncycastle2/jce/PKCS10CertificationRequest:params	Ljava/util/Hashtable;
    //   212: aload 7
    //   214: invokevirtual 298	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   217: checkcast 380	org/bouncycastle2/asn1/DEREncodable
    //   220: invokespecial 243	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:<init>	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   223: putfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   226: goto -118 -> 108
    //   229: aload_0
    //   230: new 234	org/bouncycastle2/asn1/x509/AlgorithmIdentifier
    //   233: dup
    //   234: aload 8
    //   236: getstatic 384	org/bouncycastle2/asn1/DERNull:INSTANCE	Lorg/bouncycastle2/asn1/DERNull;
    //   239: invokespecial 243	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:<init>	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   242: putfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   245: goto -137 -> 108
    //   248: astore 9
    //   250: new 300	java/lang/IllegalArgumentException
    //   253: dup
    //   254: ldc_w 386
    //   257: invokespecial 303	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   260: athrow
    //   261: aload_1
    //   262: aload 6
    //   264: invokestatic 389	java/security/Signature:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;
    //   267: astore 10
    //   269: goto -117 -> 152
    //   272: astore 11
    //   274: new 300	java/lang/IllegalArgumentException
    //   277: dup
    //   278: new 391	java/lang/StringBuilder
    //   281: dup
    //   282: ldc_w 393
    //   285: invokespecial 394	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   288: aload 11
    //   290: invokevirtual 398	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   293: invokevirtual 402	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   296: invokespecial 303	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   299: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   28	39	54	java/lang/Exception
    //   108	141	248	java/io/IOException
    //   159	174	272	java/lang/Exception
  }

  public PKCS10CertificationRequest(ASN1Sequence paramASN1Sequence)
  {
    super(paramASN1Sequence);
  }

  public PKCS10CertificationRequest(byte[] paramArrayOfByte)
  {
    super(toDERSequence(paramArrayOfByte));
  }

  private static X509Name convertName(X500Principal paramX500Principal)
  {
    try
    {
      X509Principal localX509Principal = new X509Principal(paramX500Principal.getEncoded());
      return localX509Principal;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalArgumentException("can't convert name");
  }

  private static RSASSAPSSparams creatPSSParams(AlgorithmIdentifier paramAlgorithmIdentifier, int paramInt)
  {
    return new RSASSAPSSparams(paramAlgorithmIdentifier, new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, paramAlgorithmIdentifier), new DERInteger(paramInt), new DERInteger(1));
  }

  private static String getDigestAlgName(DERObjectIdentifier paramDERObjectIdentifier)
  {
    if (PKCSObjectIdentifiers.md5.equals(paramDERObjectIdentifier))
      return "MD5";
    if (OIWObjectIdentifiers.idSHA1.equals(paramDERObjectIdentifier))
      return "SHA1";
    if (NISTObjectIdentifiers.id_sha224.equals(paramDERObjectIdentifier))
      return "SHA224";
    if (NISTObjectIdentifiers.id_sha256.equals(paramDERObjectIdentifier))
      return "SHA256";
    if (NISTObjectIdentifiers.id_sha384.equals(paramDERObjectIdentifier))
      return "SHA384";
    if (NISTObjectIdentifiers.id_sha512.equals(paramDERObjectIdentifier))
      return "SHA512";
    if (TeleTrusTObjectIdentifiers.ripemd128.equals(paramDERObjectIdentifier))
      return "RIPEMD128";
    if (TeleTrusTObjectIdentifiers.ripemd160.equals(paramDERObjectIdentifier))
      return "RIPEMD160";
    if (TeleTrusTObjectIdentifiers.ripemd256.equals(paramDERObjectIdentifier))
      return "RIPEMD256";
    if (CryptoProObjectIdentifiers.gostR3411.equals(paramDERObjectIdentifier))
      return "GOST3411";
    return paramDERObjectIdentifier.getId();
  }

  static String getSignatureName(AlgorithmIdentifier paramAlgorithmIdentifier)
  {
    DEREncodable localDEREncodable = paramAlgorithmIdentifier.getParameters();
    if ((localDEREncodable != null) && (!DERNull.INSTANCE.equals(localDEREncodable)) && (paramAlgorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.id_RSASSA_PSS)))
      return getDigestAlgName(RSASSAPSSparams.getInstance(localDEREncodable).getHashAlgorithm().getObjectId()) + "withRSAandMGF1";
    return paramAlgorithmIdentifier.getObjectId().getId();
  }

  // ERROR //
  private void setSignatureParameters(java.security.Signature paramSignature, DEREncodable paramDEREncodable)
    throws NoSuchAlgorithmException, SignatureException, InvalidKeyException
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnull +62 -> 63
    //   4: getstatic 384	org/bouncycastle2/asn1/DERNull:INSTANCE	Lorg/bouncycastle2/asn1/DERNull;
    //   7: aload_2
    //   8: invokevirtual 481	org/bouncycastle2/asn1/DERNull:equals	(Ljava/lang/Object;)Z
    //   11: ifne +52 -> 63
    //   14: aload_1
    //   15: invokevirtual 512	java/security/Signature:getAlgorithm	()Ljava/lang/String;
    //   18: aload_1
    //   19: invokevirtual 516	java/security/Signature:getProvider	()Ljava/security/Provider;
    //   22: invokestatic 521	java/security/AlgorithmParameters:getInstance	(Ljava/lang/String;Ljava/security/Provider;)Ljava/security/AlgorithmParameters;
    //   25: astore_3
    //   26: aload_3
    //   27: aload_2
    //   28: invokeinterface 525 1 0
    //   33: invokevirtual 530	org/bouncycastle2/asn1/DERObject:getDEREncoded	()[B
    //   36: invokevirtual 533	java/security/AlgorithmParameters:init	([B)V
    //   39: aload_1
    //   40: invokevirtual 512	java/security/Signature:getAlgorithm	()Ljava/lang/String;
    //   43: ldc_w 535
    //   46: invokevirtual 539	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   49: ifeq +14 -> 63
    //   52: aload_1
    //   53: aload_3
    //   54: ldc_w 541
    //   57: invokevirtual 545	java/security/AlgorithmParameters:getParameterSpec	(Ljava/lang/Class;)Ljava/security/spec/AlgorithmParameterSpec;
    //   60: invokevirtual 549	java/security/Signature:setParameter	(Ljava/security/spec/AlgorithmParameterSpec;)V
    //   63: return
    //   64: astore 4
    //   66: new 268	java/security/SignatureException
    //   69: dup
    //   70: new 391	java/lang/StringBuilder
    //   73: dup
    //   74: ldc_w 551
    //   77: invokespecial 394	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   80: aload 4
    //   82: invokevirtual 554	java/io/IOException:getMessage	()Ljava/lang/String;
    //   85: invokevirtual 505	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: invokevirtual 402	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   91: invokespecial 555	java/security/SignatureException:<init>	(Ljava/lang/String;)V
    //   94: athrow
    //   95: astore 5
    //   97: new 268	java/security/SignatureException
    //   100: dup
    //   101: new 391	java/lang/StringBuilder
    //   104: dup
    //   105: ldc_w 557
    //   108: invokespecial 394	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   111: aload 5
    //   113: invokevirtual 558	java/security/GeneralSecurityException:getMessage	()Ljava/lang/String;
    //   116: invokevirtual 505	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: invokevirtual 402	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   122: invokespecial 555	java/security/SignatureException:<init>	(Ljava/lang/String;)V
    //   125: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   26	39	64	java/io/IOException
    //   52	63	95	java/security/GeneralSecurityException
  }

  private static ASN1Sequence toDERSequence(byte[] paramArrayOfByte)
  {
    try
    {
      ASN1Sequence localASN1Sequence = (ASN1Sequence)new ASN1InputStream(paramArrayOfByte).readObject();
      return localASN1Sequence;
    }
    catch (Exception localException)
    {
    }
    throw new IllegalArgumentException("badly encoded request");
  }

  public byte[] getEncoded()
  {
    try
    {
      byte[] arrayOfByte = getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException.toString());
    }
  }

  public PublicKey getPublicKey()
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException
  {
    return getPublicKey(BouncyCastleProvider.PROVIDER_NAME);
  }

  public PublicKey getPublicKey(String paramString)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException
  {
    SubjectPublicKeyInfo localSubjectPublicKeyInfo = this.reqInfo.getSubjectPublicKeyInfo();
    X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(localSubjectPublicKeyInfo).getBytes());
    AlgorithmIdentifier localAlgorithmIdentifier = localSubjectPublicKeyInfo.getAlgorithmId();
    if (paramString == null);
    try
    {
      return KeyFactory.getInstance(localAlgorithmIdentifier.getObjectId().getId()).generatePublic(localX509EncodedKeySpec);
      PublicKey localPublicKey = KeyFactory.getInstance(localAlgorithmIdentifier.getObjectId().getId(), paramString).generatePublic(localX509EncodedKeySpec);
      return localPublicKey;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      if (keyAlgorithms.get(localAlgorithmIdentifier.getObjectId()) != null)
      {
        String str = (String)keyAlgorithms.get(localAlgorithmIdentifier.getObjectId());
        if (paramString == null)
          return KeyFactory.getInstance(str).generatePublic(localX509EncodedKeySpec);
        return KeyFactory.getInstance(str, paramString).generatePublic(localX509EncodedKeySpec);
      }
      throw localNoSuchAlgorithmException;
    }
    catch (InvalidKeySpecException localInvalidKeySpecException)
    {
    }
    throw new InvalidKeyException("error decoding public key");
  }

  public boolean verify()
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    return verify(BouncyCastleProvider.PROVIDER_NAME);
  }

  public boolean verify(String paramString)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    return verify(getPublicKey(paramString), paramString);
  }

  // ERROR //
  public boolean verify(PublicKey paramPublicKey, String paramString)
    throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException
  {
    // Byte code:
    //   0: aload_2
    //   1: ifnonnull +66 -> 67
    //   4: aload_0
    //   5: getfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   8: invokestatic 618	org/bouncycastle2/jce/PKCS10CertificationRequest:getSignatureName	(Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;)Ljava/lang/String;
    //   11: invokestatic 351	java/security/Signature:getInstance	(Ljava/lang/String;)Ljava/security/Signature;
    //   14: astore 8
    //   16: aload 8
    //   18: astore 5
    //   20: aload_0
    //   21: aload 5
    //   23: aload_0
    //   24: getfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   27: invokevirtual 480	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getParameters	()Lorg/bouncycastle2/asn1/DEREncodable;
    //   30: invokespecial 620	org/bouncycastle2/jce/PKCS10CertificationRequest:setSignatureParameters	(Ljava/security/Signature;Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   33: aload 5
    //   35: aload_1
    //   36: invokevirtual 624	java/security/Signature:initVerify	(Ljava/security/PublicKey;)V
    //   39: aload 5
    //   41: aload_0
    //   42: getfield 345	org/bouncycastle2/jce/PKCS10CertificationRequest:reqInfo	Lorg/bouncycastle2/asn1/pkcs/CertificationRequestInfo;
    //   45: ldc_w 357
    //   48: invokevirtual 360	org/bouncycastle2/asn1/pkcs/CertificationRequestInfo:getEncoded	(Ljava/lang/String;)[B
    //   51: invokevirtual 364	java/security/Signature:update	([B)V
    //   54: aload 5
    //   56: aload_0
    //   57: getfield 375	org/bouncycastle2/jce/PKCS10CertificationRequest:sigBits	Lorg/bouncycastle2/asn1/DERBitString;
    //   60: invokevirtual 590	org/bouncycastle2/asn1/DERBitString:getBytes	()[B
    //   63: invokevirtual 627	java/security/Signature:verify	([B)Z
    //   66: ireturn
    //   67: aload_0
    //   68: getfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   71: invokestatic 618	org/bouncycastle2/jce/PKCS10CertificationRequest:getSignatureName	(Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;)Ljava/lang/String;
    //   74: aload_2
    //   75: invokestatic 389	java/security/Signature:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;
    //   78: astore 7
    //   80: aload 7
    //   82: astore 5
    //   84: goto -64 -> 20
    //   87: astore_3
    //   88: getstatic 26	org/bouncycastle2/jce/PKCS10CertificationRequest:oids	Ljava/util/Hashtable;
    //   91: aload_0
    //   92: getfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   95: invokevirtual 485	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getObjectId	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   98: invokevirtual 298	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   101: ifnull +46 -> 147
    //   104: getstatic 26	org/bouncycastle2/jce/PKCS10CertificationRequest:oids	Ljava/util/Hashtable;
    //   107: aload_0
    //   108: getfield 317	org/bouncycastle2/jce/PKCS10CertificationRequest:sigAlgId	Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   111: invokevirtual 485	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:getObjectId	()Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   114: invokevirtual 298	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   117: checkcast 496	java/lang/String
    //   120: astore 4
    //   122: aload_2
    //   123: ifnonnull +13 -> 136
    //   126: aload 4
    //   128: invokestatic 351	java/security/Signature:getInstance	(Ljava/lang/String;)Ljava/security/Signature;
    //   131: astore 5
    //   133: goto -113 -> 20
    //   136: aload 4
    //   138: aload_2
    //   139: invokestatic 389	java/security/Signature:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;
    //   142: astore 5
    //   144: goto -124 -> 20
    //   147: aload_3
    //   148: athrow
    //   149: astore 6
    //   151: new 268	java/security/SignatureException
    //   154: dup
    //   155: new 391	java/lang/StringBuilder
    //   158: dup
    //   159: ldc_w 393
    //   162: invokespecial 394	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   165: aload 6
    //   167: invokevirtual 398	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   170: invokevirtual 402	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   173: invokespecial 555	java/security/SignatureException:<init>	(Ljava/lang/String;)V
    //   176: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   4	16	87	java/security/NoSuchAlgorithmException
    //   67	80	87	java/security/NoSuchAlgorithmException
    //   39	54	149	java/lang/Exception
  }
}