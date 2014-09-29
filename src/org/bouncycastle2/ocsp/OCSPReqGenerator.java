package org.bouncycastle2.ocsp;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle2.asn1.ocsp.Request;
import org.bouncycastle2.asn1.x509.GeneralName;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.jce.X509Principal;

public class OCSPReqGenerator
{
  private List list = new ArrayList();
  private X509Extensions requestExtensions = null;
  private GeneralName requestorName = null;

  // ERROR //
  private OCSPReq generateRequest(org.bouncycastle2.asn1.DERObjectIdentifier paramDERObjectIdentifier, PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate, String paramString, SecureRandom paramSecureRandom)
    throws OCSPException, NoSuchProviderException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 19	org/bouncycastle2/ocsp/OCSPReqGenerator:list	Ljava/util/List;
    //   4: invokeinterface 43 1 0
    //   9: astore 6
    //   11: new 45	org/bouncycastle2/asn1/ASN1EncodableVector
    //   14: dup
    //   15: invokespecial 46	org/bouncycastle2/asn1/ASN1EncodableVector:<init>	()V
    //   18: astore 7
    //   20: aload 6
    //   22: invokeinterface 52 1 0
    //   27: ifne +53 -> 80
    //   30: new 54	org/bouncycastle2/asn1/ocsp/TBSRequest
    //   33: dup
    //   34: aload_0
    //   35: getfield 21	org/bouncycastle2/ocsp/OCSPReqGenerator:requestorName	Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   38: new 56	org/bouncycastle2/asn1/DERSequence
    //   41: dup
    //   42: aload 7
    //   44: invokespecial 59	org/bouncycastle2/asn1/DERSequence:<init>	(Lorg/bouncycastle2/asn1/ASN1EncodableVector;)V
    //   47: aload_0
    //   48: getfield 23	org/bouncycastle2/ocsp/OCSPReqGenerator:requestExtensions	Lorg/bouncycastle2/asn1/x509/X509Extensions;
    //   51: invokespecial 62	org/bouncycastle2/asn1/ocsp/TBSRequest:<init>	(Lorg/bouncycastle2/asn1/x509/GeneralName;Lorg/bouncycastle2/asn1/ASN1Sequence;Lorg/bouncycastle2/asn1/x509/X509Extensions;)V
    //   54: astore 8
    //   56: aconst_null
    //   57: astore 9
    //   59: aload_1
    //   60: ifnull +197 -> 257
    //   63: aload_0
    //   64: getfield 21	org/bouncycastle2/ocsp/OCSPReqGenerator:requestorName	Lorg/bouncycastle2/asn1/x509/GeneralName;
    //   67: ifnonnull +48 -> 115
    //   70: new 27	org/bouncycastle2/ocsp/OCSPException
    //   73: dup
    //   74: ldc 64
    //   76: invokespecial 67	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;)V
    //   79: athrow
    //   80: aload 7
    //   82: aload 6
    //   84: invokeinterface 71 1 0
    //   89: checkcast 73	org/bouncycastle2/ocsp/OCSPReqGenerator$RequestObject
    //   92: invokevirtual 77	org/bouncycastle2/ocsp/OCSPReqGenerator$RequestObject:toRequest	()Lorg/bouncycastle2/asn1/ocsp/Request;
    //   95: invokevirtual 81	org/bouncycastle2/asn1/ASN1EncodableVector:add	(Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   98: goto -78 -> 20
    //   101: astore 22
    //   103: new 27	org/bouncycastle2/ocsp/OCSPException
    //   106: dup
    //   107: ldc 83
    //   109: aload 22
    //   111: invokespecial 86	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   114: athrow
    //   115: aload_1
    //   116: invokevirtual 92	org/bouncycastle2/asn1/DERObjectIdentifier:getId	()Ljava/lang/String;
    //   119: aload 4
    //   121: invokestatic 98	org/bouncycastle2/ocsp/OCSPUtil:createSignatureInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;
    //   124: astore 12
    //   126: aload 5
    //   128: ifnull +148 -> 276
    //   131: aload 12
    //   133: aload_2
    //   134: aload 5
    //   136: invokevirtual 104	java/security/Signature:initSign	(Ljava/security/PrivateKey;Ljava/security/SecureRandom;)V
    //   139: new 106	java/io/ByteArrayOutputStream
    //   142: dup
    //   143: invokespecial 107	java/io/ByteArrayOutputStream:<init>	()V
    //   146: astore 13
    //   148: new 109	org/bouncycastle2/asn1/ASN1OutputStream
    //   151: dup
    //   152: aload 13
    //   154: invokespecial 112	org/bouncycastle2/asn1/ASN1OutputStream:<init>	(Ljava/io/OutputStream;)V
    //   157: aload 8
    //   159: invokevirtual 116	org/bouncycastle2/asn1/ASN1OutputStream:writeObject	(Ljava/lang/Object;)V
    //   162: aload 12
    //   164: aload 13
    //   166: invokevirtual 120	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   169: invokevirtual 124	java/security/Signature:update	([B)V
    //   172: new 126	org/bouncycastle2/asn1/DERBitString
    //   175: dup
    //   176: aload 12
    //   178: invokevirtual 129	java/security/Signature:sign	()[B
    //   181: invokespecial 131	org/bouncycastle2/asn1/DERBitString:<init>	([B)V
    //   184: astore 15
    //   186: new 133	org/bouncycastle2/asn1/x509/AlgorithmIdentifier
    //   189: dup
    //   190: aload_1
    //   191: new 135	org/bouncycastle2/asn1/DERNull
    //   194: dup
    //   195: invokespecial 136	org/bouncycastle2/asn1/DERNull:<init>	()V
    //   198: invokespecial 139	org/bouncycastle2/asn1/x509/AlgorithmIdentifier:<init>	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   201: astore 16
    //   203: aload_3
    //   204: ifnull +203 -> 407
    //   207: aload_3
    //   208: arraylength
    //   209: ifle +198 -> 407
    //   212: new 45	org/bouncycastle2/asn1/ASN1EncodableVector
    //   215: dup
    //   216: invokespecial 46	org/bouncycastle2/asn1/ASN1EncodableVector:<init>	()V
    //   219: astore 17
    //   221: iconst_0
    //   222: istore 18
    //   224: aload_3
    //   225: arraylength
    //   226: istore 21
    //   228: iload 18
    //   230: iload 21
    //   232: if_icmpne +116 -> 348
    //   235: new 141	org/bouncycastle2/asn1/ocsp/Signature
    //   238: dup
    //   239: aload 16
    //   241: aload 15
    //   243: new 56	org/bouncycastle2/asn1/DERSequence
    //   246: dup
    //   247: aload 17
    //   249: invokespecial 59	org/bouncycastle2/asn1/DERSequence:<init>	(Lorg/bouncycastle2/asn1/ASN1EncodableVector;)V
    //   252: invokespecial 144	org/bouncycastle2/asn1/ocsp/Signature:<init>	(Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;Lorg/bouncycastle2/asn1/DERBitString;Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   255: astore 9
    //   257: new 146	org/bouncycastle2/ocsp/OCSPReq
    //   260: dup
    //   261: new 148	org/bouncycastle2/asn1/ocsp/OCSPRequest
    //   264: dup
    //   265: aload 8
    //   267: aload 9
    //   269: invokespecial 151	org/bouncycastle2/asn1/ocsp/OCSPRequest:<init>	(Lorg/bouncycastle2/asn1/ocsp/TBSRequest;Lorg/bouncycastle2/asn1/ocsp/Signature;)V
    //   272: invokespecial 154	org/bouncycastle2/ocsp/OCSPReq:<init>	(Lorg/bouncycastle2/asn1/ocsp/OCSPRequest;)V
    //   275: areturn
    //   276: aload 12
    //   278: aload_2
    //   279: invokevirtual 157	java/security/Signature:initSign	(Ljava/security/PrivateKey;)V
    //   282: goto -143 -> 139
    //   285: astore 11
    //   287: aload 11
    //   289: athrow
    //   290: astore 10
    //   292: new 27	org/bouncycastle2/ocsp/OCSPException
    //   295: dup
    //   296: new 159	java/lang/StringBuilder
    //   299: dup
    //   300: ldc 161
    //   302: invokespecial 162	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   305: aload 10
    //   307: invokevirtual 166	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   310: invokevirtual 169	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   313: aload 10
    //   315: invokespecial 86	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   318: athrow
    //   319: astore 14
    //   321: new 27	org/bouncycastle2/ocsp/OCSPException
    //   324: dup
    //   325: new 159	java/lang/StringBuilder
    //   328: dup
    //   329: ldc 171
    //   331: invokespecial 162	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   334: aload 14
    //   336: invokevirtual 166	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   339: invokevirtual 169	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   342: aload 14
    //   344: invokespecial 86	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   347: athrow
    //   348: aload 17
    //   350: new 173	org/bouncycastle2/asn1/x509/X509CertificateStructure
    //   353: dup
    //   354: aload_3
    //   355: iload 18
    //   357: aaload
    //   358: invokevirtual 178	java/security/cert/X509Certificate:getEncoded	()[B
    //   361: invokestatic 184	org/bouncycastle2/asn1/ASN1Object:fromByteArray	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   364: checkcast 186	org/bouncycastle2/asn1/ASN1Sequence
    //   367: invokespecial 189	org/bouncycastle2/asn1/x509/X509CertificateStructure:<init>	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   370: invokevirtual 81	org/bouncycastle2/asn1/ASN1EncodableVector:add	(Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   373: iinc 18 1
    //   376: goto -152 -> 224
    //   379: astore 20
    //   381: new 27	org/bouncycastle2/ocsp/OCSPException
    //   384: dup
    //   385: ldc 191
    //   387: aload 20
    //   389: invokespecial 86	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   392: athrow
    //   393: astore 19
    //   395: new 27	org/bouncycastle2/ocsp/OCSPException
    //   398: dup
    //   399: ldc 193
    //   401: aload 19
    //   403: invokespecial 86	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   406: athrow
    //   407: new 141	org/bouncycastle2/asn1/ocsp/Signature
    //   410: dup
    //   411: aload 16
    //   413: aload 15
    //   415: invokespecial 196	org/bouncycastle2/asn1/ocsp/Signature:<init>	(Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;Lorg/bouncycastle2/asn1/DERBitString;)V
    //   418: astore 9
    //   420: goto -163 -> 257
    //
    // Exception table:
    //   from	to	target	type
    //   80	98	101	java/lang/Exception
    //   115	126	285	java/security/NoSuchProviderException
    //   131	139	285	java/security/NoSuchProviderException
    //   276	282	285	java/security/NoSuchProviderException
    //   115	126	290	java/security/GeneralSecurityException
    //   131	139	290	java/security/GeneralSecurityException
    //   276	282	290	java/security/GeneralSecurityException
    //   139	186	319	java/lang/Exception
    //   224	228	379	java/io/IOException
    //   348	373	379	java/io/IOException
    //   224	228	393	java/security/cert/CertificateEncodingException
    //   348	373	393	java/security/cert/CertificateEncodingException
  }

  public void addRequest(CertificateID paramCertificateID)
  {
    this.list.add(new RequestObject(paramCertificateID, null));
  }

  public void addRequest(CertificateID paramCertificateID, X509Extensions paramX509Extensions)
  {
    this.list.add(new RequestObject(paramCertificateID, paramX509Extensions));
  }

  public OCSPReq generate()
    throws OCSPException
  {
    try
    {
      OCSPReq localOCSPReq = generateRequest(null, null, null, null, null);
      return localOCSPReq;
    }
    catch (NoSuchProviderException localNoSuchProviderException)
    {
      throw new OCSPException("no provider! - " + localNoSuchProviderException, localNoSuchProviderException);
    }
  }

  public OCSPReq generate(String paramString1, PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate, String paramString2)
    throws OCSPException, NoSuchProviderException, IllegalArgumentException
  {
    return generate(paramString1, paramPrivateKey, paramArrayOfX509Certificate, paramString2, null);
  }

  public OCSPReq generate(String paramString1, PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate, String paramString2, SecureRandom paramSecureRandom)
    throws OCSPException, NoSuchProviderException, IllegalArgumentException
  {
    if (paramString1 == null)
      throw new IllegalArgumentException("no signing algorithm specified");
    try
    {
      OCSPReq localOCSPReq = generateRequest(OCSPUtil.getAlgorithmOID(paramString1), paramPrivateKey, paramArrayOfX509Certificate, paramString2, paramSecureRandom);
      return localOCSPReq;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
    }
    throw new IllegalArgumentException("unknown signing algorithm specified: " + paramString1);
  }

  public Iterator getSignatureAlgNames()
  {
    return OCSPUtil.getAlgNames();
  }

  public void setRequestExtensions(X509Extensions paramX509Extensions)
  {
    this.requestExtensions = paramX509Extensions;
  }

  public void setRequestorName(X500Principal paramX500Principal)
  {
    try
    {
      this.requestorName = new GeneralName(4, new X509Principal(paramX500Principal.getEncoded()));
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalArgumentException("cannot encode principal: " + localIOException);
    }
  }

  public void setRequestorName(GeneralName paramGeneralName)
  {
    this.requestorName = paramGeneralName;
  }

  private class RequestObject
  {
    CertificateID certId;
    X509Extensions extensions;

    public RequestObject(CertificateID paramX509Extensions, X509Extensions arg3)
    {
      this.certId = paramX509Extensions;
      Object localObject;
      this.extensions = localObject;
    }

    public Request toRequest()
      throws Exception
    {
      return new Request(this.certId.toASN1Object(), this.extensions);
    }
  }
}