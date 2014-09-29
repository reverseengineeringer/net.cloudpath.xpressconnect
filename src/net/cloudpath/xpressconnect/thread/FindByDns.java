package net.cloudpath.xpressconnect.thread;

import java.util.Locale;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.remote.GetConfigFromUrl;

public class FindByDns
{
  protected static final String mWebHost = "cloudpath-xpc";
  protected GetConfigFromUrl mGetConfigFromUrl = null;
  public volatile boolean mIsXpcEs = false;
  protected Logger mLogger = null;
  public volatile String mRawUrl = null;
  public volatile String mTargetUrl = null;

  public FindByDns(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.mGetConfigFromUrl = new GetConfigFromUrl(paramLogger);
  }

  protected String buildUrl(String paramString)
  {
    String str;
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "Attempt to build a full URL from an empty string.");
      str = null;
      return str;
    }
    if (paramString.startsWith("[xpces]"))
    {
      this.mIsXpcEs = true;
      str = paramString.substring("[xpces]".length());
      this.mRawUrl = str;
    }
    while (true)
    {
      if ((!str.toLowerCase(Locale.ENGLISH).startsWith("http://")) && (!str.toLowerCase().startsWith("https://")))
        str = "http://" + str;
      if (str.toLowerCase(Locale.ENGLISH).endsWith("/network_config_android.xml"))
        break;
      return str + "/network_config_android.xml";
      str = paramString;
      this.mIsXpcEs = false;
    }
  }

  public boolean findByUrl(String paramString, boolean paramBoolean)
  {
    return this.mGetConfigFromUrl.findByUrl(paramString, paramBoolean);
  }

  public String getPageResult()
  {
    return this.mGetConfigFromUrl.getPageResult();
  }

  // ERROR //
  public String getTxtRecord(String paramString)
  {
    // Byte code:
    //   0: new 112	org/xbill/DNS/Lookup
    //   3: dup
    //   4: aload_1
    //   5: bipush 16
    //   7: invokespecial 115	org/xbill/DNS/Lookup:<init>	(Ljava/lang/String;I)V
    //   10: astore_2
    //   11: new 117	org/xbill/DNS/SimpleResolver
    //   14: dup
    //   15: invokespecial 118	org/xbill/DNS/SimpleResolver:<init>	()V
    //   18: astore_3
    //   19: aload_2
    //   20: aload_3
    //   21: invokevirtual 122	org/xbill/DNS/Lookup:setResolver	(Lorg/xbill/DNS/Resolver;)V
    //   24: aload_2
    //   25: aconst_null
    //   26: invokevirtual 126	org/xbill/DNS/Lookup:setCache	(Lorg/xbill/DNS/Cache;)V
    //   29: aload_2
    //   30: invokevirtual 130	org/xbill/DNS/Lookup:run	()[Lorg/xbill/DNS/Record;
    //   33: astore 5
    //   35: aload_2
    //   36: invokevirtual 133	org/xbill/DNS/Lookup:getResult	()I
    //   39: ifne +176 -> 215
    //   42: iconst_0
    //   43: istore 6
    //   45: iload 6
    //   47: aload 5
    //   49: arraylength
    //   50: if_icmpge +154 -> 204
    //   53: aload 5
    //   55: iload 6
    //   57: aaload
    //   58: instanceof 135
    //   61: ifeq +137 -> 198
    //   64: ldc 137
    //   66: astore 7
    //   68: aload 5
    //   70: iload 6
    //   72: aaload
    //   73: checkcast 135	org/xbill/DNS/TXTRecord
    //   76: invokevirtual 141	org/xbill/DNS/TXTRecord:getStrings	()Ljava/util/List;
    //   79: invokeinterface 147 1 0
    //   84: astore 8
    //   86: aload 8
    //   88: invokeinterface 153 1 0
    //   93: ifeq +102 -> 195
    //   96: new 83	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 84	java/lang/StringBuilder:<init>	()V
    //   103: aload 7
    //   105: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: aload 8
    //   110: invokeinterface 157 1 0
    //   115: checkcast 53	java/lang/String
    //   118: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: invokevirtual 91	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   124: astore 7
    //   126: goto -40 -> 86
    //   129: astore 10
    //   131: aload 10
    //   133: invokevirtual 160	org/xbill/DNS/TextParseException:printStackTrace	()V
    //   136: aload_0
    //   137: getfield 25	net/cloudpath/xpressconnect/thread/FindByDns:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   140: new 83	java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial 84	java/lang/StringBuilder:<init>	()V
    //   147: ldc 162
    //   149: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   152: aload_1
    //   153: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: invokevirtual 91	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   159: invokestatic 49	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   162: aconst_null
    //   163: areturn
    //   164: astore 9
    //   166: aload 9
    //   168: invokevirtual 163	java/net/UnknownHostException:printStackTrace	()V
    //   171: aload_0
    //   172: getfield 25	net/cloudpath/xpressconnect/thread/FindByDns:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   175: ldc 165
    //   177: invokestatic 49	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   180: aconst_null
    //   181: areturn
    //   182: astore 4
    //   184: aload_0
    //   185: getfield 25	net/cloudpath/xpressconnect/thread/FindByDns:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   188: ldc 167
    //   190: invokestatic 49	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   193: aconst_null
    //   194: areturn
    //   195: aload 7
    //   197: areturn
    //   198: iinc 6 1
    //   201: goto -156 -> 45
    //   204: aload_0
    //   205: getfield 25	net/cloudpath/xpressconnect/thread/FindByDns:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   208: ldc 169
    //   210: invokestatic 49	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   213: aconst_null
    //   214: areturn
    //   215: aload_0
    //   216: getfield 25	net/cloudpath/xpressconnect/thread/FindByDns:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   219: new 83	java/lang/StringBuilder
    //   222: dup
    //   223: invokespecial 84	java/lang/StringBuilder:<init>	()V
    //   226: ldc 171
    //   228: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: aload_1
    //   232: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   235: invokevirtual 91	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   238: invokestatic 49	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   241: aconst_null
    //   242: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	11	129	org/xbill/DNS/TextParseException
    //   11	19	164	java/net/UnknownHostException
    //   29	35	182	java/lang/NullPointerException
  }
}