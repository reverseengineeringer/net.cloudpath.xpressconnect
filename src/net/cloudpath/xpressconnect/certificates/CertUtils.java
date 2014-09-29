package net.cloudpath.xpressconnect.certificates;

import java.io.IOException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DEROctetString;
import org.bouncycastle2.asn1.x509.BasicConstraints;

public class CertUtils
{
  private Logger mLogger = null;

  public CertUtils(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  public static String certStringArrayToString(String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramArrayOfString.length; i++)
      localStringBuilder.append(paramArrayOfString[i] + "\n");
    return localStringBuilder.toString();
  }

  public static String stripCertData(String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
      return stripPkcs7Data(paramString);
    return stripPemData(paramString);
  }

  private static String stripPemData(String paramString)
  {
    if (paramString == null)
      return null;
    return paramString.substring(paramString.indexOf("-----BEGIN CERTIFICATE-----"), "-----END CERTIFICATE-----".length() + paramString.indexOf("-----END CERTIFICATE-----"));
  }

  private static String stripPkcs7Data(String paramString)
  {
    if (paramString == null)
      return null;
    return paramString.substring(paramString.indexOf("-----BEGIN PKCS7-----"), "-----END PKCS7-----".length() + paramString.indexOf("-----END PKCS7-----"));
  }

  public boolean certIsCa(Certificate paramCertificate)
  {
    if (paramCertificate == null);
    byte[] arrayOfByte1;
    do
    {
      return false;
      arrayOfByte1 = ((X509Certificate)paramCertificate).getExtensionValue("2.5.29.19");
    }
    while (arrayOfByte1 == null);
    ASN1InputStream localASN1InputStream = new ASN1InputStream(arrayOfByte1);
    try
    {
      localDEROctetString = (DEROctetString)localASN1InputStream.readObject();
    }
    catch (IOException localIOException3)
    {
      try
      {
        DEROctetString localDEROctetString;
        localASN1InputStream.close();
        arrayOfByte2 = localDEROctetString.getOctets();
      }
      catch (IOException localIOException3)
      {
        try
        {
          byte[] arrayOfByte2;
          ASN1Sequence localASN1Sequence = (ASN1Sequence)new ASN1InputStream(arrayOfByte2).readObject();
          return new BasicConstraints(localASN1Sequence).isCA();
          localIOException1 = localIOException1;
          Util.log(this.mLogger, "I/O exception reading certificate data as DER octet string.");
          localIOException1.printStackTrace();
          try
          {
            localASN1InputStream.close();
            return false;
          }
          catch (IOException localIOException2)
          {
            localIOException2.printStackTrace();
            return false;
          }
          localIOException3 = localIOException3;
          localIOException3.printStackTrace();
        }
        catch (IOException localIOException4)
        {
          Util.log(this.mLogger, "I/O exception reading ASN1 stream from octet data.");
          localIOException4.printStackTrace();
        }
      }
    }
    return false;
  }

  public boolean certIsRoot(Certificate paramCertificate)
  {
    X509Certificate localX509Certificate = (X509Certificate)paramCertificate;
    byte[] arrayOfByte1 = localX509Certificate.getExtensionValue("2.5.29.19");
    if (arrayOfByte1 == null);
    while (true)
    {
      return false;
      ASN1InputStream localASN1InputStream = new ASN1InputStream(arrayOfByte1);
      try
      {
        localDEROctetString = (DEROctetString)localASN1InputStream.readObject();
      }
      catch (IOException localIOException3)
      {
        try
        {
          DEROctetString localDEROctetString;
          localASN1InputStream.close();
          arrayOfByte2 = localDEROctetString.getOctets();
        }
        catch (IOException localIOException3)
        {
          try
          {
            byte[] arrayOfByte2;
            ASN1Sequence localASN1Sequence = (ASN1Sequence)new ASN1InputStream(arrayOfByte2).readObject();
            if ((new BasicConstraints(localASN1Sequence).isCA()) && (localX509Certificate.getIssuerDN().getName().equals(localX509Certificate.getSubjectDN().getName())))
            {
              return true;
              localIOException1 = localIOException1;
              Util.log(this.mLogger, "I/O exception reading certificate data as DER octet string.");
              localIOException1.printStackTrace();
              try
              {
                localASN1InputStream.close();
                return false;
              }
              catch (IOException localIOException2)
              {
                localIOException2.printStackTrace();
                return false;
              }
              localIOException3 = localIOException3;
              localIOException3.printStackTrace();
            }
          }
          catch (IOException localIOException4)
          {
            Util.log(this.mLogger, "I/O exception reading ASN1 stream from octet data.");
            localIOException4.printStackTrace();
          }
        }
      }
    }
    return false;
  }

