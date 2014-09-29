package net.cloudpath.xpressconnect.remote;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.KeystoreFactory;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.logger.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class WebInterface
{
  private static final boolean defaultIgnoreCerts = true;
  public String contentType = null;
  public String finalUrl = null;
  private Logger mLogger = null;
  private PEMValidator mPvCa = null;
  public int resultCode = -1;
  public String statusText = null;

  public WebInterface(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  private KeyStore getKeystore()
  {
    if (this.mPvCa == null)
      return (KeyStore)null;
    KeystoreFactory localKeystoreFactory = new KeystoreFactory(this.mLogger);
    ArrayList localArrayList = this.mPvCa.getCertData();
    Util.log(this.mLogger, "Adding " + localArrayList.size() + " certificate(s) to our validation engine.");
    for (int i = 0; i < localArrayList.size(); i++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      for (String str : (String[])localArrayList.get(i))
        localStringBuilder.append(str + "\n");
      localKeystoreFactory.addCertToStore(localStringBuilder.toString());
    }
    return localKeystoreFactory.getKeystore("BKS", null, null);
  }

  public boolean addCertsToValidateWith(String paramString)
  {
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "The certs string is empty. (WebInterface)");
      return false;
    }
    this.mPvCa = new PEMValidator(this.mLogger, paramString);
    Util.log(this.mLogger, "Validating CA cert(s)...");
    if ((!this.mPvCa.isValid()) && (!this.mPvCa.attemptToFix()))
    {
      Util.log(this.mLogger, "Unable to correct PEM encoding on certificates.");
      this.mPvCa = null;
      return false;
    }
    return true;
  }

  public int checkWebPage(String paramString1, String paramString2, boolean paramBoolean)
  {
    if (paramString1 == null)
      return -1;
    if (!paramString1.startsWith("http"))
      paramString1 = "http://" + paramString1;
    String str = getWebPage(paramString1, paramBoolean);
    if ((str == null) || (this.resultCode != 200))
    {
      Util.log(this.mLogger, "No valid response structure in checkWebPage()!");
      return -1;
    }
    if (paramString2 == null)
      return this.resultCode;
    if (Pattern.compile(paramString2).matcher(str).find())
    {
      Util.log(this.mLogger, "Found a regex match on the web page!");
      return this.resultCode;
    }
    Util.log(this.mLogger, "No regex match was found.");
    return -1;
  }

  public void clearCertValidationCache()
  {
    this.mPvCa = null;
    Util.log(this.mLogger, "Cert validation cache cleared.");
  }

  // ERROR //
  public boolean download(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aconst_null
    //   3: astore 4
    //   5: new 161	java/net/URL
    //   8: dup
    //   9: aload_1
    //   10: invokespecial 164	java/net/URL:<init>	(Ljava/lang/String;)V
    //   13: astore 5
    //   15: new 166	java/io/BufferedOutputStream
    //   18: dup
    //   19: new 168	java/io/FileOutputStream
    //   22: dup
    //   23: aload_2
    //   24: invokespecial 169	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   27: invokespecial 172	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;)V
    //   30: astore 6
    //   32: aload 5
    //   34: invokevirtual 176	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   37: invokevirtual 182	java/net/URLConnection:getInputStream	()Ljava/io/InputStream;
    //   40: astore 4
    //   42: sipush 1024
    //   45: newarray byte
    //   47: astore 11
    //   49: aload 4
    //   51: aload 11
    //   53: invokevirtual 188	java/io/InputStream:read	([B)I
    //   56: istore 12
    //   58: iload 12
    //   60: iconst_m1
    //   61: if_icmpeq +46 -> 107
    //   64: aload 6
    //   66: aload 11
    //   68: iconst_0
    //   69: iload 12
    //   71: invokevirtual 194	java/io/OutputStream:write	([BII)V
    //   74: goto -25 -> 49
    //   77: astore 9
    //   79: aload 6
    //   81: astore_3
    //   82: aload 9
    //   84: invokevirtual 197	java/lang/Exception:printStackTrace	()V
    //   87: aload 4
    //   89: ifnull +8 -> 97
    //   92: aload 4
    //   94: invokevirtual 200	java/io/InputStream:close	()V
    //   97: aload_3
    //   98: ifnull +7 -> 105
    //   101: aload_3
    //   102: invokevirtual 201	java/io/OutputStream:close	()V
    //   105: iconst_0
    //   106: ireturn
    //   107: aload 4
    //   109: ifnull +8 -> 117
    //   112: aload 4
    //   114: invokevirtual 200	java/io/InputStream:close	()V
    //   117: aload 6
    //   119: ifnull +8 -> 127
    //   122: aload 6
    //   124: invokevirtual 201	java/io/OutputStream:close	()V
    //   127: iconst_1
    //   128: ireturn
    //   129: astore 13
    //   131: aload_0
    //   132: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   135: ldc 203
    //   137: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   140: aload_0
    //   141: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   144: aload 13
    //   146: invokevirtual 206	java/io/IOException:getLocalizedMessage	()Ljava/lang/String;
    //   149: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   152: iconst_0
    //   153: ireturn
    //   154: astore 10
    //   156: aload_0
    //   157: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   160: ldc 203
    //   162: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   165: aload_0
    //   166: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   169: aload 10
    //   171: invokevirtual 206	java/io/IOException:getLocalizedMessage	()Ljava/lang/String;
    //   174: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   177: iconst_0
    //   178: ireturn
    //   179: astore 7
    //   181: aload 4
    //   183: ifnull +8 -> 191
    //   186: aload 4
    //   188: invokevirtual 200	java/io/InputStream:close	()V
    //   191: aload_3
    //   192: ifnull +7 -> 199
    //   195: aload_3
    //   196: invokevirtual 201	java/io/OutputStream:close	()V
    //   199: aload 7
    //   201: athrow
    //   202: astore 8
    //   204: aload_0
    //   205: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   208: ldc 203
    //   210: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   213: aload_0
    //   214: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   217: aload 8
    //   219: invokevirtual 206	java/io/IOException:getLocalizedMessage	()Ljava/lang/String;
    //   222: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   225: goto -26 -> 199
    //   228: astore 7
    //   230: aload 6
    //   232: astore_3
    //   233: goto -52 -> 181
    //   236: astore 9
    //   238: aconst_null
    //   239: astore 4
    //   241: aconst_null
    //   242: astore_3
    //   243: goto -161 -> 82
    //
    // Exception table:
    //   from	to	target	type
    //   32	49	77	java/lang/Exception
    //   49	58	77	java/lang/Exception
    //   64	74	77	java/lang/Exception
    //   112	117	129	java/io/IOException
    //   122	127	129	java/io/IOException
    //   92	97	154	java/io/IOException
    //   101	105	154	java/io/IOException
    //   5	32	179	finally
    //   82	87	179	finally
    //   186	191	202	java/io/IOException
    //   195	199	202	java/io/IOException
    //   32	49	228	finally
    //   49	58	228	finally
    //   64	74	228	finally
    //   5	32	236	java/lang/Exception
  }

  protected String encodeUsername(List<NameValuePair> paramList)
  {
    String str1 = URLEncodedUtils.format(paramList, "UTF-8");
    int i = str1.indexOf("username");
    int j = str1.indexOf("&", i);
    if (i < 0)
      return str1;
    String str2 = str1.substring(0, i);
    String str3;
    if (j >= 0)
      str3 = str1.substring(i, j);
    for (String str4 = str1.substring(j); ; str4 = null)
    {
      String str5 = str3.replace("+", "%20");
      return str2 + str5 + str4;
      str3 = str1.substring(i);
    }
  }

  public String getCertWebPageMultiAuth(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    return getCertWebPageMultiAuth(paramString1, paramString2, paramString3, paramString4, true, paramBoolean);
  }

  public String getCertWebPageMultiAuth(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2)
  {
    String str = getWebPage(paramString1, paramString2, paramString3, paramString4, false, paramBoolean1, null, paramBoolean2);
    if (str == null)
    {
      Util.log(this.mLogger, "Web page result returned null.");
      return null;
    }
    if (!str.contains("-----BEGIN CERTIFICATE-----"))
    {
      Util.log(this.mLogger, "Basic authentication failed.  Attempting NTLM.");
      str = getWebPage(paramString1, paramString2, paramString3, paramString4, true, paramBoolean1, null, paramBoolean2);
    }
    return str;
  }

  protected List<NameValuePair> getPostData(String paramString)
    throws UnsupportedEncodingException
  {
    ArrayList localArrayList = new ArrayList();
    if (Util.stringIsEmpty(paramString))
      return localArrayList;
    String[] arrayOfString1 = paramString.split("&");
    int i = 0;
    label27: String[] arrayOfString2;
    if (i < arrayOfString1.length)
    {
      arrayOfString2 = arrayOfString1[i].split("=");
      if (arrayOfString2.length >= 2)
        break label69;
      Util.log(this.mLogger, "Skipping invalid post data.");
    }
    while (true)
    {
      i++;
      break label27;
      break;
      label69: String str = arrayOfString2[1];
      if (arrayOfString2.length > 2)
        for (int j = 2; j < arrayOfString2.length; j++)
          str = str.concat("=").concat(arrayOfString2[j]);
      localArrayList.add(new BasicNameValuePair(arrayOfString2[0], str));
    }
  }

  public String getWebPage(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2)
  {
    return getWebPage(paramString1, paramString2, paramString3, paramString4, paramBoolean1, true, null, paramBoolean2);
  }

  // ERROR //
  public String getWebPage(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2, String paramString5, boolean paramBoolean3)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 9
    //   3: new 296	org/apache/http/protocol/BasicHttpContext
    //   6: dup
    //   7: invokespecial 297	org/apache/http/protocol/BasicHttpContext:<init>	()V
    //   10: astore 10
    //   12: aload_0
    //   13: aconst_null
    //   14: putfield 34	net/cloudpath/xpressconnect/remote/WebInterface:finalUrl	Ljava/lang/String;
    //   17: aload_0
    //   18: iconst_m1
    //   19: putfield 28	net/cloudpath/xpressconnect/remote/WebInterface:resultCode	I
    //   22: aload_1
    //   23: ifnonnull +15 -> 38
    //   26: aload_0
    //   27: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   30: ldc_w 299
    //   33: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   36: aconst_null
    //   37: areturn
    //   38: iload 8
    //   40: ifeq +30 -> 70
    //   43: aload_0
    //   44: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   47: new 50	java/lang/StringBuilder
    //   50: dup
    //   51: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   54: ldc_w 301
    //   57: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   60: aload_1
    //   61: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   67: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   70: aload_2
    //   71: ifnull +35 -> 106
    //   74: iload 8
    //   76: ifeq +30 -> 106
    //   79: aload_0
    //   80: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   83: new 50	java/lang/StringBuilder
    //   86: dup
    //   87: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   90: ldc_w 303
    //   93: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: aload_2
    //   97: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   103: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   106: aload_2
    //   107: ifnull +10 -> 117
    //   110: aload_2
    //   111: ldc_w 305
    //   114: if_acmpne +639 -> 753
    //   117: iload 8
    //   119: ifeq +13 -> 132
    //   122: aload_0
    //   123: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   126: ldc_w 307
    //   129: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   132: new 309	org/apache/http/client/methods/HttpGet
    //   135: dup
    //   136: aload_1
    //   137: invokespecial 310	org/apache/http/client/methods/HttpGet:<init>	(Ljava/lang/String;)V
    //   140: astore 13
    //   142: aload 13
    //   144: ldc_w 312
    //   147: ldc_w 314
    //   150: invokevirtual 317	org/apache/http/client/methods/HttpGet:setHeader	(Ljava/lang/String;Ljava/lang/String;)V
    //   153: aload 13
    //   155: ldc_w 319
    //   158: ldc_w 321
    //   161: invokevirtual 317	org/apache/http/client/methods/HttpGet:setHeader	(Ljava/lang/String;Ljava/lang/String;)V
    //   164: aload 7
    //   166: ifnull +1141 -> 1307
    //   169: aload 13
    //   171: ldc_w 323
    //   174: aload 7
    //   176: invokevirtual 317	org/apache/http/client/methods/HttpGet:setHeader	(Ljava/lang/String;Ljava/lang/String;)V
    //   179: aload 13
    //   181: astore 14
    //   183: new 325	net/cloudpath/xpressconnect/remote/MySSLSocketFactory
    //   186: dup
    //   187: aload_0
    //   188: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   191: aload_0
    //   192: invokespecial 327	net/cloudpath/xpressconnect/remote/WebInterface:getKeystore	()Ljava/security/KeyStore;
    //   195: iload 6
    //   197: invokespecial 330	net/cloudpath/xpressconnect/remote/MySSLSocketFactory:<init>	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/security/KeyStore;Z)V
    //   200: astore 15
    //   202: aload 15
    //   204: getstatic 336	org/apache/http/conn/ssl/SSLSocketFactory:ALLOW_ALL_HOSTNAME_VERIFIER	Lorg/apache/http/conn/ssl/X509HostnameVerifier;
    //   207: invokevirtual 340	org/apache/http/conn/ssl/SSLSocketFactory:setHostnameVerifier	(Lorg/apache/http/conn/ssl/X509HostnameVerifier;)V
    //   210: new 342	org/apache/http/conn/scheme/PlainSocketFactory
    //   213: dup
    //   214: invokespecial 343	org/apache/http/conn/scheme/PlainSocketFactory:<init>	()V
    //   217: astore 16
    //   219: new 345	org/apache/http/conn/scheme/SchemeRegistry
    //   222: dup
    //   223: invokespecial 346	org/apache/http/conn/scheme/SchemeRegistry:<init>	()V
    //   226: astore 17
    //   228: new 348	org/apache/http/conn/scheme/Scheme
    //   231: dup
    //   232: ldc_w 350
    //   235: aload 15
    //   237: sipush 443
    //   240: invokespecial 353	org/apache/http/conn/scheme/Scheme:<init>	(Ljava/lang/String;Lorg/apache/http/conn/scheme/SocketFactory;I)V
    //   243: astore 18
    //   245: aload 17
    //   247: aload 18
    //   249: invokevirtual 357	org/apache/http/conn/scheme/SchemeRegistry:register	(Lorg/apache/http/conn/scheme/Scheme;)Lorg/apache/http/conn/scheme/Scheme;
    //   252: pop
    //   253: new 348	org/apache/http/conn/scheme/Scheme
    //   256: dup
    //   257: ldc 118
    //   259: aload 16
    //   261: bipush 80
    //   263: invokespecial 353	org/apache/http/conn/scheme/Scheme:<init>	(Ljava/lang/String;Lorg/apache/http/conn/scheme/SocketFactory;I)V
    //   266: astore 20
    //   268: aload 17
    //   270: aload 20
    //   272: invokevirtual 357	org/apache/http/conn/scheme/SchemeRegistry:register	(Lorg/apache/http/conn/scheme/Scheme;)Lorg/apache/http/conn/scheme/Scheme;
    //   275: pop
    //   276: aload 9
    //   278: ifnull +567 -> 845
    //   281: new 359	org/apache/http/impl/client/DefaultHttpClient
    //   284: dup
    //   285: new 361	org/apache/http/impl/conn/SingleClientConnManager
    //   288: dup
    //   289: aload 9
    //   291: invokevirtual 367	org/apache/http/client/methods/HttpPost:getParams	()Lorg/apache/http/params/HttpParams;
    //   294: aload 17
    //   296: invokespecial 370	org/apache/http/impl/conn/SingleClientConnManager:<init>	(Lorg/apache/http/params/HttpParams;Lorg/apache/http/conn/scheme/SchemeRegistry;)V
    //   299: aload 9
    //   301: invokevirtual 367	org/apache/http/client/methods/HttpPost:getParams	()Lorg/apache/http/params/HttpParams;
    //   304: invokespecial 373	org/apache/http/impl/client/DefaultHttpClient:<init>	(Lorg/apache/http/conn/ClientConnectionManager;Lorg/apache/http/params/HttpParams;)V
    //   307: astore 22
    //   309: aload_3
    //   310: ifnull +100 -> 410
    //   313: aload 4
    //   315: ifnull +95 -> 410
    //   318: aload_3
    //   319: ldc_w 305
    //   322: invokevirtual 376	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   325: ifne +85 -> 410
    //   328: aload 4
    //   330: ldc_w 305
    //   333: invokevirtual 376	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   336: ifne +74 -> 410
    //   339: iload 5
    //   341: ifeq +535 -> 876
    //   344: aload 22
    //   346: invokevirtual 380	org/apache/http/impl/client/DefaultHttpClient:getAuthSchemes	()Lorg/apache/http/auth/AuthSchemeRegistry;
    //   349: ldc_w 382
    //   352: new 384	net/cloudpath/xpressconnect/remote/NTLMSchemeFactory
    //   355: dup
    //   356: invokespecial 385	net/cloudpath/xpressconnect/remote/NTLMSchemeFactory:<init>	()V
    //   359: invokevirtual 390	org/apache/http/auth/AuthSchemeRegistry:register	(Ljava/lang/String;Lorg/apache/http/auth/AuthSchemeFactory;)V
    //   362: aload 22
    //   364: invokevirtual 394	org/apache/http/impl/client/DefaultHttpClient:getCredentialsProvider	()Lorg/apache/http/client/CredentialsProvider;
    //   367: astore 35
    //   369: new 396	org/apache/http/auth/AuthScope
    //   372: dup
    //   373: getstatic 400	org/apache/http/auth/AuthScope:ANY	Lorg/apache/http/auth/AuthScope;
    //   376: invokespecial 403	org/apache/http/auth/AuthScope:<init>	(Lorg/apache/http/auth/AuthScope;)V
    //   379: astore 36
    //   381: new 405	org/apache/http/auth/NTCredentials
    //   384: dup
    //   385: aload_3
    //   386: aload 4
    //   388: ldc_w 305
    //   391: ldc_w 305
    //   394: invokespecial 408	org/apache/http/auth/NTCredentials:<init>	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
    //   397: astore 37
    //   399: aload 35
    //   401: aload 36
    //   403: aload 37
    //   405: invokeinterface 414 3 0
    //   410: aload 9
    //   412: ifnull +586 -> 998
    //   415: aload 22
    //   417: aload 9
    //   419: aload 10
    //   421: invokevirtual 418	org/apache/http/impl/client/DefaultHttpClient:execute	(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;
    //   424: astore 23
    //   426: aload_0
    //   427: aload 23
    //   429: invokeinterface 424 1 0
    //   434: invokeinterface 429 1 0
    //   439: putfield 28	net/cloudpath/xpressconnect/remote/WebInterface:resultCode	I
    //   442: aload_0
    //   443: aload 23
    //   445: invokeinterface 424 1 0
    //   450: invokeinterface 432 1 0
    //   455: putfield 30	net/cloudpath/xpressconnect/remote/WebInterface:statusText	Ljava/lang/String;
    //   458: aload 23
    //   460: ldc_w 319
    //   463: invokeinterface 436 2 0
    //   468: ifnull +544 -> 1012
    //   471: aload_0
    //   472: aload 23
    //   474: ldc_w 319
    //   477: invokeinterface 436 2 0
    //   482: invokeinterface 441 1 0
    //   487: putfield 32	net/cloudpath/xpressconnect/remote/WebInterface:contentType	Ljava/lang/String;
    //   490: iload 8
    //   492: ifeq +33 -> 525
    //   495: aload_0
    //   496: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   499: new 50	java/lang/StringBuilder
    //   502: dup
    //   503: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   506: ldc_w 443
    //   509: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: aload_0
    //   513: getfield 28	net/cloudpath/xpressconnect/remote/WebInterface:resultCode	I
    //   516: invokevirtual 66	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   519: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   522: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   525: aload_0
    //   526: getfield 28	net/cloudpath/xpressconnect/remote/WebInterface:resultCode	I
    //   529: sipush 200
    //   532: if_icmpne +64 -> 596
    //   535: aload 10
    //   537: ldc_w 445
    //   540: invokeinterface 451 2 0
    //   545: checkcast 453	org/apache/http/client/methods/HttpUriRequest
    //   548: astore 32
    //   550: aload 10
    //   552: ldc_w 455
    //   555: invokeinterface 451 2 0
    //   560: checkcast 457	org/apache/http/HttpHost
    //   563: astore 33
    //   565: aload 32
    //   567: invokeinterface 461 1 0
    //   572: invokevirtual 466	java/net/URI:isAbsolute	()Z
    //   575: ifeq +494 -> 1069
    //   578: aload 32
    //   580: invokeinterface 461 1 0
    //   585: invokevirtual 467	java/net/URI:toString	()Ljava/lang/String;
    //   588: astore 34
    //   590: aload_0
    //   591: aload 34
    //   593: putfield 34	net/cloudpath/xpressconnect/remote/WebInterface:finalUrl	Ljava/lang/String;
    //   596: new 50	java/lang/StringBuilder
    //   599: dup
    //   600: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   603: astore 24
    //   605: new 469	java/io/BufferedReader
    //   608: dup
    //   609: new 471	java/io/InputStreamReader
    //   612: dup
    //   613: aload 23
    //   615: invokeinterface 475 1 0
    //   620: invokeinterface 480 1 0
    //   625: invokespecial 483	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   628: invokespecial 486	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   631: astore 25
    //   633: aload 25
    //   635: invokevirtual 489	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   638: astore 27
    //   640: aload 27
    //   642: ifnull +556 -> 1198
    //   645: aload 24
    //   647: new 50	java/lang/StringBuilder
    //   650: dup
    //   651: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   654: aload 27
    //   656: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   659: ldc 86
    //   661: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   664: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   667: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   670: pop
    //   671: goto -38 -> 633
    //   674: astore 26
    //   676: aload 26
    //   678: invokevirtual 490	java/io/IOException:printStackTrace	()V
    //   681: aload_0
    //   682: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   685: new 50	java/lang/StringBuilder
    //   688: dup
    //   689: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   692: ldc_w 492
    //   695: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   698: aload 26
    //   700: invokevirtual 495	java/io/IOException:getMessage	()Ljava/lang/String;
    //   703: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   706: ldc_w 497
    //   709: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   712: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   715: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   718: ldc_w 305
    //   721: areturn
    //   722: astore 40
    //   724: aload_0
    //   725: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   728: new 50	java/lang/StringBuilder
    //   731: dup
    //   732: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   735: ldc_w 499
    //   738: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   741: aload_1
    //   742: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   745: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   748: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   751: aconst_null
    //   752: areturn
    //   753: new 363	org/apache/http/client/methods/HttpPost
    //   756: dup
    //   757: aload_1
    //   758: invokespecial 500	org/apache/http/client/methods/HttpPost:<init>	(Ljava/lang/String;)V
    //   761: astore 41
    //   763: aload 41
    //   765: ldc_w 312
    //   768: ldc_w 314
    //   771: invokevirtual 501	org/apache/http/client/methods/HttpPost:setHeader	(Ljava/lang/String;Ljava/lang/String;)V
    //   774: aload 41
    //   776: ldc_w 319
    //   779: ldc_w 321
    //   782: invokevirtual 501	org/apache/http/client/methods/HttpPost:setHeader	(Ljava/lang/String;Ljava/lang/String;)V
    //   785: aload 7
    //   787: ifnull +13 -> 800
    //   790: aload 41
    //   792: ldc_w 323
    //   795: aload 7
    //   797: invokevirtual 501	org/apache/http/client/methods/HttpPost:setHeader	(Ljava/lang/String;Ljava/lang/String;)V
    //   800: new 503	org/apache/http/entity/StringEntity
    //   803: dup
    //   804: aload_0
    //   805: aload_0
    //   806: aload_2
    //   807: invokevirtual 505	net/cloudpath/xpressconnect/remote/WebInterface:getPostData	(Ljava/lang/String;)Ljava/util/List;
    //   810: invokevirtual 507	net/cloudpath/xpressconnect/remote/WebInterface:encodeUsername	(Ljava/util/List;)Ljava/lang/String;
    //   813: ldc 210
    //   815: invokespecial 508	org/apache/http/entity/StringEntity:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   818: astore 42
    //   820: aload 42
    //   822: ldc_w 321
    //   825: invokevirtual 511	org/apache/http/entity/StringEntity:setContentType	(Ljava/lang/String;)V
    //   828: aload 41
    //   830: aload 42
    //   832: invokevirtual 515	org/apache/http/client/methods/HttpPost:setEntity	(Lorg/apache/http/HttpEntity;)V
    //   835: aload 41
    //   837: astore 9
    //   839: aconst_null
    //   840: astore 14
    //   842: goto -659 -> 183
    //   845: new 359	org/apache/http/impl/client/DefaultHttpClient
    //   848: dup
    //   849: new 361	org/apache/http/impl/conn/SingleClientConnManager
    //   852: dup
    //   853: aload 14
    //   855: invokevirtual 516	org/apache/http/client/methods/HttpGet:getParams	()Lorg/apache/http/params/HttpParams;
    //   858: aload 17
    //   860: invokespecial 370	org/apache/http/impl/conn/SingleClientConnManager:<init>	(Lorg/apache/http/params/HttpParams;Lorg/apache/http/conn/scheme/SchemeRegistry;)V
    //   863: aload 14
    //   865: invokevirtual 516	org/apache/http/client/methods/HttpGet:getParams	()Lorg/apache/http/params/HttpParams;
    //   868: invokespecial 373	org/apache/http/impl/client/DefaultHttpClient:<init>	(Lorg/apache/http/conn/ClientConnectionManager;Lorg/apache/http/params/HttpParams;)V
    //   871: astore 22
    //   873: goto -564 -> 309
    //   876: iload 8
    //   878: ifeq +13 -> 891
    //   881: aload_0
    //   882: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   885: ldc_w 518
    //   888: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   891: new 520	org/apache/http/auth/UsernamePasswordCredentials
    //   894: dup
    //   895: aload_3
    //   896: aload 4
    //   898: invokespecial 521	org/apache/http/auth/UsernamePasswordCredentials:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   901: astore 38
    //   903: new 523	org/apache/http/impl/client/BasicCredentialsProvider
    //   906: dup
    //   907: invokespecial 524	org/apache/http/impl/client/BasicCredentialsProvider:<init>	()V
    //   910: astore 39
    //   912: aload 39
    //   914: getstatic 400	org/apache/http/auth/AuthScope:ANY	Lorg/apache/http/auth/AuthScope;
    //   917: aload 38
    //   919: invokevirtual 525	org/apache/http/impl/client/BasicCredentialsProvider:setCredentials	(Lorg/apache/http/auth/AuthScope;Lorg/apache/http/auth/Credentials;)V
    //   922: aload 22
    //   924: aload 39
    //   926: invokevirtual 529	org/apache/http/impl/client/DefaultHttpClient:setCredentialsProvider	(Lorg/apache/http/client/CredentialsProvider;)V
    //   929: goto -519 -> 410
    //   932: astore 12
    //   934: iload 8
    //   936: ifeq +44 -> 980
    //   939: aload_0
    //   940: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   943: new 50	java/lang/StringBuilder
    //   946: dup
    //   947: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   950: ldc_w 531
    //   953: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   956: aload 12
    //   958: invokevirtual 532	java/net/SocketException:getMessage	()Ljava/lang/String;
    //   961: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   964: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   967: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   970: aload_0
    //   971: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   974: ldc_w 534
    //   977: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   980: aload_0
    //   981: sipush 302
    //   984: putfield 28	net/cloudpath/xpressconnect/remote/WebInterface:resultCode	I
    //   987: aload_0
    //   988: ldc_w 536
    //   991: putfield 30	net/cloudpath/xpressconnect/remote/WebInterface:statusText	Ljava/lang/String;
    //   994: ldc_w 305
    //   997: areturn
    //   998: aload 22
    //   1000: aload 14
    //   1002: aload 10
    //   1004: invokevirtual 418	org/apache/http/impl/client/DefaultHttpClient:execute	(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/protocol/HttpContext;)Lorg/apache/http/HttpResponse;
    //   1007: astore 23
    //   1009: goto -583 -> 426
    //   1012: aload_0
    //   1013: aconst_null
    //   1014: putfield 32	net/cloudpath/xpressconnect/remote/WebInterface:contentType	Ljava/lang/String;
    //   1017: goto -527 -> 490
    //   1020: astore 11
    //   1022: iload 8
    //   1024: ifeq +34 -> 1058
    //   1027: aload_0
    //   1028: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1031: new 50	java/lang/StringBuilder
    //   1034: dup
    //   1035: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   1038: ldc_w 531
    //   1041: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1044: aload 11
    //   1046: invokevirtual 537	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   1049: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1052: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1055: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1058: aload 11
    //   1060: invokevirtual 197	java/lang/Exception:printStackTrace	()V
    //   1063: aload 11
    //   1065: invokevirtual 537	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   1068: areturn
    //   1069: new 50	java/lang/StringBuilder
    //   1072: dup
    //   1073: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   1076: aload 33
    //   1078: invokevirtual 540	org/apache/http/HttpHost:toURI	()Ljava/lang/String;
    //   1081: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1084: aload 32
    //   1086: invokeinterface 461 1 0
    //   1091: invokevirtual 543	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1094: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1097: astore 34
    //   1099: goto -509 -> 590
    //   1102: astore 31
    //   1104: aload 31
    //   1106: invokevirtual 544	java/lang/IllegalStateException:printStackTrace	()V
    //   1109: aload_0
    //   1110: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1113: new 50	java/lang/StringBuilder
    //   1116: dup
    //   1117: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   1120: ldc_w 546
    //   1123: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1126: aload 31
    //   1128: invokevirtual 547	java/lang/IllegalStateException:getMessage	()Ljava/lang/String;
    //   1131: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1134: ldc_w 497
    //   1137: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1140: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1143: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1146: ldc_w 305
    //   1149: areturn
    //   1150: astore 30
    //   1152: aload 30
    //   1154: invokevirtual 490	java/io/IOException:printStackTrace	()V
    //   1157: aload_0
    //   1158: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1161: new 50	java/lang/StringBuilder
    //   1164: dup
    //   1165: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   1168: ldc_w 492
    //   1171: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1174: aload 30
    //   1176: invokevirtual 495	java/io/IOException:getMessage	()Ljava/lang/String;
    //   1179: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1182: ldc_w 497
    //   1185: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1188: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1191: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1194: ldc_w 305
    //   1197: areturn
    //   1198: aload_0
    //   1199: getfield 28	net/cloudpath/xpressconnect/remote/WebInterface:resultCode	I
    //   1202: sipush 200
    //   1205: if_icmpeq +72 -> 1277
    //   1208: iload 8
    //   1210: ifeq +107 -> 1317
    //   1213: aload_0
    //   1214: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1217: new 50	java/lang/StringBuilder
    //   1220: dup
    //   1221: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   1224: ldc_w 549
    //   1227: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1230: aload_0
    //   1231: getfield 30	net/cloudpath/xpressconnect/remote/WebInterface:statusText	Ljava/lang/String;
    //   1234: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1237: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1240: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1243: aload_0
    //   1244: getfield 24	net/cloudpath/xpressconnect/remote/WebInterface:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1247: new 50	java/lang/StringBuilder
    //   1250: dup
    //   1251: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   1254: ldc_w 551
    //   1257: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1260: aload 24
    //   1262: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1265: invokevirtual 57	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1268: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1271: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1274: goto +43 -> 1317
    //   1277: aload 24
    //   1279: invokevirtual 72	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1282: astore 29
    //   1284: aload 29
    //   1286: areturn
    //   1287: astore 11
    //   1289: goto -267 -> 1022
    //   1292: astore 11
    //   1294: goto -272 -> 1022
    //   1297: astore 12
    //   1299: goto -365 -> 934
    //   1302: astore 12
    //   1304: goto -370 -> 934
    //   1307: aload 13
    //   1309: astore 14
    //   1311: aconst_null
    //   1312: astore 9
    //   1314: goto -1131 -> 183
    //   1317: ldc_w 305
    //   1320: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   633	640	674	java/io/IOException
    //   645	671	674	java/io/IOException
    //   132	142	722	java/lang/IllegalArgumentException
    //   122	132	932	java/net/SocketException
    //   132	142	932	java/net/SocketException
    //   183	276	932	java/net/SocketException
    //   281	309	932	java/net/SocketException
    //   318	339	932	java/net/SocketException
    //   344	410	932	java/net/SocketException
    //   415	426	932	java/net/SocketException
    //   426	490	932	java/net/SocketException
    //   495	525	932	java/net/SocketException
    //   525	590	932	java/net/SocketException
    //   590	596	932	java/net/SocketException
    //   596	605	932	java/net/SocketException
    //   605	633	932	java/net/SocketException
    //   633	640	932	java/net/SocketException
    //   645	671	932	java/net/SocketException
    //   676	718	932	java/net/SocketException
    //   724	751	932	java/net/SocketException
    //   753	763	932	java/net/SocketException
    //   845	873	932	java/net/SocketException
    //   881	891	932	java/net/SocketException
    //   891	929	932	java/net/SocketException
    //   998	1009	932	java/net/SocketException
    //   1012	1017	932	java/net/SocketException
    //   1069	1099	932	java/net/SocketException
    //   1104	1146	932	java/net/SocketException
    //   1152	1194	932	java/net/SocketException
    //   1198	1208	932	java/net/SocketException
    //   1213	1274	932	java/net/SocketException
    //   1277	1284	932	java/net/SocketException
    //   122	132	1020	java/lang/Exception
    //   132	142	1020	java/lang/Exception
    //   183	276	1020	java/lang/Exception
    //   281	309	1020	java/lang/Exception
    //   318	339	1020	java/lang/Exception
    //   344	410	1020	java/lang/Exception
    //   415	426	1020	java/lang/Exception
    //   426	490	1020	java/lang/Exception
    //   495	525	1020	java/lang/Exception
    //   525	590	1020	java/lang/Exception
    //   590	596	1020	java/lang/Exception
    //   596	605	1020	java/lang/Exception
    //   605	633	1020	java/lang/Exception
    //   633	640	1020	java/lang/Exception
    //   645	671	1020	java/lang/Exception
    //   676	718	1020	java/lang/Exception
    //   724	751	1020	java/lang/Exception
    //   753	763	1020	java/lang/Exception
    //   845	873	1020	java/lang/Exception
    //   881	891	1020	java/lang/Exception
    //   891	929	1020	java/lang/Exception
    //   998	1009	1020	java/lang/Exception
    //   1012	1017	1020	java/lang/Exception
    //   1069	1099	1020	java/lang/Exception
    //   1104	1146	1020	java/lang/Exception
    //   1152	1194	1020	java/lang/Exception
    //   1198	1208	1020	java/lang/Exception
    //   1213	1274	1020	java/lang/Exception
    //   1277	1284	1020	java/lang/Exception
    //   605	633	1102	java/lang/IllegalStateException
    //   605	633	1150	java/io/IOException
    //   142	164	1287	java/lang/Exception
    //   169	179	1287	java/lang/Exception
    //   763	785	1292	java/lang/Exception
    //   790	800	1292	java/lang/Exception
    //   800	835	1292	java/lang/Exception
    //   142	164	1297	java/net/SocketException
    //   169	179	1297	java/net/SocketException
    //   763	785	1302	java/net/SocketException
    //   790	800	1302	java/net/SocketException
    //   800	835	1302	java/net/SocketException
  }

  public String getWebPage(String paramString1, String paramString2, boolean paramBoolean)
  {
    return getWebPage(paramString1, paramString2, "", "", false, true, null, paramBoolean);
  }

  public String getWebPage(String paramString, boolean paramBoolean)
  {
    return getWebPage(paramString, "", "", "", false, true, null, paramBoolean);
  }

  public String getWebPageMultiAuth(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean)
  {
    return getWebPageMultiAuth(paramString1, paramString2, paramString3, paramString4, true, null, paramBoolean);
  }

  public String getWebPageMultiAuth(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, String paramString5, boolean paramBoolean2)
  {
    String str = getWebPage(paramString1, paramString2, paramString3, paramString4, false, paramBoolean1, paramString5, paramBoolean2);
    if (this.resultCode != 200)
    {
      if (paramBoolean2)
        Util.log(this.mLogger, "Basic authentication failed.  Attempting NTLM.");
      str = getWebPage(paramString1, paramString2, paramString3, paramString4, true, paramBoolean1, paramString5, paramBoolean2);
    }
    return str;
  }

  public String getWebPageMultiAuth(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean1, boolean paramBoolean2)
  {
    return getWebPageMultiAuth(paramString1, paramString2, paramString3, paramString4, paramBoolean1, null, paramBoolean2);
  }

  public String getWebPageMultiAuthWithCookie(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, boolean paramBoolean)
  {
    return getWebPageMultiAuth(paramString1, paramString2, paramString3, paramString4, true, paramString5, paramBoolean);
  }

  public boolean openWebPage(Context paramContext, String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    if (paramString == null)
    {
      Util.log(this.mLogger, "URL was null in openWebPage().");
      return false;
    }
    localIntent.setData(Uri.parse(paramString));
    if (paramContext == null)
    {
      Util.log(this.mLogger, "Context was null in openWebPage.  Page will not be opened!");
      return false;
    }
    try
    {
      paramContext.startActivity(localIntent);
      return true;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Util.log(this.mLogger, "The browser activity wasn't found.");
    }
    return false;
  }

  public String sanitizeWebResponse(String paramString)
  {
    if (paramString == null)
      return "";
    return paramString.replace("<", "&lt;").replace(">", "&gt;").replace(" META", " no-meta").replace(" meta", " no-meta");
  }
}