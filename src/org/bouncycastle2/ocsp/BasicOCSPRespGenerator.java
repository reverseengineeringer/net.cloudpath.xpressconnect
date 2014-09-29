package org.bouncycastle2.ocsp;

import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle2.asn1.DERGeneralizedTime;
import org.bouncycastle2.asn1.DERNull;
import org.bouncycastle2.asn1.ocsp.CertStatus;
import org.bouncycastle2.asn1.ocsp.RevokedInfo;
import org.bouncycastle2.asn1.ocsp.SingleResponse;
import org.bouncycastle2.asn1.x509.CRLReason;
import org.bouncycastle2.asn1.x509.X509Extensions;

public class BasicOCSPRespGenerator
{
  private List list = new ArrayList();
  private RespID responderID;
  private X509Extensions responseExtensions = null;

  public BasicOCSPRespGenerator(PublicKey paramPublicKey)
    throws OCSPException
  {
    this.responderID = new RespID(paramPublicKey);
  }

  public BasicOCSPRespGenerator(RespID paramRespID)
  {
    this.responderID = paramRespID;
  }

  // ERROR //
  private BasicOCSPResp generateResponse(String paramString1, PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate, Date paramDate, String paramString2, SecureRandom paramSecureRandom)
    throws OCSPException, NoSuchProviderException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 22	org/bouncycastle2/ocsp/BasicOCSPRespGenerator:list	Ljava/util/List;
    //   4: invokeinterface 49 1 0
    //   9: astore 7
    //   11: aload_1
    //   12: invokestatic 55	org/bouncycastle2/ocsp/OCSPUtil:getAlgorithmOID	(Ljava/lang/String;)Lorg/bouncycastle2/asn1/DERObjectIdentifier;
    //   15: astore 9
    //   17: new 57	org/bouncycastle2/asn1/ASN1EncodableVector
    //   20: dup
    //   21: invokespecial 58	org/bouncycastle2/asn1/ASN1EncodableVector:<init>	()V
    //   24: astore 10
    //   26: aload 7
    //   28: invokeinterface 64 1 0
    //   33: ifne +183 -> 216
    //   36: new 66	org/bouncycastle2/asn1/ocsp/ResponseData
    //   39: dup
    //   40: aload_0
    //   41: getfield 30	org/bouncycastle2/ocsp/BasicOCSPRespGenerator:responderID	Lorg/bouncycastle2/ocsp/RespID;
    //   44: invokevirtual 70	org/bouncycastle2/ocsp/RespID:toASN1Object	()Lorg/bouncycastle2/asn1/ocsp/ResponderID;
    //   47: new 72	org/bouncycastle2/asn1/DERGeneralizedTime
    //   50: dup
    //   51: aload 4
    //   53: invokespecial 75	org/bouncycastle2/asn1/DERGeneralizedTime:<init>	(Ljava/util/Date;)V
    //   56: new 77	org/bouncycastle2/asn1/DERSequence
    //   59: dup
    //   60: aload 10
    //   62: invokespecial 80	org/bouncycastle2/asn1/DERSequence:<init>	(Lorg/bouncycastle2/asn1/ASN1EncodableVector;)V
    //   65: aload_0
    //   66: getfield 24	org/bouncycastle2/ocsp/BasicOCSPRespGenerator:responseExtensions	Lorg/bouncycastle2/asn1/x509/X509Extensions;
    //   69: invokespecial 83	org/bouncycastle2/asn1/ocsp/ResponseData:<init>	(Lorg/bouncycastle2/asn1/ocsp/ResponderID;Lorg/bouncycastle2/asn1/DERGeneralizedTime;Lorg/bouncycastle2/asn1/ASN1Sequence;Lorg/bouncycastle2/asn1/x509/X509Extensions;)V
    //   72: astore 11
    //   74: aload_1
    //   75: aload 5
    //   77: invokestatic 87	org/bouncycastle2/ocsp/OCSPUtil:createSignatureInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;
    //   80: astore 14
    //   82: aload 6
    //   84: ifnull +167 -> 251
    //   87: aload 14
    //   89: aload_2
    //   90: aload 6
    //   92: invokevirtual 93	java/security/Signature:initSign	(Ljava/security/PrivateKey;Ljava/security/SecureRandom;)V
    //   95: aload 14
    //   97: aload 11
    //   99: ldc 95
    //   101: invokevirtual 99	org/bouncycastle2/asn1/ocsp/ResponseData:getEncoded	(Ljava/lang/String;)[B
    //   104: invokevirtual 103	java/security/Signature:update	([B)V
    //   107: new 105	org/bouncycastle2/asn1/DERBitString
    //   110: dup
    //   111: aload 14
    //   113: invokevirtual 109	java/security/Signature:sign	()[B
    //   116: invokespecial 111	org/bouncycastle2/asn1/DERBitString:<init>	([B)V
    //   119: astore 16
    //   121: aload 9
    //   123: invokestatic 115	org/bouncycastle2/ocsp/OCSPUtil:getSigAlgID	(Lorg/bouncycastle2/asn1/DERObjectIdentifier;)Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;
    //   126: astore 17
    //   128: aconst_null
    //   129: astore 18
    //   131: aload_3
    //   132: ifnull +49 -> 181
    //   135: aload_3
    //   136: arraylength
    //   137: istore 19
    //   139: aconst_null
    //   140: astore 18
    //   142: iload 19
    //   144: ifle +37 -> 181
    //   147: new 57	org/bouncycastle2/asn1/ASN1EncodableVector
    //   150: dup
    //   151: invokespecial 58	org/bouncycastle2/asn1/ASN1EncodableVector:<init>	()V
    //   154: astore 20
    //   156: iconst_0
    //   157: istore 21
    //   159: aload_3
    //   160: arraylength
    //   161: istore 24
    //   163: iload 21
    //   165: iload 24
    //   167: if_icmpne +156 -> 323
    //   170: new 77	org/bouncycastle2/asn1/DERSequence
    //   173: dup
    //   174: aload 20
    //   176: invokespecial 80	org/bouncycastle2/asn1/DERSequence:<init>	(Lorg/bouncycastle2/asn1/ASN1EncodableVector;)V
    //   179: astore 18
    //   181: new 117	org/bouncycastle2/ocsp/BasicOCSPResp
    //   184: dup
    //   185: new 119	org/bouncycastle2/asn1/ocsp/BasicOCSPResponse
    //   188: dup
    //   189: aload 11
    //   191: aload 17
    //   193: aload 16
    //   195: aload 18
    //   197: invokespecial 122	org/bouncycastle2/asn1/ocsp/BasicOCSPResponse:<init>	(Lorg/bouncycastle2/asn1/ocsp/ResponseData;Lorg/bouncycastle2/asn1/x509/AlgorithmIdentifier;Lorg/bouncycastle2/asn1/DERBitString;Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   200: invokespecial 125	org/bouncycastle2/ocsp/BasicOCSPResp:<init>	(Lorg/bouncycastle2/asn1/ocsp/BasicOCSPResponse;)V
    //   203: areturn
    //   204: astore 8
    //   206: new 127	java/lang/IllegalArgumentException
    //   209: dup
    //   210: ldc 129
    //   212: invokespecial 132	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   215: athrow
    //   216: aload 10
    //   218: aload 7
    //   220: invokeinterface 136 1 0
    //   225: checkcast 138	org/bouncycastle2/ocsp/BasicOCSPRespGenerator$ResponseObject
    //   228: invokevirtual 142	org/bouncycastle2/ocsp/BasicOCSPRespGenerator$ResponseObject:toResponse	()Lorg/bouncycastle2/asn1/ocsp/SingleResponse;
    //   231: invokevirtual 146	org/bouncycastle2/asn1/ASN1EncodableVector:add	(Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   234: goto -208 -> 26
    //   237: astore 25
    //   239: new 14	org/bouncycastle2/ocsp/OCSPException
    //   242: dup
    //   243: ldc 148
    //   245: aload 25
    //   247: invokespecial 151	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   250: athrow
    //   251: aload 14
    //   253: aload_2
    //   254: invokevirtual 154	java/security/Signature:initSign	(Ljava/security/PrivateKey;)V
    //   257: goto -162 -> 95
    //   260: astore 13
    //   262: aload 13
    //   264: athrow
    //   265: astore 12
    //   267: new 14	org/bouncycastle2/ocsp/OCSPException
    //   270: dup
    //   271: new 156	java/lang/StringBuilder
    //   274: dup
    //   275: ldc 158
    //   277: invokespecial 159	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   280: aload 12
    //   282: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   285: invokevirtual 167	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   288: aload 12
    //   290: invokespecial 151	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   293: athrow
    //   294: astore 15
    //   296: new 14	org/bouncycastle2/ocsp/OCSPException
    //   299: dup
    //   300: new 156	java/lang/StringBuilder
    //   303: dup
    //   304: ldc 169
    //   306: invokespecial 159	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   309: aload 15
    //   311: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   314: invokevirtual 167	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   317: aload 15
    //   319: invokespecial 151	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   322: athrow
    //   323: aload 20
    //   325: new 171	org/bouncycastle2/asn1/x509/X509CertificateStructure
    //   328: dup
    //   329: aload_3
    //   330: iload 21
    //   332: aaload
    //   333: invokevirtual 175	java/security/cert/X509Certificate:getEncoded	()[B
    //   336: invokestatic 181	org/bouncycastle2/asn1/ASN1Object:fromByteArray	([B)Lorg/bouncycastle2/asn1/ASN1Object;
    //   339: checkcast 183	org/bouncycastle2/asn1/ASN1Sequence
    //   342: invokespecial 186	org/bouncycastle2/asn1/x509/X509CertificateStructure:<init>	(Lorg/bouncycastle2/asn1/ASN1Sequence;)V
    //   345: invokevirtual 146	org/bouncycastle2/asn1/ASN1EncodableVector:add	(Lorg/bouncycastle2/asn1/DEREncodable;)V
    //   348: iinc 21 1
    //   351: goto -192 -> 159
    //   354: astore 23
    //   356: new 14	org/bouncycastle2/ocsp/OCSPException
    //   359: dup
    //   360: ldc 188
    //   362: aload 23
    //   364: invokespecial 151	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   367: athrow
    //   368: astore 22
    //   370: new 14	org/bouncycastle2/ocsp/OCSPException
    //   373: dup
    //   374: ldc 190
    //   376: aload 22
    //   378: invokespecial 151	org/bouncycastle2/ocsp/OCSPException:<init>	(Ljava/lang/String;Ljava/lang/Exception;)V
    //   381: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   11	17	204	java/lang/Exception
    //   216	234	237	java/lang/Exception
    //   74	82	260	java/security/NoSuchProviderException
    //   87	95	260	java/security/NoSuchProviderException
    //   251	257	260	java/security/NoSuchProviderException
    //   74	82	265	java/security/GeneralSecurityException
    //   87	95	265	java/security/GeneralSecurityException
    //   251	257	265	java/security/GeneralSecurityException
    //   95	121	294	java/lang/Exception
    //   159	163	354	java/io/IOException
    //   323	348	354	java/io/IOException
    //   159	163	368	java/security/cert/CertificateEncodingException
    //   323	348	368	java/security/cert/CertificateEncodingException
  }

  public void addResponse(CertificateID paramCertificateID, CertificateStatus paramCertificateStatus)
  {
    this.list.add(new ResponseObject(paramCertificateID, paramCertificateStatus, new Date(), null, null));
  }

  public void addResponse(CertificateID paramCertificateID, CertificateStatus paramCertificateStatus, Date paramDate1, Date paramDate2, X509Extensions paramX509Extensions)
  {
    this.list.add(new ResponseObject(paramCertificateID, paramCertificateStatus, paramDate1, paramDate2, paramX509Extensions));
  }

  public void addResponse(CertificateID paramCertificateID, CertificateStatus paramCertificateStatus, Date paramDate, X509Extensions paramX509Extensions)
  {
    this.list.add(new ResponseObject(paramCertificateID, paramCertificateStatus, new Date(), paramDate, paramX509Extensions));
  }

  public void addResponse(CertificateID paramCertificateID, CertificateStatus paramCertificateStatus, X509Extensions paramX509Extensions)
  {
    this.list.add(new ResponseObject(paramCertificateID, paramCertificateStatus, new Date(), null, paramX509Extensions));
  }

  public BasicOCSPResp generate(String paramString1, PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate, Date paramDate, String paramString2)
    throws OCSPException, NoSuchProviderException, IllegalArgumentException
  {
    return generate(paramString1, paramPrivateKey, paramArrayOfX509Certificate, paramDate, paramString2, null);
  }

  public BasicOCSPResp generate(String paramString1, PrivateKey paramPrivateKey, X509Certificate[] paramArrayOfX509Certificate, Date paramDate, String paramString2, SecureRandom paramSecureRandom)
    throws OCSPException, NoSuchProviderException, IllegalArgumentException
  {
    if (paramString1 == null)
      throw new IllegalArgumentException("no signing algorithm specified");
    return generateResponse(paramString1, paramPrivateKey, paramArrayOfX509Certificate, paramDate, paramString2, paramSecureRandom);
  }

  public Iterator getSignatureAlgNames()
  {
    return OCSPUtil.getAlgNames();
  }

  public void setResponseExtensions(X509Extensions paramX509Extensions)
  {
    this.responseExtensions = paramX509Extensions;
  }

  private class ResponseObject
  {
    CertificateID certId;
    CertStatus certStatus;
    X509Extensions extensions;
    DERGeneralizedTime nextUpdate;
    DERGeneralizedTime thisUpdate;

    public ResponseObject(CertificateID paramCertificateStatus, CertificateStatus paramDate1, Date paramDate2, Date paramX509Extensions, X509Extensions arg6)
    {
      this.certId = paramCertificateStatus;
      if (paramDate1 == null)
      {
        this.certStatus = new CertStatus();
        this.thisUpdate = new DERGeneralizedTime(paramDate2);
        if (paramX509Extensions == null)
          break label189;
      }
      label189: for (this.nextUpdate = new DERGeneralizedTime(paramX509Extensions); ; this.nextUpdate = null)
      {
        Object localObject;
        this.extensions = localObject;
        return;
        if ((paramDate1 instanceof UnknownStatus))
        {
          this.certStatus = new CertStatus(2, new DERNull());
          break;
        }
        RevokedStatus localRevokedStatus = (RevokedStatus)paramDate1;
        if (localRevokedStatus.hasRevocationReason())
        {
          this.certStatus = new CertStatus(new RevokedInfo(new DERGeneralizedTime(localRevokedStatus.getRevocationTime()), new CRLReason(localRevokedStatus.getRevocationReason())));
          break;
        }
        this.certStatus = new CertStatus(new RevokedInfo(new DERGeneralizedTime(localRevokedStatus.getRevocationTime()), null));
        break;
      }
    }

    public SingleResponse toResponse()
      throws Exception
    {
      return new SingleResponse(this.certId.toASN1Object(), this.certStatus, this.thisUpdate, this.nextUpdate, this.extensions);
    }
  }
}