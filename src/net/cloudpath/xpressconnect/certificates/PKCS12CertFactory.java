package net.cloudpath.xpressconnect.certificates;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import net.cloudpath.xpressconnect.logger.Logger;

public class PKCS12CertFactory
{
  private boolean mDumpPkcs12File = false;
  public int mFailureCode = 0;
  private KeystoreFactory mKeyStore = null;
  private Logger mLogger = null;
  private boolean mShouldHaveUserCert = false;

  public PKCS12CertFactory(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.mKeyStore = new KeystoreFactory(this.mLogger);
  }

  public boolean addCertToStore(String paramString)
  {
    return this.mKeyStore.addCertToStore(paramString);
  }

  public boolean addCertToStore(Certificate paramCertificate)
  {
    return this.mKeyStore.addCertToStore(paramCertificate);
  }

  public boolean addUserCertToStore(String paramString, PrivateKey paramPrivateKey)
  {
    this.mShouldHaveUserCert = true;
    return this.mKeyStore.addUserCertToStore(paramString, paramPrivateKey);
  }

  public boolean addUserCertToStore(Certificate paramCertificate, PrivateKey paramPrivateKey)
  {
    this.mShouldHaveUserCert = true;
    return this.mKeyStore.addUserCertToStore(paramCertificate, paramPrivateKey);
  }

  public void enableFileDump()
  {
    this.mDumpPkcs12File = true;
  }

