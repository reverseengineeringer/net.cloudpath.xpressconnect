package org.bouncycastle2.jce.provider;

import org.bouncycastle2.asn1.ASN1Null;
import org.bouncycastle2.asn1.ASN1ObjectIdentifier;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEREncodable;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.DERObjectIdentifier;
import org.bouncycastle2.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle2.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle2.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle2.asn1.pkcs.RSASSAPSSparams;
import org.bouncycastle2.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle2.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle2.asn1.x9.X9ObjectIdentifiers;

class X509SignatureUtil
{
  private static final ASN1Null derNull = new DERNull();

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
    if ((localDEREncodable != null) && (!derNull.equals(localDEREncodable)))
    {
      if (paramAlgorithmIdentifier.getObjectId().equals(PKCSObjectIdentifiers.id_RSASSA_PSS))
        return getDigestAlgName(RSASSAPSSparams.getInstance(localDEREncodable).getHashAlgorithm().getObjectId()) + "withRSAandMGF1";
      if (paramAlgorithmIdentifier.getObjectId().equals(X9ObjectIdentifiers.ecdsa_with_SHA2))
        return getDigestAlgName((DERObjectIdentifier)ASN1Sequence.getInstance(localDEREncodable).getObjectAt(0)) + "withECDSA";
    }
    return paramAlgorithmIdentifier.getObjectId().getId();
  }

  // ERROR //
  static void setSignatureParameters(java.security.Signature paramSignature, DEREncodable paramDEREncodable)
    throws java.security.NoSuchAlgorithmException, java.security.SignatureException, java.security.InvalidKeyException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +60 -> 61
    //   4: getstatic 15	org/bouncycastle2/jce/provider/X509SignatureUtil:derNull	Lorg/bouncycastle2/asn1/ASN1Null;
    //   7: aload_1
    //   8: invokevirtual 104	org/bouncycastle2/asn1/ASN1Null:equals	(Ljava/lang/Object;)Z
    //   11: ifne +50 -> 61
    //   14: aload_0
    //   15: invokevirtual 176	java/security/Signature:getAlgorithm	()Ljava/lang/String;
    //   18: aload_0
    //   19: invokevirtual 180	java/security/Signature:getProvider	()Ljava/security/Provider;
    //   22: invokestatic 185	java/security/AlgorithmParameters:getInstance	(Ljava/lang/String;Ljava/security/Provider;)Ljava/security/AlgorithmParameters;
    //   25: astore_2
    //   26: aload_2
    //   27: aload_1
    //   28: invokeinterface 191 1 0
    //   33: invokevirtual 197	org/bouncycastle2/asn1/DERObject:getDEREncoded	()[B
    //   36: invokevirtual 201	java/security/AlgorithmParameters:init	([B)V
    //   39: aload_0
    //   40: invokevirtual 176	java/security/Signature:getAlgorithm	()Ljava/lang/String;
    //   43: ldc 203
    //   45: invokevirtual 207	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   48: ifeq +13 -> 61
    //   51: aload_0
    //   52: aload_2
    //   53: ldc 209
    //   55: invokevirtual 213	java/security/AlgorithmParameters:getParameterSpec	(Ljava/lang/Class;)Ljava/security/spec/AlgorithmParameterSpec;
    //   58: invokevirtual 217	java/security/Signature:setParameter	(Ljava/security/spec/AlgorithmParameterSpec;)V
    //   61: return
    //   62: astore_3
    //   63: new 165	java/security/SignatureException
    //   66: dup
    //   67: new 113	java/lang/StringBuilder
    //   70: dup
    //   71: ldc 219
    //   73: invokespecial 134	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   76: aload_3
    //   77: invokevirtual 222	java/io/IOException:getMessage	()Ljava/lang/String;
    //   80: invokevirtual 140	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: invokevirtual 143	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   86: invokespecial 223	java/security/SignatureException:<init>	(Ljava/lang/String;)V
    //   89: athrow
    //   90: astore 4
    //   92: new 165	java/security/SignatureException
    //   95: dup
    //   96: new 113	java/lang/StringBuilder
    //   99: dup
    //   100: ldc 225
    //   102: invokespecial 134	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   105: aload 4
    //   107: invokevirtual 226	java/security/GeneralSecurityException:getMessage	()Ljava/lang/String;
    //   110: invokevirtual 140	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   113: invokevirtual 143	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   116: invokespecial 223	java/security/SignatureException:<init>	(Ljava/lang/String;)V
    //   119: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   26	39	62	java/io/IOException
    //   51	61	90	java/security/GeneralSecurityException
  }
}