  // ERROR //
  public String getPemCert(X509Certificate paramX509Certificate)
  {
    // Byte code:
    //   0: new 140	java/io/StringWriter
    //   3: dup
    //   4: invokespecial 141	java/io/StringWriter:<init>	()V
    //   7: astore_2
    //   8: new 143	org/bouncycastle2/openssl/PEMWriter
    //   11: dup
    //   12: aload_2
    //   13: invokespecial 146	org/bouncycastle2/openssl/PEMWriter:<init>	(Ljava/io/Writer;)V
    //   16: astore_3
    //   17: aload_3
    //   18: aload_1
    //   19: invokevirtual 150	org/bouncycastle2/openssl/PEMWriter:writeObject	(Ljava/lang/Object;)V
    //   22: aload_3
    //   23: invokevirtual 151	org/bouncycastle2/openssl/PEMWriter:close	()V
    //   26: aload_2
    //   27: invokevirtual 152	java/io/StringWriter:close	()V
    //   30: aload_2
    //   31: invokevirtual 153	java/io/StringWriter:toString	()Ljava/lang/String;
    //   34: areturn
    //   35: astore 10
    //   37: aload_0
    //   38: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   41: ldc 155
    //   43: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   46: aload 10
    //   48: invokevirtual 156	java/lang/SecurityException:printStackTrace	()V
    //   51: ldc 158
    //   53: areturn
    //   54: astore 9
    //   56: aload_0
    //   57: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   60: ldc 160
    //   62: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   65: aload 9
    //   67: invokevirtual 161	java/lang/IllegalArgumentException:printStackTrace	()V
    //   70: ldc 158
    //   72: areturn
    //   73: astore 4
    //   75: aload_0
    //   76: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   79: new 17	java/lang/StringBuilder
    //   82: dup
    //   83: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   86: ldc 163
    //   88: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   91: aload 4
    //   93: invokevirtual 166	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   96: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: ldc 168
    //   101: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: invokevirtual 28	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   110: aload 4
    //   112: invokevirtual 169	java/lang/Exception:printStackTrace	()V
    //   115: aload_3
    //   116: invokevirtual 151	org/bouncycastle2/openssl/PEMWriter:close	()V
    //   119: ldc 158
    //   121: areturn
    //   122: astore 5
    //   124: aload 5
    //   126: invokevirtual 111	java/io/IOException:printStackTrace	()V
    //   129: goto -10 -> 119
    //   132: astore 7
    //   134: aload_0
    //   135: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   138: ldc 171
    //   140: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   143: aload 7
    //   145: invokevirtual 111	java/io/IOException:printStackTrace	()V
    //   148: ldc 158
    //   150: areturn
    //   151: astore 6
    //   153: aload_0
    //   154: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   157: new 17	java/lang/StringBuilder
    //   160: dup
    //   161: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   164: ldc 173
    //   166: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: aload 6
    //   171: invokevirtual 166	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   174: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: ldc 175
    //   179: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: invokevirtual 28	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   185: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   188: aload 6
    //   190: invokevirtual 169	java/lang/Exception:printStackTrace	()V
    //   193: ldc 158
    //   195: areturn
    //   196: astore 8
    //   198: aload_0
    //   199: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   202: ldc 177
    //   204: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   207: aload 8
    //   209: invokevirtual 111	java/io/IOException:printStackTrace	()V
    //   212: ldc 158
    //   214: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   8	17	35	java/lang/SecurityException
    //   8	17	54	java/lang/IllegalArgumentException
    //   17	22	73	java/lang/Exception
    //   115	119	122	java/io/IOException
    //   22	26	132	java/io/IOException
    //   22	26	151	java/lang/Exception
    //   26	30	196	java/io/IOException
  }