  // ERROR //
  public byte[] getPkcs12Stream(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +14 -> 15
    //   4: aload_0
    //   5: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   8: ldc 61
    //   10: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   13: aconst_null
    //   14: areturn
    //   15: aload_2
    //   16: ifnonnull +6 -> 22
    //   19: ldc 69
    //   21: astore_2
    //   22: aload_0
    //   23: getfield 28	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mShouldHaveUserCert	Z
    //   26: ifne +14 -> 40
    //   29: aload_0
    //   30: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   33: ldc 71
    //   35: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   38: aconst_null
    //   39: areturn
    //   40: aload_0
    //   41: getfield 26	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mKeyStore	Lnet/cloudpath/xpressconnect/certificates/KeystoreFactory;
    //   44: ldc 73
    //   46: aload_1
    //   47: aload_2
    //   48: invokevirtual 77	net/cloudpath/xpressconnect/certificates/KeystoreFactory:getKeystore	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/security/KeyStore;
    //   51: astore_3
    //   52: aload_3
    //   53: ifnonnull +14 -> 67
    //   56: aload_0
    //   57: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   60: ldc 79
    //   62: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   65: aconst_null
    //   66: areturn
    //   67: aload_0
    //   68: aload_0
    //   69: getfield 26	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mKeyStore	Lnet/cloudpath/xpressconnect/certificates/KeystoreFactory;
    //   72: getfield 80	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mFailureCode	I
    //   75: putfield 24	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mFailureCode	I
    //   78: aload_0
    //   79: getfield 22	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mDumpPkcs12File	Z
    //   82: ifeq +60 -> 142
    //   85: new 82	java/io/FileOutputStream
    //   88: dup
    //   89: new 84	java/lang/StringBuilder
    //   92: dup
    //   93: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   96: invokestatic 91	android/os/Environment:getExternalStorageDirectory	()Ljava/io/File;
    //   99: invokevirtual 97	java/io/File:getPath	()Ljava/lang/String;
    //   102: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: ldc 103
    //   107: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   113: invokespecial 109	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   116: astore 12
    //   118: aload 12
    //   120: astore 13
    //   122: aload 13
    //   124: ifnull +18 -> 142
    //   127: aload_3
    //   128: aload 13
    //   130: aload_1
    //   131: invokevirtual 115	java/lang/String:toCharArray	()[C
    //   134: invokevirtual 121	java/security/KeyStore:store	(Ljava/io/OutputStream;[C)V
    //   137: aload 13
    //   139: invokevirtual 124	java/io/FileOutputStream:close	()V
    //   142: new 126	java/io/ByteArrayOutputStream
    //   145: dup
    //   146: invokespecial 127	java/io/ByteArrayOutputStream:<init>	()V
    //   149: astore 4
    //   151: aload_3
    //   152: aload 4
    //   154: aload_1
    //   155: invokevirtual 115	java/lang/String:toCharArray	()[C
    //   158: invokevirtual 121	java/security/KeyStore:store	(Ljava/io/OutputStream;[C)V
    //   161: aload_3
    //   162: aload_2
    //   163: invokevirtual 131	java/security/KeyStore:getCertificateChain	(Ljava/lang/String;)[Ljava/security/cert/Certificate;
    //   166: astore 10
    //   168: aload 10
    //   170: ifnonnull +471 -> 641
    //   173: aload_0
    //   174: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   177: ldc 133
    //   179: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   182: aload_0
    //   183: iconst_4
    //   184: putfield 24	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mFailureCode	I
    //   187: aconst_null
    //   188: areturn
    //   189: astore 9
    //   191: aload 9
    //   193: invokevirtual 136	java/security/KeyStoreException:printStackTrace	()V
    //   196: aload_0
    //   197: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   200: ldc 138
    //   202: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   205: aload_0
    //   206: iconst_4
    //   207: putfield 24	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mFailureCode	I
    //   210: aconst_null
    //   211: areturn
    //   212: astore 19
    //   214: aload_0
    //   215: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   218: new 84	java/lang/StringBuilder
    //   221: dup
    //   222: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   225: ldc 140
    //   227: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: aload 19
    //   232: invokevirtual 143	java/io/FileNotFoundException:getLocalizedMessage	()Ljava/lang/String;
    //   235: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   238: ldc 145
    //   240: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   246: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   249: aload 19
    //   251: invokevirtual 146	java/io/FileNotFoundException:printStackTrace	()V
    //   254: aconst_null
    //   255: astore 13
    //   257: goto -135 -> 122
    //   260: astore 18
    //   262: aload_0
    //   263: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   266: new 84	java/lang/StringBuilder
    //   269: dup
    //   270: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   273: ldc 140
    //   275: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: aload 18
    //   280: invokevirtual 147	java/security/KeyStoreException:getLocalizedMessage	()Ljava/lang/String;
    //   283: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: ldc 145
    //   288: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   291: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   294: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   297: aload 18
    //   299: invokevirtual 136	java/security/KeyStoreException:printStackTrace	()V
    //   302: goto -165 -> 137
    //   305: astore 17
    //   307: aload_0
    //   308: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   311: new 84	java/lang/StringBuilder
    //   314: dup
    //   315: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   318: ldc 140
    //   320: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: aload 17
    //   325: invokevirtual 148	java/security/NoSuchAlgorithmException:getLocalizedMessage	()Ljava/lang/String;
    //   328: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   331: ldc 145
    //   333: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   336: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   339: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   342: aload 17
    //   344: invokevirtual 149	java/security/NoSuchAlgorithmException:printStackTrace	()V
    //   347: goto -210 -> 137
    //   350: astore 16
    //   352: aload_0
    //   353: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   356: new 84	java/lang/StringBuilder
    //   359: dup
    //   360: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   363: ldc 140
    //   365: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   368: aload 16
    //   370: invokevirtual 150	java/security/cert/CertificateException:getLocalizedMessage	()Ljava/lang/String;
    //   373: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   376: ldc 145
    //   378: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   381: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   384: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   387: aload 16
    //   389: invokevirtual 151	java/security/cert/CertificateException:printStackTrace	()V
    //   392: goto -255 -> 137
    //   395: astore 14
    //   397: aload_0
    //   398: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   401: new 84	java/lang/StringBuilder
    //   404: dup
    //   405: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   408: ldc 140
    //   410: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   413: aload 14
    //   415: invokevirtual 152	java/io/IOException:getLocalizedMessage	()Ljava/lang/String;
    //   418: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   421: ldc 145
    //   423: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   426: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   429: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   432: aload 14
    //   434: invokevirtual 153	java/io/IOException:printStackTrace	()V
    //   437: goto -300 -> 137
    //   440: astore 15
    //   442: aload_0
    //   443: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   446: new 84	java/lang/StringBuilder
    //   449: dup
    //   450: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   453: ldc 140
    //   455: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   458: aload 15
    //   460: invokevirtual 152	java/io/IOException:getLocalizedMessage	()Ljava/lang/String;
    //   463: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   466: ldc 145
    //   468: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   471: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   474: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   477: aload 15
    //   479: invokevirtual 153	java/io/IOException:printStackTrace	()V
    //   482: goto -340 -> 142
    //   485: astore 8
    //   487: aload_0
    //   488: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   491: new 84	java/lang/StringBuilder
    //   494: dup
    //   495: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   498: ldc 155
    //   500: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   503: aload 8
    //   505: invokevirtual 158	java/security/KeyStoreException:getMessage	()Ljava/lang/String;
    //   508: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   511: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   514: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   517: aload 8
    //   519: invokevirtual 136	java/security/KeyStoreException:printStackTrace	()V
    //   522: aconst_null
    //   523: areturn
    //   524: astore 7
    //   526: aload_0
    //   527: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   530: new 84	java/lang/StringBuilder
    //   533: dup
    //   534: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   537: ldc 160
    //   539: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   542: aload 7
    //   544: invokevirtual 161	java/security/NoSuchAlgorithmException:getMessage	()Ljava/lang/String;
    //   547: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   550: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   553: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   556: aload 7
    //   558: invokevirtual 149	java/security/NoSuchAlgorithmException:printStackTrace	()V
    //   561: aconst_null
    //   562: areturn
    //   563: astore 6
    //   565: aload_0
    //   566: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   569: new 84	java/lang/StringBuilder
    //   572: dup
    //   573: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   576: ldc 163
    //   578: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   581: aload 6
    //   583: invokevirtual 164	java/security/cert/CertificateException:getMessage	()Ljava/lang/String;
    //   586: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   589: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   592: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   595: aload 6
    //   597: invokevirtual 151	java/security/cert/CertificateException:printStackTrace	()V
    //   600: aconst_null
    //   601: areturn
    //   602: astore 5
    //   604: aload_0
    //   605: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   608: new 84	java/lang/StringBuilder
    //   611: dup
    //   612: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   615: ldc 166
    //   617: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   620: aload 5
    //   622: invokevirtual 167	java/io/IOException:getMessage	()Ljava/lang/String;
    //   625: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   628: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   631: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   634: aload 5
    //   636: invokevirtual 153	java/io/IOException:printStackTrace	()V
    //   639: aconst_null
    //   640: areturn
    //   641: aload_0
    //   642: getfield 26	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mKeyStore	Lnet/cloudpath/xpressconnect/certificates/KeystoreFactory;
    //   645: invokevirtual 171	net/cloudpath/xpressconnect/certificates/KeystoreFactory:numCerts	()I
    //   648: istore 11
    //   650: aload 10
    //   652: arraylength
    //   653: iload 11
    //   655: if_icmpge +46 -> 701
    //   658: aload_0
    //   659: getfield 20	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   662: new 84	java/lang/StringBuilder
    //   665: dup
    //   666: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   669: ldc 173
    //   671: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   674: iload 11
    //   676: invokevirtual 176	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   679: ldc 178
    //   681: invokevirtual 101	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   684: aload 10
    //   686: arraylength
    //   687: invokevirtual 176	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   690: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   693: invokestatic 67	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   696: aload_0
    //   697: iconst_2
    //   698: putfield 24	net/cloudpath/xpressconnect/certificates/PKCS12CertFactory:mFailureCode	I
    //   701: aload 4
    //   703: invokevirtual 182	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   706: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   161	168	189	java/security/KeyStoreException
    //   173	187	189	java/security/KeyStoreException
    //   641	701	189	java/security/KeyStoreException
    //   85	118	212	java/io/FileNotFoundException
    //   127	137	260	java/security/KeyStoreException
    //   127	137	305	java/security/NoSuchAlgorithmException
    //   127	137	350	java/security/cert/CertificateException
    //   127	137	395	java/io/IOException
    //   137	142	440	java/io/IOException
    //   151	161	485	java/security/KeyStoreException
    //   151	161	524	java/security/NoSuchAlgorithmException
    //   151	161	563	java/security/cert/CertificateException
    //   151	161	602	java/io/IOException
  }
}