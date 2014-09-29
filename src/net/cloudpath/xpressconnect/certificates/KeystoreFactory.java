package net.cloudpath.xpressconnect.certificates;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class KeystoreFactory
{
  private ArrayList<Certificate> mCertificates = new ArrayList();
  public int mFailureCode = 0;
  private Logger mLogger = null;
  private PrivateKey mPrivateKey = null;
  private Certificate mUserCert = null;

  public KeystoreFactory(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  private Certificate[] getCertArray(ArrayList<Certificate> paramArrayList)
  {
    Certificate[] arrayOfCertificate;
    int i;
    if (this.mUserCert != null)
    {
      arrayOfCertificate = new Certificate[1 + paramArrayList.size()];
      arrayOfCertificate[0] = this.mUserCert;
      i = 1;
    }
    for (int j = i; ; j++)
      if (j < arrayOfCertificate.length)
      {
        arrayOfCertificate[j] = ((Certificate)paramArrayList.get(j - i));
        if (arrayOfCertificate[j] == null)
        {
          this.mFailureCode = 5;
          Util.log(this.mLogger, "One of the CA chain certs was null in getCertArray()!");
          arrayOfCertificate = null;
        }
      }
      else
      {
        return arrayOfCertificate;
        arrayOfCertificate = new Certificate[paramArrayList.size()];
        i = 0;
        break;
      }
  }

  public boolean addCertToStore(String paramString)
  {
    if (Util.stringIsEmpty(paramString))
      return false;
    return addCertToStore(new CertUtils(this.mLogger).getX509Cert(paramString));
  }

  public boolean addCertToStore(Certificate paramCertificate)
  {
    if (paramCertificate == null)
      return false;
    return this.mCertificates.add(paramCertificate);
  }

  public boolean addUserCertToStore(String paramString, PrivateKey paramPrivateKey)
  {
    if (Util.stringIsEmpty(paramString))
      return false;
    return addUserCertToStore(new CertUtils(this.mLogger).getX509Cert(paramString), paramPrivateKey);
  }

  public boolean addUserCertToStore(Certificate paramCertificate, PrivateKey paramPrivateKey)
  {
    if ((paramCertificate == null) || (paramPrivateKey == null))
    {
      Util.log(this.mLogger, "Cert or private key is null in addUserCertToStore()!");
      return false;
    }
    this.mPrivateKey = paramPrivateKey;
    this.mUserCert = paramCertificate;
    return true;
  }

  // ERROR //
  public java.security.KeyStore getKeystore(String paramString1, String paramString2, String paramString3)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 58	net/cloudpath/xpressconnect/Util:stringIsEmpty	(Ljava/lang/String;)Z
    //   4: ifeq +18 -> 22
    //   7: aload_0
    //   8: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   11: ldc 92
    //   13: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   16: aconst_null
    //   17: astore 6
    //   19: aload 6
    //   21: areturn
    //   22: aload_1
    //   23: invokestatic 98	java/security/KeyStore:getInstance	(Ljava/lang/String;)Ljava/security/KeyStore;
    //   26: astore 5
    //   28: aload 5
    //   30: astore 6
    //   32: aload 6
    //   34: aconst_null
    //   35: aconst_null
    //   36: invokevirtual 102	java/security/KeyStore:load	(Ljava/io/InputStream;[C)V
    //   39: aload_0
    //   40: aload_0
    //   41: getfield 25	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mCertificates	Ljava/util/ArrayList;
    //   44: invokespecial 104	net/cloudpath/xpressconnect/certificates/KeystoreFactory:getCertArray	(Ljava/util/ArrayList;)[Ljava/security/cert/Certificate;
    //   47: astore 10
    //   49: aload 10
    //   51: ifnonnull +170 -> 221
    //   54: aload_0
    //   55: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   58: ldc 106
    //   60: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   63: aconst_null
    //   64: areturn
    //   65: astore 4
    //   67: aload_0
    //   68: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   71: new 108	java/lang/StringBuilder
    //   74: dup
    //   75: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   78: ldc 111
    //   80: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: aload 4
    //   85: invokevirtual 119	java/security/KeyStoreException:getMessage	()Ljava/lang/String;
    //   88: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   94: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   97: aload 4
    //   99: invokevirtual 125	java/security/KeyStoreException:printStackTrace	()V
    //   102: aconst_null
    //   103: areturn
    //   104: astore 9
    //   106: aload_0
    //   107: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   110: new 108	java/lang/StringBuilder
    //   113: dup
    //   114: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   117: ldc 127
    //   119: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: aload 9
    //   124: invokevirtual 128	java/security/NoSuchAlgorithmException:getMessage	()Ljava/lang/String;
    //   127: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   133: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   136: aload 9
    //   138: invokevirtual 129	java/security/NoSuchAlgorithmException:printStackTrace	()V
    //   141: aconst_null
    //   142: areturn
    //   143: astore 8
    //   145: aload_0
    //   146: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   149: new 108	java/lang/StringBuilder
    //   152: dup
    //   153: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   156: ldc 131
    //   158: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: aload 8
    //   163: invokevirtual 132	java/security/cert/CertificateException:getMessage	()Ljava/lang/String;
    //   166: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   172: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   175: aload 8
    //   177: invokevirtual 133	java/security/cert/CertificateException:printStackTrace	()V
    //   180: aconst_null
    //   181: areturn
    //   182: astore 7
    //   184: aload_0
    //   185: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   188: new 108	java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   195: ldc 135
    //   197: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: aload 7
    //   202: invokevirtual 136	java/io/IOException:getMessage	()Ljava/lang/String;
    //   205: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   211: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   214: aload 7
    //   216: invokevirtual 137	java/io/IOException:printStackTrace	()V
    //   219: aconst_null
    //   220: areturn
    //   221: iconst_0
    //   222: istore 11
    //   224: iload 11
    //   226: aload 10
    //   228: arraylength
    //   229: if_icmpge +76 -> 305
    //   232: aload 10
    //   234: iload 11
    //   236: aaload
    //   237: ifnonnull +14 -> 251
    //   240: aload_0
    //   241: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   244: ldc 139
    //   246: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   249: aconst_null
    //   250: areturn
    //   251: aload 6
    //   253: new 108	java/lang/StringBuilder
    //   256: dup
    //   257: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   260: ldc 141
    //   262: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: iload 11
    //   267: invokevirtual 144	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   270: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   273: aload 10
    //   275: iload 11
    //   277: aaload
    //   278: invokevirtual 148	java/security/KeyStore:setCertificateEntry	(Ljava/lang/String;Ljava/security/cert/Certificate;)V
    //   281: iinc 11 1
    //   284: goto -60 -> 224
    //   287: astore 13
    //   289: aload_0
    //   290: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   293: ldc 150
    //   295: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   298: aload 13
    //   300: invokevirtual 125	java/security/KeyStoreException:printStackTrace	()V
    //   303: aconst_null
    //   304: areturn
    //   305: aload_0
    //   306: getfield 27	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mPrivateKey	Ljava/security/PrivateKey;
    //   309: ifnull -290 -> 19
    //   312: aload_3
    //   313: invokestatic 58	net/cloudpath/xpressconnect/Util:stringIsEmpty	(Ljava/lang/String;)Z
    //   316: ifeq +14 -> 330
    //   319: aload_0
    //   320: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   323: ldc 152
    //   325: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   328: aconst_null
    //   329: areturn
    //   330: aload_2
    //   331: ifnonnull +14 -> 345
    //   334: aload_0
    //   335: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   338: ldc 154
    //   340: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   343: aconst_null
    //   344: areturn
    //   345: aload 6
    //   347: aload_3
    //   348: aload_0
    //   349: getfield 27	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mPrivateKey	Ljava/security/PrivateKey;
    //   352: aload_2
    //   353: invokevirtual 160	java/lang/String:toCharArray	()[C
    //   356: aload 10
    //   358: invokevirtual 164	java/security/KeyStore:setKeyEntry	(Ljava/lang/String;Ljava/security/Key;[C[Ljava/security/cert/Certificate;)V
    //   361: aload 6
    //   363: areturn
    //   364: astore 12
    //   366: aload_0
    //   367: getfield 31	net/cloudpath/xpressconnect/certificates/KeystoreFactory:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   370: new 108	java/lang/StringBuilder
    //   373: dup
    //   374: invokespecial 109	java/lang/StringBuilder:<init>	()V
    //   377: ldc 111
    //   379: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   382: aload 12
    //   384: invokevirtual 119	java/security/KeyStoreException:getMessage	()Ljava/lang/String;
    //   387: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: invokevirtual 122	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   393: invokestatic 53	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   396: aload 12
    //   398: invokevirtual 125	java/security/KeyStoreException:printStackTrace	()V
    //   401: aconst_null
    //   402: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   22	28	65	java/security/KeyStoreException
    //   32	39	104	java/security/NoSuchAlgorithmException
    //   32	39	143	java/security/cert/CertificateException
    //   32	39	182	java/io/IOException
    //   251	281	287	java/security/KeyStoreException
    //   345	361	364	java/security/KeyStoreException
  }

  public int numCerts()
  {
    return getCertArray(this.mCertificates).length;
  }
}