  public String getSanPrincipalString(String paramString)
  {
    X509Certificate localX509Certificate = new CertUtils(this.mLogger).getX509Cert(paramString);
    if (localX509Certificate == null)
      return null;
    SANPlucker localSANPlucker = new SANPlucker(this.mLogger, localX509Certificate);
    if (!localSANPlucker.parse())
    {
      Util.log(this.mLogger, "No 'Principal Name' found.");
      return null;
    }
    return localSANPlucker.getValueForOid("1.3.6.1.4.1.311.20.2.3");
  }

  // ERROR //
  public X509Certificate getX509Cert(String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 205	net/cloudpath/xpressconnect/Util:stringIsEmpty	(Ljava/lang/String;)Z
    //   4: ifeq +5 -> 9
    //   7: aconst_null
    //   8: areturn
    //   9: new 207	java/io/ByteArrayInputStream
    //   12: dup
    //   13: aload_1
    //   14: invokevirtual 210	java/lang/String:getBytes	()[B
    //   17: invokespecial 211	java/io/ByteArrayInputStream:<init>	([B)V
    //   20: astore_2
    //   21: new 213	java/io/BufferedInputStream
    //   24: dup
    //   25: aload_2
    //   26: invokespecial 216	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
    //   29: astore_3
    //   30: ldc 218
    //   32: invokestatic 224	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
    //   35: astore 5
    //   37: aconst_null
    //   38: astore 6
    //   40: aload_3
    //   41: invokevirtual 227	java/io/BufferedInputStream:available	()I
    //   44: ifle +157 -> 201
    //   47: aload 5
    //   49: aload_3
    //   50: invokevirtual 231	java/security/cert/CertificateFactory:generateCertificate	(Ljava/io/InputStream;)Ljava/security/cert/Certificate;
    //   53: checkcast 65	java/security/cert/X509Certificate
    //   56: astore 6
    //   58: goto -18 -> 40
    //   61: astore 4
    //   63: aload 4
    //   65: invokevirtual 232	java/security/cert/CertificateException:printStackTrace	()V
    //   68: aload_0
    //   69: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   72: ldc 234
    //   74: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   77: aconst_null
    //   78: areturn
    //   79: astore 8
    //   81: aload 8
    //   83: invokevirtual 232	java/security/cert/CertificateException:printStackTrace	()V
    //   86: aload_0
    //   87: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   90: ldc 236
    //   92: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   95: aload_0
    //   96: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   99: new 17	java/lang/StringBuilder
    //   102: dup
    //   103: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   106: ldc 238
    //   108: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: aload 8
    //   113: invokevirtual 239	java/security/cert/CertificateException:getMessage	()Ljava/lang/String;
    //   116: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: invokevirtual 28	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   122: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   125: aload_0
    //   126: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   129: new 17	java/lang/StringBuilder
    //   132: dup
    //   133: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   136: ldc 241
    //   138: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: aload_1
    //   142: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: invokevirtual 28	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   148: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   151: aconst_null
    //   152: areturn
    //   153: astore 7
    //   155: aload 7
    //   157: invokevirtual 111	java/io/IOException:printStackTrace	()V
    //   160: aload_0
    //   161: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   164: ldc 243
    //   166: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   169: aload_0
    //   170: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   173: new 17	java/lang/StringBuilder
    //   176: dup
    //   177: invokespecial 18	java/lang/StringBuilder:<init>	()V
    //   180: ldc 238
    //   182: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: aload 7
    //   187: invokevirtual 244	java/io/IOException:getMessage	()Ljava/lang/String;
    //   190: invokevirtual 22	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: invokevirtual 28	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   196: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   199: aconst_null
    //   200: areturn
    //   201: aload_3
    //   202: invokevirtual 245	java/io/BufferedInputStream:close	()V
    //   205: aload_2
    //   206: invokevirtual 248	java/io/InputStream:close	()V
    //   209: aload 6
    //   211: areturn
    //   212: astore 9
    //   214: aload 9
    //   216: invokevirtual 111	java/io/IOException:printStackTrace	()V
    //   219: aload_0
    //   220: getfield 13	net/cloudpath/xpressconnect/certificates/CertUtils:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   223: ldc 250
    //   225: invokestatic 108	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   228: aload 6
    //   230: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   30	37	61	java/security/cert/CertificateException
    //   40	58	79	java/security/cert/CertificateException
    //   40	58	153	java/io/IOException
    //   201	209	212	java/io/IOException
  }
}